package com.javadocmd.libgdx.scene2d

import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.scenes.scene2d.InputEvent

object When {
	def apply(listener: CanListen): ListenBuilder = new ListenBuilder(listener)
		
	class ListenBuilder(val listener: CanListen) {
		def clicked(action: => Unit): Unit = {
			val wc = new InputListener {
				override def touchDown(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int) = true
				override def touchUp(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int) = action
			}
			listener.addListener(wc)
		}
		
		def clicked(action: (Float,Float) => Unit): Unit = {
			val wc = new InputListener {
				override def touchDown(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int) = true
				override def touchUp(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int) = action(x,y)
			}
			listener.addListener(wc)
		}
		
		def keyed(key: Int)(action: => Unit): Unit = {
			val wk = new InputListener {
				override def keyUp(event: InputEvent, keycode: Int) = keycode match {
					case `key` => action; true
					case _ => false
				}
			}
			listener.addListener(wk)
		}
		
		def keyed(action: (Int) => Boolean): Unit = {
			val wk = new InputListener {
				override def keyUp(event: InputEvent, keycode: Int) = action(keycode)
			}
			listener.addListener(wk)
		}
	}
}