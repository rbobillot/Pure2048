package com.github.rbobillo.pure2048

import java.awt.Color

import com.github.rbobillo.pure2048.gui.TileColor

object Config {

  protected case class GameConf(victoryValue:         Int,
                                gridWidth:            Int,
                                gridHeight:           Int,
                                boardWidth:           Int,
                                boardHeight:          Int,
                                boardBackgroundColor: Color,
                                tileColor:            Map[Int, TileColor])

  // TODO: Change this hardcoded Config... It should be done with PureConfig
  val config: GameConf = GameConf(
    victoryValue         = 2048,
    gridWidth            = 4,
    gridHeight           = 4,
    boardWidth           = 540,
    boardHeight          = 540,
    boardBackgroundColor = Color.decode("#a99c90"),
    tileColor            = Map(
      0 -> TileColor(Color decode "#cdc0b4", Color decode "#bbada0"),
      2 -> TileColor(Color decode "#776E65", Color decode "#EEE4DA"),
      4 -> TileColor(Color decode "#776E65", Color decode "#EDE0C8"),
      8 -> TileColor(Color decode "#F9F6F2", Color decode "#F2B179"),
      16 -> TileColor(Color decode "#F9F6F2", Color decode "#F59563"),
      32 -> TileColor(Color decode "#F9F6F2", Color decode "#F67C5F"),
      64 -> TileColor(Color decode "#F9F6F2", Color decode "#F65E3B"),
      128 -> TileColor(Color decode "#F9F6F2", Color decode "#EDCF72"),
      256 -> TileColor(Color decode "#F9F6F2", Color decode "#EDCC61"),
      512 -> TileColor(Color decode "#F9F6F2", Color decode "#EDC850"),
      1024 -> TileColor(Color decode "#F9F6F2", Color decode "#EDC53F"),
      2048 -> TileColor(Color decode "#F9F6F2", Color decode "#EDC22E")))

}
