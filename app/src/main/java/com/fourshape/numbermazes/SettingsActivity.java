package com.fourshape.numbermazes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.fourshape.numbermazes.analytics.TrackScreen;
import com.fourshape.numbermazes.maze_core.LiveGameSettings;
import com.fourshape.numbermazes.utils.AppColor;
import com.fourshape.numbermazes.utils.ScreenConfiguration;
import com.fourshape.numbermazes.utils.SharedData;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.switchmaterial.SwitchMaterial;

public class SettingsActivity extends AppCompatActivity {

    private ConstraintLayout parent;
    private AppBarLayout appBarLayout;
    private MaterialToolbar materialToolbar;
    private SwitchMaterial scoreSM, timeSM, audioSM, vibrationSM;
    private TextView scoreHelpTV, timeHelpTV;

    private AppColor appColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);

        ScreenConfiguration.set(this, this);

        setContentView(R.layout.activity_settings);

        TrackScreen.now(this);

        parent = findViewById(R.id.parent);
        appBarLayout = findViewById(R.id.app_bar_layout);
        materialToolbar = findViewById(R.id.material_toolbar);
        scoreSM = findViewById(R.id.show_score);
        timeSM = findViewById(R.id.show_time);
        audioSM = findViewById(R.id.game_sound);
        vibrationSM = findViewById(R.id.game_vibration);

        scoreHelpTV = findViewById(R.id.score_switch_help_tv);
        timeHelpTV = findViewById(R.id.time_switch_help_tv);

        appColor = new AppColor();
        appColor.setThemeId(new SharedData(this).getAppCurrentTheme());

        scoreHelpTV.setTextColor(getColor(appColor.getSwitchSuggestionTextColor()));
        timeHelpTV.setTextColor(getColor(appColor.getSwitchSuggestionTextColor()));

        parent.setBackgroundColor(getColor(this.appColor.getAppBackgroundColor()));
        appBarLayout.setBackgroundColor(getColor(this.appColor.getAppBarBackgroundColor()));
        materialToolbar.setTitleTextColor(getColor(this.appColor.getAppBarTextColor()));
        materialToolbar.setNavigationIconTint(getColor(this.appColor.getAppBarIconTintColor()));
        materialToolbar.setBackgroundColor(getColor(this.appColor.getAppBarBackgroundColor()));

        int[][] states = new int[][] {
                new int[] {android.R.attr.state_checked},
                new int[] {}
        };

        int[] colors = new int[] {
                this.getColor(appColor.getSwitchOnTintColor()),
                this.getColor(appColor.getAppBarIconTintColor())
        };

        int[][] statesForTrack = new int[][] {
                new int[] {android.R.attr.state_checked},
                new int[] {}
        };

        int[] colorsForTrack = new int[] {
                this.getColor(appColor.getSwitchTrackCheckedColor()),
                this.getColor(appColor.getSwitchTrackUncheckedColor())
        };

        scoreSM.setTextColor(getColor(appColor.getSwitchTextColor()));
        timeSM.setTextColor(getColor(appColor.getSwitchTextColor()));
        audioSM.setTextColor(getColor(appColor.getSwitchTextColor()));
        vibrationSM.setTextColor(getColor(appColor.getSwitchTextColor()));

        scoreSM.setTrackTintList(new ColorStateList(statesForTrack, colorsForTrack));
        timeSM.setTrackTintList(new ColorStateList(statesForTrack, colorsForTrack));
        audioSM.setTrackTintList(new ColorStateList(statesForTrack, colorsForTrack));
        vibrationSM.setTrackTintList(new ColorStateList(statesForTrack, colorsForTrack));

        scoreSM.setThumbTintList(new ColorStateList(states, colors));
        timeSM.setThumbTintList(new ColorStateList(states, colors));
        audioSM.setThumbTintList(new ColorStateList(states, colors));
        vibrationSM.setThumbTintList(new ColorStateList(states, colors));

        SharedData sharedData = new SharedData(this);

        audioSM.setChecked(sharedData.getGameSoundStatus());
        scoreSM.setChecked(sharedData.getGameScoreStatus());
        timeSM.setChecked(sharedData.getGameTimeStatus());
        vibrationSM.setChecked(sharedData.getGameVibrationStatus());

        setViewsListener();

    }

    private void setViewsListener () {

        materialToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        scoreSM.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                LiveGameSettings.GAME_SCORE = b;
                new SharedData(compoundButton.getContext()).toggleGameScore(b);
            }
        });

        timeSM.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                LiveGameSettings.GAME_TIME = b;
                new SharedData(compoundButton.getContext()).toggleGameTime(b);
            }
        });

        audioSM.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                LiveGameSettings.GAME_SOUND = b;
                new SharedData(compoundButton.getContext()).toggleGameSound(b);
            }
        });

        vibrationSM.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                LiveGameSettings.GAME_VIBRATION = b;
                new SharedData(compoundButton.getContext()).toggleGameVibration(b);
            }
        });

    }
}