package com.github.rbobillo.pure2048

import com.github.rbobillo.pure2048.grid.Merging
import com.github.rbobillo.pure2048.grid.Merging.Row
import org.scalatest.{ Matchers, PrivateMethodTester, WordSpec }

class MergingSpecs extends WordSpec with Matchers with PrivateMethodTester {

  "The Tiles of the grid" should {

    "properly move RIGHT in a given row" when {

      val mergeRowRight = PrivateMethod[Row](Symbol("mergeRowRight"))

      "they can move" in {
        val rightMovableTiles1 = Array(2, 0, 0, 0)
        val rightMoveTiles1 = Merging.invokePrivate(mergeRowRight(rightMovableTiles1))
        val rightMovedTiles1 = Array(0, 0, 0, 2)

        val rightMovableTiles2 = Array(0, 2, 0, 0)
        val rightMoveTiles2 = Merging.invokePrivate(mergeRowRight(rightMovableTiles2))
        val rightMovedTiles2 = Array(0, 0, 0, 2)

        val rightMovableTiles3 = Array(0, 0, 2, 0)
        val rightMoveTiles3 = Merging.invokePrivate(mergeRowRight(rightMovableTiles3))
        val rightMovedTiles3 = Array(0, 0, 0, 2)

        rightMoveTiles1 shouldEqual rightMovedTiles1
        rightMoveTiles2 shouldEqual rightMovedTiles2
        rightMoveTiles3 shouldEqual rightMovedTiles3
      }

      "they cannot move" in {
        val immutableTiles = Array(0, 0, 0, 2)
        val moveTiles = Merging.invokePrivate(mergeRowRight(immutableTiles))
        val movedTiles = Array(0, 0, 0, 2)

        moveTiles shouldEqual movedTiles
      }

    }

    "properly merge RIGHT in a row" when {

      val mergeRowRight = PrivateMethod[Row](Symbol("mergeRowRight"))

      "they can merge" in {
        val rightMovableTiles1 = Array(2, 0, 0, 2)
        val rightMoveTiles1 = Merging.invokePrivate(mergeRowRight(rightMovableTiles1))
        val rightMovedTiles1 = Array(0, 0, 0, 4)

        val rightMovableTiles2 = Array(0, 2, 0, 2)
        val rightMoveTiles2 = Merging.invokePrivate(mergeRowRight(rightMovableTiles2))
        val rightMovedTiles2 = Array(0, 0, 0, 4)

        val rightMovableTiles3 = Array(0, 0, 2, 2)
        val rightMoveTiles3 = Merging.invokePrivate(mergeRowRight(rightMovableTiles3))
        val rightMovedTiles3 = Array(0, 0, 0, 4)

        val rightMovableTiles4 = Array(2, 2, 2, 2)
        val rightMoveTiles4 = Merging.invokePrivate(mergeRowRight(rightMovableTiles4))
        val rightMovedTiles4 = Array(0, 0, 4, 4)

        val rightMovableTiles5 = Array(2, 2, 0, 2)
        val rightMoveTiles5 = Merging.invokePrivate(mergeRowRight(rightMovableTiles5))
        val rightMovedTiles5 = Array(0, 0, 2, 4)

        val rightMovableTiles6 = Array(2, 4, 4, 2)
        val rightMoveTiles6 = Merging.invokePrivate(mergeRowRight(rightMovableTiles6))
        val rightMovedTiles6 = Array(0, 2, 8, 2)

        val rightMovableTiles7 = Array(2, 2, 2, 4)
        val rightMoveTiles7 = Merging.invokePrivate(mergeRowRight(rightMovableTiles7))
        val rightMovedTiles7 = Array(0, 2, 4, 4)

        val rightMovableTiles8 = Array(2, 2, 4, 4)
        val rightMoveTiles8 = Merging.invokePrivate(mergeRowRight(rightMovableTiles8))
        val rightMovedTiles8 = Array(0, 0, 4, 8)

        val rightMovableTiles9 = Array(16, 16, 4, 2)
        val rightMoveTiles9 = Merging.invokePrivate(mergeRowRight(rightMovableTiles9))
        val rightMovedTiles9 = Array(0, 32, 4, 2)

        rightMoveTiles1 shouldEqual rightMovedTiles1
        rightMoveTiles2 shouldEqual rightMovedTiles2
        rightMoveTiles3 shouldEqual rightMovedTiles3
        rightMoveTiles4 shouldEqual rightMovedTiles4
        rightMoveTiles5 shouldEqual rightMovedTiles5
        rightMoveTiles6 shouldEqual rightMovedTiles6
        rightMoveTiles7 shouldEqual rightMovedTiles7
        rightMoveTiles8 shouldEqual rightMovedTiles8
        rightMoveTiles9 shouldEqual rightMovedTiles9
      }

      "they cannot merge" in {
        val immutableTiles = Array(4, 2, 4, 2)
        val moveTiles = Merging.invokePrivate(mergeRowRight(immutableTiles))
        val movedTiles = Array(4, 2, 4, 2)

        moveTiles shouldEqual movedTiles
      }

    }

    "all move with the given direction" when {

      "trying to move them RIGHT" in {
        val rightMovableTiles = Array(
          Array(2, 0, 0, 0),
          Array(0, 2, 0, 0),
          Array(0, 0, 2, 0),
          Array(0, 0, 0, 2))

        val rightMoveTiles = Merging.mergeRight(rightMovableTiles).flatten

        val rightMovedTiles = Array(
          Array(0, 0, 0, 2),
          Array(0, 0, 0, 2),
          Array(0, 0, 0, 2),
          Array(0, 0, 0, 2)).flatten

        rightMoveTiles shouldEqual rightMovedTiles
      }

      "trying to move them LEFT" in {
        val leftMovableTiles = Array(
          Array(2, 0, 0, 0),
          Array(0, 2, 0, 0),
          Array(0, 0, 2, 0),
          Array(0, 0, 0, 2))

        val leftMoveTiles = Merging.mergeLeft(leftMovableTiles).flatten

        val leftMovedTiles = Array(
          Array(2, 0, 0, 0),
          Array(2, 0, 0, 0),
          Array(2, 0, 0, 0),
          Array(2, 0, 0, 0)).flatten

        leftMoveTiles shouldEqual leftMovedTiles
      }

      "trying to move them UP" in {
        val upMovableTiles = Array(
          Array(2, 0, 0, 0),
          Array(0, 2, 0, 0),
          Array(0, 0, 2, 0),
          Array(0, 0, 0, 2))

        val upMoveTiles = Merging.mergeUp(upMovableTiles).flatten

        val upMovedTiles = Array(
          Array(2, 2, 2, 2),
          Array(0, 0, 0, 0),
          Array(0, 0, 0, 0),
          Array(0, 0, 0, 0)).flatten

        upMoveTiles shouldEqual upMovedTiles
      }

      "trying to move them DOWN" in {
        val downMovableTiles = Array(
          Array(2, 0, 0, 0),
          Array(0, 2, 0, 0),
          Array(0, 0, 2, 0),
          Array(0, 0, 0, 2))

        val downMoveTiles = Merging.mergeDown(downMovableTiles).flatten

        val downMovedTiles = Array(
          Array(0, 0, 0, 0),
          Array(0, 0, 0, 0),
          Array(0, 0, 0, 0),
          Array(2, 2, 2, 2)).flatten

        downMoveTiles shouldEqual downMovedTiles
      }

    }

    "all merge with the given direction" when {

      "trying to merge them RIGHT" in {
        val rightMergeableTiles = Array(
          Array(2, 2, 2, 2),
          Array(0, 2, 2, 2),
          Array(0, 0, 2, 2),
          Array(0, 0, 0, 2))

        val rightMergeTiles = Merging.mergeRight(rightMergeableTiles).flatten

        val rightMergedTiles = Array(
          Array(0, 0, 4, 4),
          Array(0, 0, 2, 4),
          Array(0, 0, 0, 4),
          Array(0, 0, 0, 2)).flatten

        rightMergeTiles shouldEqual rightMergedTiles
      }

      "trying to merge them LEFT" in {
        val leftMergeableTiles = Array(
          Array(2, 2, 2, 2),
          Array(0, 2, 2, 2),
          Array(0, 0, 2, 2),
          Array(0, 0, 0, 2))

        val leftMergeTiles = Merging.mergeLeft(leftMergeableTiles).flatten

        val leftMergedTiles = Array(
          Array(4, 4, 0, 0),
          Array(4, 2, 0, 0),
          Array(4, 0, 0, 0),
          Array(2, 0, 0, 0)).flatten

        leftMergeTiles shouldEqual leftMergedTiles
      }

      "trying to merge them UP" in {
        val upMergeableTiles = Array(
          Array(2, 2, 2, 2),
          Array(0, 2, 2, 2),
          Array(0, 0, 2, 2),
          Array(0, 0, 0, 2))

        val upMergeTiles = Merging.mergeUp(upMergeableTiles).flatten

        val rightMergedTiles = Array(
          Array(2, 4, 4, 4),
          Array(0, 0, 2, 4),
          Array(0, 0, 0, 0),
          Array(0, 0, 0, 0)).flatten

        upMergeTiles shouldEqual rightMergedTiles
      }

      "trying to merge them DOWN" in {
        val downMergeableTiles = Array(
          Array(2, 2, 2, 2),
          Array(0, 2, 2, 2),
          Array(0, 0, 2, 2),
          Array(0, 0, 0, 2))

        val downMergeTiles = Merging.mergeDown(downMergeableTiles).flatten

        val downMergedTiles = Array(
          Array(0, 0, 0, 0),
          Array(0, 0, 0, 0),
          Array(0, 0, 2, 4),
          Array(2, 4, 4, 4)).flatten

        downMergeTiles shouldEqual downMergedTiles
      }

    }

  }

}
