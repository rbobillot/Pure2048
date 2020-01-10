package com.github.rbobillo.pure2048.gui

import java.awt.{ Color, Font, Graphics, Graphics2D, Point, Rectangle }
import java.awt.event.{ KeyEvent, KeyListener }

import cats.effect.IO
import com.github.rbobillo.pure2048.board.{ BoardHandler, Direction }
import javax.swing.{ JFrame, JPanel }

class BoardPanel(val frame: JFrame) extends JPanel with KeyListener {

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
      _ <- Gui.drawIndexedTiles(g, this)(BoardHandler.grid)
    } yield ()
  }.unsafeRunSync()

  addKeyListener(this)

}
