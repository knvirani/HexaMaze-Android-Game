package com.fourshape.numbermazes.listeners;

import android.content.Intent;

public interface OnDailySessionLaunchListener {
    void onResume (Intent intent);
    void onRestart (Intent intent);
}
