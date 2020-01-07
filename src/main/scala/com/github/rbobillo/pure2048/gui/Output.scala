package com.github.rbobillo.pure2048.gui

import akka.actor.ActorRef
import cats.effect.IO
import com.github.rbobillo.pure2048.actors.GameBoardActor
import com.github.rbobillo.pure2048.grid.Grid
import javax.swing.JFrame

object Output {

  def logError[T](msg: String): IO[Unit] =
    IO.apply(println("\u001b[91merror\u001b[0m: " + msg))

  def displayGuiGrid(grid: Grid, frame: JFrame, gameBoardActor: ActorRef): IO[Unit] =
    for {
      p <- IO.pure(new GridPanel(grid, frame, gameBoardActor)) // TODO: should be removed (for now it avoids the initial keypress bug)
      _ <- IO.apply(frame.setContentPane(p))
      _ <- IO.apply(frame.setFocusable(true))
      _ <- IO.apply(frame.setTitle(s"2048 - Score: ${grid.score}"))
      _ <- IO.apply(frame.validate())
    } yield ()

}
