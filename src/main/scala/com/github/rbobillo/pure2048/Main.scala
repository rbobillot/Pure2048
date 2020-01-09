package com.github.rbobillo.pure2048

import cats.effect.{ ExitCode, IO, IOApp }
import com.github.rbobillo.pure2048.board.BoardHandler
import com.github.rbobillo.pure2048.gui.{ Gui, Output }

object Main extends IOApp {

  def run(args: List[String]): IO[ExitCode] =
    for {
      g <- BoardHandler.initGrid
      p <- Gui.initGUI
      _ <- Output.displayGuiGrid(frame = p.frame)
    } yield ExitCode.Success

}
