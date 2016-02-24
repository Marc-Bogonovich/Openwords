package com.openwords.util.graphics;


import android.graphics.PorterDuff;
import android.view.View;
import android.widget.ImageView;

public class MapImageColor {

    public static void mapColor(View v, int color) {
        ImageView img = (ImageView) v;
        img.setColorFilter(color, PorterDuff.Mode.MULTIPLY);
    }

    private MapImageColor() {
    }
}
