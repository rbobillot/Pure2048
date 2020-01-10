package com.github.rbobillo.pure2048.board

import Merging.{ IndexedTiles, TilesGrid }
import com.github.rbobillo.pure2048.Config.config

import scala.util.Random

case class Grid(tiles: TilesGrid,
                prev:  TilesGrid,
                score: Int       = 0) {

  def addTile(): Grid = {
    lazy val newTileValue = if (Random.nextFloat < 0.10f) 4 else 2
    lazy val fullTiles = indexedTiles.collect { case (t, j, i) if t.nonEmpty => i -> j }.toSet

    val Seq((x, y), _*) =
      Random.shuffle(
        (0 until config.gridWidth)
          .flatMap(x => (0 until config.gridHeight).map(_ -> x))
          .filterNot(fullTiles))

    this.copy(tiles = tiles.updated(x, tiles(x).updated(y, Tile(newTileValue))))
  }

  def differs(newGrid: Grid): Boolean =
    this.indexedTiles.map(_._1)
      .zip(newGrid.indexedTiles.map(_._1))
      .exists(x => x._1.v != x._2.v)

  def isPlayable: Boolean = !isGameLost && !isGameWon

  def isGameLost: Boolean =
    Seq(Direction.RIGHT, Direction.DOWN, Direction.LEFT, Direction.UP).foldLeft(tiles :: Nil) { (tss, d) =>
      mergeFunction(d)(tss.last) :: tss
    }.transpose.forall(xs => xs.forall(_ sameElements xs.head))

  def isGameWon: Boolean =
    tiles.flatten.map(_.v).contains(config.victoryValue)

  private def indexed(ts: TilesGrid): IndexedTiles =
    ts.zipWithIndex.flatMap { case (row, y) =>
      row.zipWithIndex.map { case (t, x) =>
        (t, x, y)
      }
    }

  def indexedTiles: IndexedTiles = indexed(tiles)

  def indexedPrev: IndexedTiles = indexed(prev)

  private val mergeFunction: Direction.Value => TilesGrid => TilesGrid = {
    case Direction.RIGHT => Merging.mergeRight
    case Direction.LEFT  => Merging.mergeLeft
    case Direction.DOWN  => Merging.mergeDown
    case Direction.UP    => Merging.mergeUp
  }

  // returns a tuple: (PreviousGrid, NextGrid)
  def merge(direction: Direction.Value): (Grid, Grid) = {
    val newTiles = mergeFunction(direction)(tiles)
    val newScore = newTiles.flatten.map(_.v).toSeq.diff(tiles.flatten.map(_.v).toSeq).sum

    this -> this.copy(
      tiles = newTiles,
      prev  = tiles,
      score = score + newScore)
  }

}
