package com.github.rbobillo.pure2048

import akka.actor.{ ActorRef, ActorSystem, Props }
import cats.effect.{ ExitCode, IO, IOApp }
import com.github.rbobillo.pure2048.actors.{ GameBoardActor, GridActor, GuiActor }
import com.github.rbobillo.pure2048.grid.Grid
import com.github.rbobillo.pure2048.gui.{ Gui, Output }

object Main extends IOApp {

  private def initGameBoardActor: IO[(ActorRef, ActorRef, ActorRef)] =
    for {
      system2048 <- IO.apply(ActorSystem("2048ActorSystem"))
      boardActor <- IO.apply(system2048.actorOf(Props(new GameBoardActor), "GameBoardActor"))
      grdActor <- IO.apply(system2048.actorOf(Props(new GridActor), "GridActor"))
      guiActor <- IO.apply(system2048.actorOf(Props(new GuiActor), "GuiActor"))
    } yield (boardActor, grdActor, guiActor)

  private def initGrid: IO[Grid] =
    for {
      cc <- IO.pure(Config.config)
      it <- IO.pure(Array.fill(cc.gridHeight)(Array.fill(cc.gridWidth)(0)))
      g0 <- IO.pure(Grid(tiles = it))
      g1 <- IO.apply(g0.addTile())
      g2 <- IO.apply(g1.addTile())
    } yield g2

  def run(args: List[String]): IO[ExitCode] =
    for {
      g <- initGrid
      a <- initGameBoardActor
      f <- Gui.initGUI(g, a._3)
      _ <- Output.displayGuiGrid(grid           = g, frame = f._1, gameBoardActor = a._1)
    } yield ExitCode.Success

}
