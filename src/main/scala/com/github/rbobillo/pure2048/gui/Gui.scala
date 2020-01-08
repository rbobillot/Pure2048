package com.github.rbobillo.pure2048.gui

import java.awt.{ Color, Dimension, Font, Graphics2D }

import akka.actor.ActorRef
import cats.effect.IO
import com.github.rbobillo.pure2048.grid.Grid
import javax.swing.JFrame

object Gui {

  private val offset = 540 / 4

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

  private def drawTileBackGround(g: Graphics2D)(v: Int, x: Int, y: Int): IO[Unit] =
    for {
      xx <- IO.pure(x * offset)
      yy <- IO.pure(y * offset)
      _ <- IO.apply(g.setColor(tileColor(v).background))
      _ <- IO.apply(g.fillRect(xx, yy, xx + offset, yy + offset))
    } yield ()

  private def drawSplitters(g: Graphics2D): IO[Unit] =
    for {
      offset <- IO.pure(540 / 4)
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

  def initGUI(grid: Grid, width: Int, height: Int, gameBoardActor: ActorRef): IO[(JFrame, GridPanel)] =
    for {
      fp <- createFrameAndPanel(grid, width, height, gameBoardActor)
      _ <- initFrame(frame = fp._1, panel = fp._2)
    } yield fp

}
