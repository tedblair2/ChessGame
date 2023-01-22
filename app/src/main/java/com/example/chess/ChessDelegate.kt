package com.example.chess

import com.example.chess.Model.ChessPiece
import com.example.chess.Model.Square

interface ChessDelegate {
    fun pieceAt(square: Square): ChessPiece?
    fun movePiece(from:Square,to:Square)
}