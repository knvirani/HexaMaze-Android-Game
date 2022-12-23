package com.fourshape.numbermazes.ui_popups;

import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.fourshape.numbermazes.OurAppsActivity;
import com.fourshape.numbermazes.R;
import com.fourshape.numbermazes.utils.DimPopupWindow;
import com.fourshape.numbermazes.utils.MakeLog;
import com.fourshape.numbermazes.utils.OpenExternalUrl;
import com.fourshape.numbermazes.utils.VariablesControl;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

public class PopupAbout {

    private static final String TAG = "PopupAbout";

    private PopupWindow popupWindow;
    private View view;

    private MaterialCardView twitterConnectCV, policyCV;
    private TextView appTitleTV, appSubtitleTV, appVersionTV, developerLineTV;

    public PopupAbout (Context context) {

        view = LayoutInflater.from(context).inflate(R.layout.popup_about, null);
        preparePopup();

    }

    private void preparePopup () {

        MaterialButton moreAppMB;

        moreAppMB = view.findViewById(R.id.more_apps_mb);
        twitterConnectCV = view.findViewById(R.id.contact_on_twitter_cv);
        policyCV = view.findViewById(R.id.policy_cv);
        appVersionTV = view.findViewById(R.id.app_version);

        moreAppMB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.getContext().startActivity(new Intent(view.getContext(), OurAppsActivity.class));
            }
        });

        twitterConnectCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenExternalUrl.open(view.getContext(), view, VariablesControl.TWITTER_ACCOUNT_URL);
            }
        });

        policyCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenExternalUrl.open(view.getContext(), view, VariablesControl.PRIVACY_POLICY_URL);
            }
        });

        try {
            String versionName = this.view.getContext().getPackageManager().getPackageInfo(this.view.getContext().getPackageName(), 0).versionName;
            appVersionTV.setText("v" + versionName);
        } catch (Exception e){
            appVersionTV.setVisibility(View.GONE);
            MakeLog.exception(e);
        }

        int width = ConstraintLayout.LayoutParams.WRAP_CONTENT;
        int height = ConstraintLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = false;

        popupWindow = new PopupWindow(view, width, height, focusable);

    }

    public void show () {

        if (this.popupWindow == null) {
            MakeLog.error(TAG, "PopupWindow is NULL");
            return;
        }

        if (this.view == null) {
            MakeLog.error(TAG, "HolderView is NULL");
            return;
        }

        if (this.popupWindow.isShowing())
            return;

        popupWindow.showAtLocation(this.view, Gravity.CENTER, 0, 0);
        DimPopupWindow.dimBehind(popupWindow);
        MakeLog.info(TAG, "Popup is Showing.");


    }

}


