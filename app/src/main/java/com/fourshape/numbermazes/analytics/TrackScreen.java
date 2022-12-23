package com.fourshape.numbermazes.analytics;

import android.content.Context;

import com.fourshape.numbermazes.utils.MakeLog;
import com.google.firebase.analytics.FirebaseAnalytics;

public class TrackScreen {

    private static final String TAG = "TrackScreen";

    public static void now (Context context) {

        try {
            if (context != null)
                FirebaseAnalytics.getInstance(context);
        } catch (Exception e) {
            MakeLog.error(TAG, "Failed to Track Screen");
            MakeLog.exception(e);
        }

    }

}
