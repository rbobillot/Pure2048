package com.github.rbobillo.pure2048.gui

import java.awt.{ Color, Dimension, Font, Graphics2D }

import akka.actor.ActorRef
import cats.effect.IO
import com.github.rbobillo.pure2048.Config.config
import com.github.rbobillo.pure2048.grid.Grid
import com.github.rbobillo.pure2048.grid.Merging.IndexedTiles
import javax.swing.JFrame

object Gui {

  private val wOffset = config.boardWidth / config.gridWidth
  private val hOffset = config.boardHeight / config.gridHeight

  /* ************** */
  /* **          ** */
  /* ** GUI Init ** */
  /* **          ** */
  /* ************** */

  private def createFrameAndPanel(grid: Grid, width: Int, height: Int, gameBoardActor: ActorRef): IO[(JFrame, GridPanel)] =
    for {
      f <- IO.pure(new JFrame)
      d <- IO.pure(new Dimension(width, height))
      p <- IO.pure(new GridPanel(grid, f, gameBoardActor))
      _ <- IO.apply(p.setPreferredSize(d))
    } yield f -> p

  private def initFrame(frame: JFrame, panel: GridPanel): IO[JFrame] =
    for {
      _ <- IO.apply(frame.setDefaultCloseOperation(3)) // EXIT_ON_CLOSE
      _ <- IO.apply(frame.getContentPane.add(panel))
      _ <- IO.apply(frame.pack())
      _ <- IO.apply(frame.setVisible(true))
      _ <- IO.apply(frame.addKeyListener(panel))
    } yield frame

  def initGUI(grid: Grid, gameBoardActor: ActorRef): IO[(JFrame, GridPanel)] =
    for {
      fp <- createFrameAndPanel(grid, config.boardWidth, config.boardHeight, gameBoardActor)
      _ <- initFrame(frame = fp._1, panel = fp._2)
    } yield fp

  /* ***************** */
  /* **             ** */
  /* ** GUI Updates ** */
  /* **             ** */
  /* ***************** */

}
