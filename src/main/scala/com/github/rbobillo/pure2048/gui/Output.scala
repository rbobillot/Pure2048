package com.github.rbobillo.pure2048.gui

import cats.effect.IO
import javax.swing.JFrame

object Output {

  def logError[T](msg: String): IO[Unit] =
    IO.apply(println("\u001b[91merror\u001b[0m: " + msg))

  def displayGuiGrid(frame: JFrame): IO[Unit] =
    for {
      p <- IO.pure(new BoardPanel(frame)) // TODO: should be removed (for now it avoids the initial keypress bug)
      _ <- IO.apply(frame.setContentPane(p))
      _ <- IO.apply(frame.setFocusable(true))
      _ <- IO.apply(frame.setTitle(s"2048"))
      _ <- IO.apply(frame.validate())
    } yield ()

}
