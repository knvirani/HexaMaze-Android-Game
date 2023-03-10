package com.fourshape.numbermazes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.fourshape.easythingslib.GujMCQAppsListActivity;
import com.fourshape.easythingslib.ui_popups.PopupAbout;
import com.fourshape.numbermazes.analytics.TrackScreen;
import com.fourshape.numbermazes.listeners.OnMazeBoxGetListener;
import com.fourshape.numbermazes.maze_core.structure.ControlLimits;
import com.fourshape.numbermazes.maze_core.structure.GameLives;
import com.fourshape.numbermazes.ui_popups.PopupGetMazeBox;
import com.fourshape.numbermazes.ui_popups.PopupMazeBoxStatus;
import com.fourshape.numbermazes.utils.AppColor;
import com.fourshape.numbermazes.utils.MakeLog;
import com.fourshape.numbermazes.utils.OpenExternalUrl;
import com.fourshape.numbermazes.utils.ScreenConfiguration;
import com.fourshape.numbermazes.utils.SharedData;
import com.fourshape.numbermazes.utils.VariablesControl;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.card.MaterialCardView;

public class OptionsActivity extends AppCompatActivity {

    private static final String TAG = "OptionsActivity";

    private MaterialCardView mazeBoxCV, statsCV, settingsCV, feedbackCV, aboutCV, privacyPolicyCV, shareGameCV, moreAppsCV, twitterContactCV, updateCV, howtoplayCV, addHexaPatternCV;
    private MaterialToolbar materialToolbar;
    private TextView mazeBoxTV, statsTV, settingsTV, feedbackTV, aboutTV, privacyPolicyTV, shareGameTV, moreAppsTV, twitterContactTV, updateTV, howtoplayTV;
    private View dividerView1, dividerView2, dividerView3, dividerView4;

    private AppColor appColor;

