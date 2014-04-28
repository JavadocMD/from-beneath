package com.javadocmd.icfbss.actor

import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.javadocmd.icfbss.screen.Resources
import com.badlogic.gdx.scenes.scene2d.utils.Align

class LifeCounter extends Label("", Resources.skin.get("big", classOf[Label.LabelStyle])) {
	setAlignment(Align.center)
	
	var life = 20L
	doDamage(0)
	
	def reset() = {
		life = 20
		doDamage(0)
	}
	
	def doDamage(damage: Int) = {
		life -= damage
		life = life.max(0)
		this.setText("Life: " + life)
		
		if (life == 0)
			fire(DeathEvent())
	}
}