package com.example.myapplication;

import android.graphics.Color;
import android.widget.ImageView;

public class Tile {

    private ImageView imageView;
    private Color color;
    private Piece piece;

    public Tile(ImageView imageView,Color color, Piece piece) {
        this.imageView=imageView;
        this.color = color;
        this.piece = piece;
        imageView.setBackgroundColor(color.toArgb());

    }

    public Tile(ImageView imageView,Color color) {
        this.imageView = imageView;
        this.color=color;
    }
}
