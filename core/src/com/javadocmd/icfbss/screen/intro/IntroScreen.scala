package com.javadocmd.icfbss.screen.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.InputAdapter
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.ScreenAdapter
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.javadocmd.libgdx.implicitColorFromHexLong
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.math.Vector2
import com.javadocmd.icfbss.actor.HitEvent
import com.javadocmd.icfbss.actor.DeathEvent
import com.javadocmd.libgdx.Size
import com.javadocmd.icfbss.screen.Resources
import com.javadocmd.icfbss.IcfbssGame
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.badlogic.gdx.utils.viewport.Viewport
import com.badlogic.gdx.scenes.scene2d.Stage
import com.javadocmd.icfbss.actor.IntroActor

class IntroScreen(game: IcfbssGame) extends ScreenAdapter {
	
	var size = Size(Gdx.graphics.getWidth(), Gdx.graphics.getHeight())
	val bg: Color = 0x000000
	
	var viewport: Viewport = _
	var stage: Stage = _
	
	var introActor: IntroActor = _
	
	override def show() = {
		viewport = new ScreenViewport()
		viewport.update(size.w.toInt, size.h.toInt, true)
		
		stage = new Stage()
		stage.setViewport(viewport)
		
		introActor = new IntroActor()
		introActor.setPosition(0,0)
		introActor.setSize(size.w, size.h)
		stage.addActor(introActor)
		
		Gdx.input.setInputProcessor(IntroInput)
	}
	override def hide() = {
		Gdx.input.setInputProcessor(null)
	}
	override def resize(width: Int, height: Int) = {
		size = Size(width, height)
		viewport.update(width, height, true)
		introActor.setSize(size.w, size.h)
	}
	
	override def render(delta: Float) = {
		Gdx.gl.glClearColor(bg.r, bg.g, bg.b, bg.a);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		stage.act()
		stage.draw()
	}
	
	object IntroInput extends InputAdapter {
		override def keyDown(keycode: Int) = keycode match {
			case Input.Keys.ESCAPE => Gdx.app.exit(); true
			case Input.Keys.SPACE => game.startGame(); true
			case _ => false
		}
	}
	
	override def dispose() = {
		stage.dispose()
	}
}