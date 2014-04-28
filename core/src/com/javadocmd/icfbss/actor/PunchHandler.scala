package com.javadocmd.icfbss.actor

import com.badlogic.gdx.scenes.scene2d.Event
import com.javadocmd.libgdx.Position
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.badlogic.gdx.scenes.scene2d.Action
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.Actor
import com.javadocmd.icfbss.screen.game.GameUI
import com.badlogic.gdx.math.Rectangle
import com.javadocmd.libgdx._
import com.badlogic.gdx.math.Vector2
import com.javadocmd.icfbss.screen.Resources

class PunchHandler extends EventListener {

	override def handle(e: Event): Boolean = e match {
		case PunchEvent(punchbox) =>
			Resources.swipe.play()
			GameUI.actors.buildings foreach { b =>
				if (punchbox.overlaps(b.rectangle))
					b.applyPunch(punchbox)
			}
			GameUI.actors.helicopters foreach { h =>
				if (punchbox.overlaps(h.hitbox)) {
					h.fire(new Destroyed(Destructable.Helicopter, h.hitbox.getCenter(new Vector2())))
					h.remove()
				}
			}
			true
		case _ => false
	}
}

case class PunchEvent(box: Rectangle) extends Event

object PunchEvent {
	def getPunchAction(wait: Float, monster: Monster): Action = Actions.sequence(
		Actions.delay(wait),
		Actions.run(new Runnable() {
			override def run() = {
				monster.fire(PunchEvent(monster.punchbox()))
			}
		})
	)
}