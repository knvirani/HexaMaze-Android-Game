package com.fourshape.numbermazes.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.fourshape.numbermazes.maze_core.structure.ControlLimits;
import com.fourshape.numbermazes.maze_core.structure.GameLives;

public class SharedData {

    private static final String SHARED_PREF_TITLE = "NUMBERMAZE_GUJMCQ_PREF";
    private SharedPreferences sharedPreference;
    private static final String DATE_FORMAT = "dd-MMM-yyyy";

    public SharedData(Context context) {
        if (context != null) {
            this.sharedPreference = context.getSharedPreferences(SHARED_PREF_TITLE, Context.MODE_PRIVATE);
        }
    }

    public float getAppRatings () {
        if (this.sharedPreference == null)
            return -0.1f;
        else {
            return this.sharedPreference.getFloat("app_ratings", -0.1f);
        }
    }

    public void setAppRatings (float ratings) {

        if (this.sharedPreference == null)
            return;

        this.sharedPreference.edit().putFloat("app_ratings", ratings).apply();

    }

    public void setFirstTime () {
        if (this.sharedPreference == null)
            return;
        this.sharedPreference.edit().putBoolean("first_time", false).apply();
    }

    public boolean isFirstTime () {
        if (this.sharedPreference == null)
            return false;
        else
            return this.sharedPreference.getBoolean("first_time", true);
    }

    public void saveGeneralGame (String generalGameJson) {
        if (this.sharedPreference == null)
            return;
        this.sharedPreference.edit().putString("general_game", generalGameJson).apply();
    }

    public String getSavedGeneralGame () {
        if (this.sharedPreference == null)
            return null;
        else
            return this.sharedPreference.getString("general_game", null);
    }

    public void saveDailyGame (String dailyGameJson) {
        if (this.sharedPreference == null)
            return;
        this.sharedPreference.edit().putString("daily_game", dailyGameJson).apply();
    }

    public String getSavedDailyGame () {
        if (this.sharedPreference == null)
            return null;
        else
            return this.sharedPreference.getString("daily_game", null);
    }

    public void setLastShapeId (int shapeId) {
        if (this.sharedPreference == null)
            return;
        this.sharedPreference.edit().putInt("last_shape", shapeId).apply();
    }

    public int getLastShapeId () {
        if (this.sharedPreference == null) {
            return 0;
        } else {
            return (this.sharedPreference.getInt("last_shape", 0));
        }
    }

    public void setLastHexagonId (int hexagonId) {
        if (this.sharedPreference == null)
            return;
        this.sharedPreference.edit().putInt("last_hexagon_shape", hexagonId).apply();
    }

    public int getLastHexagonId () {
        if (this.sharedPreference == null) {
            return 0;
        } else {
            return (this.sharedPreference.getInt("last_hexagon_shape", 0));
        }
    }

    public void policyStatus (boolean isAccepted) {
        if (this.sharedPreference == null)
            return;
        this.sharedPreference.edit().putBoolean("policy_status", isAccepted).apply();
    }

    public boolean getPolicyStatus () {
        if (this.sharedPreference == null)
            return true;
        else
            return this.sharedPreference.getBoolean("policy_status", false);
    }

    public void setCurrentAppTheme (int appThemeId) {
        if (this.sharedPreference == null)
            return;
        this.sharedPreference.edit().putInt("app_c_theme", appThemeId).apply();
    }

    public int getAppCurrentTheme () {
        if (this.sharedPreference == null)
            return AppThemes.DEFAULT;
        else
            return this.sharedPreference.getInt("app_c_theme", AppThemes.DEFAULT);
    }

    public void setGameLives (int latestLives) {
        if (this.sharedPreference == null) {
            return;
        }
        this.sharedPreference.edit().putInt("game_lives", latestLives).apply();
    }

    public int getGameLives () {
        if (this.sharedPreference == null) {
            return GameLives.MAXIMUM;
        } else {
            return this.sharedPreference.getInt("game_lives", GameLives.MAXIMUM);
        }
    }

    public void setUndoActions (int undoActions) {
        if (this.sharedPreference == null) {
            return;
        }
        this.sharedPreference.edit().putInt("undo_actions", undoActions).apply();
    }

    public int getUndoActions () {
        if (this.sharedPreference == null) {
            return ControlLimits.UNDO_ACTION;
        } else {
            return this.sharedPreference.getInt("undo_actions", ControlLimits.UNDO_ACTION);
        }
    }

    public void setHintActions (int hintActions) {
        if (this.sharedPreference == null) {
            return;
        }
        this.sharedPreference.edit().putInt("hint_actions", hintActions).apply();
    }

    public int getHintActions () {
        if (this.sharedPreference == null) {
            return ControlLimits.UNDO_ACTION;
        } else {
            return this.sharedPreference.getInt("hint_actions", ControlLimits.HINT_ACTION);
        }
    }

    public void toggleGameSound (boolean status) {
        if (this.sharedPreference == null)
            return;

        this.sharedPreference.edit().putBoolean("game_sound", status).apply();
    }

    public boolean getGameSoundStatus () {
        if (this.sharedPreference == null)
            return true;
        else {
            return this.sharedPreference.getBoolean("game_sound", true);
        }
    }

    public void toggleGameVibration (boolean status) {
        if (this.sharedPreference == null)
            return;

        this.sharedPreference.edit().putBoolean("game_vibration", status).apply();
    }

    public boolean getGameVibrationStatus () {
        if (this.sharedPreference == null)
            return true;
        else {
            return this.sharedPreference.getBoolean("game_vibration", true);
        }
    }

    public void toggleGameScore (boolean status) {
        if (this.sharedPreference == null)
            return;

        this.sharedPreference.edit().putBoolean("game_score", status).apply();
    }

    public boolean getGameScoreStatus () {
        if (this.sharedPreference == null)
            return true;
        else {
            return this.sharedPreference.getBoolean("game_score", true);
        }
    }

    public void toggleGameTime (boolean status) {
        if (this.sharedPreference == null)
            return;

        this.sharedPreference.edit().putBoolean("game_time", status).apply();
    }

    public boolean getGameTimeStatus () {
        if (this.sharedPreference == null)
            return true;
        else {
            return this.sharedPreference.getBoolean("game_time", true);
        }
    }


}
