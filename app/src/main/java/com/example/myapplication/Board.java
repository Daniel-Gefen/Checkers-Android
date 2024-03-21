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

import java.util.Iterator;
import java.util.LinkedList;


public class Board {
    private ImageView[][] tiles;


    private Piece[][] pieces;

    private LinkedList<Integer> possibleMoves;
    private Piece.PieceColor currentPlayerTurn;
    private PiecePosition firstPiecePosition;

    private PiecePosition secondPiecePosition;
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

            //           PiecePosition secondPiecePosition;
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
                    checkAllPossibleMove(pieces[i][j], i, j);
                    if (possibleMoves.isEmpty()){
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

                        //simonremovepiecefromboard();
                        firstClick = true;
                        movePiece(firstPiecePosition, secondPiecePosition);





                    }
                }
            }
        }
    };

    private void movePiece(PiecePosition first, PiecePosition second) {


        if (Math.abs(first.getRow() - second.getRow()) > 1) {
            removeCapturedPawns(first, second);

            // Check for further capturing moves
            checkForFurtherCaptures(second);


        }

        Piece piece = pieces[first.getRow()][first.getColumn()];
        pieces[first.getRow()][first.getColumn()] = new Piece(Piece.PieceColor.EMPTY);
        pieces[second.getRow()][second.getColumn()] = piece;
        if (second.getRow()==7&&piece.getColor()== Piece.PieceColor.BLACK){
            piece.setType(Piece.PieceType.KING);
        }
        else if(second.getRow()==0&piece.getColor()== Piece.PieceColor.RED){
            piece.setType(Piece.PieceType.KING);
        }

        // Check if it's a capturing move

        if (possibleMoves.isEmpty()) {
            currentPlayerTurn = (currentPlayerTurn == Piece.PieceColor.BLACK) ? Piece.PieceColor.RED : Piece.PieceColor.BLACK;

        }
        else {


            firstPiecePosition = second;
            firstClick = false;
            int i = firstPiecePosition.getRow();
            int j = firstPiecePosition.getColumn();
            ImageView image = tiles[i][j];
            image.setBackgroundColor(Color.rgb(0,255,0));
        }

        loadBoard();
    }



    private void checkForFurtherCaptures(PiecePosition position) {
        deHighlightPossibleMoves();
        int row = position.getRow();
        int col = position.getColumn();
        Piece piece = pieces[firstPiecePosition.getRow()][firstPiecePosition.getColumn()];
        checkForJumps(piece, row, col); // Recursively check for further capturing moves
        highlightPossibleMoves();

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

    private void checkAllPossibleMove(Piece piece, int row, int column) {
        int[][] directions;
        Piece.PieceColor color = piece.getColor();

        possibleMoves = new LinkedList<Integer>();

        // Define the directions based on the color of the piece
        if (piece.getType() == Piece.PieceType.PAWN) {
            int forward = (color == Piece.PieceColor.RED) ? -1 : 1;
            // Define the possible diagonal directions
            directions = new int[][]{{forward, 1}, {forward, -1}};
        } else {
            directions = new int[][]{{1, 1}, {1, -1}, {-1, 1}, {-1, -1}};
        }
        // Check all possible directions

        for (int[] direction : directions) {
            int newRow = row + direction[0];
            int newCol = column + direction[1];

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

                            // Check for multiple jumps


                        }
                    }


                }
            }
        }
        // At this point, possibleMoves contains all possible moves for the piece
        if (!possibleMoves.isEmpty()) {
            int maxDistance = findMaxInList(row);
            if (maxDistance > 1) {
                Iterator<Integer> iterator = possibleMoves.iterator();
                while (iterator.hasNext()) {
                    int pos = iterator.next();
                    int distance = Math.abs(row - pos / 8);
                    if (distance <= 1) {
                        iterator.remove();
                    }
                }
            }
        }

    }




    private void checkForJumps(Piece selectedPiece, int rowPos, int colPos) {
        Piece.PieceColor color = selectedPiece.getColor();
        int[][] directions;
        // Define the directions based on the color of the piece
        if (selectedPiece.getType() == Piece.PieceType.PAWN) {
            int forward = (color == Piece.PieceColor.RED) ? -1 : 1;
            // Define the possible diagonal directions
            directions = new int[][]{{forward, 1}, {forward, -1}};
        } else {
            directions = new int[][]{{1, 1}, {1, -1}, {-1, 1}, {-1, -1}};
        }

        // check if piece is in a valid position
        if (!isValidPosition(rowPos, colPos) )
            return;


        Piece currentPiece = pieces[rowPos][colPos];

        if (currentPiece.getColor() != color && currentPiece.isEmpty()) {
            for (int[] dir : directions) {

                int jumpFirstRow=rowPos + dir[0];
                int jumpFirstColumn=colPos + dir[1];
                int jumpSecondRow = jumpFirstRow+dir[0];
                int jumpSecondColumn = jumpFirstColumn+dir[1];

                //checks if the piece that you can eat is the opposite color, the tile is not empty and its in a valid position
                if (isValidPosition(jumpFirstRow,jumpFirstColumn ) && !pieces[jumpFirstRow][jumpFirstColumn].isEmpty()&& pieces[jumpFirstRow][jumpFirstColumn].getColor()!=color) {
                    //checks if there is not a piece blocking
                    if (isValidPosition(jumpSecondRow, jumpSecondColumn) && pieces[jumpSecondRow][jumpSecondColumn].isEmpty())
                        possibleMoves.add(new PiecePosition(jumpSecondRow, jumpSecondColumn).getValue());

                    //  checkForJumps(selectedPiece, jumpSecondRow ,jumpSecondColumn);

                }
            }

        }
//        if (!possibleMoves.isEmpty()) {
//            int maxDistance = findMaxInList(rowPos);
//            if (maxDistance>1) {
//                Iterator<Integer> iterator = possibleMoves.iterator();
//                while (iterator.hasNext()) {
//                    int pos = iterator.next();
//                    int distance = Math.abs(rowPos - pos / 8);
//                    if (distance <=1) {
//                        iterator.remove();
//                    }
//                }
//            }
//        }

    }
    private void removeCapturedPawns(PiecePosition firstPos, PiecePosition secondPos){
        int row = Math.abs((firstPos.getRow()+secondPos.getRow())/2);
        int column = Math.abs((firstPos.getColumn()+secondPos.getColumn())/2);
        pieces[row][column]=new Piece(Piece.PieceColor.EMPTY);

    }



    private int findMaxInList(int row){
        int max =0;
        for (int pos:possibleMoves) {
            int distance=Math.abs(row-pos/8);
            if (distance>max){
                max=distance;
            }

        }
        return max;
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
