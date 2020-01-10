package com.github.rbobillo.pure2048.gui

import java.awt.{ Color, Dimension, Font, Graphics2D, Point, Rectangle, Shape }
import java.awt.event.{ ActionEvent, ComponentAdapter, ComponentEvent }

import cats.effect.IO
import com.github.rbobillo.pure2048.Config.config
import com.github.rbobillo.pure2048.board.Merging.IndexedTiles
import com.github.rbobillo.pure2048.board.{ Grid, Tile }
import javax.swing.JFrame

object Gui {

  val spacing: Int = (config.boardWidth / config.gridWidth + config.boardHeight / config.gridHeight) / 2 / 10
  val wOffset: Int = config.boardWidth / config.gridWidth - spacing / config.gridWidth
  val hOffset: Int = config.boardHeight / config.gridHeight - spacing / config.gridHeight

  private def drawTileText(g: Graphics2D)(v: Int, x: Int, y: Int): IO[Unit] =
    for {
      xx <- IO.pure(x * wOffset)
      yy <- IO.pure(y * hOffset)
      f <- IO.pure(new Font("Helvetica Neue", Font.BOLD, hOffset / 3))
      n <- IO.pure(v.toString.dropWhile(_ == '0'))
      _ <- IO.apply(g.setColor(config.tileColor(v).font))
      _ <- IO.apply(g.setFont(f))
      _ <- GuiUtils.drawCenteredString(g)(n, f, xx + spacing, yy + spacing, wOffset - spacing, hOffset - spacing)
    } yield ()

  private def drawTileBackGround(g: Graphics2D)(v: Int, x: Int, y: Int): IO[Unit] =
    for {
      xx <- IO.pure(x * wOffset)
      yy <- IO.pure(y * hOffset)
      _ <- IO.apply(g.setColor(config.tileColor(v).background))
      _ <- IO.apply(g.fillRoundRect(xx + spacing, yy + spacing, wOffset - spacing, hOffset - spacing, 15, 15))
    } yield ()

  private def drawPopingTile(g: Graphics2D)(t: Tile, x: Int, y: Int, prev: IndexedTiles): IO[Unit] = ???
  private def drawSlidingTile(g: Graphics2D)(t: Tile, x: Int, y: Int, prev: IndexedTiles): IO[Unit] = ???
  private def drawBasicTile(g: Graphics2D)(t: Tile, x: Int, y: Int, prev: IndexedTiles): IO[Unit] = ???

  private def drawTile(g: Graphics2D, p: BoardPanel)(t: Tile, x: Int, y: Int, prev: IndexedTiles): IO[Unit] =
    for {
      //o <- IO.pure(prev.find(_._1.id == t.id).getOrElse(t, x, y))
      //_ <- IO.apply(println(o))
      //_ <- IO.apply(g.clipRect(o._2, o._3, wOffset, hOffset))
      //_ <- GuiUtils.slide(p, new Point(x, y))
      _ <- drawTileBackGround(g)(t.v, x, y)
      _ <- drawTileText(g)(t.v, x, y)
    } yield ()

  def drawIndexedTiles(g: Graphics2D, boardPanel: BoardPanel)(grid: Grid): IO[Unit] =
    for {
      its <- IO.pure(grid.indexedTiles)
      _ <- IO.apply(boardPanel.frame.setBackground(config.boardBackgroundColor))
      _ <- IO.apply(its.map { case (t, x, y) => drawTile(g, boardPanel)(t, x, y, grid.indexedPrev).unsafeRunSync() })
    } yield ()

  // TODO: Fix: too much repaint() seem to happen, and block this action.
  //  Meanwhile, we call changeTitle instead
  def showGameStopMessage(g: Graphics2D)(msg: String): IO[Unit] =
    for {
      f <- IO.pure(new Font("Helvetica Neue", Font.BOLD, hOffset / 3))
      _ <- IO.apply(g.setColor(new Color(187, 173, 160, 30)))
      _ <- IO.apply(g.fillRect(0, 0, config.boardWidth, config.boardHeight))
      _ <- IO.apply(g.setColor(Color.DARK_GRAY))
      _ <- IO.apply(g.setFont(f))
      _ <- GuiUtils.drawCenteredString(g)(msg, f, 0, 0, config.boardWidth, config.boardHeight)
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
      _ <- IO.apply(frame.setTitle("2048"))
      _ <- IO.apply(frame.validate())
    } yield frame

  def initGUI: IO[BoardPanel] =
    for {
      p <- createFrameAndPanel(config.boardWidth, config.boardHeight)
      _ <- initFrame(frame = p.frame, panel = p)
    } yield p

}