    private ConstraintLayout parentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        MakeLog.info(TAG, "onCreate");

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);

        appColor = new AppColor();

        ScreenConfiguration.set(this, this);

        setContentView(R.layout.activity_options);

        parentView = findViewById(R.id.parent);
        materialToolbar = findViewById(R.id.material_toolbar);

        materialToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mazeBoxCV = findViewById(R.id.get_sudo_box_cv);
        statsCV = findViewById(R.id.stats_cv);
        settingsCV = findViewById(R.id.settings_cv);
        feedbackCV = findViewById(R.id.feedback_cv);
        aboutCV = findViewById(R.id.about_cv);
        privacyPolicyCV = findViewById(R.id.privacy_policy_cv);
        shareGameCV = findViewById(R.id.share_game_cv);
        moreAppsCV = findViewById(R.id.ourapps_cv);
        twitterContactCV = findViewById(R.id.twitter_contact_cv);
        updateCV = findViewById(R.id.update_app_cv);
        howtoplayCV = findViewById(R.id.howtoplay_cv);
        addHexaPatternCV = findViewById(R.id.add_pattern_cv);

        mazeBoxTV = findViewById(R.id.maze_box_tv);
        statsTV = findViewById(R.id.stats_tv);
        settingsTV = findViewById(R.id.settings_tv);
        feedbackTV = findViewById(R.id.feedback_tv);
        aboutTV = findViewById(R.id.about_tv);
        privacyPolicyTV = findViewById(R.id.privacy_policy_tv);
        shareGameTV = findViewById(R.id.share_game_tv);
        moreAppsTV = findViewById(R.id.more_apps_tv);
        twitterContactTV = findViewById(R.id.twitter_contact_tv);
        updateTV = findViewById(R.id.update_app_tv);
        howtoplayTV = findViewById(R.id.howtoplay_tv);

        dividerView1 = findViewById(R.id.divider_view_1);
        dividerView2 = findViewById(R.id.divider_view_2);
        dividerView3 = findViewById(R.id.divider_view_3);
        dividerView4 = findViewById(R.id.divider_view_4);

        if (BuildConfig.DEBUG) {
            addHexaPatternCV.setVisibility(View.VISIBLE);
        }

        addHexaPatternCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(OptionsActivity.this, StActivity.class));
            }
        });

        twitterContactCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenExternalUrl.open(view.getContext(), view, VariablesControl.TWITTER_ACCOUNT_URL);
            }
        });

        moreAppsCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(view.getContext(), GujMCQAppsListActivity.class));

            }
        });

        shareGameCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {

                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    intent.putExtra(Intent.EXTRA_SUBJECT, "Share HexaMaze Puzzle Game");
                    intent.putExtra(Intent.EXTRA_TEXT, VariablesControl.APP_STORE_URL);

                    Intent chooser = Intent.createChooser(intent, "Share HexaMaze Puzzle Game");
                    startActivity(chooser);

                } catch (ActivityNotFoundException e) {
                    MakeLog.exception(e);
                }

            }
        });

        howtoplayCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(), HowToPlayActivity.class));
            }
        });

        mazeBoxCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new PopupGetMazeBox(view.getContext(), OptionsActivity.this).setOnMazeBoxGetListener(onMazeBoxGetListener).show();
            }
        });

        statsCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(), StatisticsActivity.class));
            }
        });

        settingsCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(), SettingsActivity.class));
            }
        });

        feedbackCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(), ContactActivity.class));
            }
        });

        aboutCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PopupAbout popupAbout = new PopupAbout(view.getContext());
                popupAbout.setAppTitle(getString(R.string.app_name));
                popupAbout.setAppLogo(getDrawable(R.drawable.app_launcher));
                popupAbout.setAppSubTitleTV("Solve the number sequence in hexagonal puzzle.");
                popupAbout.setDeveloperLine("This puzzle game, Hexa Maze, is designed and developed by GujMCQ Developers.");
                popupAbout.show();

            }
        });

        privacyPolicyCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenExternalUrl.open(view.getContext(), view, VariablesControl.PRIVACY_POLICY_URL);
            }
        });

        updateCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenExternalUrl.open(view.getContext(), view, VariablesControl.APP_STORE_URL);
            }
        });

        refreshViewsColor();

    }

    @Override
    protected void onResume() {
        super.onResume();
        TrackScreen.now(this);
    }

    private void refreshViewsColor () {

        appColor.setThemeId(new SharedData(this).getAppCurrentTheme());

        parentView.setBackgroundColor(this.getColor(appColor.getAppBackgroundColor()));

        materialToolbar.setBackgroundColor(this.getColor(appColor.getAppBarBackgroundColor()));
        materialToolbar.setTitleTextColor(this.getColor(appColor.getAppBarTextColor()));
        materialToolbar.setNavigationIconTint(this.getColor(appColor.getAppBarIconTintColor()));

        mazeBoxCV.setCardBackgroundColor(this.getColor(appColor.getAppBackgroundColor()));
        statsCV.setCardBackgroundColor(this.getColor(appColor.getAppBackgroundColor()));
        settingsCV.setCardBackgroundColor(this.getColor(appColor.getAppBackgroundColor()));
        feedbackCV.setCardBackgroundColor(this.getColor(appColor.getAppBackgroundColor()));
        aboutCV.setCardBackgroundColor(this.getColor(appColor.getAppBackgroundColor()));
        moreAppsCV.setCardBackgroundColor(this.getColor(appColor.getAppBackgroundColor()));
        twitterContactCV.setCardBackgroundColor(this.getColor(appColor.getAppBackgroundColor()));
        shareGameCV.setCardBackgroundColor(this.getColor(appColor.getAppBackgroundColor()));
        privacyPolicyCV.setCardBackgroundColor(this.getColor(appColor.getAppBackgroundColor()));
        updateCV.setCardBackgroundColor(this.getColor(appColor.getAppBackgroundColor()));
        howtoplayCV.setCardBackgroundColor(this.getColor(appColor.getAppBackgroundColor()));

        mazeBoxTV.setTextColor(this.getColor(appColor.getOptionMenuTextColor()));
        statsTV.setTextColor(this.getColor(appColor.getOptionMenuTextColor()));
        settingsTV.setTextColor(this.getColor(appColor.getOptionMenuTextColor()));
        feedbackTV.setTextColor(this.getColor(appColor.getOptionMenuTextColor()));
        aboutTV.setTextColor(this.getColor(appColor.getOptionMenuTextColor()));
        moreAppsTV.setTextColor(this.getColor(appColor.getOptionMenuTextColor()));
        twitterContactTV.setTextColor(this.getColor(appColor.getOptionMenuTextColor()));
        shareGameTV.setTextColor(this.getColor(appColor.getOptionMenuTextColor()));
        privacyPolicyTV.setTextColor(this.getColor(appColor.getOptionMenuTextColor()));
        updateTV.setTextColor(this.getColor(appColor.getOptionMenuTextColor()));
        howtoplayTV.setTextColor(this.getColor(appColor.getOptionMenuTextColor()));

        mazeBoxTV.setCompoundDrawableTintList(ColorStateList.valueOf(this.getColor(appColor.getAppBarIconTintColor())));
        statsTV.setCompoundDrawableTintList(ColorStateList.valueOf(this.getColor(appColor.getAppBarIconTintColor())));
        settingsTV.setCompoundDrawableTintList(ColorStateList.valueOf(this.getColor(appColor.getAppBarIconTintColor())));
        feedbackTV.setCompoundDrawableTintList(ColorStateList.valueOf(this.getColor(appColor.getAppBarIconTintColor())));
        aboutTV.setCompoundDrawableTintList(ColorStateList.valueOf(this.getColor(appColor.getAppBarIconTintColor())));
        moreAppsTV.setCompoundDrawableTintList(ColorStateList.valueOf(this.getColor(appColor.getAppBarIconTintColor())));
        twitterContactTV.setCompoundDrawableTintList(ColorStateList.valueOf(this.getColor(appColor.getAppBarIconTintColor())));
        shareGameTV.setCompoundDrawableTintList(ColorStateList.valueOf(this.getColor(appColor.getAppBarIconTintColor())));
        privacyPolicyTV.setCompoundDrawableTintList(ColorStateList.valueOf(this.getColor(appColor.getAppBarIconTintColor())));
        updateTV.setCompoundDrawableTintList(ColorStateList.valueOf(this.getColor(appColor.getAppBarIconTintColor())));
        howtoplayTV.setCompoundDrawableTintList(ColorStateList.valueOf(this.getColor(appColor.getAppBarIconTintColor())));

        dividerView1.setBackgroundColor(this.getColor(appColor.getNormalDividerColor()));
        dividerView2.setBackgroundColor(this.getColor(appColor.getNormalDividerColor()));
        dividerView3.setBackgroundColor(this.getColor(appColor.getNormalDividerColor()));
        dividerView4.setBackgroundColor(this.getColor(appColor.getNormalDividerColor()));

    }

    private final OnMazeBoxGetListener onMazeBoxGetListener = new OnMazeBoxGetListener() {
        @Override
        public void onReceive() {
            new SharedData(OptionsActivity.this).setGameLives(GameLives.MAXIMUM);
            new SharedData(OptionsActivity.this).setUndoActions(ControlLimits.UNDO_ACTION);
            new PopupMazeBoxStatus(OptionsActivity.this, true, null).show();
        }

        @Override
        public void onCancel(String reasonText) {
            new PopupMazeBoxStatus(OptionsActivity.this, false, reasonText).show();
        }
    };

}