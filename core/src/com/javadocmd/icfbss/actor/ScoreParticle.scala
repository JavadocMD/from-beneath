package com.javadocmd.icfbss.actor

import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import com.badlogic.gdx.scenes.scene2d.utils.Align
import com.javadocmd.icfbss.screen.Resources
import com.javadocmd.libgdx.Position
import com.javadocmd.icfbss.screen.game.GameUI

trait Particle extends Actor

class ScoreParticle(val center: Position, val desc: String, val points: Int) 
	extends Label("", Resources.skin.get(classOf[Label.LabelStyle])) with Particle {
	
	setWidth(200)
	setOriginX(100)
	setPosition(center.x, center.y)
	
	setText("%s!\n+%d".format(desc, points))
	setAlignment(Align.center)
	
	addAction(Actions.sequence(
		Actions.moveBy(0, 200, 0.5f),
		Actions.fadeOut(1f, new Interpolation.PowOut(3)),
		Actions.removeActor()
	))
}