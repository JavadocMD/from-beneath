package com.javadocmd.icfbss.actor

import com.javadocmd.icfbss.screen.game.GameUI
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.badlogic.gdx.scenes.scene2d.Event

class VictoryHandler extends EventListener {

	override def handle(e: Event): Boolean = e match {
		case WinEvent() =>
			if (GameUI.done)
				return true
			else
				GameUI.done = true
			GameUI.actors.blackCurtain.fadeIn({
				GameUI.actors.instructions.clearActions()
				GameUI.actors.instructions.flyIn(
					"You have defeated Zungo Corp!\n" +
					"Now you can return to the ocean for a nice little nap.\n\n" +
					"press r to restart\n" +
					"or escape to quit\n\n" +
					"it came from beneath the surface of the sea\n" +
					"a javadocmd.com game"
				)
			})
			
			true
		case _ => false
	}
}

case class WinEvent() extends Event