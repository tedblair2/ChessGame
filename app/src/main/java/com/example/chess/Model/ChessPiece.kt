package com.example.chess.Model

data class ChessPiece(
    val col:Int,
    val row:Int,
    val player:ChessPlayer,
    val rank: ChessRank,
    val resId:Int
)
