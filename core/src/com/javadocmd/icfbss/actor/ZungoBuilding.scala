package com.javadocmd.icfbss.actor

import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.javadocmd.libgdx.SizeI
import com.javadocmd.libgdx.Position
import com.javadocmd.icfbss.screen.Resources

class ZungoBuilding(myPos: Position, myDim: SizeI) extends Building(myPos, myDim) {
	
	// omg so much hax
	bldg.tiles(0)(1).texNrm = Resources.atlas.findRegion("zungo-top-1")
	
	override def applyPunch(punchBox: Rectangle) = {
		bldg.tiles.flatten.filterNot(_.damaged).foreach { t =>
			if (punchBox.overlaps(t.hitbox)) {
				t.damaged = true
				fire(Destroyed(Destructable.ZungoBuildingSection, t.rectangle.getCenter(new Vector2())))
			}
		}
	}
}