package com.github.rbobillo.pure2048

import cats.effect.{ ExitCode, IO, IOApp }
import com.github.rbobillo.pure2048.grid.Grid
import com.github.rbobillo.pure2048.io.Output
import com.github.rbobillo.pure2048.io.gui.Gui

object Main extends IOApp {

  def run(args: List[String]): IO[ExitCode] =
    for {
      g <- IO.pure(Grid(Array.fill(4)(Array.fill(4)(0)))) // initial tiles
      a <- IO.apply(g.addTile())
      b <- IO.apply(a.addTile())
      f <- Gui.initGUI(b)
      _ <- Output.displayGuiGrid(f, b)
    } yield ExitCode.Success

}
