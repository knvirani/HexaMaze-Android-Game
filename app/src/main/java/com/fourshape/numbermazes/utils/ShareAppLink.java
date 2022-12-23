package com.fourshape.numbermazes.utils;

import android.content.Context;
import android.content.Intent;
import android.view.View;

public class ShareAppLink {

    private static final String CHOOSER_TITLE = "Share Number Maze Game";

    public static void share (View view) {

        try {

            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            intent.putExtra(Intent.EXTRA_SUBJECT, CHOOSER_TITLE);
            intent.putExtra(Intent.EXTRA_TEXT, VariablesControl.APP_STORE_URL);

            Intent chooser = Intent.createChooser(intent, CHOOSER_TITLE);

            view.getContext().startActivity(chooser);

        } catch (Exception e) {
            MakeLog.exception(e);

            if (view != null) {
                if (view.getContext() != null) {
                    new ActionSnackbar().show(view, false, "Unable to share app link");
                }
            }

        }
    }

}
