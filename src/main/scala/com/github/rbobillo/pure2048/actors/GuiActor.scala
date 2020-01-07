package com.github.rbobillo.pure2048.actors

import akka.actor.Actor
import akka.event.Logging
import cats.effect.IO
import com.github.rbobillo.pure2048.dto.{ MergeGrid, NewGridState }

class GuiActor extends Actor {
  private val log = Logging(context.system, this)

  private def updateGui(): IO[Unit] = IO.unit

  def receive: Receive = {
    // GUI Handling Messages
    case NewGridState(g, s) => log.info(s"NewGridState message received: $g")
    // Other messages
    case m                  => log.error(s"Unknown message received: $m")
  }

}
