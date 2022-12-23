package com.fourshape.numbermazes.ui_popups;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.fourshape.numbermazes.R;
import com.fourshape.numbermazes.app_ads.CombinedVideoAd;
import com.fourshape.numbermazes.listeners.OnMazeBoxGetListener;
import com.fourshape.numbermazes.listeners.OnRewardedAdItemAccountListener;
import com.fourshape.numbermazes.utils.AppColor;
import com.fourshape.numbermazes.utils.DimPopupWindow;
import com.fourshape.numbermazes.utils.MakeLog;
import com.fourshape.numbermazes.utils.SharedData;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.progressindicator.CircularProgressIndicator;

public class PopupGetMazeBox {

    private static final String TAG = "PopupGetMazeBox";

    private View view;
    private PopupWindow popupWindow;
    private Activity activity;
    private Context context;

    private MaterialCardView parentCV, closeCV, watchAdCV;
    private TextView popupHeaderTV, popupBodyTV, undoHeaderTV, undoValueTV, lifeHeaderTV, closeTV, watchAdTV;
    private ImageView lifeIV1, lifeIV2, lifeIV3;
    private View dividerView1, dividerView2;
    private CircularProgressIndicator progressIndicator;

    private AppColor appColor;

    private CombinedVideoAd combinedVideoAd;

    private OnMazeBoxGetListener onMazeBoxGetListener;

    public PopupGetMazeBox (Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
        this.view = LayoutInflater.from(context).inflate(R.layout.popup_get_maze_box, null);
        appColor = new AppColor();
        combinedVideoAd = new CombinedVideoAd(context, activity).setOnRewardedAdItemAccountListener(onRewardedAdItemAccountListener);
        preparePopup();
        resetViewsColor();
        setViewsListener();
        prepareWindow();
    }

    public PopupGetMazeBox setOnMazeBoxGetListener (OnMazeBoxGetListener onMazeBoxGetListener) {
        this.onMazeBoxGetListener = onMazeBoxGetListener;
        return this;
    }

    public PopupGetMazeBox show () {

        if (this.view == null) {
            MakeLog.error(TAG, "Holder view is NULL");
            return this;
        }

        if (this.popupWindow == null) {
            MakeLog.error(TAG, "Window is NULL");
            return this;
        }

        if (this.popupWindow.isShowing()) {
            MakeLog.error(TAG, "Already visible. No need to make a duplicate.");
            return this;
        }

        popupWindow.showAtLocation(this.view, Gravity.CENTER, 0, 0);
        DimPopupWindow.dimBehindWithFactor(popupWindow, 0.5f);
        MakeLog.info(TAG, "Popup is Showing.");

        return this;

    }

    private void prepareWindow () {

        int width = ConstraintLayout.LayoutParams.MATCH_PARENT;
        int height = ConstraintLayout.LayoutParams.MATCH_PARENT;
        boolean focusable = false;

        this.popupWindow = new PopupWindow(this.view, width, height, focusable);

    }

    private final OnRewardedAdItemAccountListener onRewardedAdItemAccountListener = new OnRewardedAdItemAccountListener() {
        @Override
        public void onItemRewarded(boolean isRewarded) {

            if (isRewarded) {

                if (popupWindow != null)
                    if (popupWindow.isShowing())
                        popupWindow.dismiss();

                if (onMazeBoxGetListener != null) {
                    onMazeBoxGetListener.onReceive();
                }
            }

        }

        @Override
        public void onFailed(int errCode) {

            if (popupWindow != null)
                if (popupWindow.isShowing())
                    popupWindow.dismiss();

            String errorMessage = "Unable to reward you. Try again after few minutes.";

            if (errCode == 0) {
                errorMessage += " " + "Perhaps, your internet connection is offline.";
            } else if (errCode == 3) {
                errorMessage += " " + "Perhaps, it'll be available after a minute.";
            }

            if (onMazeBoxGetListener != null) {
                onMazeBoxGetListener.onCancel(errorMessage);
            }

        }
    };

