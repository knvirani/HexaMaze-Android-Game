package com.fourshape.numbermazes.ui_popups;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.AsyncTask;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.appcompat.widget.LinearLayoutCompat;

import com.fourshape.numbermazes.GameSessionActivity;
import com.fourshape.numbermazes.R;
import com.fourshape.numbermazes.custom_calender.data_formats.DateData;
import com.fourshape.numbermazes.game_db.DailyGameDB;
import com.fourshape.numbermazes.listeners.OnDailySessionLaunchListener;
import com.fourshape.numbermazes.maze_core.structure.GameConfig;
import com.fourshape.numbermazes.maze_core.structure.GameLevel;
import com.fourshape.numbermazes.utils.AppColor;
import com.fourshape.numbermazes.utils.BundleParams;
import com.fourshape.numbermazes.utils.DimPopupWindow;
import com.fourshape.numbermazes.utils.GameType;
import com.fourshape.numbermazes.utils.MakeLog;
import com.fourshape.numbermazes.utils.RandNumber;
import com.fourshape.numbermazes.utils.SharedData;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.gson.Gson;

import java.util.Date;

public class PopupDailyGameSessionLaunch {

    private static final String TAG = "PopupDailyGameSessionLaunch";

    private View view;
    private PopupWindow popupWindow;

    private LinearLayoutCompat contentLayout;
    private MaterialCardView resumeCV, restartCV;
    private TextView resumeTV, restartTV;
    private CircularProgressIndicator progressIndicator;

    private AppColor appColor;
    private OnDailySessionLaunchListener onDailySessionLaunchListener;
    private int recordId;
    private DateData targetDateData;

    private int gameLevel, gameSeconds;
    private String gameLevelTitle, gameSessionData;

    public PopupDailyGameSessionLaunch (Context context) {
        this.view = LayoutInflater.from(context).inflate(R.layout.popup_daily_game_session_launch_option, null);
        appColor = new AppColor();
        preparePopup();
    }

    public void setOnDailySessionLaunchListener (OnDailySessionLaunchListener onDailySessionLaunchListener) {
        this.onDailySessionLaunchListener = onDailySessionLaunchListener;
    }

    private void makeContentLayoutVisible () {
        contentLayout.setVisibility(View.VISIBLE);
        progressIndicator.setIndicatorColor(View.INVISIBLE);
    }

    private void makeProgressVisible () {
        contentLayout.setVisibility(View.INVISIBLE);
        progressIndicator.setIndicatorColor(View.VISIBLE);
    }

    public void setSearchData (int recordId, DateData dateData) {
        this.recordId = recordId;
        this.targetDateData = dateData;
    }

    public boolean isShowing () {
        if (popupWindow == null)
            return false;
        else
            return popupWindow.isShowing();
    }

    public void show () {

        refreshViewsColor();
        makeProgressVisible();

        if (this.view == null) {
            MakeLog.error(TAG, "View is NULL.");
            return;
        }

        if (this.popupWindow == null) {
            MakeLog.error(TAG, "Window is NULL");
            return;
        }

        if (this.popupWindow.isShowing()) {
            MakeLog.error(TAG, "Window is already visible");
            return;
        }

        popupWindow.setOverlapAnchor(true);
        popupWindow.setAttachedInDecor(true);
        popupWindow.showAtLocation(this.view, Gravity.BOTTOM, 0, 0);
        DimPopupWindow.dimBehindWithFactor(popupWindow, 0.5f);
        MakeLog.info(TAG, "Popup is Showing.");

        DailyGameDB dailyGameDB = new DailyGameDB(view.getContext());
        new FetchDBRecords(dailyGameDB).execute();

    }

    private void refreshViewsColor () {

        if (this.view.getContext() == null) {
            return;
        }

        appColor.setThemeId(new SharedData(this.view.getContext()).getAppCurrentTheme());

        this.view.setBackgroundColor(this.view.getContext().getColor(this.appColor.getPopupCardBackgroundColor()));

        if (contentLayout != null) {
            contentLayout.setBackgroundColor(this.view.getContext().getColor(this.appColor.getPopupCardBackgroundColor()));
        }

        if (resumeCV != null) {
            resumeCV.setCardBackgroundColor(this.view.getContext().getColor(this.appColor.getPopupCardBackgroundColor()));
        }

        if (restartCV != null) {
            restartCV.setCardBackgroundColor(this.view.getContext().getColor(this.appColor.getPopupCardBackgroundColor()));
        }

        if (resumeTV != null) {
            resumeTV.setTextColor(this.view.getContext().getColor(this.appColor.getPopupBodyTextColor()));
            resumeTV.setCompoundDrawableTintList(ColorStateList.valueOf(this.view.getContext().getColor(this.appColor.getPopupBodyTextColor())));
        }

        if (restartTV != null) {
            restartTV.setTextColor(this.view.getContext().getColor(this.appColor.getPopupBodyTextColor()));
            restartTV.setCompoundDrawableTintList(ColorStateList.valueOf(this.view.getContext().getColor(this.appColor.getPopupBodyTextColor())));
        }

        if (progressIndicator != null) {
            progressIndicator.setIndicatorColor(this.view.getContext().getColor(this.appColor.getProgressCircularIndicatorColor()));
        }

    }

