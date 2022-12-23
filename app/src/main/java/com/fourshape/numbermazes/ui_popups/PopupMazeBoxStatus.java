package com.fourshape.numbermazes.ui_popups;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.fourshape.numbermazes.R;
import com.fourshape.numbermazes.utils.AppColor;
import com.fourshape.numbermazes.utils.DimPopupWindow;
import com.fourshape.numbermazes.utils.MakeLog;
import com.fourshape.numbermazes.utils.SharedData;
import com.google.android.material.card.MaterialCardView;

public class PopupMazeBoxStatus {

    private final static String TAG = "PopupMazeBoxStatus";

    private View view;
    private PopupWindow popupWindow;
    private boolean status;

    private MaterialCardView parentCV, closeCV;
    private ImageView statusIV;
    private TextView statusTV, closeTV;

    private AppColor appColor;
    private String reasonText;

    public PopupMazeBoxStatus (Context context, boolean status, String reasonText) {
        this.view = LayoutInflater.from(context).inflate(R.layout.popup_maz_box_status, null);
        this.status = status;
        this.reasonText = reasonText;
        appColor = new AppColor();
        preparePopup();
    }

    private void preparePopup () {

        parentCV = this.view.findViewById(R.id.parent_cv);
        this.closeCV = this.view.findViewById(R.id.close_cv);
        statusIV = this.view.findViewById(R.id.status_iv);
        statusTV = this.view.findViewById(R.id.status_tv);
        closeTV = this.view.findViewById(R.id.close_tv);

        appColor.setThemeId(new SharedData(this.view.getContext()).getAppCurrentTheme());

        parentCV.setCardBackgroundColor(this.view.getContext().getColor(this.appColor.getPopupCardBackgroundColor()));
        closeCV.setCardBackgroundColor(this.view.getContext().getColor(this.appColor.getPopupPrimaryButtonBackgroundColor()));
        closeTV.setTextColor(this.view.getContext().getColor(this.appColor.getPopupPrimaryButtonTextColor()));
        statusTV.setTextColor(this.view.getContext().getColor(this.appColor.getAppBarTextColor()));

        if (this.status) {
            statusIV.setImageDrawable(this.view.getContext().getDrawable(R.drawable.ic_check_outline));
            statusIV.setImageTintList(ColorStateList.valueOf(this.view.getContext().getColor(this.appColor.getSuccessTintColor())));
            statusTV.setText("Maze Box has been rewarded successfully.");
        } else {
            statusIV.setImageDrawable(this.view.getContext().getDrawable(R.drawable.ic_error_outline));
            statusIV.setImageTintList(ColorStateList.valueOf(this.view.getContext().getColor(this.appColor.getErrorTintColor())));

            if (reasonText == null) {
                reasonText = "Unable to reward you the maze box. Try again after few minutes.";
            }

            statusTV.setText(reasonText);

        }

        int width = FrameLayout.LayoutParams.WRAP_CONTENT;
        int height = FrameLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = false;

        this.popupWindow = new PopupWindow(this.view, width, height, focusable);

        closeCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });

    }

    public PopupMazeBoxStatus show () {

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

}
