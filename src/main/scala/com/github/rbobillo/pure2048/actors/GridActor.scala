package com.github.rbobillo.pure2048.actors

import akka.actor.Actor
import akka.event.Logging
import com.github.rbobillo.pure2048.dto.MergeGrid

class GridActor extends Actor {
  private val log = Logging(context.system, this)

  def receive: Receive = {
    case MergeGrid(d) => log.debug(s"MergeGrid message received: $d")
    case m            => log.error(s"Unknown message received: $m")
  }

}
