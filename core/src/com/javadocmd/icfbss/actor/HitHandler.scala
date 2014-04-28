package com.javadocmd.icfbss.actor

import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.javadocmd.icfbss.screen.game.GameUI

class HitHandler extends EventListener {

	override def handle(e: Event) = e match {
		case HitEvent(bullet) =>
			bullet foreach { _.remove() }
			GameUI.actors.life.doDamage(1)
			GameUI.actors.redCurtain.flash({})
			true
		case _ => false
	}
}

case class HitEvent(bullet: Option[Bullet]) extends Event