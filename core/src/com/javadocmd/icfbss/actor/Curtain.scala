package com.javadocmd.icfbss.actor

import com.javadocmd.icfbss.screen.Resources
import com.javadocmd.libgdx.Offset
import com.javadocmd.libgdx.Size
import com.javadocmd.libgdx.actor.PrototypeActor
import com.javadocmd.libgdx.implicitColorFromHexLong
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.Touchable
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.scenes.scene2d.Action

class Curtain(size: Size, color: Color) extends Actor {
	setPosition(0,0)
	setSize(size.w, size.h)
	setColor(color)
	setTouchable(Touchable.disabled)
	setVisible(false)
	
	override def draw(batch: Batch, parentAlpha: Float) = {
		val color = new Color(getColor())
		color.a *= parentAlpha
		batch.setColor(color)
		batch.draw(Resources.pixel, getX(), getY(), getWidth(), getHeight())
	}
	
	def setAlpha(alpha: Float) = {
		val color = getColor()
		color.a = alpha
		setColor(color)
	}
	
	def flash(then: => Unit) = {
		setAlpha(.3f)
		setVisible(true)
		
		addAction(Actions.sequence(
			Actions.fadeOut(0.4f, Interpolation.pow3Out),
			Actions.run(new Runnable() { override def run() = then })
		))
	}
	
	def fadeIn(then: => Unit) = {
		setAlpha(0)
		setVisible(true)
		
		addAction(Actions.sequence(
			Actions.alpha(0.5f, 3f, Interpolation.sineIn),
			Actions.run(new Runnable() { override def run() = then })
		))
	}
}