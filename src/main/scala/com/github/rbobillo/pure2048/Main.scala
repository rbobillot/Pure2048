package com.github.rbobillo.pure2048

import akka.actor.{ ActorRef, ActorSystem, Props }
import cats.effect.{ ExitCode, IO, IOApp }
import com.github.rbobillo.pure2048.actors.{ GameBoardActor, GridActor, GuiActor }
import com.github.rbobillo.pure2048.board.BoardHandler
import com.github.rbobillo.pure2048.gui.{ Gui, Output }

object Main extends IOApp {

  private def initGameBoardActor: IO[(ActorRef, ActorRef, ActorRef)] =
    for {
      system2048 <- IO.apply(ActorSystem("2048ActorSystem"))
      boardActor <- IO.apply(system2048.actorOf(Props(new GameBoardActor), "GameBoardActor"))
      grdActor <- IO.apply(system2048.actorOf(Props(new GridActor), "GridActor"))
      guiActor <- IO.apply(system2048.actorOf(Props(new GuiActor), "GuiActor"))
    } yield (boardActor, grdActor, guiActor)

  def run(args: List[String]): IO[ExitCode] =
    for {
      g <- BoardHandler.initGrid
      a <- initGameBoardActor
      p <- Gui.initGUI(a._3)
      _ <- Output.displayGuiGrid(frame          = p.frame, gameBoardActor = a._1)
    } yield ExitCode.Success

}
