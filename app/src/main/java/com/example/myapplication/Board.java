package com.example.myapplication;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;

import java.util.LinkedList;


public class Board {
    private ImageView[][] tiles;

    private Piece[][] pieces;

    private LinkedList<Integer> possibleMoves;
    private Piece.PieceColor currentPlayerTurn;
    private PiecePosition firstPiecePosition;
    private boolean firstClick;
    private static final int BOARD_SIZE = 8;
    private static final int TILE_HEIGHT = 135;

    public Board(Context context) {
        currentPlayerTurn = Piece.PieceColor.RED;

        firstClick = true;
        this.tiles = new ImageView[BOARD_SIZE][BOARD_SIZE];
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[i].length; j++) {
                tiles[i][j] = new ImageView(context);
                tiles[i][j].setOnClickListener(listener);
            }
        }
        this.pieces = new Piece[BOARD_SIZE][BOARD_SIZE];


    }


    public void createBoard(TableLayout mainLayout) {
        for (int i = 0; i < 8; i++) {
            TableRow tableRow = new TableRow(mainLayout.getContext());
            tableRow.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            mainLayout.setStretchAllColumns(true);
            mainLayout.setShrinkAllColumns(true);
            for (int j = 0; j < 8; j++) {


                // Set alternate background colors

                ImageView image = tiles[i][j];
                image.setTag(i * 8 + j);
                image.setBackgroundColor((i + j) % 2 == 0 ? Color.WHITE : Color.BLACK);
                //init pieces
                setPiece(i, j, image);

                // Set equal width and height for square buttons
                TableRow.LayoutParams imageParams = new TableRow.LayoutParams(0, TILE_HEIGHT, 1f);
                image.setLayoutParams(imageParams);

                tableRow.addView(image);
            }


            mainLayout.addView(tableRow);
        }
    }


    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Handler handler = new Handler();

            PiecePosition secondPiecePosition;
            if (v instanceof ImageView) {

                if (firstClick) {
                    firstPiecePosition = new PiecePosition(Integer.parseInt(v.getTag().toString()));
                    int i = firstPiecePosition.getRow();
                    int j = firstPiecePosition.getColumn();
                    ImageView image = tiles[i][j];
                    ColorDrawable drawable = (ColorDrawable) image.getBackground();
                    int backGroundColor = (drawable.getColor());
                    if (pieces[i][j].isEmpty() || currentPlayerTurn != pieces[i][j].getColor()) {

                        image.setBackgroundColor(Color.rgb(242, 31, 31));
                        handler.removeCallbacksAndMessages(image);

                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                image.setBackgroundColor(backGroundColor);
                            }
                        }, 200);
                        return;
                    }
                    image.setBackgroundColor(Color.rgb(0, 250, 0));
                    possibleMoves = checkAllPossibleMoves(pieces[i][j], i, j);
                    highlightPossibleMoves();
                    firstClick = false;


                } else {

                    secondPiecePosition = new PiecePosition(Integer.parseInt(v.getTag().toString()));

                    int i = firstPiecePosition.getRow();
                    int j = firstPiecePosition.getColumn();
                    if (secondPiecePosition.getValue() == firstPiecePosition.getValue()) {
                        tiles[i][j].setBackgroundColor((i + j) % 2 == 0 ? Color.WHITE : Color.BLACK);
                        deHighlightPossibleMoves();
                        firstClick = true;
                    } else if (!possibleMoves.contains(secondPiecePosition.getValue())) {


                        ImageView image = tiles[i][j];
                        ColorDrawable drawable = (ColorDrawable) image.getBackground();
                        int backGroundColor = (drawable.getColor());
                        image.setBackgroundColor(Color.rgb(242, 31, 31));
                        handler.removeCallbacksAndMessages(image);

                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                image.setBackgroundColor(backGroundColor);
                            }
                        }, 200);
                        return;

                    } else {

                        deHighlightPossibleMoves();

                        ImageView image = tiles[i][j];
                        image.setBackgroundColor((i + j) % 2 == 0 ? Color.WHITE : Color.BLACK);


                        movePiece(firstPiecePosition, secondPiecePosition);
                        firstClick = true;

                    }
                }
            }
        }
    };

    private void movePiece(PiecePosition first, PiecePosition second) {
        Piece piece = pieces[first.getRow()][first.getColumn()];
        pieces[first.getRow()][first.getColumn()] = new Piece(Piece.PieceColor.EMPTY);
        pieces[second.getRow()][second.getColumn()] = piece;
        currentPlayerTurn = (currentPlayerTurn == Piece.PieceColor.BLACK) ? Piece.PieceColor.RED : Piece.PieceColor.BLACK;
        loadBoard();

    }

    private void setPiece(int i, int j, ImageView image) {
        Piece.PieceColor color = Piece.PieceColor.EMPTY;

        if (i > 2 && i < 5) {
            pieces[i][j] = new Piece(color);
            return;
        }

        if ((i + j) % 2 == 1) {
            if (i < 3)
                color = Piece.PieceColor.BLACK;
            else
                color = Piece.PieceColor.RED;
            Piece piece = new Piece(color);
            image.setImageResource(piece.getPicId());
            pieces[i][j] = new Piece(color);
        } else {
            Piece piece = new Piece(Piece.PieceColor.EMPTY);
            pieces[i][j] = piece;
        }


    }

    private void loadBoard() {
        for (int i = 0; i < 8; i++) {

            for (int j = 0; j < 8; j++) {
                ImageView image = tiles[i][j];
                image.setImageResource(pieces[i][j].getPicId());


            }
        }
    }

    private LinkedList<Integer> checkAllPossibleMoves(Piece piece, int i, int j) {
        Piece.PieceColor color = piece.getColor();
        int row = i;
        int col = j;
        possibleMoves = new LinkedList<Integer>();

        // Define the directions based on the color of the piece
        int forward = (color == Piece.PieceColor.RED) ? -1 : 1;

        // Define the possible diagonal directions
        int[][] directions = {{forward, 1}, {forward, -1}};

        // Check all possible directions
        for (int[] direction : directions) {
            int newRow = row + direction[0];
            int newCol = col + direction[1];

            // Check if the new position is within the bounds of the board
            if (isValidPosition(newRow, newCol)) {
                PiecePosition newPosition = new PiecePosition(newRow, newCol);
                // If the square is empty, it's a potential move
                if (pieces[newRow][newCol].isEmpty()) {
                    possibleMoves.add(newPosition.getValue());
                } else {
                    //checks if the same color  piece is blocking
                    if (pieces[newRow][newCol].getColor() == color) {
                        continue;
                    }

                    int jumpRow = newRow + direction[0];
                    int jumpCol = newCol + direction[1];
                    if (isValidPosition(jumpRow, jumpCol)) {
                        if (pieces[jumpRow][jumpCol].isEmpty()) {
                            possibleMoves.add(new PiecePosition(jumpRow, jumpCol).getValue());
                            tiles[jumpRow][jumpCol].setBackgroundColor(Color.rgb(0, 0, 255));
                            // Check for multiple jumps
                            for (int[] dir : directions) {
                                checkForJumps(piece, jumpRow, jumpCol);
                            }
                        }
                    }


                }
            }
        }
        return possibleMoves;

        // At this point, possibleMoves contains all possible moves for the piece
    }

    public void checkForJumps(Piece piece, int rowPos, int colPos) {
        Piece.PieceColor color = piece.getColor();
        int forward = (color == Piece.PieceColor.RED) ? -1 : 1;
        int[][] directions = {{forward, 1}, {forward, -1}};


        if (!isValidPosition(rowPos, colPos) || !pieces[rowPos][colPos].isEmpty() || pieces[rowPos][colPos].getColor()==color)
            return;


        Piece test = pieces[rowPos][colPos];
        if (test.getColor() != color && test.isEmpty()) {
            for (int[] dir : directions) {

                if (isValidPosition(rowPos + dir[0], colPos + dir[1]) && !pieces[rowPos + dir[0]][colPos + dir[1]].isEmpty()&& pieces[rowPos + dir[0]][colPos + dir[1]].getColor()!=color) {
                    if (isValidPosition(rowPos + dir[0]*2, colPos + dir[1]*2) && pieces[rowPos + dir[0]*2][colPos + dir[1]*2].isEmpty())
                        possibleMoves.add(new PiecePosition(rowPos + dir[0]*2, colPos + dir[1]*2).getValue());

                    checkForJumps(piece, rowPos + dir[0]*2 ,colPos + dir[1]*2);

                }
            }

        }


    }



    // Helper method to check if a position is within the bounds of the board
    private boolean isValidPosition(int row, int col) {
        return row >= 0 && row < BOARD_SIZE && col >= 0 && col < BOARD_SIZE;
    }


    private void highlightPossibleMoves() {
        for (Integer num : possibleMoves) {
            PiecePosition position = new PiecePosition(num);
            tiles[position.getRow()][position.getColumn()].setBackgroundColor(Color.rgb(0, 0, 255));

        }
    }

    private void deHighlightPossibleMoves() {
        for (Integer num : possibleMoves) {
            PiecePosition position = new PiecePosition(num);
            tiles[position.getRow()][position.getColumn()].setBackgroundColor((position.getRow() + position.getColumn()) % 2 == 0 ? Color.WHITE : Color.BLACK);

        }
        possibleMoves = new LinkedList<Integer>();
    }


}
