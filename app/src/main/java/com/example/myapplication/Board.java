package com.example.myapplication;

import android.content.Context;
import android.graphics.Color;

import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

import java.io.PipedReader;
import java.util.Arrays;


public class Board  {
    private ImageView[][] tiles;

    private Piece[][] pieces;

    private PiecePosition firstPiecePosition;
    private boolean firstClick;
    private static final int BOARD_SIZE = 8;
    private static final int TILE_HEIGHT = 135;
    public Board(Context context) {
        firstClick=true;
        this.tiles = new ImageView[BOARD_SIZE][BOARD_SIZE];
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[i].length; j++) {
                tiles[i][j]=new ImageView(context);
                tiles[i][j].setOnClickListener(listener);
            }
        }
        this.pieces = new Piece[BOARD_SIZE][BOARD_SIZE];

//
    }



    public void createBoard (TableLayout mainLayout){
        for (int i = 0; i < 8; i++) {
            TableRow tableRow = new TableRow(mainLayout.getContext());
            tableRow.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            mainLayout.setStretchAllColumns(true);
            mainLayout.setShrinkAllColumns(true);
            for (int j = 0; j < 8; j++) {




                // Set alternate background colors

                ImageView image = tiles[i][j];
                image.setTag(i*8+j);
                image.setBackgroundColor((i + j) % 2 == 0 ? Color.WHITE : Color.BLACK);
                setPiece(i,j,image);

                // Set equal width and height for square buttons
                TableRow.LayoutParams imageParams = new TableRow.LayoutParams(0, TILE_HEIGHT, 1f);
                image.setLayoutParams(imageParams);

                tableRow.addView(image);
            }


            mainLayout.addView(tableRow);
        }
    }

    View.OnClickListener listener= new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            PiecePosition secondPiecePosition;
            if(v instanceof ImageView){

               if (firstClick) {
                   firstPiecePosition = new PiecePosition(Integer.parseInt(v.getTag().toString()));
                   int i = firstPiecePosition.getRow();
                   int j = firstPiecePosition.getColumn();
                   if (pieces[i][j].isEmpty()) {
                       ImageView image = tiles[i][j];
                       image.setBackgroundColor(Color.RED);

                       new Handler().postDelayed(new Runnable() {
                           @Override
                           public void run() {
                               image.setBackgroundColor(Color.WHITE);
                           }
                       }, 200);

                       return;
                   }

                   firstClick=false;

               }
               else  {

                   secondPiecePosition= new PiecePosition(Integer.parseInt(v.getTag().toString()));

                   movePiece(firstPiecePosition,secondPiecePosition);
                   firstClick=true;

               }
            }
        }
    };

    private void movePiece(PiecePosition first,PiecePosition second){
        Piece piece=pieces[first.getRow()][first.getColumn()];
        pieces[first.getRow()][first.getColumn()]=new Piece(Piece.PieceColor.Empty);
        pieces[second.getRow()][second.getColumn()]=piece;
        loadBoard();

    }
    private void setPiece(int i, int j, ImageView image) {
        Piece.PieceColor color = Piece.PieceColor.Empty;

        if (i > 2 && i < 5) {
            pieces[i][j] = new Piece(color);
            return;
        }

        if ((i + j) % 2 == 1) {
            if (i < 3)
                color = Piece.PieceColor.Black;
            else
                color = Piece.PieceColor.Red;
            Piece piece = new Piece(color);
            image.setImageResource(piece.getPicId());
            pieces[i][j] = new Piece(color);
        } else {
            Piece piece = new Piece(Piece.PieceColor.Empty);
            pieces[i][j] = piece;
        }

        // Log statements to help identify issues

    }

    public void loadBoard(){
        for (int i = 0; i < 8; i++) {

            for (int j = 0; j < 8; j++) {
                ImageView image = tiles[i][j];
                    image.setImageResource(pieces[i][j].getPicId());


            }
        }
    }




}
