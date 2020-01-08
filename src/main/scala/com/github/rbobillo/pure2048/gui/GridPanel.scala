package com.github.rbobillo.pure2048.gui

import java.awt.{ Color, Font, Graphics, Graphics2D }
import java.awt.event.{ KeyEvent, KeyListener }

import akka.actor.ActorRef
import cats.effect.IO
import com.github.rbobillo.pure2048.Config.config
import com.github.rbobillo.pure2048.dto.{ Direction, MergeGrid }
import com.github.rbobillo.pure2048.grid.Merging.IndexedTiles
import com.github.rbobillo.pure2048.grid.Grid
import com.github.rbobillo.pure2048.gui.Gui.{ hOffset, wOffset }
import javax.swing.{ JFrame, JPanel }

class GridPanel(initialGrid: Grid, frame: JFrame, gameBoardActor: ActorRef) extends JPanel with KeyListener {

  private val wOffset = config.boardWidth / config.gridWidth
  private val hOffset = config.boardHeight / config.gridHeight

  private var grid = initialGrid

  private def drawTileBackGround(g: Graphics2D)(v: Int, x: Int, y: Int): IO[Unit] =
    for {
      xx <- IO.pure(x * wOffset)
      yy <- IO.pure(y * hOffset)
      _ <- IO.apply(g.setColor(config.tileColor(v).background))
      _ <- IO.apply(g.fillRect(xx, yy, xx + wOffset, yy + hOffset))
    } yield ()

  private def drawSplitters(g: Graphics2D): IO[Unit] =
    for {
      of <- IO.pure(config.boardWidth / config.gridWidth)
      _ <- IO.apply(g.setColor(config.boardBackgroundColor))
      _ <- IO.apply((0 to config.gridHeight).foreach(n => g.fillRect(n * of - 5, 0 * of, 10, config.boardHeight))) // vertical lines
      _ <- IO.apply((0 to config.gridWidth).foreach(n => g.fillRect(0 * of, n * of - 5, config.boardWidth, 10))) // horizontal lines
    } yield ()

  private def drawTileText(g: Graphics2D)(v: Int, x: Int, y: Int): IO[Unit] =
    for {
      xx <- IO.pure(x * wOffset)
      yy <- IO.pure(y * hOffset)
      f <- IO.pure(new Font("Helvetica Neue", Font.BOLD, 42))
      n <- IO.pure(v.toString.dropWhile(_ == '0'))
      c <- IO.pure(wOffset / 2 - 12 * n.length)
      _ <- IO.apply(g.setColor(config.tileColor(v).font))
      _ <- IO.apply(g.setFont(f))
      _ <- IO.apply(g.drawString(n, xx + c, yy + hOffset / 2 + 12))
    } yield ()

  private def drawTile(g: Graphics2D)(v: Int, x: Int, y: Int): IO[Unit] =
    for {
      _ <- drawTileBackGround(g)(v, x, y)
      _ <- drawTileText(g)(v, x, y)
    } yield ()

  private def drawIndexedTiles(g: Graphics2D)(its: IndexedTiles): IO[Unit] =
    for {
      _ <- IO.apply(its.map { case (v, x, y) => drawTile(g)(v, x, y).unsafeRunSync() })
      _ <- drawSplitters(g)
    } yield ()

  private def reloadGridAndAddTile(g: Graphics2D)(newGrid: Grid): IO[Unit] =
    for {
      _ <- IO.apply(removeAll())
      _ <- IO.apply(setBackground(config.boardBackgroundColor))
      // reload grid
      _ <- IO.apply { grid = newGrid }
      _ <- drawIndexedTiles(g)(grid.indexed)
      // sleep and add tile
      _ <- IO.apply { Thread.sleep(30) }
      _ <- IO.apply { grid = newGrid.addTile() }
      _ <- drawIndexedTiles(g)(grid.indexed)
      _ <- IO.apply(frame.setTitle(s"2048 - Score: ${grid.score}"))
    } yield ()

  private def gridChanged(oldGrid: Grid, newGrid: Grid): Boolean =
    oldGrid.indexed.map(_._1)
      .zip(newGrid.indexed.map(_._1))
      .exists(x => x._1 != x._2) && !newGrid.isGameLost

  private def showGameStopMessage(g: Graphics2D)(msg: String): IO[Unit] =
    for {
      _ <- IO.apply(removeAll())
      _ <- IO.apply(setBackground(config.boardBackgroundColor))
      f <- IO.pure(new Font("Helvetica Neue", Font.BOLD, 42))
      _ <- IO.apply(g.setColor(new Color(187, 173, 160, 30)))
      _ <- IO.apply(g.fillRect(0, 0, config.gridWidth, config.gridHeight))
      _ <- IO.apply(g.setColor(Color.DARK_GRAY))
      _ <- IO.apply(g.setFont(f))
      _ <- IO.apply(g.drawString(msg, 540 / 2 - (msg.length + 3) * 10, 540 / 2))
    } yield ()

  private def merge(direction: Direction.Value)(graphics: Graphics): IO[Unit] =
    for {
      g <- IO.apply(graphics.asInstanceOf[Graphics2D])
      m <- IO.pure(grid merge direction)
      _ <- if (gridChanged(m._1, m._2)) reloadGridAndAddTile(g)(m._2) else IO.unit
      _ <- if (m._2.isGameLost) showGameStopMessage(g)("Game Over") else IO.unit
      _ <- if (m._2.isGameWon) showGameStopMessage(g)("Game Won") else IO.unit
    } yield ()

  override def keyPressed(e: KeyEvent): Unit =
    (e.getKeyCode match {
      case KeyEvent.VK_RIGHT =>
        gameBoardActor ! MergeGrid(Direction.RIGHT)
        merge(Direction.RIGHT)(e.getComponent.getGraphics)
      case KeyEvent.VK_LEFT =>
        gameBoardActor ! MergeGrid(Direction.LEFT)
        merge(Direction.LEFT)(e.getComponent.getGraphics)
      case KeyEvent.VK_DOWN =>
        gameBoardActor ! MergeGrid(Direction.DOWN)
        merge(Direction.DOWN)(e.getComponent.getGraphics)
      case KeyEvent.VK_UP =>
        gameBoardActor ! MergeGrid(Direction.UP)
        merge(Direction.UP)(e.getComponent.getGraphics)
      case _ => IO.unit
    }).unsafeRunSync()

  override def keyReleased(e: KeyEvent): Unit = ()

  override def keyTyped(e: KeyEvent): Unit = ()

  override def paint(gs: Graphics): Unit = {
    for {
      g <- IO.apply(gs.asInstanceOf[Graphics2D])
      _ <- drawIndexedTiles(g)(initialGrid.indexed)
    } yield ()
  }.unsafeRunSync()

  addKeyListener(this)

}