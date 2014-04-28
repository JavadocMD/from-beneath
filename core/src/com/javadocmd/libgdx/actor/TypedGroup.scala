package com.javadocmd.libgdx.actor

import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.Actor

class TypedGroup[T <: Actor](implicit val t: Class[T]) extends Group with Iterable[T] {
	
	private val queue = scala.collection.mutable.HashSet[T]()
	
	override def addActor(actor: Actor) = {
		super.addActor(actor)
		if (t.isInstance(actor))
			queue += t.cast(actor)
	}
	
	override def removeActor(actor: Actor) = {
		val result = super.removeActor(actor)
		if (t.isInstance(actor))
			queue -= t.cast(actor)
		result
	}
	
	override def toIterable() = queue
	
	override def iterator = queue.iterator
}