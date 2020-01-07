package com.github.rbobillo.pure2048.dto

sealed trait GameStatus

case object GameWonStatus extends GameStatus
case object GameLostStatus extends GameStatus