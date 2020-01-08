package com.github.rbobillo.pure2048.grid

object Merging {

  type Row = Array[Int]
  type Tiles = Array[Row]
  type IndexedTiles = Array[(Int, Int, Int)]

  private val mergeTilesWhenIdentical =
    (mergedTiles: Array[Option[Int]], newTile: Int) => mergedTiles.lastOption.flatten match {
      case Some(`newTile`) => mergedTiles.dropRight(1) :+ Some(newTile + newTile) :+ None // ([Some(4), Some(2)], 2) => [Some(4)] :+ Some(2+2) => [Some(4), Some(4)]
      case _               => mergedTiles :+ Some(newTile) // ([Some(4)], 2) => [Some(4)] :+ Some(2) => [Some(4), Some(2)]
    }

  private def mergeRowLeft(rw: Row): Row =
    rw.filterNot(_ == 0)
      .foldLeft(Array.empty[Option[Int]])(mergeTilesWhenIdentical)
      .flatten
      .padTo(rw.length, 0)

  val mergeLeft: Tiles => Tiles = ts =>
    ts.map(mergeRowLeft)

  val mergeRight: Tiles => Tiles = ts =>
    ts.map(_.reverse)
      .map(mergeRowLeft)
      .map(_.reverse)

  val mergeUp: Tiles => Tiles = ts =>
    ts.transpose
      .map(mergeRowLeft)
      .transpose

  val mergeDown: Tiles => Tiles = ts =>
    ts.transpose
      .map(_.reverse)
      .map(mergeRowLeft)
      .map(_.reverse)
      .transpose

}
