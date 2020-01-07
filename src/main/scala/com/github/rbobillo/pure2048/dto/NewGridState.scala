package com.github.rbobillo.pure2048.dto

import com.github.rbobillo.pure2048.grid.Grid

case class NewGridState(grid: Grid, status: GameStatus.Value)