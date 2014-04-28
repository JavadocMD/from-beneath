package com.javadocmd

import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.utils.SnapshotArray

package object libgdx {
	implicit def tupleToVector2f[T,U](xs: Tuple2[T,U])(implicit t:Numeric[T], u:Numeric[U]): com.badlogic.gdx.math.Vector2 = {
		new com.badlogic.gdx.math.Vector2(t.toFloat(xs._1), u.toFloat(xs._2))
	}
	
	implicit def tupleToVector3f[T,U,V](xs: Tuple3[T,U,V])(implicit t:Numeric[T], u:Numeric[U], v:Numeric[V]): com.badlogic.gdx.math.Vector3 = {
		new com.badlogic.gdx.math.Vector3(t.toFloat(xs._1), u.toFloat(xs._2), v.toFloat(xs._3))
	}
	
	implicit def implicit2RunnableAction(f: () => Unit): RunnableAction = new RunnableAction() { override def run = f() }

	implicit def implicitColorFromInts(t: Tuple4[Int, Int, Int, Int]): Color = {
		new Color(t._1 / 255.0f, t._2 / 255.0f, t._3 / 255.0f, t._4 / 255.0f)
	}
	implicit def implicitColorFromLongs(t: Tuple4[Long, Long, Long, Long]): Color = {
		new Color(t._1 / 255.0f, t._2 / 255.0f, t._3 / 255.0f, t._4 / 255.0f)
	}
	implicit def implicitColorFromHexLong(hex: Long): Color = {
		val h = hex match {
			case i if (i > 0XFFFFFFFFL) => throw new IllegalArgumentException("Long 0X" + i.toHexString + " is too large to be used as a color.");
			case i if (i < 0X00000000L) => throw new IllegalArgumentException("Long 0X" + i.toHexString + " is too small to be used as a color.");
			case i if (i < 0X01000000L) => (i << 8) | 0XFF // Provide alpha 100%
			case i => i
		}
		(h >> 24 & 0xFF, h >> 16 & 0xFF, h >> 8 & 0xFF, h & 0xFF)
	}
	
	class ColorWrapper(val color: Color) {
		def alpha(alpha: Float) = {
			val newColor = new Color(color)
			newColor.a = alpha
			newColor
		}
	}
	implicit def implicitColorWrapper(color: Color): ColorWrapper = new ColorWrapper(color)
	
	implicit def implicitSnapshotArrayIteration[T](sa: SnapshotArray[T]): scala.Iterable[T] = { sa.toIterable }
}