package com.example.myapplication;

public class PiecePosition {

    private int row;
    private int column;

    public PiecePosition(int num) {

        this.row = num / 8;  // Integer division to get the row
        this.column = num % 8;      // Modulo operation to get the column
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
