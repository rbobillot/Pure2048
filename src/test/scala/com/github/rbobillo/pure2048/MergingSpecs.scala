package com.github.rbobillo.pure2048

import com.github.rbobillo.pure2048.board.{ Merging, Tile }
import com.github.rbobillo.pure2048.board.Merging.TilesRow
import org.scalatest.{ Matchers, PrivateMethodTester, WordSpec }

class MergingSpecs extends WordSpec with Matchers with PrivateMethodTester {

  implicit class ArrayToTiles(xs: Array[Int]) {
    def toTiles: Array[Tile] = xs.map(v => if (v == 0) Tile.empty else Tile(v, 1))
  }

  implicit class GridToTiles(xs: Array[Array[Int]]) {
    def toTiles: Array[Array[Tile]] = xs.map(_.map(v => if (v == 0) Tile.empty else Tile(v, 1)))
  }

  "The Tiles of the grid" should {

    "properly move RIGHT in a given row" when {

      val mergeRowLeft = PrivateMethod[TilesRow](Symbol("mergeRowLeft"))

      "they can move" in {
        val leftMovableTiles1 = Array(0, 0, 0, 2).toTiles
        val leftMoveTiles1 = Merging.invokePrivate(mergeRowLeft(leftMovableTiles1))

        val leftMovableTiles2 = Array(0, 0, 2, 0).toTiles
        val leftMoveTiles2 = Merging.invokePrivate(mergeRowLeft(leftMovableTiles2))

        val leftMovableTiles3 = Array(0, 2, 0, 0).toTiles
        val leftMoveTiles3 = Merging.invokePrivate(mergeRowLeft(leftMovableTiles3))

        val leftMovedTiles = Array(2, 0, 0, 0).toTiles

        leftMoveTiles1 shouldEqual leftMovedTiles
        leftMoveTiles2 shouldEqual leftMovedTiles
        leftMoveTiles3 shouldEqual leftMovedTiles
      }

      "they cannot move" in {
        val immutableTiles = Array(0, 0, 0, 2).toTiles
        val moveTiles = Merging.invokePrivate(mergeRowLeft(immutableTiles))
        val movedTiles = Array(2, 0, 0, 0).toTiles

        moveTiles shouldEqual movedTiles
      }

    }

    "properly merge RIGHT in a row" when {

      val mergeRowLeft = PrivateMethod[TilesRow](Symbol("mergeRowLeft"))

      "they can merge" in {
        val leftMovableTiles1 = Array(2, 0, 0, 2).toTiles
        val leftMoveTiles1 = Merging.invokePrivate(mergeRowLeft(leftMovableTiles1))
        val leftMovedTiles1 = Array(4, 0, 0, 0).toTiles

        val leftMovableTiles2 = Array(0, 2, 0, 2).toTiles
        val leftMoveTiles2 = Merging.invokePrivate(mergeRowLeft(leftMovableTiles2))
        val leftMovedTiles2 = Array(4, 0, 0, 0).toTiles

        val leftMovableTiles3 = Array(0, 0, 2, 2).toTiles
        val leftMoveTiles3 = Merging.invokePrivate(mergeRowLeft(leftMovableTiles3))
        val leftMovedTiles3 = Array(4, 0, 0, 0).toTiles

        val leftMovableTiles4 = Array(2, 2, 2, 2).toTiles
        val leftMoveTiles4 = Merging.invokePrivate(mergeRowLeft(leftMovableTiles4))
        val leftMovedTiles4 = Array(4, 4, 0, 0).toTiles

        val leftMovableTiles5 = Array(2, 0, 2, 2).toTiles
        val leftMoveTiles5 = Merging.invokePrivate(mergeRowLeft(leftMovableTiles5))
        val leftMovedTiles5 = Array(4, 2, 0, 0).toTiles

        val leftMovableTiles6 = Array(2, 4, 4, 2).toTiles
        val leftMoveTiles6 = Merging.invokePrivate(mergeRowLeft(leftMovableTiles6))
        val leftMovedTiles6 = Array(2, 8, 2, 0).toTiles

        val leftMovableTiles7 = Array(2, 2, 2, 4).toTiles
        val leftMoveTiles7 = Merging.invokePrivate(mergeRowLeft(leftMovableTiles7))
        val leftMovedTiles7 = Array(4, 2, 4, 0).toTiles

        val leftMovableTiles8 = Array(2, 2, 4, 4).toTiles
        val leftMoveTiles8 = Merging.invokePrivate(mergeRowLeft(leftMovableTiles8))
        val leftMovedTiles8 = Array(4, 8, 0, 0).toTiles

        val leftMovableTiles9 = Array(16, 16, 4, 2).toTiles
        val leftMoveTiles9 = Merging.invokePrivate(mergeRowLeft(leftMovableTiles9))
        val leftMovedTiles9 = Array(32, 4, 2, 0).toTiles

        leftMoveTiles1 shouldEqual leftMovedTiles1
        leftMoveTiles2 shouldEqual leftMovedTiles2
        leftMoveTiles3 shouldEqual leftMovedTiles3
        leftMoveTiles4 shouldEqual leftMovedTiles4
        leftMoveTiles5 shouldEqual leftMovedTiles5
        leftMoveTiles6 shouldEqual leftMovedTiles6
        leftMoveTiles7 shouldEqual leftMovedTiles7
        leftMoveTiles8 shouldEqual leftMovedTiles8
        leftMoveTiles9 shouldEqual leftMovedTiles9
      }

      "they cannot merge" in {
        val immutableTiles = Array(4, 2, 4, 2).toTiles
        val moveTiles = Merging.invokePrivate(mergeRowLeft(immutableTiles))
        val movedTiles = Array(4, 2, 4, 2).toTiles

        moveTiles shouldEqual movedTiles
      }

    }

    "all move with the given direction" when {

      "trying to move them RIGHT" in {
        val rightMovableTiles = Array(
          Array(2, 0, 0, 0),
          Array(0, 2, 0, 0),
          Array(0, 0, 2, 0),
          Array(0, 0, 0, 2)).toTiles

        val rightMoveTiles = Merging.mergeRight(rightMovableTiles).flatten

        val rightMovedTiles = Array(
          Array(0, 0, 0, 2),
          Array(0, 0, 0, 2),
          Array(0, 0, 0, 2),
          Array(0, 0, 0, 2)).flatten.toTiles

        rightMoveTiles shouldEqual rightMovedTiles
      }

      "trying to move them LEFT" in {
        val leftMovableTiles = Array(
          Array(2, 0, 0, 0),
          Array(0, 2, 0, 0),
          Array(0, 0, 2, 0),
          Array(0, 0, 0, 2)).toTiles

        val leftMoveTiles = Merging.mergeLeft(leftMovableTiles).flatten

        val leftMovedTiles = Array(
          Array(2, 0, 0, 0),
          Array(2, 0, 0, 0),
          Array(2, 0, 0, 0),
          Array(2, 0, 0, 0)).flatten.toTiles

        leftMoveTiles shouldEqual leftMovedTiles
      }

      "trying to move them UP" in {
        val upMovableTiles = Array(
          Array(2, 0, 0, 0),
          Array(0, 2, 0, 0),
          Array(0, 0, 2, 0),
          Array(0, 0, 0, 2)).toTiles

        val upMoveTiles = Merging.mergeUp(upMovableTiles).flatten

        val upMovedTiles = Array(
          Array(2, 2, 2, 2),
          Array(0, 0, 0, 0),
          Array(0, 0, 0, 0),
          Array(0, 0, 0, 0)).flatten.toTiles

        upMoveTiles shouldEqual upMovedTiles
      }

      "trying to move them DOWN" in {
        val downMovableTiles = Array(
          Array(2, 0, 0, 0),
          Array(0, 2, 0, 0),
          Array(0, 0, 2, 0),
          Array(0, 0, 0, 2)).toTiles

        val downMoveTiles = Merging.mergeDown(downMovableTiles).flatten

        val downMovedTiles = Array(
          Array(0, 0, 0, 0),
          Array(0, 0, 0, 0),
          Array(0, 0, 0, 0),
          Array(2, 2, 2, 2)).flatten.toTiles

        downMoveTiles shouldEqual downMovedTiles
      }

    }

    "all merge with the given direction" when {

      "trying to merge them RIGHT" in {
        val rightMergeableTiles = Array(
          Array(2, 2, 2, 2),
          Array(0, 2, 2, 2),
          Array(0, 0, 2, 2),
          Array(0, 0, 0, 2)).toTiles

        val rightMergeTiles = Merging.mergeRight(rightMergeableTiles).flatten

        val rightMergedTiles = Array(
          Array(0, 0, 4, 4),
          Array(0, 0, 2, 4),
          Array(0, 0, 0, 4),
          Array(0, 0, 0, 2)).flatten.toTiles

        rightMergeTiles shouldEqual rightMergedTiles
      }

      "trying to merge them LEFT" in {
        val leftMergeableTiles = Array(
          Array(2, 2, 2, 2),
          Array(0, 2, 2, 2),
          Array(0, 0, 2, 2),
          Array(0, 0, 0, 2)).toTiles

        val leftMergeTiles = Merging.mergeLeft(leftMergeableTiles).flatten

        val leftMergedTiles = Array(
          Array(4, 4, 0, 0),
          Array(4, 2, 0, 0),
          Array(4, 0, 0, 0),
          Array(2, 0, 0, 0)).flatten.toTiles

        leftMergeTiles shouldEqual leftMergedTiles
      }

      "trying to merge them UP" in {
        val upMergeableTiles = Array(
          Array(2, 2, 2, 2),
          Array(0, 2, 2, 2),
          Array(0, 0, 2, 2),
          Array(0, 0, 0, 2)).toTiles

        val upMergeTiles = Merging.mergeUp(upMergeableTiles).flatten

        val rightMergedTiles = Array(
          Array(2, 4, 4, 4),
          Array(0, 0, 2, 4),
          Array(0, 0, 0, 0),
          Array(0, 0, 0, 0)).flatten.toTiles

        upMergeTiles shouldEqual rightMergedTiles
      }

      "trying to merge them DOWN" in {
        val downMergeableTiles = Array(
          Array(2, 2, 2, 2),
          Array(0, 2, 2, 2),
          Array(0, 0, 2, 2),
          Array(0, 0, 0, 2)).toTiles

        val downMergeTiles = Merging.mergeDown(downMergeableTiles).flatten

        val downMergedTiles = Array(
          Array(0, 0, 0, 0),
          Array(0, 0, 0, 0),
          Array(0, 0, 2, 4),
          Array(2, 4, 4, 4)).flatten.toTiles

        downMergeTiles shouldEqual downMergedTiles
      }

    }

  }

}
