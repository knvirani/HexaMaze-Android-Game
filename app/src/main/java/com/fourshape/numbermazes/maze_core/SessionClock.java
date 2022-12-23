package com.fourshape.numbermazes.maze_core;

import android.os.Handler;

import com.fourshape.numbermazes.listeners.OnSessionClockTickListener;
import com.fourshape.numbermazes.utils.MakeLog;

public class SessionClock {

    private static final String TAG = "SessionClock";

    private static int seconds;

    private static boolean shouldLogTick = true;

    private static Handler handler;
    private static Runnable runnable;

    private static boolean shouldLog = true;

    private static OnSessionClockTickListener onSessionClockTickListener;

    public static void setOnSessionClockTickListener (OnSessionClockTickListener onSessionClockTickListener) {
        SessionClock.onSessionClockTickListener = onSessionClockTickListener;
    }

    public static void init () {
        seconds = 0;
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {

                increaseSeconds();

                if (shouldLogTick) {
                    if (SessionClock.onSessionClockTickListener != null) {
                        SessionClock.onSessionClockTickListener.onTick(seconds);
                    }
                }

            }
        };
    }

    public static void start () {
        shouldLogTick = true;
        runnable.run();
        if (shouldLog) {
            MakeLog.info(TAG, "onStart");
        }
    }

    public static void resume () {
        shouldLogTick = true;
        runnable.run();
        if (shouldLog) {
            MakeLog.info(TAG, "onResume");
        }
    }

    public static void stop () {

        handler.removeCallbacks(runnable);
        shouldLogTick = false;
        if (shouldLog) {
            MakeLog.info(TAG, "onStop");
        }

    }

    public static int getSeconds () {
        return seconds;
    }

    public static void resetClock () {
        shouldLogTick = true;
        handler.removeCallbacks(runnable);
        seconds = 0;
        if (shouldLog) {
            MakeLog.info(TAG, "onReset");
        }
    }

    public static void setSeconds (int seconds) {

        SessionClock.seconds = seconds;
        if (shouldLog) {
            MakeLog.info(TAG, "onSetSeconds: " + seconds + " secs");
        }

    }

    private static void increaseSeconds () {
        seconds++;
        handler.postDelayed(runnable, 1000);
        if (shouldLog) {
            MakeLog.info(TAG, "onRun");
        }
    }

    public static void terminate () {
        handler.removeCallbacks(runnable);
        shouldLogTick = false;
        seconds = 0;
        runnable = null;
        handler = null;
        if (shouldLog) {
            MakeLog.info(TAG, "onTerminate");
        }
    }


}
