package com.javadocmd.icfbss.screen.game

import com.javadocmd.icfbss.actor.Building
import com.javadocmd.icfbss.actor.Ground
import com.javadocmd.libgdx.Position
import com.javadocmd.libgdx.SizeI
import com.javadocmd.icfbss.actor.Helicopter
import com.javadocmd.icfbss.actor.ZungoBuilding

trait Level {
	def load(): Unit
	def leftFence: Float
	def rightFence: Float
	
	val triggers = scala.collection.mutable.Queue[Trigger]()
	
	def checkTriggers(playerX: Float) = {
		triggers.dequeueAll(_.x < playerX) foreach { _.f() }
	}
}

abstract class Trigger(val x: Float, val f: ()=>Unit)
case class TextTrigger(myX: Float, readTime: Float, text: String)
	extends Trigger(myX, () => GameUI.actors.instructions.flyBy(text, readTime))

object LevelOne extends Level {
	val y = 90
	val u = 450 // handy building spacing unit
	
	override def leftFence = 0
	override def rightFence = 50 * u
	override def load() = {
		GameUI.actors.monster.setX(100)
		
		GameUI.actors.ground.setWidth(rightFence)
		
		triggers.clear()
		triggers += TextTrigger(0, 4f, "Use A and D to move\nW to jump\nSpace to punch")
		triggers += TextTrigger(0, 3f, "Now go and seek your vengeance against Zungo Corp")
		triggers += TextTrigger(3 * u, 3f, "Try punching this building for points!")
		triggers += TextTrigger(9 * u, 4f, "Watch out!\nZungo Corp is sure to send its attack choppers after you")
		triggers += TextTrigger(37 * u, 3f, "Zungo Corp tower is just ahead!")
		triggers += TextTrigger(45 * u, 4f, "Destroy Zungo Corp!")
		
		val bldgs = Seq(
			new Building(Position(4 * u,  y), SizeI(2,2)),
			
//			new ZungoBuilding(Position(1 * u, y), SizeI(2,2)),
//			new ZungoBuilding(Position(2 * u, y), SizeI(2,2)),
//			new ZungoBuilding(Position(3 * u, y), SizeI(2,2)),
			
			new Building(Position(6 * u, y), SizeI(2,2)),
			new Building(Position(7 * u, y), SizeI(2,2)),
			new Building(Position(8 * u, y), SizeI(2,2)),
			new Building(Position(9 * u, y), SizeI(2,2)),
			new Building(Position(10 * u, y), SizeI(2,2)),
			
			new Building(Position(12 * u, y), SizeI(2,2)),
			new Building(Position(13 * u, y), SizeI(2,2)),
			new Building(Position(14 * u, y), SizeI(2,2)),
			
			new Building(Position(16 * u, y), SizeI(2,2)),
			new Building(Position(17 * u, y), SizeI(2,2)),
			new Building(Position(18 * u, y), SizeI(2,2)),
			new Building(Position(19 * u, y), SizeI(2,2)),
			
			new Building(Position(21 * u, y), SizeI(2,2)),
			new Building(Position(22 * u, y), SizeI(2,2)),
			new Building(Position(23 * u, y), SizeI(2,2)),
			
			new Building(Position(25 * u, y), SizeI(2,2)),
			new Building(Position(26 * u, y), SizeI(2,2)),
			new Building(Position(27 * u, y), SizeI(2,3)),
			new Building(Position(28 * u, y), SizeI(2,3)),
			new Building(Position(29 * u, y), SizeI(2,2)),
			new Building(Position(30 * u, y), SizeI(2,3)),
			
			new Building(Position(32 * u, y), SizeI(2,3)),
			new Building(Position(33 * u, y), SizeI(2,3)),
			new Building(Position(34 * u, y), SizeI(2,3)),
			new Building(Position(35 * u, y), SizeI(2,3)),
			new Building(Position(36 * u, y), SizeI(2,3)),
			new Building(Position(37 * u, y), SizeI(2,3)),
			new Building(Position(38 * u, y), SizeI(2,3)),
			new Building(Position(39 * u, y), SizeI(2,3)),
			
			new ZungoBuilding(Position(46 * u, y), SizeI(2,2)),
			new ZungoBuilding(Position(47 * u, y), SizeI(2,2)),
			new ZungoBuilding(Position(48 * u, y), SizeI(2,2))
		)
		
		bldgs foreach { GameUI.actors.buildings.addActor(_) }
		
		val helis = Seq(
			new Helicopter(Position(14 * u, 500)),
			
			new Helicopter(Position(18 * u, 500)),
			
			new Helicopter(Position(24 * u, 400)),
			new Helicopter(Position(24 * u, 500)),
			new Helicopter(Position(24 * u, 200)),
			
			new Helicopter(Position(29 * u, 1200)),
			
			new Helicopter(Position(32 * u, 400)),
			new Helicopter(Position(32 * u, 100)),
			
			new Helicopter(Position(36 * u, 800)),
			new Helicopter(Position(36 * u, 1200)),
			new Helicopter(Position(37 * u, 500)),
			new Helicopter(Position(38 * u, 600)),
			new Helicopter(Position(39 * u, 1500)),
			new Helicopter(Position(40 * u, 300)),
			
			new Helicopter(Position(46 * u, 600)),
			new Helicopter(Position(47 * u, 1500)),
			new Helicopter(Position(48 * u, 300))
		)
		
		helis foreach { GameUI.actors.helicopters.addActor(_) }
	}
}