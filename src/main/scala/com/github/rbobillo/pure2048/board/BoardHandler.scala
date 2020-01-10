package com.github.rbobillo.pure2048.board

import java.awt.{ Color, Font, Graphics2D }

import cats.effect.IO
import com.github.rbobillo.pure2048.Config
import com.github.rbobillo.pure2048.gui.{ BoardPanel, Gui, GuiUtils }

object BoardHandler {

  var grid: Grid = _ // TODO: different state handling ? -> DB, File.. ?

  private def updateGrid(newGrid: Grid): IO[Unit] = IO.apply { grid = newGrid }

  private def updateBoard(newGrid: Grid, isReset: Boolean = false)(boardPanel: BoardPanel, g: Graphics2D): IO[Unit] =
    for {
      n <- IO.apply(newGrid.addTile())
      _ <- updateGrid(if (isReset) newGrid else n)
      _ <- Gui.drawIndexedTiles(g, boardPanel.frame)(grid)
      _ <- GuiUtils.changeTitle(boardPanel.frame)(s"Score: ${grid.score}")
      _ <- IO.apply(boardPanel.repaint())
    } yield ()

  def merge(direction: Direction.Value)(p: BoardPanel): IO[Unit] =
    for {
      g <- IO.apply(p.getGraphics.asInstanceOf[Graphics2D])
      m <- if (grid.isPlayable) IO.pure(grid merge direction) else IO.pure(grid -> grid)
      _ <- if (m._1 differs m._2) updateBoard(m._2)(p, g) else IO.unit
      _ <- if (m._2.isGameLost) GuiUtils.changeTitle(p.frame)(s"Game Over - Score: ${grid.score}") else IO.unit
      _ <- if (m._2.isGameWon) GuiUtils.changeTitle(p.frame)(s"Game Won - Score: ${grid.score}") else IO.unit
      //_ <- if (m._2.isGameLost) p.showGameStopMessage(g)(s"Game Over - Score: ${grid.score}") else IO.unit
      //_ <- if (m._2.isGameWon) p.showGameStopMessage(g)(s"Game Won - Score: ${grid.score}") else IO.unit
    } yield ()

  def reset(boardPanel: BoardPanel): IO[Unit] =
    for {
      n <- initGrid
      g <- IO.apply(boardPanel.getGraphics.asInstanceOf[Graphics2D])
      _ <- updateBoard(n, isReset = true)(boardPanel, g)
    } yield ()

  def initGrid: IO[Grid] =
    for {
      cc <- IO.pure(Config.config)
      it <- IO.pure(Array.fill(cc.gridHeight)(Array.fill(cc.gridWidth)(Tile.empty)))
      g0 <- IO.pure(Grid(tiles = it, prev = it))
      g1 <- IO.apply(g0.addTile().addTile())
      _ <- updateGrid(g1)
    } yield g1

}
