package com.fourshape.numbermazes.maze_core;

import android.content.Context;

import com.fourshape.numbermazes.maze_core.structure.GameConfig;
import com.fourshape.numbermazes.utils.MakeLog;
import com.fourshape.numbermazes.utils.SharedData;
import com.google.gson.Gson;

public class SavedGame {

    public static GameConfig convertToGameConfig (String json) {
        try {
            if (json == null) {
                return null;
            } else {
                return new Gson().fromJson(json, GameConfig.class);
            }
        } catch (Exception e) {
            MakeLog.exception(e);
            return null;
        }
    }

    public static GameConfig getSavedGeneralGame (Context context) {

        try {
            if (new SharedData(context).getSavedGeneralGame() == null)
                return null;
            else
                return new Gson().fromJson(new SharedData(context).getSavedGeneralGame(), GameConfig.class);
        } catch (Exception e) {
            MakeLog.exception(e);
            return null;
        }

    }

    public static GameConfig getSavedDailyGame (Context context) {

        try {
            if (new SharedData(context).getSavedGeneralGame() == null)
                return null;
            else
                return new Gson().fromJson(new SharedData(context).getSavedDailyGame(), GameConfig.class);
        } catch (Exception e) {
            MakeLog.exception(e);
            return null;
        }

    }


}
