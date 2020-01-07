package com.github.rbobillo.pure2048.grid

import Merging.{ Tiles, IndexingTiles }

import scala.util.Random

case class Grid(tiles: Tiles,
                score: Int   = 0) {

  def addTile(): Grid = {
    val n = if (Random.nextFloat < 0.10f) 4 else 2

    val fullTiles = tiles.indexed.collect { case (n, j, i) if n != 0 => i -> j }.toSet

    val Seq((x, y), _*) =
      Random.shuffle((0 until 4).flatMap(x => (0 until 4).map(_ -> x)).filterNot(fullTiles))

    this.copy(tiles.updated(x, tiles(x).updated(y, n)))
  }

  def isGameOver: Boolean =
    Seq(Merging.RIGHT, Merging.DOWN, Merging.LEFT, Merging.UP).foldLeft(tiles) { (ts, d) =>
      mergeFunction(d)(ts)
    }.flatten.toSeq.zip(tiles.flatten.toSeq).forall(x => x._1 == x._2)

  private val mergeFunction: Merging.Value => Tiles => Tiles = {
    case Merging.RIGHT => Merging.mergeRight
    case Merging.LEFT  => Merging.mergeLeft
    case Merging.DOWN  => Merging.mergeDown
    case Merging.UP    => Merging.mergeUp
  }

  // returns a tuple: (PreviousGrid, NextGrid)
  def merge(direction: Merging.Value): (Grid, Grid) = {
    val newTiles = mergeFunction(direction)(tiles)
    val newScore = newTiles.flatten.toSeq.diff(tiles.flatten.toSeq).sum

    this -> this.copy(newTiles, score + newScore)
  }

}
