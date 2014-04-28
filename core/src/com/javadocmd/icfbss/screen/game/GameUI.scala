package com.javadocmd.icfbss.screen.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.badlogic.gdx.utils.viewport.Viewport
import com.javadocmd.icfbss.actor.Building
import com.javadocmd.icfbss.actor.Ground
import com.javadocmd.icfbss.actor.Monster
import com.javadocmd.icfbss.screen.Resources
import com.javadocmd.libgdx.Position
import com.javadocmd.libgdx.Size
import com.javadocmd.libgdx.SizeI
import com.javadocmd.libgdx.actor.Guides
import com.javadocmd.libgdx.implicitColorFromHexLong
import scala.collection._
import com.badlogic.gdx.scenes.scene2d.Group
import com.javadocmd.icfbss.actor.PunchHandler
import com.javadocmd.icfbss.actor.DestroyedHandler
import com.javadocmd.icfbss.actor.Particle
import com.javadocmd.libgdx.actor.TypedGroup
import com.javadocmd.icfbss.actor.ScoreCounter
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.math.Vector2
import com.javadocmd.icfbss.actor.Helicopter
import com.javadocmd.icfbss.actor.Bullet
import com.javadocmd.icfbss.actor.LifeCounter
import com.badlogic.gdx.scenes.scene2d.utils.Align
import com.javadocmd.icfbss.actor.DeathHandler
import com.javadocmd.icfbss.actor.Curtain
import com.javadocmd.icfbss.actor.Instructions
import com.javadocmd.icfbss.actor.HitEvent
import com.javadocmd.icfbss.actor.HitHandler
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.InputAdapter
import com.javadocmd.icfbss.actor.DeathEvent
import com.badlogic.gdx.Input
import com.javadocmd.icfbss.actor.VictoryHandler
import com.javadocmd.icfbss.actor.WinEvent

object GameUI {

	var done: Boolean = false
	var stage: Stage = _
	var viewport: Viewport = _
	var size = Size(Gdx.graphics.getWidth(), Gdx.graphics.getHeight())
	var level: Level = _
	
	def init() = {
		done = false
		viewport = new ScreenViewport()
		viewport.update(size.w.toInt, size.h.toInt, true)
		
		stage = new Stage()
		stage.setViewport(viewport)
		
		actors = new Actors()
		
		level = LevelOne
		level.load()
		
		val monster = GameUI.actors.monster
		Gdx.input.setInputProcessor(new InputMultiplexer(GeneralGameInput, new monster.Input()))
	}
	
	def reset() = {
		done = false
		stage.dispose()
		
		viewport = new ScreenViewport()
		viewport.update(size.w.toInt, size.h.toInt, true)
		
		stage = new Stage()
		stage.setViewport(viewport)
		
		actors = new Actors()
		
		level = LevelOne
		level.load()
		
		val monster = GameUI.actors.monster
		Gdx.input.setInputProcessor(new InputMultiplexer(GeneralGameInput, new monster.Input()))
		
		updateSize(size.w.toInt, size.h.toInt)
	}
	
	def updateSize(width: Int, height: Int) = {
		size = Size(width.toFloat, height.toFloat)
		viewport.update(width, height, true)
		
		// Hud has to fill the screen.
		actors.hud.setPosition(0,0)
		actors.hud.setSize(width, height)
		
		// So does fourthWall
		actors.fourthWall.setPosition(0,0)
		actors.fourthWall.setSize(width, height)
		
		actors.blackCurtain.setSize(size.w,size.h)
		actors.redCurtain.setSize(size.w,size.h)
		actors.instructions.setWidth(size.w)
	}
	
	def moveCam(dx: Float, dy: Float) = {
		actors.hud.moveBy(dx,dy)
		actors.fourthWall.moveBy(dx,dy)
	}
	
	def act(delta: Float) = {
		if (!done) {
			stage.act(delta)
		} else {
			actors.fourthWall.act(delta)
		}
		
		val monsterHb = actors.monster.hitbox()
		actors.bullets foreach { b =>
			if (b.hitbox().overlaps(monsterHb))
				b.fire(new HitEvent(Some(b)))
		}
		
		level.checkTriggers(actors.monster.getX())
	}
	
	var actors: Actors = _
	class Actors {
		val back = add(new Group())
		val mid  = add(new Group())
		val fore = add(new Group())
		val hud  = add(new Group())
		
		val fourthWall = add(new Group())
		val blackCurtain = add(new Curtain(size, 0x000000), fourthWall)
		val redCurtain = add(new Curtain(size, 0xFF0000), fourthWall)
		val instructions = add(new Instructions(), fourthWall)
		
		val ground = add(new Ground(), mid)
		val buildings = add(new TypedGroup()(classOf[Building]), mid)
		
		val bullets = add(new TypedGroup()(classOf[Bullet]), fore)
		val monster = add(new Monster(), fore)
		val helicopters = add(new TypedGroup()(classOf[Helicopter]), fore)
		val particles = add(new Group(), fore)
		
		val guides = add(new Guides(size, 0x111111, Resources), hud)
		//guides.addGrid(50)
		
		val score = new ScoreCounter()
		val life = new LifeCounter()
		
		private val layout = add(new Table(), hud)
		layout.setFillParent(true)
		layout.center().top().padTop(10)
		layout.add(life).align(Align.center).minWidth(300)
		layout.add(score).align(Align.center).minWidth(500)
		layout.add().minWidth(300)
		
		stage.addListener(new PunchHandler())
		stage.addListener(new DestroyedHandler())
		stage.addListener(new HitHandler())
		stage.addListener(new DeathHandler())
		stage.addListener(new VictoryHandler())
	}
	
	def add[T <: Actor](actor: T): T = { stage.addActor(actor); actor }
	def add[T <: Actor](actor: T, group: Group): T = { group.addActor(actor); actor }
}

object GeneralGameInput extends InputAdapter {
	override def keyDown(keycode: Int) = keycode match {
		case Input.Keys.ESCAPE => Gdx.app.exit(); true
		case Input.Keys.H => GameUI.actors.fourthWall.fire(HitEvent(None)); true
		case Input.Keys.X => GameUI.actors.fourthWall.fire(DeathEvent()); true
		case Input.Keys.V => GameUI.actors.fourthWall.fire(WinEvent()); true
		case Input.Keys.R => 
			if (GameUI.done)
				GameUI.reset()
			true
		case _ => false
	}
}