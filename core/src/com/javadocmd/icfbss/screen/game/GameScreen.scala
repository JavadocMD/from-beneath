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

class GameScreen extends ScreenAdapter {
	
	val rightFence = 721f //840f
	val leftFence = 719f //600f
	
	override def show() = {
		GameUI.init()
	}
	override def hide() = {
		Gdx.input.setInputProcessor(null)
	}
	override def resize(width: Int, height: Int) = {
		GameUI.updateSize(width, height)
	}
	
	override def render(delta: Float) = {
		val c: Color = 0x9099DD
		Gdx.gl.glClearColor(c.r, c.g, c.b, c.a);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		GameUI.act(delta)
		
		// Follow player if they go outside of the fences.
		val cam = GameUI.stage.getViewport().getCamera()
		val screenCoords = cam.project(new Vector3(GameUI.actors.monster.center(), 0))
		
		var camX = (screenCoords.x - rightFence).max(0)
		if (camX == 0)
			camX = (screenCoords.x - leftFence).min(0)
		if (camX != 0)
			moveCam(camX,0)
		
		GameUI.stage.draw()
	}
	
	def moveCam(dx: Float, dy: Float) = {
		val cam = GameUI.stage.getViewport().getCamera()
		cam.translate(dx,dy,0)
		GameUI.moveCam(dx,dy)
	}
}