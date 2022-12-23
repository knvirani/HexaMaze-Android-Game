package com.fourshape.numbermazes.ui_popups;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.fourshape.numbermazes.R;
import com.fourshape.numbermazes.listeners.OnPolicyRequiredMessageListener;
import com.fourshape.numbermazes.listeners.OnSessionStatusListener;
import com.fourshape.numbermazes.utils.AppColor;
import com.fourshape.numbermazes.utils.DimPopupWindow;
import com.fourshape.numbermazes.utils.MakeLog;
import com.fourshape.numbermazes.utils.SharedData;
import com.google.android.material.card.MaterialCardView;

public class PopupPolicyRequired {

    private static final String TAG = "PopupExitSession";

    private View view;
    private PopupWindow popupWindow;

    private MaterialCardView parentCV, exitAppCV, okayCV;
    private TextView titleTV, bodyTV, exitAppTV, okayTV;
    private View dividerView1;

    private AppColor appColor;

    private OnPolicyRequiredMessageListener onPolicyRequiredMessageListener;

    public PopupPolicyRequired (Context context) {

        this.view = LayoutInflater.from(context).inflate(R.layout.popup_policy_required, null);

        appColor = new AppColor();

        preparePopup();
        setViewsListener();
    }

    public PopupPolicyRequired setOnPolicyRequiredMessageListener (OnPolicyRequiredMessageListener onPolicyRequiredMessageListener) {
        this.onPolicyRequiredMessageListener = onPolicyRequiredMessageListener;
        return this;
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

        if (exitAppCV != null) {
            exitAppCV.setCardBackgroundColor(this.view.getContext().getColor(this.appColor.getPopupSecondaryButtonBackgroundColor()));
        }

        if (okayCV != null) {
            okayCV.setCardBackgroundColor(this.view.getContext().getColor(this.appColor.getPopupPrimaryButtonBackgroundColor()));
        }

        if (exitAppTV != null) {
            exitAppTV.setTextColor(this.view.getContext().getColor(this.appColor.getPopupSecondaryButtonTextColor()));
        }

        if (okayTV != null) {
            okayTV.setTextColor(this.view.getContext().getColor(this.appColor.getPopupPrimaryButtonTextColor()));
        }

        if (dividerView1 != null) {
            dividerView1.setBackgroundColor(this.view.getContext().getColor(this.appColor.getNormalDividerColor()));
        }

    }

    public PopupPolicyRequired show () {

        refreshViewsColor();

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

    private void preparePopup () {

        this.parentCV = this.view.findViewById(R.id.parent_cv);
        this.exitAppCV = this.view.findViewById(R.id.exit_cv);
        this.okayCV = this.view.findViewById(R.id.okay_cv);
        this.titleTV = this.view.findViewById(R.id.popup_header);
        this.bodyTV = this.view.findViewById(R.id.popup_body);
        this.exitAppTV = this.view.findViewById(R.id.exit_tv);
        this.okayTV = this.view.findViewById(R.id.okay_tv);
        this.dividerView1 = this.view.findViewById(R.id.divider_view_1);

        int width = ConstraintLayout.LayoutParams.MATCH_PARENT;
        int height = ConstraintLayout.LayoutParams.MATCH_PARENT;
        boolean isFocusable = false;

        this.popupWindow = new PopupWindow(this.view, width, height, isFocusable);

    }

    private void setViewsListener () {

        exitAppCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                popupWindow.dismiss();

                if (onPolicyRequiredMessageListener != null) {
                    onPolicyRequiredMessageListener.onExitApp();
                }

            }
        });

        okayCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                popupWindow.dismiss();

                if (onPolicyRequiredMessageListener != null) {
                    onPolicyRequiredMessageListener.onOkay();
                }

            }
        });

    }

}
