package com.github.rbobillo.pure2048.board

import java.awt.Graphics2D

import cats.effect.IO
import com.github.rbobillo.pure2048.Config
import com.github.rbobillo.pure2048.dto.Direction
import com.github.rbobillo.pure2048.gui.BoardPanel

object BoardHandler {

  var grid: Grid = _

  private def updateBoard(newGrid: Grid)(boardPanel: BoardPanel, g: Graphics2D): IO[Unit] =
    for {
      // reload grid
      _ <- IO.apply { grid = newGrid }
      _ <- boardPanel.drawIndexedTiles(g)(grid.indexed)
      // sleep and add tile
      _ <- IO.apply { Thread.sleep(50) }
      _ <- IO.apply { grid = newGrid.addTile() }
      _ <- boardPanel.drawIndexedTiles(g)(grid.indexed)
      _ <- IO.apply(boardPanel.frame.setTitle(s"2048 - Score: ${grid.score}"))
    } yield ()

  def merge(direction: Direction.Value)(boardPanel: BoardPanel): IO[Unit] =
    for {
      m <- IO.pure(grid merge direction)
      g <- IO.apply(boardPanel.frame.getGraphics.asInstanceOf[Graphics2D])
      _ <- if (m._1 differs m._2) updateBoard(m._2)(boardPanel, g) else IO.unit
      _ <- if (m._2.isGameLost) boardPanel.showGameStopMessage(g)("Game Over") else IO.unit
      _ <- if (m._2.isGameWon) boardPanel.showGameStopMessage(g)("Game Won") else IO.unit
    } yield ()

  def initGrid: IO[Grid] =
    for {
      cc <- IO.pure(Config.config)
      it <- IO.pure(Array.fill(cc.gridHeight)(Array.fill(cc.gridWidth)(0)))
      g0 <- IO.pure(Grid(tiles = it))
      g1 <- IO.apply(g0.addTile().addTile())
      _ <- IO.apply { grid = g1 }
    } yield g1

}
