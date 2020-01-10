package com.github.rbobillo.pure2048.board

object Merging {

  type TilesRow = Array[Tile]
  type TilesGrid = Array[TilesRow]
  type IndexedTiles = Array[(Tile, Int, Int)]

  private val mergeTilesWhenIdentical =
    (mergedTiles: Array[Option[Tile]], newTile: Tile) => mergedTiles.lastOption.flatten match {
      case Some(Tile(newTile.v, _)) => mergedTiles.dropRight(1) :+ Some(newTile.copy(v = newTile.v * 2)) :+ None // ([Some(4), Some(2)], 2) => [Some(4)] :+ Some(2+2) => [Some(4), Some(4)]
      case _                        => mergedTiles :+ Some(newTile) // ([Some(4)], 2) => [Some(4)] :+ Some(2) => [Some(4), Some(2)]
    }

  private def mergeRowLeft(rw: TilesRow): TilesRow =
    rw.filterNot(_.v == 0)
      .foldLeft(Array.empty[Option[Tile]])(mergeTilesWhenIdentical)
      .flatten
      .padTo(rw.length, Tile(0, 0))

  val mergeLeft: TilesGrid => TilesGrid = ts =>
    ts.map(mergeRowLeft)

  val mergeRight: TilesGrid => TilesGrid = ts =>
    ts.map(_.reverse)
      .map(mergeRowLeft)
      .map(_.reverse)

  val mergeUp: TilesGrid => TilesGrid = ts =>
    ts.transpose
      .map(mergeRowLeft)
      .transpose

  val mergeDown: TilesGrid => TilesGrid = ts =>
    ts.transpose
      .map(_.reverse)
      .map(mergeRowLeft)
      .map(_.reverse)
      .transpose

}