    private void preparePopup () {

        this.contentLayout = this.view.findViewById(R.id.content_layout);
        this.resumeCV = this.view.findViewById(R.id.resume_cv);
        this.restartCV = this.view.findViewById(R.id.restart_cv);
        this.progressIndicator = this.view.findViewById(R.id.progress_circular);
        this.resumeTV = this.view.findViewById(R.id.resume_tv);
        this.restartTV = this.view.findViewById(R.id.restart_tv);

        int width = FrameLayout.LayoutParams.MATCH_PARENT;
        int height = FrameLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = false;

        this.popupWindow = new PopupWindow(this.view, width, height, focusable);

        this.contentLayout.setVisibility(View.INVISIBLE);
        this.progressIndicator.setVisibility(View.VISIBLE);

        setViewsListener();

    }

    private void setViewsListener () {

        this.resumeCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                popupWindow.dismiss();

                Intent intent = new Intent(view.getContext(), GameSessionActivity.class);
                intent.putExtra(BundleParams.GAME_LEVEL, gameLevel);
                intent.putExtra(BundleParams.GAME_TYPE, GameType.SAVED_DAILY_FROM_DB);
                intent.putExtra(BundleParams.GAME_SESSION, gameSessionData);
                intent.putExtra(BundleParams.DAILY_GAME_RECORD_ID, recordId);

                if (targetDateData != null) {
                    intent.putExtra(BundleParams.DAILY_GAME_DAY, targetDateData.getDay());
                    intent.putExtra(BundleParams.DAILY_GAME_MONTH, targetDateData.getMonth());
                    intent.putExtra(BundleParams.DAILY_GAME_YEAR, targetDateData.getYear());
                }

                if (onDailySessionLaunchListener != null) {
                    onDailySessionLaunchListener.onResume(intent);
                }

            }
        });

        this.restartCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int gameLevel = RandNumber.get(GameLevel.EASY, GameLevel.LEGEND);

                Intent intent = new Intent(view.getContext(), GameSessionActivity.class);
                intent.putExtra(BundleParams.GAME_LEVEL, gameLevel);
                intent.putExtra(BundleParams.GAME_TYPE, GameType.NEW_DAILY_WITH_DB);
                intent.putExtra(BundleParams.DAILY_GAME_RECORD_ID, recordId);

                if (targetDateData != null) {
                    intent.putExtra(BundleParams.DAILY_GAME_DAY, targetDateData.getDay());
                    intent.putExtra(BundleParams.DAILY_GAME_MONTH, targetDateData.getMonth());
                    intent.putExtra(BundleParams.DAILY_GAME_YEAR, targetDateData.getYear());
                }

                popupWindow.dismiss();

                if (onDailySessionLaunchListener != null) {
                    onDailySessionLaunchListener.onRestart(intent);
                }

            }
        });

    }

    private class FetchDBRecords extends AsyncTask<Void, Void, String> {

        private final DailyGameDB dailyGameDB;

        public FetchDBRecords (DailyGameDB dailyGameDB) {
            this.dailyGameDB = dailyGameDB;
        }

        @Override
        protected void onPostExecute(String data) {
            super.onPostExecute(data);

            try {

                if (data != null) {

                    GameConfig gameConfig = new Gson().fromJson(data, GameConfig.class);

                    if (gameConfig != null) {

                        int time = gameConfig.getGameSeconds();
                        int gameLevel = gameConfig.getGameLevel();

                        if (time != -1 && gameLevel != -1) {

                            PopupDailyGameSessionLaunch.this.gameLevel = gameLevel;
                            PopupDailyGameSessionLaunch.this.gameLevelTitle = getGameLevel(gameLevel);
                            PopupDailyGameSessionLaunch.this.gameSeconds = time;

                            gameSessionData = data;

                            makeContentLayoutVisible();

                        } else {
                            gameSessionData = null;
                            makeContentLayoutVisible();
                            resumeCV.setVisibility(View.GONE);
                        }

                    } else {
                        gameSessionData = null;
                        makeContentLayoutVisible();
                        resumeCV.setVisibility(View.GONE);
                    }

                } else {
                    gameSessionData = null;
                    makeContentLayoutVisible();
                    resumeCV.setVisibility(View.GONE);
                }

            } catch (Exception e) {
                MakeLog.exception(e);
            }

        }

        @Override
        protected String doInBackground(Void... voids) {

            try {

                return dailyGameDB.getGameSessionData(recordId);

            } catch (Exception e) {
                MakeLog.exception(e);
            }

            return null;
        }
    }

    public String getGameLevel (int gameLevel) {
        if (gameLevel == GameLevel.EASY) {
            return "EASY";
        } else if (gameLevel == GameLevel.MEDIUM) {
            return "MEDIUM";
        } else if (gameLevel == GameLevel.HARD) {
            return "HARD";
        } else if (gameLevel == GameLevel.LEGEND) {
            return "LEGEND";
        } else {
            return "NONE";
        }
    }


}
