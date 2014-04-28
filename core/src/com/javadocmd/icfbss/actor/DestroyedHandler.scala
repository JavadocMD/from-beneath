package com.javadocmd.icfbss.actor

import scala.util.Random
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.javadocmd.icfbss.screen.game.GameUI
import com.javadocmd.libgdx.Position
import com.javadocmd.icfbss.screen.Resources

class DestroyedHandler extends EventListener {

	val victoryGoal = 12
	var victoryPoints = 0
	
	val explosions = Seq(Resources.explosion1, 
			Resources.explosion2,
			Resources.explosion3,
			Resources.explosion4,
			Resources.explosion5)
	
	val random = new Random()
	
	def pick[T](options: Seq[T]): T = options(random.nextInt(options.size))
	
	override def handle(e: Event): Boolean = e match {
		case Destroyed(d,c) =>
			GameUI.actors.score.addScore(d.points)
			val p = new ScoreParticle(Position(c.x, c.y), pick(d.desc), d.points)
			GameUI.actors.particles.addActor(p)
			
			pick(explosions).play()
			
			if (d == Destructable.ZungoBuildingSection) {
				victoryPoints += 1
				if (victoryPoints == victoryGoal)
					GameUI.actors.fore.fire(WinEvent())
			}
			
			true
		case _ => false
	}
}

sealed trait Destructable {
	def desc: Seq[String]
	def points: Int
}
object Destructable {
	case object BuildingSection extends Destructable {
		def desc = Seq(
			"Lawyers", "ISP", "24-7 News Media", "Shock Jocks", "Reply Girls",
			"Telemarketers", "Lobbyists", "Pay2Win Developers", "Pointy-Haired Bosses",
			"FCC", "Car Salesmen", "Blog Spammers", "Spammers", "Astroturfers", "Trolls",
			"Pollsters", "Talking Heads", "Phone Sanitizers", "Monster Hunters", "Half Life 3 Team",
			"Marketers", "Elevator Musicians", "Mimes", "Day Traders", "Insurance Salesmen", 
			"Meteorologists", "Parking Enforcers", "Auditors", "Nickelback", "Nicolas Cage",
			"Millionaire Matchmakers", "Plastic Surgeons", "Uwe Boll", "Patent Trolls", "Reality TV Producers",
			"Bankers", "The Kardashians", "Publisher's Clearing House"
		)
		def points = 1000
	}
	case object ZungoBuildingSection extends Destructable {
		def desc = Seq(
			"Take that"
		)
		def points = 50000
	}
	case object Helicopter extends Destructable {
		def desc = Seq("Boom", "Pow", "Zok", "Blammo", "Kapow", "Crash", "Kronk", "Bash", "Biff", "Bop", "Powee")
		def points = 2000
	}
	case object Tank extends Destructable {
		def desc = Seq("Smash", "Squish", "Crunch", "Splat")
		def points = 1500
	}
}

case class Destroyed(d: Destructable, center: Vector2) extends Event