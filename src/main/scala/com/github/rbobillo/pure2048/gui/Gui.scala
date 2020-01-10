package com.github.rbobillo.pure2048.gui

import java.awt.{ Dimension, Font, Graphics2D, Point, Rectangle }
import java.awt.event.{ ActionEvent, ActionListener, ComponentAdapter, ComponentEvent }

import cats.effect.IO
import com.github.rbobillo.pure2048.Config.config
import com.github.rbobillo.pure2048.board.BoardHandler
import javax.swing.{ JComponent, JFrame, Timer }

object Gui {

  val wOffset: Int = config.boardWidth / config.gridWidth
  val hOffset: Int = config.boardHeight / config.gridHeight

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

  def slide(component: JComponent, newPoint: Point, frames: Int, interval: Int): IO[Unit] =
    for {
      bounds <- IO.apply(component.getBounds())
      source <- IO.pure(new Point(bounds.x, bounds.y))
      target <- IO.pure(new Point((newPoint.x - source.x) / frames, (newPoint.y - source.y) / frames))
      _ <- IO.apply {
        new Timer(interval, (e: ActionEvent) => (0 to frames).map { currentFrame =>
          component.setBounds(
            source.x + (target.x * currentFrame),
            source.y + (target.y * currentFrame),
            bounds.width,
            bounds.height)
        }.headOption.foreach(_ => e.getSource.asInstanceOf[Timer].stop())).start()
      }
    } yield ()

  private def createFrameAndPanel(width: Int, height: Int): IO[BoardPanel] =
    for {
      f <- IO.pure(new JFrame)
      d <- IO.pure(new Dimension(width, height))
      p <- IO.pure(new BoardPanel(f))
      _ <- IO.apply(p.setPreferredSize(d))
    } yield p

  private def initFrame(frame: JFrame, panel: BoardPanel): IO[JFrame] =
    for {
      _ <- IO.apply(frame.setDefaultCloseOperation(3)) // EXIT_ON_CLOSE
      _ <- IO.apply(frame.getContentPane.add(panel))
      _ <- IO.apply(frame.pack())
      _ <- IO.apply(frame.setVisible(true))
      _ <- IO.apply(frame.addKeyListener(panel))
      _ <- IO.apply(frame.setFocusable(true))
      _ <- IO.apply(frame.setResizable(false))
      _ <- IO.apply(frame.setTitle(s"2048"))
      _ <- IO.apply(frame.validate())
    } yield frame

  def initGUI: IO[BoardPanel] =
    for {
      p <- createFrameAndPanel(config.boardWidth, config.boardHeight)
      _ <- initFrame(frame = p.frame, panel = p)
    } yield p

}
