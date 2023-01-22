package com.example.chess

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.chess.Model.ChessModel
import com.example.chess.Model.ChessPiece
import com.example.chess.Model.Square
import com.example.chess.databinding.ActivityMainBinding

const val TAG="MainActivity"
class MainActivity : AppCompatActivity(),ChessDelegate {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.chessview.chessDelegate=this
        binding.btnReset.setOnClickListener {
            ChessModel.reset()
            binding.chessview.invalidate()
        }

        Log.d(TAG,"$ChessModel")
    }

    override fun pieceAt(square: Square): ChessPiece? {
        return ChessModel.pieceAt(square)
    }

    override fun movePiece(from:Square,to:Square) {
        ChessModel.movePiece(from,to)
        binding.chessview.invalidate()
    }
}