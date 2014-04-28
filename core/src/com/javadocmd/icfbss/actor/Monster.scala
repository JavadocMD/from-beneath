package com.javadocmd.icfbss.actor

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.Actor
import com.javadocmd.icfbss.screen.Resources
import com.badlogic.gdx.InputAdapter
import com.badlogic.gdx.Input
import com.javadocmd.libgdx._
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.math.Vector3
import com.javadocmd.icfbss.screen.game.GameUI

class Monster extends Actor {
	import Movement._
	
	val tex = Resources.atlas.findRegion("monster")
	val walkAnim = new Animation(0.1f, Resources.walkFrames:_*)
	val punchAnim = new Animation(punchDuration / 4f, Resources.punchFrames:_*)
	
	setSize(walkAnim.getKeyFrame(0).getRegionWidth(), walkAnim.getKeyFrame(0).getRegionHeight())
	setOrigin(getWidth() / 2f, getHeight() / 2f)
	setPosition(0, ground.y)
	
	def center() = new Vector2(getX() + getOriginX(), getY() + getOriginY())
	
	var mvmt = new Movement()
	
	val punchboxLeft = RectangleUtil(Position(3,50), Size(50,50))
	val punchboxRight = RectangleUtil(Position(100,50), Size(50,50))
	def punchbox() = {
		val pb = if (mvmt.facingRight) punchboxRight else punchboxLeft
		RectangleUtil(pb, getX(), getY())
	}
	
	// Magic numbers! Hurray!
	val hbSize = Size(getWidth() / 1.7f, getHeight() / 1.5f)
	val baseHitbox = RectangleUtil(Position(//0,0),
			(getWidth() - hbSize.w) / 2f,
			(getHeight() - hbSize.h) / 4f),
			hbSize)
	def hitbox() = RectangleUtil(baseHitbox, Position(getX(), getY()))
	
	override def act(delta: Float): Unit = {
		super.act(delta)
		
		mvmt.update(delta, isAirborne)
		setX(getX() + (mvmt.travelVelocity * delta).toFloat)
		setY(getY() + (mvmt.jumpVelocity * delta).toFloat)

		if (getX() < GameUI.level.leftFence)
			setX(GameUI.level.leftFence)
		else if (getX() > GameUI.level.rightFence)
			setX(GameUI.level.rightFence)
		
		// Jump over!
		if (getY() <= ground.y) {
			setY(ground.y)
			mvmt.jumpTime = 0
			mvmt.jumpVelocity = 0
		}
		
		// Punch over!
		if (mvmt.punch && mvmt.punchTime > punchDuration) {
			mvmt.punch = false
			mvmt.punchTime = 0
		}
	}
	
	def isAirborne() = getY() > ground.y
	
	override def draw(batch: Batch, parentAlpha: Float) = {
		batch.setColor(1,1,1,parentAlpha)
		val tex = mvmt.punch match {
			case true => punchAnim.getKeyFrame(mvmt.punchTime.toFloat, false)
			case false => walkAnim.getKeyFrame(mvmt.walkTime.toFloat, true)
		}
		setScaleX(if (mvmt.facingRight) 1 else -1)
		batch.draw(tex, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation())
		
//		val pb = punchbox()
//		batch.setColor(1,0,0,.5f)
//		batch.draw(Resources.pixel, pb.x, pb.y, pb.width, pb.height)
		
//		val hb = hitbox()
//		batch.setColor(1,0,0,.5f)
//		batch.draw(Resources.pixel, hb.x, hb.y, hb.width, hb.height)
	}
	
	class Input extends InputAdapter {
		override def keyDown(keycode: Int): Boolean = {
			keycode match {
				case Input.Keys.A => mvmt.left = true; true
				case Input.Keys.D => mvmt.right = true; true
				case Input.Keys.W => mvmt.jump = true; true
				case Input.Keys.SPACE => 
					if (mvmt.punch == true) return true
					mvmt.punch = true;
					val me = Monster.this
					me.addAction(PunchEvent.getPunchAction(0.1f, me))
					true
				case _ => false
			}
		}
		
		override def keyUp(keycode: Int) = {
			keycode match {
				case Input.Keys.A => mvmt.left = false; true
				case Input.Keys.D => mvmt.right = false; true
				case Input.Keys.W => mvmt.jump = false; true
				case Input.Keys.SPACE => true // Punch code will set mvmt.punch to false
				case _ => false
			}
		}
	}
}

object Movement {
	val walkVelocity = 400d // Maximum left-to-right velocity.
	val jumpVelocity = 800d // Maximum jump velocity.
	
	val gravity = 1600d // Effect of gravity.
	val airTraction = 600d // How quickly the player can change direction in mid air.
	
	val punchDuration = 0.25f
	
	val ground = Position(0, 80)
	val jumpTime = .3f
}
class Movement() {
	var left, right, jump, punch: Boolean = false
	
	var facingRight = true
	
	var jumpTime, walkTime, punchTime = 0d
	
	var jumpVelocity = 0d
	var travelVelocity = 0d
	
	def update(delta: Float, isAirborne: Boolean) = isAirborne match {
		case true => airborne(delta)
		case false => grounded(delta)
	}
	
	private def airborne(delta: Float) = {
		// Keep track of jump time.
		jumpTime += delta
		
		// Factor in gravity if jump is no longer in effect.
		if (!jump || jumpTime > Movement.jumpTime)
			jumpVelocity -= Movement.gravity * delta
		
		if (punch)
			punchTime += delta
			
		if (left ^ right) {
			if (left)
				travelVelocity -= Movement.airTraction * delta
			if (right)
				travelVelocity += Movement.airTraction * delta
			facingRight = right
			travelVelocity = travelVelocity.min(Movement.walkVelocity).max(-Movement.walkVelocity)
		}
	}
	
	private def grounded(delta: Float) = {
		// If we're not currently jumping, but we've pressed the jump button, start jump velocity.
		if (jump) {
			jumpVelocity = Movement.jumpVelocity
			walkTime = 0
		}
		
		if (punch)
			punchTime += delta
		
		if (left ^ right) {
			if (left) {
				travelVelocity = -Movement.walkVelocity
			} else {
				travelVelocity = Movement.walkVelocity
			}
			facingRight = right
			walkTime += delta
		} else {
			travelVelocity = 0
			walkTime = 0
		}
	}
}