    private void setViewsListener () {

        closeCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                popupWindow.dismiss();

                if (onMazeBoxGetListener != null) {
                    onMazeBoxGetListener.onCancel("You've not watched an ad. Hence, we couldn't reward you the maze box.");
                }

            }
        });

        watchAdCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                watchAdTV.setVisibility(View.INVISIBLE);
                progressIndicator.setVisibility(View.VISIBLE);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        combinedVideoAd.load();
                    }
                }, 1500);

            }
        });

    }

    private void resetViewsColor () {

        if (context == null)
            return;

        appColor.setThemeId(new SharedData(this.context).getAppCurrentTheme());

        parentCV.setCardBackgroundColor(this.context.getColor(this.appColor.getPopupCardBackgroundColor()));
        closeCV.setCardBackgroundColor(this.context.getColor(this.appColor.getPopupSecondaryButtonBackgroundColor()));
        watchAdCV.setCardBackgroundColor(this.context.getColor(this.appColor.getPopupPrimaryButtonBackgroundColor()));
        popupHeaderTV.setTextColor(this.context.getColor(this.appColor.getPopupTitleTextColor()));
        popupBodyTV.setTextColor(this.context.getColor(this.appColor.getPopupBodyTextColor()));
        undoHeaderTV.setTextColor(this.context.getColor(this.appColor.getPopupBodyTextColor()));
        undoValueTV.setTextColor(this.context.getColor(this.appColor.getPopupBodyTextColor()));
        lifeHeaderTV.setTextColor(this.context.getColor(this.appColor.getPopupBodyTextColor()));
        lifeIV1.setImageTintList(ColorStateList.valueOf(this.context.getColor(this.appColor.getAvailableLifeIconTintColor())));
        lifeIV2.setImageTintList(ColorStateList.valueOf(this.context.getColor(this.appColor.getAvailableLifeIconTintColor())));
        lifeIV3.setImageTintList(ColorStateList.valueOf(this.context.getColor(this.appColor.getAvailableLifeIconTintColor())));
        closeTV.setTextColor(this.context.getColor(this.appColor.getPopupSecondaryButtonTextColor()));
        watchAdTV.setTextColor(this.context.getColor(this.appColor.getPopupPrimaryButtonTextColor()));
        progressIndicator.setIndicatorColor(this.context.getColor(this.appColor.getPopupPrimaryButtonTextColor()));
        dividerView1.setBackgroundColor(this.context.getColor(this.appColor.getNormalDividerColor()));
        dividerView2.setBackgroundColor(this.context.getColor(this.appColor.getNormalDividerColor()));

    }

    private void preparePopup () {

        parentCV = this.view.findViewById(R.id.parent_cv);
        closeCV = this.view.findViewById(R.id.close_cv);
        watchAdCV = this.view.findViewById(R.id.watch_ad_cv);
        popupHeaderTV = this.view.findViewById(R.id.popup_header);
        popupBodyTV = this.view.findViewById(R.id.popup_body);
        undoHeaderTV = this.view.findViewById(R.id.undo_action_header_tv);
        undoValueTV = this.view.findViewById(R.id.undo_value_tv);
        lifeHeaderTV = this.view.findViewById(R.id.life_header_tv);
        closeTV = this.view.findViewById(R.id.close_tv);
        watchAdTV = this.view.findViewById(R.id.watch_ad_tv);
        lifeIV1 = this.view.findViewById(R.id.life_img_1);
        lifeIV2 = this.view.findViewById(R.id.life_img_2);
        lifeIV3 = this.view.findViewById(R.id.life_img_3);
        dividerView1 = this.view.findViewById(R.id.divider_view_1);
        dividerView2 = this.view.findViewById(R.id.divider_view_2);
        progressIndicator = this.view.findViewById(R.id.progress_circular);

    }

}
