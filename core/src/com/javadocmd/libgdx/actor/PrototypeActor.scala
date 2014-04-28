package com.javadocmd.libgdx.actor

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.utils.Align
import com.badlogic.gdx.scenes.scene2d.utils.TiledDrawable
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.javadocmd.libgdx.Offset
import com.javadocmd.libgdx.Size

trait SimpleDrawable {
	val color: Color = new Color(0f, 0f, 0f, 1f);
	def color(color: Color): SimpleDrawable = { this.color.set(color); return this }
	
	def draw(batch: Batch, parentAlpha: Float)
	
	def setBatchColor(batch: Batch, parentAlpha: Float) {
		batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
	}
}

trait BasicResources {
	def pixel: TextureRegion
	def skin: Skin
}

abstract class PrototypeActor(resources: BasicResources) extends Actor {
	def drawables: Seq[SimpleDrawable]
	
	override def draw(batch: Batch, parentAlpha: Float) = {
		drawables foreach { _.draw(batch, parentAlpha) }
	}
	
	class Tiled(t: TiledDrawable, p: Offset, size: Size) extends SimpleDrawable {
		override def draw(batch: Batch, parentAlpha: Float) {
			setBatchColor(batch, parentAlpha)
			t.draw(batch, getX() + p.dx, getY() + p.dy, size.w, size.h);
		}
	}
	class Box(p: Offset, size: Size, color: Color, center: Offset) extends SimpleDrawable {
		def this(p: Offset, size: Size, color: Color) = {
			this(p, size, color, Offset(size.w / 2, size.h / 2))
		}
		color(color)
		override def draw(batch: Batch, parentAlpha: Float) {
			setBatchColor(batch, parentAlpha)
			batch.draw(resources.pixel, getX() + p.dx, getY() + p.dy, center.dx, center.dy, 
					size.w, size.h, getScaleX(), getScaleY(), getRotation());
		}
	}
	class Text(text: String, p: Offset, size: Size) extends SimpleDrawable {
		private val label = new Label(text, resources.skin)
		label.setSize(size.w, size.h)
		label.setWrap(true)
		label.setAlignment(Align.top, Align.left)
		override def draw(batch: Batch, parentAlpha: Float) {
			setBatchColor(batch, parentAlpha)
			label.setPosition(getX() + p.dx, getY() + p.dy)
			label.draw(batch, parentAlpha)
		}
	}
	class VerticalLine(x: Float, weight: Float) extends SimpleDrawable {
		override def draw(batch: Batch, parentAlpha: Float) {
			setBatchColor(batch, parentAlpha)
			batch.draw(resources.pixel, getX() + x, getY(), weight, getHeight());
		}
	}
	class HorizontalLine(y: Float, weight: Float) extends SimpleDrawable {
		override def draw(batch: Batch, parentAlpha: Float) {
			setBatchColor(batch, parentAlpha)
			batch.draw(resources.pixel, getX(), getY() + y, getWidth(), weight);
		}
	}
}
