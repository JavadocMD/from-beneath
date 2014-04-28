package com.javadocmd.icfbss

import com.badlogic.gdx.Game
import com.badlogic.gdx.Screen
import com.javadocmd.icfbss.screen.Resources
import com.javadocmd.icfbss.screen.game.GameScreen
import com.javadocmd.icfbss.screen.game.IntroScreen

class IcfbssGame extends Game {

	var gameScreen: Screen = _
	var introScreen: Screen = _
	
	override def create() = {
		startIntro()
	}
	
	def startIntro() = {
		introScreen = new IntroScreen(this)
		setScreen(introScreen)
	}
	
	def startGame() = {
		gameScreen = new GameScreen()
		setScreen(gameScreen)
	}
	
	override def dispose() = {
		Resources.dispose()
	}
}