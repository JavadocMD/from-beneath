package com.javadocmd.icfbss.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.utils.Disposable
import com.javadocmd.libgdx.actor.BasicResources

object Resources extends BasicResources with Disposable {
	val skin = new Skin(Gdx.files.internal("uiskin.json"))

	private val pixture = new Texture(Gdx.files.internal("pixel.png"))
	val pixel = new TextureRegion(pixture, 1, 1)
	
	val atlas = new TextureAtlas("icfbss.pack")
	
	val walkFrames = Array(atlas.findRegion("stand"),
			atlas.findRegion("walk1"),
			atlas.findRegion("stand"),
			atlas.findRegion("walk2"))
	val punchFrames = Array(atlas.findRegion("punch1"),
			atlas.findRegion("punch2"),
			atlas.findRegion("punch3"))
	
	val explosion1 = Gdx.audio.newSound(Gdx.files.internal("Explosion.wav"))
	val explosion2 = Gdx.audio.newSound(Gdx.files.internal("Explosion2.wav"))
	val explosion3 = Gdx.audio.newSound(Gdx.files.internal("Explosion3.wav"))
	val explosion4 = Gdx.audio.newSound(Gdx.files.internal("Explosion4.wav"))
	val explosion5 = Gdx.audio.newSound(Gdx.files.internal("Explosion5.wav"))
	val chopper = Gdx.audio.newSound(Gdx.files.internal("Chopper.wav"))
	val shoot = Gdx.audio.newSound(Gdx.files.internal("Shoot.wav"))
	val swipe = Gdx.audio.newSound(Gdx.files.internal("Swipe.wav"))
			
	def dispose() = { Seq(skin, pixture, atlas, explosion1, explosion2,
			explosion3, explosion4, explosion5, chopper, shoot) foreach { _.dispose() } }
}