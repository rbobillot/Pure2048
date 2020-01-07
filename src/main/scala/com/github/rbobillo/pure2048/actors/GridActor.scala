package com.github.rbobillo.pure2048.actors

import akka.actor.Actor
import akka.event.Logging
import cats.effect.IO
import com.github.rbobillo.pure2048.dto.{ MergeGrid, NewGridState }

class GridActor extends Actor {
  private val log = Logging(context.system, this)

  private def updateGrid(): IO[Unit] = IO.unit

  def receive: Receive = {
    // Grid Handling Messages
    case MergeGrid(d) => log.info(s"MergeGrid message received: $d")
    // Other messages
    case m            => log.error(s"Unknown message received: $m")
  }

}
