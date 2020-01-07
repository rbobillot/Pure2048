package com.github.rbobillo.pure2048.actors

import akka.actor.Actor
import akka.event.Logging
import com.github.rbobillo.pure2048.dto.{ MergeGrid, NewGridState }

class GameBoardActor extends Actor {
  private val log = Logging(context.system, this)

  def receive: Receive = {
    // GUI Handling Messages
    case NewGridState(g, s) => log.info(s"NewGridState message received: $g")
    // Grid Handling Messages
    case MergeGrid(d)       => log.info(s"MergeGrid message received: $d")
    // Other messages
    case m                  => log.error(s"Unknown message received: $m")
  }

}