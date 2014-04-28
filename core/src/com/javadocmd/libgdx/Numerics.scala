package com.javadocmd.libgdx

import com.badlogic.gdx.math.Rectangle

object RectangleUtil {
	def apply(p: Position, s: Size) = new Rectangle(p.x, p.y, s.w, s.h)
	def apply(r: Rectangle, p: Position): Rectangle = apply(r, p.x, p.y)
	def apply(r: Rectangle, x: Float, y: Float): Rectangle = {
		val newRect = new Rectangle(r)
		newRect.setPosition(newRect.x + x, newRect.y + y)
		newRect
	}
}

case class Position(val x: Float, val y: Float) {
	def +(o: Offset): Position = Position(x + o.dx, y + o.dy)
	def +(s: Size): Position = Position(x + s.w, y + s.w)
}
case class Velocity(val x: Float, val y: Float)
case class Acceleration(val x: Float, val y: Float)
case class Offset(val dx: Float, val dy: Float)

case class Size(val w: Float, val h: Float)
case class SizeI(val w: Int, val h: Int)