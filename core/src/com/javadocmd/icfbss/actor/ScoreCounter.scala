package com.javadocmd.icfbss.actor

import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.javadocmd.icfbss.screen.game.GameUI
import com.javadocmd.icfbss.screen.Resources
import com.javadocmd.libgdx.Position
import com.badlogic.gdx.scenes.scene2d.utils.Align

class ScoreCounter extends Label("", Resources.skin.get("big", classOf[Label.LabelStyle])) {
	setAlignment(Align.center)
	
	var score = 0L
	addScore(0)
	
	def reset() = {
		score = 0
		addScore(0)
	}
	
	def addScore(points: Int) = {
		score += points
		this.setText("Score: " + score)
	}
}