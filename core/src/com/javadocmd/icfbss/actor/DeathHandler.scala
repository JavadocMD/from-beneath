package com.javadocmd.icfbss.actor

import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.javadocmd.icfbss.screen.game.GameUI

class DeathHandler extends EventListener {

	override def handle(e: Event): Boolean = e match {
		case DeathEvent() =>
			if (GameUI.done)
				return true
			else
				GameUI.done = true
			GameUI.actors.blackCurtain.fadeIn({
				GameUI.actors.instructions.clearActions()
				GameUI.actors.instructions.flyIn(
					"Zungo has defeated you!\n" +
					"I guess you'll have to find some other ocean to live in.\n\n" +
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

case class DeathEvent() extends Event