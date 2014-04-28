package com.javadocmd.libgdx

import com.badlogic.gdx.scenes.scene2d.EventListener

package object scene2d {
	type CanListen = { def addListener(e: EventListener): Boolean }
}