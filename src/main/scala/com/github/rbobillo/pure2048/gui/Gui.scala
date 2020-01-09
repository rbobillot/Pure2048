package com.github.rbobillo.pure2048.gui

import java.awt.{ Color, Dimension, Font, Graphics2D }

import akka.actor.ActorRef
import cats.effect.IO
import com.github.rbobillo.pure2048.Config.config
import com.github.rbobillo.pure2048.board.Grid
import javax.swing.JFrame

object Gui {

  private def createFrameAndPanel(width: Int, height: Int, gameBoardActor: ActorRef): IO[BoardPanel] =
    for {
      f <- IO.pure(new JFrame)
      d <- IO.pure(new Dimension(width, height))
      p <- IO.pure(new BoardPanel(f, gameBoardActor))
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

  def initGUI(gameBoardActor: ActorRef): IO[BoardPanel] =
    for {
      p <- createFrameAndPanel(config.boardWidth, config.boardHeight, gameBoardActor)
      _ <- initFrame(frame = p.frame, panel = p)
    } yield p

}
