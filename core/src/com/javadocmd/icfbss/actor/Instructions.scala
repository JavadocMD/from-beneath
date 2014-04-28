package com.javadocmd.icfbss.actor

import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.javadocmd.icfbss.screen.Resources
import com.javadocmd.libgdx.Size
import com.badlogic.gdx.scenes.scene2d.utils.Align
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.math.Interpolation
import com.javadocmd.icfbss.screen.game.GameUI

class Instructions() extends Label("", Resources.skin.get("big", classOf[Label.LabelStyle])) {
	setWidth(GameUI.size.w)
	setOriginX(GameUI.size.w / 2f)
	setPosition(0, -150f)
	setAlignment(Align.center + Align.top)
	setWrap(true)
	
	def flyBy(text: String, readTime: Float) = {
		addAction(Actions.after(Actions.sequence(
			Actions.run(new Runnable() {
				override def run() {
					setText(text)
					setPosition(0, -150f)
				}
			}),
			Actions.moveTo(0, GameUI.size.h - 150f, 0.6f, Interpolation.swingOut),
			Actions.delay(readTime),
			Actions.moveTo(0, GameUI.size.h + 650f, 0.6f, Interpolation.swingIn)
		)))
	}
	
	def flyIn(text: String) = {
		addAction(Actions.after(Actions.sequence(
			Actions.run(new Runnable() {
				override def run() {
					setText(text)
					setPosition(0, -150f)
				}
			}),
			Actions.moveTo(0, GameUI.size.h - 150f, 0.6f, Interpolation.swingOut)
		)))
	}
}