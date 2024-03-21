package com.example.myapplication;


public class Piece {


    private PieceColor color;
    private PieceType type;
    private int picId ;
    public Piece(PieceColor color){
        this.color=color;
        this.type=PieceType.PAWN;

        if (color == PieceColor.BLACK)
            this.picId = R.drawable.blackpawn;
        else if (color == PieceColor.RED)
            this.picId = R.drawable.redpawn;
        else if (color == PieceColor.EMPTY)
            this.picId = 0;




    }

    public PieceType getType() {
        return type;

    }

    public void setType(PieceType type) {
        this.type = type;
        if (type==PieceType.KING) {
            if (color == PieceColor.BLACK)
                this.picId = R.drawable.blackking;
            else if (color == PieceColor.RED)
                this.picId = R.drawable.redking;
        }
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






    @Override
    public String toString() {
        return "Piece{" +
                "color=" + color +
                '}';
    }

    public enum PieceColor{
        BLACK,
        RED,

        EMPTY;

    }
    public enum PieceType{
        PAWN,
        KING
    }


}
