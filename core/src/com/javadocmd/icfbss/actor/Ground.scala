package com.javadocmd.icfbss.actor

import com.javadocmd.icfbss.screen.Resources
import com.javadocmd.libgdx.Offset
import com.javadocmd.libgdx.Size
import com.javadocmd.libgdx.actor.PrototypeActor
import com.javadocmd.libgdx.implicitColorFromHexLong
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.utils.TiledDrawable
import com.javadocmd.icfbss.screen.game.GameUI

class Ground() extends Actor {
	setPosition(0,0)
	setSize(0, 100)
	
	val land: Color = 0x33AA33
	val ocean: Color = 0x3333AA
	val default: Color = 0xFFFFFF
	
	val beach = Resources.atlas.findRegion("beach")
	
	val skyline = new TiledDrawable(Resources.atlas.findRegion("skyline"))
	val skylineHeight = skyline.getRegion().getRegionHeight().toFloat
	
	override def draw(batch: Batch, parentAlpha: Float) = {
		land.a = parentAlpha
		ocean.a = parentAlpha
		default.a = parentAlpha
		
		batch.setColor(land)
		batch.draw(Resources.pixel, 0, getY(), getWidth() + 2000, getHeight())
		
		batch.setColor(ocean)
		batch.draw(Resources.pixel, 0, getY(), -2000, getHeight())
		
		batch.setColor(default)
		batch.draw(beach, -200f, 0, beach.getRegionWidth(), beach.getRegionHeight())
		skyline.draw(batch, 300f - GameUI.actors.monster.getX() / 25f, getHeight(), getWidth() + 2000, skylineHeight)
	}
}