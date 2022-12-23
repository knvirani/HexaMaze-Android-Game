package com.fourshape.numbermazes.ui_popups;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.fourshape.numbermazes.R;
import com.fourshape.numbermazes.listeners.OnExitSessionPopupDismissListener;
import com.fourshape.numbermazes.listeners.OnSessionStatusListener;
import com.fourshape.numbermazes.utils.AppColor;
import com.fourshape.numbermazes.utils.DimPopupWindow;
import com.fourshape.numbermazes.utils.MakeLog;
import com.fourshape.numbermazes.utils.SharedData;
import com.google.android.material.card.MaterialCardView;

public class PopupExitSession {

    private static final String TAG = "PopupExitSession";

    private View view;
    private PopupWindow popupWindow;

    private MaterialCardView parentCV, yesCV, noCV;
    private TextView titleTV, bodyTV, yesTV, noTV;
    private View dividerView1;

    private AppColor appColor;

    private OnSessionStatusListener onSessionStatusListener;
    private OnExitSessionPopupDismissListener onExitSessionPopupDismissListener;

    public PopupExitSession (Context context) {

        this.view = LayoutInflater.from(context).inflate(R.layout.popup_exit_session, null);

        appColor = new AppColor();

        preparePopup();
        setViewsListener();
    }

    public void setOnExitSessionPopupDismissListener (OnExitSessionPopupDismissListener onExitSessionPopupDismissListener) {
        this.onExitSessionPopupDismissListener = onExitSessionPopupDismissListener;
    }

    public void setOnSessionStatusListener (OnSessionStatusListener onSessionStatusListener) {
        this.onSessionStatusListener = onSessionStatusListener;
    }

    private void refreshViewsColor () {

        appColor.setThemeId(new SharedData(this.view.getContext()).getAppCurrentTheme());

        if (parentCV != null) {
            parentCV.setCardBackgroundColor(this.view.getContext().getColor(this.appColor.getPopupCardBackgroundColor()));
        }

        if (titleTV != null) {
            titleTV.setTextColor(this.view.getContext().getColor(this.appColor.getPopupTitleTextColor()));
        }

        if (bodyTV != null) {
            bodyTV.setTextColor(this.view.getContext().getColor(this.appColor.getPopupBodyTextColor()));
        }

        if (yesCV != null) {
            yesCV.setCardBackgroundColor(this.view.getContext().getColor(this.appColor.getPopupSecondaryButtonBackgroundColor()));
        }

        if (noCV != null) {
            noCV.setCardBackgroundColor(this.view.getContext().getColor(this.appColor.getPopupPrimaryButtonBackgroundColor()));
        }

        if (yesTV != null) {
            yesTV.setTextColor(this.view.getContext().getColor(this.appColor.getPopupSecondaryButtonTextColor()));
        }

        if (noTV != null) {
            noTV.setTextColor(this.view.getContext().getColor(this.appColor.getPopupPrimaryButtonTextColor()));
        }

        if (dividerView1 != null) {
            dividerView1.setBackgroundColor(this.view.getContext().getColor(this.appColor.getNormalDividerColor()));
        }

    }

    public void show () {

        refreshViewsColor();

        if (this.view == null) {
            MakeLog.error(TAG, "Holder view is NULL");
            return;
        }

        if (this.popupWindow == null) {
            MakeLog.error(TAG, "Window is NULL");
            return;
        }

        if (this.popupWindow.isShowing()) {
            MakeLog.error(TAG, "Already visible. No need to make a duplicate.");
            return;
        }

        popupWindow.showAtLocation(this.view, Gravity.CENTER, 0, 0);
        DimPopupWindow.dimBehindWithFactor(popupWindow, 0.5f);
        MakeLog.info(TAG, "Popup is Showing.");

    }

    private void preparePopup () {

        this.parentCV = this.view.findViewById(R.id.parent_cv);
        this.yesCV = this.view.findViewById(R.id.yes_cv);
        this.noCV = this.view.findViewById(R.id.no_cv);
        this.titleTV = this.view.findViewById(R.id.popup_header);
        this.bodyTV = this.view.findViewById(R.id.popup_body);
        this.yesTV = this.view.findViewById(R.id.yes_tv);
        this.noTV = this.view.findViewById(R.id.no_tv);
        this.dividerView1 = this.view.findViewById(R.id.divider_view_1);

        int width = FrameLayout.LayoutParams.WRAP_CONTENT;
        int height = FrameLayout.LayoutParams.WRAP_CONTENT;
        boolean isFocusable = false;

        this.popupWindow = new PopupWindow(this.view, width, height, isFocusable);

        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {

                if (onExitSessionPopupDismissListener != null) {
                    onExitSessionPopupDismissListener.onPopupDismiss();
                }

            }
        });

    }

    private void setViewsListener () {

        yesCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                popupWindow.dismiss();

                if (onSessionStatusListener != null) {
                    onSessionStatusListener.onExit();
                }

            }
        });

        noCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                popupWindow.dismiss();

                if (onSessionStatusListener != null) {
                    onSessionStatusListener.onContinue();
                }

            }
        });

    }

}
