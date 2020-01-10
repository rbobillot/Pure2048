package com.github.rbobillo.pure2048.gui

import java.awt.{ Color, Font, Graphics, Graphics2D, Point, Rectangle }
import java.awt.event.{ KeyEvent, KeyListener }

import cats.effect.IO
import com.github.rbobillo.pure2048.Config.config
import com.github.rbobillo.pure2048.board.Merging.IndexedTiles
import com.github.rbobillo.pure2048.gui.Gui.{ hOffset, spacing, wOffset }
import com.github.rbobillo.pure2048.board.{ BoardHandler, Direction, Grid, Tile }
import javax.swing.{ JFrame, JPanel }

class BoardPanel(val frame: JFrame) extends JPanel with KeyListener {

  // TODO: Maybe most of these functions should be Gui's object methods ?

  private def drawTileText(g: Graphics2D)(v: Int, x: Int, y: Int): IO[Unit] =
    for {
      xx <- IO.pure(x * wOffset)
      yy <- IO.pure(y * hOffset)
      f <- IO.pure(new Font("Helvetica Neue", Font.BOLD, hOffset / 3))
      n <- IO.pure(v.toString.dropWhile(_ == '0'))
      _ <- IO.apply(g.setColor(config.tileColor(v).font))
      _ <- IO.apply(g.setFont(f))
      _ <- Gui.drawCenteredString(g)(n, f, xx + spacing, yy + spacing, wOffset - spacing, hOffset - spacing)
    } yield ()

  private def drawTileBackGround(g: Graphics2D)(v: Int, x: Int, y: Int): IO[Unit] =
    for {
      xx <- IO.pure(x * wOffset)
      yy <- IO.pure(y * hOffset)
      _ <- IO.apply(g.setColor(config.tileColor(v).background))
      _ <- IO.apply(g.fillRoundRect(xx + spacing, yy + spacing, wOffset - spacing, hOffset - spacing, 15, 15))
    } yield ()

  private def drawTile(g: Graphics2D)(t: Tile, x: Int, y: Int, prev: IndexedTiles): IO[Unit] =
    for {
      //o <- IO.pure(prev.groupBy(_._1.id).get(t.id).flatMap(_.headOption).getOrElse(t, x, y))
      //r <- IO.apply(g.clipRect(o._2, o._3, wOffset - wOffset / 10, hOffset - hOffset / 10))
      //_ <- Gui.slide(g.getClip.asInstanceOf[Rectangle], new Point(x, y))
      _ <- drawTileBackGround(g)(t.v, x, y)
      _ <- drawTileText(g)(t.v, x, y)
    } yield ()

  def drawIndexedTiles(g: Graphics2D)(grid: Grid): IO[Unit] =
    for {
      its <- IO.pure(grid.indexedTiles)
      _ <- IO.apply(frame.setBackground(config.boardBackgroundColor))
      _ <- IO.apply(its.map { case (t, x, y) => drawTile(g)(t, x, y, grid.indexedPrev).unsafeRunSync() })
    } yield ()

  // TODO: Fix: drawIndexedTiles's repaint(), seems to block this action.
  //  Meanwhile, we call changeTitle instead
  def showGameStopMessage(g: Graphics2D)(msg: String): IO[Unit] =
    for {
      f <- IO.pure(new Font("Helvetica Neue", Font.BOLD, hOffset / 3))
      _ <- IO.apply(g.setColor(new Color(187, 173, 160, 30)))
      _ <- IO.apply(g.fillRect(0, 0, config.boardWidth, config.boardHeight))
      _ <- IO.apply(g.setColor(Color.DARK_GRAY))
      _ <- IO.apply(g.setFont(f))
      _ <- Gui.drawCenteredString(g)(msg, f, 0, 0, config.boardWidth, config.boardHeight)
    } yield ()

  override def keyPressed(e: KeyEvent): Unit =
    (e.getKeyCode match {
      case KeyEvent.VK_RIGHT => BoardHandler.merge(Direction.RIGHT)(this)
      case KeyEvent.VK_LEFT  => BoardHandler.merge(Direction.LEFT)(this)
      case KeyEvent.VK_DOWN  => BoardHandler.merge(Direction.DOWN)(this)
      case KeyEvent.VK_UP    => BoardHandler.merge(Direction.UP)(this)
      case KeyEvent.VK_R     => BoardHandler.reset(this)
      case _                 => IO.unit
    }).unsafeRunSync()

  override def keyReleased(e: KeyEvent): Unit = ()

  override def keyTyped(e: KeyEvent): Unit = ()

  override def paint(gs: Graphics): Unit = {
    for {
      g <- IO.apply(gs.asInstanceOf[Graphics2D])
      _ <- drawIndexedTiles(g)(BoardHandler.grid)
    } yield ()
  }.unsafeRunSync()

  addKeyListener(this)

}
