package com.github.rbobillo.pure2048.dto

import com.github.rbobillo.pure2048.grid.Grid

case class GameStop[Status <: GameStatus](grid: Grid, status: Status)