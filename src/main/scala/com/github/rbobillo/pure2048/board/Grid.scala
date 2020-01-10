package com.github.rbobillo.pure2048.board

import Merging.{ IndexedTiles, Tiles }
import com.github.rbobillo.pure2048.Config.config
import com.github.rbobillo.pure2048.Direction

import scala.util.Random

case class Grid(tiles: Tiles,
                score: Int   = 0) {

  def addTile(): Grid = {
    lazy val newTileValue = if (Random.nextFloat < 0.10f) 4 else 2
    lazy val fullTiles = indexed.collect { case (n, j, i) if n != 0 => i -> j }.toSet

    val Seq((x, y), _*) =
      Random.shuffle(
        (0 until config.gridWidth)
          .flatMap(x => (0 until config.gridHeight).map(_ -> x))
          .filterNot(fullTiles))

    this.copy(tiles.updated(x, tiles(x).updated(y, newTileValue)))
  }

  def differs(newGrid: Grid): Boolean =
    this.indexed.map(_._1)
      .zip(newGrid.indexed.map(_._1))
      .exists(x => x._1 != x._2)

  def isPlayable: Boolean = !isGameLost && !isGameWon

  def isGameLost: Boolean =
    Seq(Direction.RIGHT, Direction.DOWN, Direction.LEFT, Direction.UP).foldLeft(tiles :: Nil) { (tss, d) =>
      mergeFunction(d)(tss.last) :: tss
    }.transpose.forall(xs => xs.forall(_ sameElements xs.head))

  def isGameWon: Boolean =
    tiles.flatten.contains(config.victoryValue)

  def indexed: IndexedTiles =
    tiles.zipWithIndex.flatMap { case (row, y) =>
      row.zipWithIndex.map { case (n, x) =>
        (n, x, y)
      }
    }

  private val mergeFunction: Direction.Value => Tiles => Tiles = {
    case Direction.RIGHT => Merging.mergeRight
    case Direction.LEFT  => Merging.mergeLeft
    case Direction.DOWN  => Merging.mergeDown
    case Direction.UP    => Merging.mergeUp
  }

  // returns a tuple: (PreviousGrid, NextGrid)
  def merge(direction: Direction.Value): (Grid, Grid) = {
    val newTiles = mergeFunction(direction)(tiles)
    val newScore = newTiles.flatten.toSeq.diff(tiles.flatten.toSeq).sum

    this -> this.copy(newTiles, score + newScore)
  }

}
