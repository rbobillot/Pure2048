package com.github.rbobillo.pure2048

import akka.actor.{ ActorRef, ActorSystem, Props }
import cats.effect.{ ExitCode, IO, IOApp }
import com.github.rbobillo.pure2048.actors.{ GameBoardActor, GridActor, GuiActor }
import com.github.rbobillo.pure2048.grid.Grid
import com.github.rbobillo.pure2048.grid.Merging.Tiles
import com.github.rbobillo.pure2048.gui.{ Gui, Output }

object Main extends IOApp {

  private def initGameBoardActor: IO[(ActorRef, ActorRef, ActorRef)] =
    for {
      system2048 <- IO.apply(ActorSystem("2048ActorSystem"))
      boardActor <- IO.apply(system2048.actorOf(Props[GameBoardActor], "GameBoardActor"))
      grdActor <- IO.apply(system2048.actorOf(Props[GridActor], "GridActor"))
      guiActor <- IO.apply(system2048.actorOf(Props[GuiActor], "GuiActor"))
    } yield (boardActor, grdActor, guiActor)

  private def initGrid(initialTiles: Tiles): IO[Grid] =
    for {
      g0 <- IO.pure(Grid(initialTiles))
      g1 <- IO.apply(g0.addTile())
      g2 <- IO.apply(g1.addTile())
    } yield g2

  def run(args: List[String]): IO[ExitCode] =
    for {
      g <- initGrid(initialTiles = Array.fill(4)(Array.fill(4)(0)))
      a <- initGameBoardActor
      f <- Gui.initGUI(g, 540, 540, a._3)
      _ <- Output.displayGuiGrid(grid  = g, frame = f._1, a._3)
    } yield ExitCode.Success

}
