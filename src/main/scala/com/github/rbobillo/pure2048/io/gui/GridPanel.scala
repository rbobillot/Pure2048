package com.github.rbobillo.pure2048.io.gui

import java.awt.{ Color, Font, Graphics, Graphics2D }
import java.awt.event.{ KeyEvent, KeyListener }

import cats.effect.IO
import com.github.rbobillo.pure2048.grid.Merging.{ IndexedTiles, Tiles }
import com.github.rbobillo.pure2048.grid.Merging.IndexingTiles
import com.github.rbobillo.pure2048.grid.{ Grid, Merging, TileColor }
import javax.swing.{ JFrame, JPanel }

class GridPanel(initialGrid: Grid, frame: JFrame) extends JPanel with KeyListener {

  private val offset = 540 / 4

  private var grid = initialGrid

  private val tileColor: Map[Int, TileColor] = Map(
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
    2048 -> TileColor(Color decode "#F9F6F2", Color decode "#EDC22E"))

  private def drawTileText(g: Graphics2D)(v: Int, x: Int, y: Int): IO[Unit] =
    for {
      xx <- IO.pure(x * offset)
      yy <- IO.pure(y * offset)
      f <- IO.pure(new Font("Helvetica Neue", Font.BOLD, 42))
      n <- IO.pure(v.toString.dropWhile(_ == '0'))
      c <- IO.pure(offset / 2 - 12 * n.length)
      _ <- IO.apply(g.setColor(tileColor(v).font))
      _ <- IO.apply(g.setFont(f))
      _ <- IO.apply(g.drawString(n, xx + c, yy + offset / 2 + 12))
    } yield ()

  private def drawTileBackGround(g: Graphics2D)(v: Int, x: Int, y: Int): IO[Unit] =
    for {
      xx <- IO.pure(x * offset)
      yy <- IO.pure(y * offset)
      _ <- IO.apply(g.setColor(tileColor(v).background))
      _ <- IO.apply(g.fillRect(xx, yy, xx + offset, yy + offset))
    } yield ()

  private def drawTile(g: Graphics2D)(v: Int, x: Int, y: Int): IO[Unit] =
    for {
      _ <- drawTileBackGround(g)(v, x, y)
      _ <- drawTileText(g)(v, x, y)
    } yield ()

  private def drawGrid(g: Graphics2D): IO[Unit] =
    for {
      _ <- IO.apply(g.setColor(Color.decode("#a99c90")))
      // vertical lines
      _ <- IO.apply(g.fillRect(0 * offset - 5, 0 * offset, 10, 540))
      _ <- IO.apply(g.fillRect(1 * offset - 5, 0 * offset, 10, 540))
      _ <- IO.apply(g.fillRect(2 * offset - 5, 0 * offset, 10, 540))
      _ <- IO.apply(g.fillRect(3 * offset - 5, 0 * offset, 10, 540))
      _ <- IO.apply(g.fillRect(4 * offset - 5, 0 * offset, 10, 540))
      // horizontal lines
      _ <- IO.apply(g.fillRect(0 * offset, 0 * offset - 5, 540, 10))
      _ <- IO.apply(g.fillRect(0 * offset, 1 * offset - 5, 540, 10))
      _ <- IO.apply(g.fillRect(0 * offset, 2 * offset - 5, 540, 10))
      _ <- IO.apply(g.fillRect(0 * offset, 3 * offset - 5, 540, 10))
      _ <- IO.apply(g.fillRect(0 * offset, 4 * offset - 5, 540, 10))
    } yield ()

  private def drawIndexedTiles(g: Graphics2D)(its: IndexedTiles): IO[Unit] =
    for {
      _ <- IO.apply(its.map { case (v, x, y) => drawTile(g)(v, x, y).unsafeRunSync() })
      _ <- drawGrid(g)
    } yield ()

