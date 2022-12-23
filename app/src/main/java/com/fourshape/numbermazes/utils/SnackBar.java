package com.fourshape.numbermazes.utils;

import android.view.View;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

public class SnackBar {

    public static void show (View view, String text) {
        Snackbar snackbar = Snackbar.make( (View) view, text, Snackbar.LENGTH_SHORT);
        snackbar.setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE);
        snackbar.show();
    }

}
