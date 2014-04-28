package com.javadocmd.icfbss.actor

import com.javadocmd.libgdx.Position
import com.badlogic.gdx.scenes.scene2d.Actor
import com.javadocmd.icfbss.screen.Resources
import com.badlogic.gdx.graphics.g2d.Batch
import com.javadocmd.libgdx.Size
import com.javadocmd.libgdx.Offset
import com.javadocmd.icfbss.screen.game.GameUI
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import scala.util.Random
import com.badlogic.gdx.math.Interpolation
import com.javadocmd.libgdx.RectangleUtil
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2

class Helicopter(val p: Position) extends Actor with HasAI {
	setPosition(p.x,p.y)
	
	val tex = Resources.atlas.findRegion("helicopter")
	setSize(tex.getRegionWidth(), tex.getRegionHeight())
	setOrigin(getWidth() / 2f, getHeight() / 2f)
	
	var ai: AI = new Sleep(this, new HelicopterChase(this))
	ai.start()
	
	val hbSize = Size(getWidth() / 1.3f, getHeight() / 1.6f)
	val baseHitbox = RectangleUtil(Position(hbSize.w / 4f, hbSize.h / 4f), hbSize)
	def hitbox() = RectangleUtil(baseHitbox, Position(getX(), getY()))
	
	override def act(delta: Float) = {
		super.act(delta)
		ai.act(delta)
	}
	
	override def draw(batch: Batch, parentAlpha: Float) = {
		batch.setColor(1,1,1,parentAlpha)
		batch.draw(tex, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation())
		
//		val hb = hitbox()
//		batch.setColor(1,0,0,.5f)
//		batch.draw(Resources.pixel, hb.x, hb.y, hb.width, hb.height)
	}
}

trait HasAI { var ai: AI }
trait AI {
	def actor: Actor with HasAI
	def act(delta: Float)
	def start()
}

class Sleep(val actor: Actor with HasAI, val wakeAI: AI) extends AI {
	var sleepTime = 0f
	val maxSleep = 1f
	val activationProximity = 1000f
	
	override def start() = {}
	override def act(delta: Float) = {
		sleepTime += delta
		if (sleepTime > maxSleep) {
			if ((actor.getX() - GameUI.actors.monster.getX()).abs < activationProximity) {
				actor.ai = wakeAI
				actor.ai.start()
			}
			sleepTime = 0
		}
	}
}

class HelicopterChase(val actor: Actor with HasAI) extends AI {
	val chaseDistance = 300f
	
	val acceleration = 400f
	val maxTravel = 275f
	val random = new Random()
	
	var velocityX = -100f
	var facingRight = false
	
	//val shotOriginLeft = Offset(5,2)
	//val shotOriginRight = Offset(actor.getWidth() - 5,2)
	val timeBetweenShots = 2.5f
	var shootTime = 0f
	
	override def start() = {
		Resources.chopper.play()
		altitudeChangeLoop()
	}
	
	override def act(delta: Float) = {
		val monsterX = GameUI.actors.monster.getX()
		
		val leftZone = monsterX - chaseDistance
		val rightZone = monsterX + chaseDistance
		
		val dLeft = (actor.getX() - leftZone).abs
		val dRight = (actor.getX() - rightZone).abs
		
		val targetX = if (dLeft < dRight) leftZone else rightZone
		
		// Modify velocity
		if (targetX - actor.getX() < 0) {
			velocityX -= acceleration * delta
		} else {
			velocityX += acceleration * delta
		}
		
		velocityX = velocityX.min(maxTravel).max(-maxTravel)
		
		// Update position
		actor.setX(actor.getX() + velocityX * delta)
		
		// Face player.
		facingRight = (monsterX - actor.getX() > 0)
		actor.setScaleX(if (facingRight) -1 else 1)
		
		shootTime += delta
		if (shootTime > timeBetweenShots) {
			shootTime -= timeBetweenShots
			
			val dir = new Vector2(-actor.getX(), -actor.getY())
			dir.add(new Vector2(GameUI.actors.monster.center()))
			dir.nor()
			
			var orig = Position(actor.getX() - 10, actor.getY() - 10)
			if (facingRight)
				orig = orig + Offset(actor.getWidth() + 20, 0)
				
			val bullet = new Bullet(orig, dir)
			GameUI.actors.bullets.addActor(bullet)
			Resources.shoot.play()
		}
	}
	
	def altitudeChangeLoop(): Unit = {
		val newY = random.nextFloat() * 400f + 200f
		val dy = newY - actor.getY()
		
		actor.addAction(Actions.sequence(
			Actions.moveBy(0, dy, 3f, Interpolation.sine),
			Actions.run(new Runnable() {
				override def run = altitudeChangeLoop
			})
		))
	}
}