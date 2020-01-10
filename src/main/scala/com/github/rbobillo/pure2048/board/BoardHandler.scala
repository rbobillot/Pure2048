package com.github.rbobillo.pure2048.board

import java.awt.{ Color, Font, Graphics2D }

import cats.effect.IO
import com.github.rbobillo.pure2048.{ Config, Direction }
import com.github.rbobillo.pure2048.gui.{ BoardPanel, Gui }

object BoardHandler {

  var grid: Grid = _ // TODO: different state handling ? -> DB, File.. ?

  private def updateGrid(newGrid: Grid): IO[Unit] = IO.apply { grid = newGrid }

  private def updateBoard(newGrid: Grid)(boardPanel: BoardPanel, g: Graphics2D): IO[Unit] =
    for {
      o <- IO.pure(grid)
      _ <- updateGrid(newGrid.addTile())
      _ <- boardPanel.drawIndexedTiles(g)(grid.indexed)
      _ <- Gui.changeTitle(boardPanel.frame)(s"Score: ${grid.score}")
    } yield ()

  def merge(direction: Direction.Value)(p: BoardPanel): IO[Unit] =
    for {
      g <- IO.apply(p.frame.getGraphics.asInstanceOf[Graphics2D])
      m <- if (grid.isPlayable) IO.pure(grid merge direction) else IO.pure(grid -> grid)
      _ <- if (m._1 differs m._2) updateBoard(m._2)(p, g) else IO.unit
      _ <- if (m._2.isGameLost) Gui.changeTitle(p.frame)(s"Game Over - Score: ${grid.score}") else IO.unit
      _ <- if (m._2.isGameWon) Gui.changeTitle(p.frame)(s"Game Won - Score: ${grid.score}") else IO.unit
    } yield ()

  def reset(boardPanel: BoardPanel): IO[Unit] =
    for {
      n <- initGrid
      g <- IO.apply(boardPanel.frame.getGraphics.asInstanceOf[Graphics2D])
      _ <- boardPanel.drawIndexedTiles(g)(n.indexed)
    } yield ()

  def initGrid: IO[Grid] =
    for {
      cc <- IO.pure(Config.config)
      it <- IO.pure(Array.fill(cc.gridHeight)(Array.fill(cc.gridWidth)(0)))
      g0 <- IO.pure(Grid(tiles = it))
      g1 <- IO.apply(g0.addTile().addTile())
      _ <- updateGrid(g1)
    } yield g1

}
