package com.example.myapplication;



import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;

import androidx.lifecycle.viewmodel.CreationExtras;

public class Piece {


    private PieceColor color;
    private int picId ;
    public Piece(PieceColor color){
        this.color=color;
        if (color==PieceColor.Black)
            this.picId=R.drawable.blackpawn;
        else if (color==PieceColor.Red)
            this.picId=R.drawable.redpawn;
        else if (color==PieceColor.Empty)
            this.picId=0;



    }

    public boolean isEmpty(){
        return color==PieceColor.Empty;
    }

    public int getPicId() {
        return picId;
    }

    public enum PieceColor{
        Black,
        Red,

        Empty;
    }

    @Override
    public String toString() {
        return "Piece{" +
                "color=" + color +
                '}';
    }
}
