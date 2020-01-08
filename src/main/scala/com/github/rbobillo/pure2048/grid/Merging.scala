package com.github.rbobillo.pure2048.grid

object Merging {

  type Row = Array[Int]
  type Tiles = Array[Row]
  type IndexedTiles = Array[(Int, Int, Int)]

  private type MaybeInts = List[Option[Int]]
  private type Ints = Array[Option[Int]]

  private val mergeWhenIdentical =
    (current: Int, accumulator: MaybeInts) => accumulator match {
      case Some(`current`) :: t => None :: Some(current + current) :: t
      case t                    => Some(current) :: t
    }

  private def mergeRowRight(rw: Row): Row =
    rw.filterNot(_ == 0)
      .foldRight[MaybeInts](Nil)(mergeWhenIdentical)
      .flatten.toArray
      .reverse.padTo(rw.length, 0).reverse

  val mergeRight1: Tiles => Tiles = ts =>
    ts.map(mergeRowRight)

  val mergeLeft1: Tiles => Tiles = ts =>
    ts.map(_.reverse)
      .map(mergeRowRight)
      .map(_.reverse)

  val mergeDown1: Tiles => Tiles = ts =>
    ts.transpose
      .map(mergeRowRight)
      .transpose

  val mergeUp1: Tiles => Tiles = ts =>
    ts.transpose
      .map(_.reverse)
      .map(mergeRowRight)
      .map(_.reverse)
      .transpose

  private val mergeLeftWhenIdentical =
    (accumulator: Ints, current: Int) => accumulator.lastOption.flatten match {
      case Some(`current`) => accumulator.dropRight(1) :+ Some(current + current) :+ None
      case _               => accumulator :+ Some(current)
    }

  private def mergeRowLeft(rw: Row): Row =
    rw.filterNot(_ == 0)
      .foldLeft[Ints](Array.empty)(mergeLeftWhenIdentical)
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
