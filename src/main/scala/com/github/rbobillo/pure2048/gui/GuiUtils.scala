package com.github.rbobillo.pure2048.gui

import java.awt.{ Font, Graphics2D, Point, Rectangle }
import java.awt.event.ActionEvent

import cats.effect.IO
import javax.swing.{ JFrame, Timer }

object GuiUtils {

  def changeTitle(frame: JFrame)(msg: String): IO[Unit] =
    for {
      r <- IO.pure(" - Press 'r' to reset")
      _ <- IO.apply(frame.setTitle(msg + r))
    } yield ()

  def drawCenteredString(g: Graphics2D)(s: String, f: Font, x: Int, y: Int, w: Int, h: Int): IO[Unit] =
    for {
      m <- IO.apply(g.getFontMetrics(f))
      i <- IO.apply(x + (w - m.stringWidth(s)) / 2)
      j <- IO.apply(y + ((h - m.getHeight) / 2) + m.getAscent)
      _ <- IO.apply(g.drawString(s, i, j))
    } yield ()

  def slide(component: Rectangle, newPoint: Point): IO[Unit] =
    for {
      frames <- IO.pure(5) // TODO: should it be linked to grid width/length ?
      bounds <- IO.apply(component.getBounds)
      source <- IO.pure(new Point(bounds.x, bounds.y))
      target <- IO.pure(new Point((newPoint.x - source.x) / frames, (newPoint.y - source.y) / frames))
      _ <- IO.apply {
        new Timer(10, (e: ActionEvent) => (0 to frames).map { currentFrame =>
          component.setBounds(
            source.x + (target.x * currentFrame),
            source.y + (target.y * currentFrame),
            bounds.width,
            bounds.height)
        }.headOption.foreach(_ => e.getSource.asInstanceOf[Timer].stop())).start()
      }
    } yield ()

}
