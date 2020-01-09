package com.github.rbobillo.pure2048.dto

import com.github.rbobillo.pure2048.board.Grid

case class NewGridState(grid: Grid, status: GameStatus.Value)