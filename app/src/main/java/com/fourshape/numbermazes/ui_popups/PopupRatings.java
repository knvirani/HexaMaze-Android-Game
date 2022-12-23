package com.fourshape.numbermazes.ui_popups;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatRatingBar;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.fourshape.numbermazes.R;
import com.fourshape.numbermazes.utils.AppColor;
import com.fourshape.numbermazes.utils.DimPopupWindow;
import com.fourshape.numbermazes.utils.MakeLog;
import com.fourshape.numbermazes.utils.OpenExternalUrl;
import com.fourshape.numbermazes.utils.SharedData;
import com.fourshape.numbermazes.utils.VariablesControl;
import com.google.android.material.card.MaterialCardView;

public class PopupRatings {

    private static final String TAG = "PopupRatings";

    private View view;
    private PopupWindow popupWindow;

    private MaterialCardView parentCV, laterCV, alreadyRatedCV;
    private AppCompatRatingBar appCompatRatingBar;
    private TextView headerTV, laterTV, alreadyRatedTV;

    private AppColor appColor;

    public PopupRatings (Context context) {
        view = LayoutInflater.from(context).inflate(R.layout.popup_app_ratings, null);
        appColor = new AppColor();
        appColor.setThemeId(new SharedData(context).getAppCurrentTheme());
        preparePopup();
    }

    private void dismissPopup () {
        if (popupWindow != null) {
            if (popupWindow.isShowing()) {
                popupWindow.dismiss();
            }
        }
    }

    private void preparePopup () {

        parentCV = this.view.findViewById(R.id.parent_cv);
        laterCV = this.view.findViewById(R.id.later_cv);
        alreadyRatedCV = this.view.findViewById(R.id.already_rated_cv);
        appCompatRatingBar = this.view.findViewById(R.id.rating_bar);
        headerTV = this.view.findViewById(R.id.popup_header);
        laterTV = this.view.findViewById(R.id.later_tv);
        alreadyRatedTV = this.view.findViewById(R.id.already_rated_tv);

        parentCV.setCardBackgroundColor(this.view.getContext().getColor(appColor.getPopupCardBackgroundColor()));
        laterCV.setCardBackgroundColor(this.view.getContext().getColor(appColor.getPopupSecondaryButtonBackgroundColor()));
        alreadyRatedCV.setCardBackgroundColor(this.view.getContext().getColor(appColor.getPopupSecondaryButtonBackgroundColor()));

        headerTV.setTextColor(this.view.getContext().getColor(appColor.getPopupTitleTextColor()));
        laterTV.setTextColor(this.view.getContext().getColor(appColor.getPopupSecondaryButtonTextColor()));
        alreadyRatedTV.setTextColor(this.view.getContext().getColor(appColor.getPopupSecondaryButtonTextColor()));

        preparePopupWindow();
        prepareViewListeners();

    }
    private void preparePopupWindow () {
        int width = LinearLayoutCompat.LayoutParams.MATCH_PARENT;
        int height = LinearLayoutCompat.LayoutParams.MATCH_PARENT;
        boolean focusable = false;

        popupWindow = new PopupWindow(view, width, height, focusable);
    }
    private void prepareViewListeners () {

        laterCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dismissPopup();

            }
        });

        alreadyRatedCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dismissPopup();

            }
        });

        appCompatRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {

                new SharedData(ratingBar.getContext()).setAppRatings(v);
                OpenExternalUrl.open(ratingBar.getContext(), ratingBar, VariablesControl.APP_STORE_URL);

            }
        });

    }

    public PopupRatings show () {

        if (this.popupWindow == null) {
            MakeLog.error(TAG, "PopupWindow is NULL");
            return this;
        }

        if (this.popupWindow.isShowing())
            return this;

        popupWindow.showAtLocation(this.view, Gravity.CENTER, 0, 0);
        DimPopupWindow.dimBehind(popupWindow);
        MakeLog.info(TAG, "Popup is Showing.");

        return this;

    }

}