  private def reloadGridAndAddTile(g: Graphics2D)(newGrid: Grid): IO[Unit] =
    for {
      _ <- IO.apply(removeAll())
      _ <- IO.apply(setBackground(Color decode "#bbada0"))
      // reload grid
      _ <- IO.apply { grid = newGrid }
      _ <- drawIndexedTiles(g)(grid.tiles.indexed)
      // sleep and add tile
      _ <- IO.apply { Thread.sleep(30) }
      _ <- IO.apply { grid = newGrid.addTile() }
      _ <- drawIndexedTiles(g)(grid.tiles.indexed)
      _ <- IO.apply(frame.setTitle(s"2048 - Score: ${grid.score}"))
    } yield ()

  private def gridChanged(oldGrid: Grid, newGrid: Grid): Boolean =
    oldGrid.tiles.indexed.map(_._1)
      .zip(newGrid.tiles.indexed.map(_._1))
      .exists(x => x._1 != x._2) && !newGrid.isGameLost // && !newGrid.isGameWon

  private def showGameOver(g: Graphics2D): IO[Unit] =
    for {
      _ <- IO.apply(removeAll())
      _ <- IO.apply(setBackground(Color decode "#bbada0"))
      f <- IO.pure(new Font("Helvetica Neue", Font.BOLD, 42))
      _ <- IO.apply(g.setColor(new Color(187, 173, 160, 30)))
      _ <- IO.apply(g.fillRect(0, 0, 540, 540))
      _ <- IO.apply(g.setColor(Color.DARK_GRAY))
      _ <- IO.apply(g.setFont(f))
      _ <- IO.apply(g.drawString("Game Over", 540 / 2 - 120, 540 / 2))
    } yield ()

  private def showGameWon(g: Graphics2D): IO[Unit] =
    for {
      _ <- IO.apply(removeAll())
      _ <- IO.apply(setBackground(Color decode "#bbada0"))
      f <- IO.pure(new Font("Helvetica Neue", Font.BOLD, 42))
      _ <- IO.apply(g.setColor(new Color(187, 173, 160, 30)))
      _ <- IO.apply(g.fillRect(0, 0, 540, 540))
      _ <- IO.apply(g.setColor(Color.DARK_GRAY))
      _ <- IO.apply(g.setFont(f))
      _ <- IO.apply(g.drawString("Game Won", 540 / 2 - 110, 540 / 2))
    } yield ()

  private def merge(direction: Merging.Value)(graphics: Graphics): IO[Unit] =
    for {
      g <- IO.apply(graphics.asInstanceOf[Graphics2D])
      m <- IO.pure(grid merge direction)
      _ <- if (gridChanged(m._1, m._2)) reloadGridAndAddTile(g)(m._2) else IO.unit
      _ <- if (m._2.isGameLost) showGameOver(g) else IO.unit
      _ <- if (m._2.isGameWon) showGameWon(g) else IO.unit
    } yield ()

  override def keyPressed(e: KeyEvent): Unit =
    (e.getKeyCode match {
      case KeyEvent.VK_RIGHT => merge(Merging.RIGHT)(e.getComponent.getGraphics)
      case KeyEvent.VK_LEFT  => merge(Merging.LEFT)(e.getComponent.getGraphics)
      case KeyEvent.VK_DOWN  => merge(Merging.DOWN)(e.getComponent.getGraphics)
      case KeyEvent.VK_UP    => merge(Merging.UP)(e.getComponent.getGraphics)
      case _                 => IO.unit
    }).unsafeRunSync()

  override def keyReleased(e: KeyEvent): Unit = ()

  override def keyTyped(e: KeyEvent): Unit = ()

  override def paint(gs: Graphics): Unit = {
    for {
      g <- IO.apply(gs.asInstanceOf[Graphics2D])
      _ <- drawIndexedTiles(g)(initialGrid.tiles.indexed)
    } yield ()
  }.unsafeRunSync()

  addKeyListener(this)

}