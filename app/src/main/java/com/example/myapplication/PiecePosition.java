package com.example.myapplication;

public class PiecePosition {

    private int row;
    private int column;

    public PiecePosition(int num) {

        this.row = num / 8;  // Integer division to get the row
        this.column = num % 8;      // Modulo operation to get the column
    }

    public PiecePosition(int row,int column){
        this.row=row;
        this.column=column;

    }

    public int getValue(){
        return this.row*8 +this.column;
    }

    // Add getters for rowPosition and column if needed
    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    @Override
    public String toString() {
        return "["+row+","+column+"]";
    }
}
