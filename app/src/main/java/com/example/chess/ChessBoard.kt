package com.example.chess

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.MotionEvent.ACTION_MOVE
import android.view.View
import com.example.chess.Model.ChessPiece
import com.example.chess.Model.Square
import kotlin.math.min

class ChessBoard(context: Context?, attrs: AttributeSet?) : View(context, attrs) {
    private val scaleFactor=.9f
    private var originX=1f
    private var originY=1f
    private var cellSide=1f
    private val paint=Paint()
    private val imageIds= setOf(
        R.drawable.bishop_black,
        R.drawable.bishop_white,
        R.drawable.king_black,
        R.drawable.king_white,
        R.drawable.knight_black,
        R.drawable.knight_white,
        R.drawable.pawn_black,
        R.drawable.pawn_white,
        R.drawable.queen_black,
        R.drawable.queen_white,
        R.drawable.rook_black,
        R.drawable.rook_white
    )
    private val bitmaps= mutableMapOf<Int,Bitmap>()
    lateinit var chessDelegate: ChessDelegate
    private var fromCol=-1
    private var fromRow=-1
    private var movingX=-1f
    private var movingY=-1f
    private var movingBitmap: Bitmap?=null
    private var movingPiece:ChessPiece?=null

    init {
        loadBitmaps()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val smaller= min(widthMeasureSpec,heightMeasureSpec)
        setMeasuredDimension(smaller,smaller)
    }

    override fun onDraw(canvas: Canvas?) {
        canvas?:return
        val chessboardSide= min(width,height)*scaleFactor
        cellSide=chessboardSide/8f
        originX=(width-chessboardSide)/2f
        originY=(height-chessboardSide)/2f
        drawChessBoard(canvas)
        drawPieces(canvas)

    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event?:return false
        when(event.action){
            MotionEvent.ACTION_DOWN->{
                fromCol=((event.x-originX)/cellSide).toInt()
                fromRow=7-((event.y-originY)/cellSide).toInt()
                chessDelegate.pieceAt(Square(fromCol,fromRow))?.let {
                    movingPiece=it
                    movingBitmap=bitmaps[it.resId]
                }
            }
            MotionEvent.ACTION_UP->{
                val col=((event.x-originX)/cellSide).toInt()
                val row=7-((event.y-originY)/cellSide).toInt()
                chessDelegate.movePiece(Square(fromCol,fromRow),Square(col,row))
                movingPiece=null
                movingBitmap=null
            }
            ACTION_MOVE ->{
                movingX=event.x
                movingY=event.y
                invalidate()
            }
        }
        return true
    }
    private fun drawPieces(canvas: Canvas){
        for (row in 0..7){
            for (col in 0..7){
                chessDelegate.pieceAt(Square(col,row))?.let {
                    if (it !=movingPiece){
                        drawPieceAt(col,row,canvas,it.resId)
                    }
                }
            }
        }
        movingBitmap?.let {
            canvas.drawBitmap(it,null,RectF(movingX-(cellSide/2),movingY-(cellSide/2),movingX+(cellSide/2),movingY+(cellSide/2)),paint)
        }
    }
    private fun drawPieceAt(col:Int,row:Int,canvas: Canvas,resId:Int){
        val row=7-row
        val piece=bitmaps[resId]!!
        canvas.drawBitmap(piece,null,RectF(originX+col*cellSide,originY+row*cellSide,originX+(col+1)*cellSide,originY+(row+1)*cellSide),paint)
    }
    private fun drawChessBoard(canvas: Canvas){
        for (i in 0..7){
            for (j in 0..7){
                paint.color=if((i+j)%2==0)Color.LTGRAY else Color.DKGRAY
                canvas.drawRect(originX+i*cellSide,originY+j*cellSide,originX+(i+1)*cellSide,originY+(j+1)*cellSide,paint)
            }
        }
    }
    private fun loadBitmaps(){
        imageIds.forEach {
            bitmaps[it]=BitmapFactory.decodeResource(resources,it)
        }
    }
}