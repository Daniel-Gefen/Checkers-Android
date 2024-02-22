package com.example.myapplication;


public class Piece {

    PiecePosition position;
    private PieceColor color;
    private int picId ;
    public Piece(PieceColor color){
        this.color=color;
        if (color==PieceColor.BLACK)
            this.picId=R.drawable.blackpawn;
        else if (color==PieceColor.RED)
            this.picId=R.drawable.redpawn;
        else if (color==PieceColor.EMPTY)
            this.picId=0;



    }




    public boolean isEmpty(){
        return color==PieceColor.EMPTY;
    }

    public int getPicId() {
        return picId;
    }

    public PieceColor getColor() {
        return color;
    }

    public enum PieceColor{
        BLACK,
        RED,

        EMPTY

    }


    @Override
    public String toString() {
        return "Piece{" +
                "color=" + color +
                '}';
    }




}
