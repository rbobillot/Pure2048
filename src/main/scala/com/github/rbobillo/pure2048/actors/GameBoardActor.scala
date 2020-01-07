package com.github.rbobillo.pure2048.actors

import akka.actor.Actor
import akka.event.Logging
import com.github.rbobillo.pure2048.dto.{ GameStop, GridState, MergeGrid }

class GameBoardActor extends Actor {
  private val log = Logging(context.system, this)

  def receive: Receive = {
    case GameStop(g, s) => log.debug(s"GameStop message received: $s")
    case GridState(g)   => log.debug(s"GridState message received: $g")
    case m              => log.error(s"Unknown message received: $m")
  }

}
