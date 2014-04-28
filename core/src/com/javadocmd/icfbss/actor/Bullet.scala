package com.javadocmd.icfbss.actor

import com.badlogic.gdx.math.Vector2
import com.javadocmd.libgdx.Position
import com.javadocmd.icfbss.screen.Resources
import com.javadocmd.icfbss.screen.game.GameUI
import com.badlogic.gdx.graphics.g2d.Batch
import com.javadocmd.libgdx.RectangleUtil
import com.javadocmd.libgdx.Size

class Bullet(start: Position, direction: Vector2) extends Particle {
	
	val tex = Resources.atlas.findRegion("bullet")
	setPosition(start.x,start.y)
	setSize(tex.getRegionWidth(), tex.getRegionHeight())
	setOrigin(getWidth() / 2f, getHeight() / 2f)
	
	val speed = 200f
	val velocity = new Vector2(direction).nor().scl(speed)
	
	val hbSize = Size(getWidth() / 1.6f, getHeight() / 1.6f)
	val baseHitbox = RectangleUtil(Position(-hbSize.w / 2f, -hbSize.h / 2f), hbSize)
	def hitbox() = RectangleUtil(baseHitbox, getX(), getY())
	
	val timeToLive = 5f
	var life = 0f
	
	override def act(delta: Float) = {
		super.act(delta)
		
		setX(getX() + velocity.x * delta)
		setY(getY() + velocity.y * delta)
		
		if (getX() < GameUI.level.leftFence - 2000 || getX() > GameUI.level.rightFence + 2000)
			remove()
		if (getY() < -100 || getY() > 2000)
			remove()
			
		life += delta
		if (life > timeToLive)
			remove()
	}
	
	override def draw(batch: Batch, parentAlpha: Float) = {
		batch.setColor(1,1,1,parentAlpha)
		batch.draw(tex, getX() - getOriginX(), getY() - getOriginY())
		
//		val hb = hitbox()
//		batch.setColor(1,0,0,.5f)
//		batch.draw(Resources.pixel, hb.x, hb.y, hb.width, hb.height)
	}
}