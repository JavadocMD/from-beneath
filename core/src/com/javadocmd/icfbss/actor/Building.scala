package com.javadocmd.icfbss.actor

import scala.util.Random

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Actor
import com.javadocmd.icfbss.screen.Resources
import com.javadocmd.libgdx.Offset
import com.javadocmd.libgdx.Position
import com.javadocmd.libgdx.RectangleUtil
import com.javadocmd.libgdx.Size
import com.javadocmd.libgdx.SizeI

import NormalBuildingTiles.tileSize

class Building(val pos: Position, val dim: SizeI, val tileInfo: BuildingTileInfo = NormalBuildingTiles) extends Actor {
	setPosition(pos.x, pos.y)
	
	val bldg = new BuildingTiles(dim)
	val rectangle = RectangleUtil(pos, Size(dim.w * tileSize.width, dim.h * tileSize.height))
	
	def applyPunch(punchBox: Rectangle) = {
		bldg.tiles.flatten.filterNot(_.damaged).foreach { t =>
			if (punchBox.overlaps(t.hitbox)) {
				t.damaged = true
				fire(Destroyed(Destructable.BuildingSection, t.rectangle.getCenter(new Vector2())))
			}
		}
	}
	
	override def draw(batch: Batch, parentAlpha: Float) = {
		batch.setColor(1f, 1f, 1f, parentAlpha)
		bldg.tiles.flatten.foreach { t =>
			val d = if (t.flip) Offset(t.offset.dx + tileSize.width, t.offset.dy) else t.offset
			val scaleX = if (t.flip) -1 else 1
			batch.draw(t.tex, getX() + d.dx, getY() + d.dy, 0, 0, tileSize.width, tileSize.height, scaleX, 1, 0)
		}
	}
	
	class BuildingTiles(val dimension: SizeI) {
		val tiles = Array.ofDim[Tile](dimension.w, dimension.h)
		
		for (i <- 0 until dimension.w;
			 j <- 0 until dimension.h) {
			val flip = i != 0 // Flipping strategy only works if dim.w == 2 obviously.
			
			val (tex, texDmg) = (i,j) match {
				case (_,0) => (tileInfo.pick(tileInfo.btms), tileInfo.pick(tileInfo.btmsDmg))
				case (_,y) if y == dimension.h - 1 => (tileInfo.pick(tileInfo.tops), tileInfo.pick(tileInfo.topsDmg))
				case (_,_) => (tileInfo.pick(tileInfo.mids), tileInfo.pick(tileInfo.midsDmg))
			}
			
			tiles(i)(j) = new Tile(tex, texDmg, Offset(i * tileSize.width, j * tileSize.height), flip)
		}
		
		class Tile(var texNrm: TextureRegion, var texDmg: TextureRegion, val offset: Offset, val flip: Boolean) {
			var damaged = false
			val rectangle = RectangleUtil(tileSize, pos + offset)
			val hitbox = RectangleUtil(tileInfo.tileHitbox, pos + offset)
			
			def tex() = damaged match {
				case true => texDmg
				case false => texNrm
			}
		}
	}
}

trait BuildingTileInfo {
	val random = new Random(193) // Happy Primes are the Best Primes
	val tileSize = new Rectangle(0,0,200,300)
	val tileHitbox = new Rectangle(20,20,160,240)
	
	val tops, mids, btms: Seq[TextureRegion]
	val topsDmg, midsDmg, btmsDmg: Seq[TextureRegion]
	
	def pick(texSeq: Seq[TextureRegion]) = texSeq(random.nextInt(texSeq.size))
}
object NormalBuildingTiles extends BuildingTileInfo {
	val tops = Seq(Resources.atlas.findRegion("bldg-top-1"))
	val mids = Seq(Resources.atlas.findRegion("bldg-mid-1"))
	val btms = Seq(Resources.atlas.findRegion("bldg-btm-1"))
	
	val topsDmg = Seq(Resources.atlas.findRegion("bldg-dmg-top-1"))
	val midsDmg = Seq(Resources.atlas.findRegion("bldg-dmg-mid-1"))
	val btmsDmg = Seq(Resources.atlas.findRegion("bldg-dmg-btm-1"))
}