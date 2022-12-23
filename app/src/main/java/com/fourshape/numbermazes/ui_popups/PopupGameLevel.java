package com.fourshape.numbermazes.ui_popups;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.fourshape.numbermazes.GameSessionActivity;
import com.fourshape.numbermazes.R;
import com.fourshape.numbermazes.app_ads.PlacementIds;
import com.fourshape.numbermazes.app_ads.VideoAd;
import com.fourshape.numbermazes.listeners.OnBasicVideoAdListener;
import com.fourshape.numbermazes.maze_core.structure.GameLevel;
import com.fourshape.numbermazes.utils.AppColor;
import com.fourshape.numbermazes.utils.BundleParams;
import com.fourshape.numbermazes.utils.DimPopupWindow;
import com.fourshape.numbermazes.utils.GameType;
import com.fourshape.numbermazes.utils.MakeLog;
import com.fourshape.numbermazes.utils.SharedData;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.progressindicator.CircularProgressIndicator;

public class PopupGameLevel {

    private static final String TAG = "PopupGameLevel";

    private View view;
    private PopupWindow popupWindow;
    private MaterialCardView parentCV, easyCV, mediumCV, hardCV, legendCV, closeCV;
    private TextView easyTV, mediumTV, hardTV, legendTV;
    private AppColor appColor;
    private Activity activity;
    private LinearLayoutCompat contentLayout;
    private CircularProgressIndicator progressIndicator;
    private int delayMilliSeconds = 500;

    private int gameLevel = -1;

    public PopupGameLevel (Context context, Activity activity) {
        this.activity = activity;
        this.view = LayoutInflater.from(context).inflate(R.layout.popup_view_difficulty_level_chooser, null);
        appColor = new AppColor();
        appColor.setThemeId(new SharedData(context).getAppCurrentTheme());
        preparePopup();
    }

    private void preparePopup () {

        this.contentLayout = this.view.findViewById(R.id.content_layout);
        this.progressIndicator = this.view.findViewById(R.id.progress_circular);
        this.closeCV = this.view.findViewById(R.id.close_cv);
        this.parentCV = this.view.findViewById(R.id.parent_cv);
        this.easyCV = this.view.findViewById(R.id.easy_cv);
        this.mediumCV = this.view.findViewById(R.id.medium_cv);
        this.hardCV = this.view.findViewById(R.id.hard_cv);
        this.legendCV = this.view.findViewById(R.id.legend_cv);
        this.easyTV = this.view.findViewById(R.id.easy_tv);
        this.mediumTV = this.view.findViewById(R.id.medium_tv);
        this.hardTV = this.view.findViewById(R.id.hard_tv);
        this.legendTV = this.view.findViewById(R.id.legend_tv);

        resetViewsColor();
        setViewsListener();

        int width = ConstraintLayout.LayoutParams.MATCH_PARENT;
        int height = ConstraintLayout.LayoutParams.MATCH_PARENT;
        boolean focusable = false;

        popupWindow = new PopupWindow(this.view, width, height, focusable);

    }

    public void show () {

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
        popupWindow.showAtLocation(this.view, Gravity.NO_GRAVITY, 0, 0);
        DimPopupWindow.dimBehindWithFactor(popupWindow, 0.5f);
        MakeLog.info(TAG, "Popup is Showing.");

    }

    private void resetViewsColor () {

        this.parentCV.setCardBackgroundColor(this.view.getContext().getColor(appColor.getPopupCardBackgroundColor()));
        this.easyCV.setCardBackgroundColor(this.view.getContext().getColor(appColor.getEasyLevelCardBackgroundColor()));
        this.mediumCV.setCardBackgroundColor(this.view.getContext().getColor(appColor.getMediumLevelCardBackgroundColor()));
        this.hardCV.setCardBackgroundColor(this.view.getContext().getColor(appColor.getHardLevelCardBackgroundColor()));
        this.legendCV.setCardBackgroundColor(this.view.getContext().getColor(appColor.getLegendLevelCardBackgroundColor()));
        this.easyTV.setTextColor(this.view.getContext().getColor(appColor.getDifficultyLevelTitleColor()));
        this.mediumTV.setTextColor(this.view.getContext().getColor(appColor.getDifficultyLevelTitleColor()));
        this.hardTV.setTextColor(this.view.getContext().getColor(appColor.getDifficultyLevelTitleColor()));
        this.legendTV.setTextColor(this.view.getContext().getColor(appColor.getDifficultyLevelTitleColor()));
        this.progressIndicator.setIndicatorColor(this.view.getContext().getColor(appColor.getProgressCircularIndicatorColor()));

    }

    private void setViewsListener () {

        this.closeCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (popupWindow != null)
                    if (popupWindow.isShowing())
                        popupWindow.dismiss();
            }
        });

        this.easyCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                closeCV.setVisibility(View.INVISIBLE);

                gameLevel = GameLevel.EASY;

                if (new SharedData(view.getContext()).isFirstTime()) {

                    new SharedData(view.getContext()).setFirstTime();
                    startGame();

                } else {
                    startVideoAd();
                }

            }
        });

        this.mediumCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                closeCV.setVisibility(View.INVISIBLE);

                gameLevel = GameLevel.MEDIUM;

                if (new SharedData(view.getContext()).isFirstTime()) {
                    new SharedData(view.getContext()).setFirstTime();
                    startGame();
                } else {
                    startVideoAd();
                }

            }
        });

        this.hardCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                closeCV.setVisibility(View.INVISIBLE);

                gameLevel = GameLevel.HARD;

                if (new SharedData(view.getContext()).isFirstTime()) {
                    new SharedData(view.getContext()).setFirstTime();
                    startGame();
                } else {
                    startVideoAd();
                }

            }
        });

        this.legendCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                closeCV.setVisibility(View.INVISIBLE);

                gameLevel = GameLevel.LEGEND;

                if (new SharedData(view.getContext()).isFirstTime()) {
                    new SharedData(view.getContext()).setFirstTime();
                    startGame();
                } else {
                    startVideoAd();
                }

            }
        });

    }

    private void startVideoAd () {

        this.contentLayout.setVisibility(View.INVISIBLE);
        this.progressIndicator.setVisibility(View.VISIBLE);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                new VideoAd().withContext(view.getContext()).withActivity(activity).setPlacementId(PlacementIds.INTERSTITIAL_ON_GAME_START_AD).setOnBasicVideoAdListener(onBasicVideoAdListener).load();

            }
        }, delayMilliSeconds);


    }

    private void startGame () {

        this.contentLayout.setVisibility(View.INVISIBLE);
        this.progressIndicator.setVisibility(View.VISIBLE);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent intent = new Intent(view.getContext(), GameSessionActivity.class);
                intent.putExtra(BundleParams.GAME_LEVEL, gameLevel);
                intent.putExtra(BundleParams.GAME_TYPE, GameType.GENERAL);
                view.getContext().startActivity(intent);

            }
        }, delayMilliSeconds);

    }

    private final OnBasicVideoAdListener onBasicVideoAdListener = new OnBasicVideoAdListener() {
        @Override
        public void onShowRequest() {

        }

        @Override
        public void onProceedAfter() {

            progressIndicator.setVisibility(View.INVISIBLE);

            if (popupWindow.isShowing())
                popupWindow.dismiss();

            startGame();

        }
    };

}
