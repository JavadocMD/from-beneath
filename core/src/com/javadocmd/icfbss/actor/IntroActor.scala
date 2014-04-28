package com.javadocmd.icfbss.actor

import com.javadocmd.icfbss.screen.Resources
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.graphics.g2d.Batch
import com.javadocmd.libgdx.Size
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.math.Interpolation

class IntroActor extends Actor {
	
	val frames = Seq(
		Resources.atlas.findRegion("intro1"),
		Resources.atlas.findRegion("intro2"),
		Resources.atlas.findRegion("intro3"),
		Resources.atlas.findRegion("intro4"),
		Resources.atlas.findRegion("intro5")
	)
	val frameSize = Size(frames(0).getRegionWidth(), frames(0).getRegionHeight())
	
	var index = 0
	val delay = 5f
	val transition = 1.5f
	
	addAction(Actions.sequence(
		Actions.delay(3f),
			
		Actions.fadeOut(transition, Interpolation.pow2),
		Actions.run(new Runnable() { override def run() = { index += 1 } }),
		Actions.fadeIn(transition, Interpolation.pow2),
		
		Actions.delay(delay),
		
		Actions.fadeOut(transition, Interpolation.pow2),
		Actions.run(new Runnable() { override def run() = { index += 1 } }),
		Actions.fadeIn(transition, Interpolation.pow2),
		
		Actions.delay(delay),
		
		Actions.fadeOut(transition, Interpolation.pow2),
		Actions.run(new Runnable() { override def run() = { index += 1 } }),
		Actions.fadeIn(transition, Interpolation.pow2),
		
		Actions.delay(delay),
		
		Actions.fadeOut(transition, Interpolation.pow2),
		Actions.run(new Runnable() { override def run() = { index += 1 } }),
		Actions.fadeIn(transition, Interpolation.pow2)
	))
	
	override def draw(batch: Batch, parentAlpha: Float) = {
		batch.setColor(1,1,1,getColor().a)
		batch.draw(frames(index), getWidth() / 2f - frameSize.w / 2f, getHeight() / 2f - frameSize.h / 2f)
	}
}