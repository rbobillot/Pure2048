package com.github.rbobillo.pure2048.board

/**
  *
  * @param v Current Tile's value
  * @param id Current Tile's creation time (nano seconds, so the id has less collision possibilities)
  *           Used to for Tiles moving animation
  *           Its default value is highly impure
  */
case class Tile(v: Int, id: Long = System.nanoTime) {
  def nonEmpty: Boolean = v != 0
  def isEmpty: Boolean = v == 0
}

object Tile {
  def empty: Tile = Tile(0, 0)
}
