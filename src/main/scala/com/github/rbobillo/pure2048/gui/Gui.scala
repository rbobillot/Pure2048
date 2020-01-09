package com.github.rbobillo.pure2048.gui

import java.awt.{ Dimension, Font, Graphics2D }

import cats.effect.IO
import com.github.rbobillo.pure2048.Config.config
import javax.swing.JFrame

object Gui {

  def drawCenteredString(g: Graphics2D)(s: String, f: Font, x: Int, y: Int, w: Int, h: Int): IO[Unit] =
    for {
      m <- IO.apply(g.getFontMetrics(f))
      i <- IO.apply(x + (w - m.stringWidth(s)) / 2)
      j <- IO.apply(y + ((h - m.getHeight) / 2) + m.getAscent)
      _ <- IO.apply(g.drawString(s, i, j))
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
    } yield frame

  def initGUI: IO[BoardPanel] =
    for {
      p <- createFrameAndPanel(config.boardWidth, config.boardHeight)
      _ <- initFrame(frame = p.frame, panel = p)
    } yield p

}
