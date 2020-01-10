package com.github.rbobillo.pure2048.gui

import java.awt.{ Color, Font, Graphics, Graphics2D }
import java.awt.event.{ KeyEvent, KeyListener }

import cats.effect.IO
import com.github.rbobillo.pure2048.Config.config
import com.github.rbobillo.pure2048.Direction
import com.github.rbobillo.pure2048.gui.Gui.{ hOffset, wOffset }
import com.github.rbobillo.pure2048.board.Merging.IndexedTiles
import com.github.rbobillo.pure2048.board.BoardHandler
import javax.swing.{ JFrame, JPanel }

class BoardPanel(val frame: JFrame) extends JPanel with KeyListener {

  // TODO: Maybe most of these functions should be Gui's object methods ?

  // TODO: Fix splitters drawing, when grid dimensions are not the same: 4x5, for example
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
      f <- IO.pure(new Font("Helvetica Neue", Font.BOLD, hOffset / 3))
      n <- IO.pure(v.toString.dropWhile(_ == '0'))
      _ <- IO.apply(g.setColor(config.tileColor(v).font))
      _ <- IO.apply(g.setFont(f))
      _ <- Gui.drawCenteredString(g)(n, f, xx, yy, wOffset, hOffset)
    } yield ()

  private def drawTileBackGround(g: Graphics2D)(v: Int, x: Int, y: Int): IO[Unit] =
    for {
      xx <- IO.pure(x * wOffset)
      yy <- IO.pure(y * hOffset)
      _ <- IO.apply(g.setColor(config.tileColor(v).background))
      _ <- IO.apply(g.fillRect(xx, yy, xx + wOffset, yy + hOffset))
    } yield ()

  private def drawTile(g: Graphics2D)(v: Int, x: Int, y: Int): IO[Unit] =
    for {
      _ <- drawTileBackGround(g)(v, x, y)
      _ <- drawTileText(g)(v, x, y)
    } yield ()

  def drawIndexedTiles(g: Graphics2D)(its: IndexedTiles): IO[Unit] =
    for {
      _ <- IO.apply(its.map { case (v, x, y) => drawTile(g)(v, x, y).unsafeRunSync() })
      _ <- drawSplitters(g)
      _ <- IO.apply(repaint())
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
      _ <- drawIndexedTiles(g)(BoardHandler.grid.indexed)
    } yield ()
  }.unsafeRunSync()

  addKeyListener(this)

}
