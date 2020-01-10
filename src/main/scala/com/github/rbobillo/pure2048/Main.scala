package com.github.rbobillo.pure2048

import cats.effect.{ ExitCode, IO, IOApp }
import com.github.rbobillo.pure2048.board.BoardHandler
import com.github.rbobillo.pure2048.gui.BoardGui

object Main extends IOApp {

  def run(args: List[String]): IO[ExitCode] =
    for {
      _ <- BoardHandler.initGrid
      _ <- BoardGui.initGUI
    } yield ExitCode.Success

}
