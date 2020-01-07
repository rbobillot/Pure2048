package com.github.rbobillo.pure2048.grid

object Merging extends Enumeration {

  type Row = Array[Int]
  type Tiles = Array[Row]
  type IndexedTiles = Array[(Int, Int, Int)]

  val RIGHT, LEFT, UP, DOWN = Value

  implicit class IndexingTiles(ts: Tiles) {
    def indexed: IndexedTiles =
      ts.zipWithIndex.flatMap { case (row, y) =>
        row.zipWithIndex.map { case (n, x) =>
          (n, x, y)
        }
      }
  }

  private val mergeRowRight: Row => Row =
    _.filterNot(_ == 0).reverse.padTo(4, 0).reverse match {
      case Array(a, b, c, d) if a == b && c == d => Array(0, 0, a + b, c + d)
      case Array(a, b, c, d) if c == d           => Array(0, a, b, c + d)
      case Array(a, b, c, d) if b == c           => Array(0, a, b + c, d)
      case Array(a, b, c, d) if a == b           => Array(0, a + b, c, d)
      case Array(a, b, c, d)                     => Array(a, b, c, d)
    }

  val mergeRight: Tiles => Tiles = ts =>
    ts.map(mergeRowRight)

  val mergeLeft: Tiles => Tiles = ts =>
    ts.map(_.reverse)
      .map(mergeRowRight)
      .map(_.reverse)

  val mergeDown: Tiles => Tiles = ts =>
    ts.transpose
      .map(mergeRowRight)
      .transpose

  val mergeUp: Tiles => Tiles = ts =>
    ts.transpose
      .map(_.reverse)
      .map(mergeRowRight)
      .map(_.reverse)
      .transpose

}
