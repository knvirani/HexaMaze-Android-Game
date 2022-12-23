package com.fourshape.numbermazes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.fourshape.easythingslib.fragments.GujMCQAppsListFragment;
import com.fourshape.numbermazes.analytics.TrackScreen;
import com.fourshape.numbermazes.fragments.DailyFragment;
import com.fourshape.numbermazes.fragments.FragmentTitle;
import com.fourshape.numbermazes.fragments.HomeFragment;
import com.fourshape.numbermazes.listeners.OnMazeBoxGetListener;
import com.fourshape.numbermazes.listeners.OnThemeChangeListener;
import com.fourshape.numbermazes.maze_core.structure.ControlLimits;
import com.fourshape.numbermazes.maze_core.structure.GameLives;
import com.fourshape.numbermazes.ui_popups.PopupGetMazeBox;
import com.fourshape.numbermazes.ui_popups.PopupMazeBoxStatus;
import com.fourshape.numbermazes.ui_popups.PopupSoundControl;
import com.fourshape.numbermazes.ui_popups.PopupThemeControl;
import com.fourshape.numbermazes.utils.AppColor;
import com.fourshape.numbermazes.utils.CurrentFragment;
import com.fourshape.numbermazes.utils.ScreenConfiguration;
import com.fourshape.numbermazes.utils.ShareAppLink;
import com.fourshape.numbermazes.utils.SharedData;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.navigation.NavigationBarView;

import org.jetbrains.annotations.NotNull;

public class MainActivity extends AppCompatActivity {

    private ConstraintLayout parent;
    private LinearLayoutCompat appBarView;
    private MaterialCardView shareCV, cartCV, soundCV, themeCV, optionsCV;
    private ImageView shareIV, cartIV, soundIV, themeIV, optionsIV;
    private AppColor appColor;
    private BottomNavigationView bottomNavigationView;
    private int fragmentContainerId;
    private View bottomNavigationDividerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);

        ScreenConfiguration.set(this, this);

        setContentView(R.layout.activity_main);

        parent = findViewById(R.id.parent);
        appBarView = findViewById(R.id.app_bar_layout);

        shareCV = findViewById(R.id.share_app_link_cv);
        cartCV = findViewById(R.id.get_maze_box_cv);
        soundCV = findViewById(R.id.sound_control_cv);
        themeCV = findViewById(R.id.palette_cv);
        optionsCV = findViewById(R.id.options_cv);

        shareIV = findViewById(R.id.share_app_link_iv);
        cartIV = findViewById(R.id.get_maze_box_iv);
        soundIV = findViewById(R.id.sound_control_iv);
        themeIV = findViewById(R.id.palette_iv);
        optionsIV = findViewById(R.id.options_iv);

        bottomNavigationDividerView = findViewById(R.id.bottom_nav_divider_view);
        bottomNavigationView = findViewById(R.id.bottom_nav_view);

        fragmentContainerId = findViewById(R.id.fragment_container).getId();

        appColor = new AppColor();

        setViewsListener();

        bottomNavigationView.setSelectedItemId(R.id.nav_home);

    }

    @Override
    protected void onResume() {
        super.onResume();
        TrackScreen.now(this);
        resetViewColors();
    }

    private void setViewsListener () {

        shareCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShareAppLink.share(view);
            }
        });

        cartCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new PopupGetMazeBox(view.getContext(), MainActivity.this).setOnMazeBoxGetListener(onMazeBoxGetListener).show();
            }
        });

        soundCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new PopupSoundControl(view.getContext()).showAtTop(view);
            }
        });

        themeCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new PopupThemeControl(view.getContext()).setOnThemeChangeListener(onThemeChangeListener).showAtTop(view);
            }
        });

        optionsCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.getContext().startActivity(new Intent(view.getContext(), OptionsActivity.class));
            }
        });

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {

                int itemId = item.getItemId();

                if (itemId == R.id.nav_home) {

                    CurrentFragment.FRAGMENT = FragmentTitle.HOME_FRAGMENT;
                    getSupportFragmentManager().beginTransaction().replace(fragmentContainerId, new HomeFragment()).commit();

                    return true;

                } else if (itemId == R.id.nav_daily) {

                    CurrentFragment.FRAGMENT = FragmentTitle.DAILY_FRAGMENT;
                    getSupportFragmentManager().beginTransaction().addToBackStack("daily_fragment").replace(fragmentContainerId, new DailyFragment()).commit();

                    return true;

                } else if (itemId == R.id.nav_our_apps) {

                    CurrentFragment.FRAGMENT = FragmentTitle.OUR_APPS_FRAGMENT;
                    getSupportFragmentManager().beginTransaction().addToBackStack("our_apps_fragment").replace(fragmentContainerId, new GujMCQAppsListFragment()).commit();

                    return true;

                } else {
                    return false;
                }

            }
        });

    }

    private void resetViewColors () {

        appColor.setThemeId(new SharedData(this).getAppCurrentTheme());

        if (parent != null) {
            parent.setBackgroundColor(this.getColor(this.appColor.getAppBackgroundColor()));
        }

        if (appBarView != null) {
            appBarView.setBackgroundColor(getColor(appColor.getAppBarBackgroundColor()));
        }

        if (shareCV != null) {
            shareCV.setCardBackgroundColor(this.getColor(appColor.getAppBarBackgroundColor()));
        }
        if (cartCV != null) {
            cartCV.setCardBackgroundColor(this.getColor(appColor.getAppBarBackgroundColor()));
        }
        if (soundCV != null) {
            soundCV.setCardBackgroundColor(this.getColor(appColor.getAppBarBackgroundColor()));
        }
        if (themeCV != null) {
            themeCV.setCardBackgroundColor(this.getColor(appColor.getAppBarBackgroundColor()));
        }
        if (optionsCV != null) {
            optionsCV.setCardBackgroundColor(this.getColor(appColor.getAppBarBackgroundColor()));
        }

        if (shareIV != null) {
            shareIV.setImageTintList(ColorStateList.valueOf(this.getColor(appColor.getAppBarIconTintColor())));
        }
        if (cartIV != null) {
            cartIV.setImageTintList(ColorStateList.valueOf(this.getColor(appColor.getAppBarIconTintColor())));
        }
        if (soundIV != null) {
            soundIV.setImageTintList(ColorStateList.valueOf(this.getColor(appColor.getAppBarIconTintColor())));
        }
        if (themeIV != null) {
            themeIV.setImageTintList(ColorStateList.valueOf(this.getColor(appColor.getAppBarIconTintColor())));
        }
        if (optionsIV != null) {
            optionsIV.setImageTintList(ColorStateList.valueOf(this.getColor(appColor.getAppBarIconTintColor())));
        }

        if (bottomNavigationDividerView != null) {
            bottomNavigationDividerView.setBackgroundColor(this.getColor(appColor.getBottomNavigationBarDividerBackgroundColor()));
        }

        if (bottomNavigationView != null) {

            int[][] states = new int[][] {
                    new int[] {android.R.attr.state_checked},
                    new int[] {}
            };

            int[] colors = new int[] {
                    this.getColor(appColor.getBottomNavigationItemActiveTintColor()),
                    this.getColor(appColor.getAppBarIconTintColor())
            };

            bottomNavigationView.setBackgroundTintList(ColorStateList.valueOf(this.getColor(appColor.getAppBackgroundColor())));
            bottomNavigationView.setItemTextColor(new ColorStateList(states, colors));
            bottomNavigationView.setItemIconTintList(new ColorStateList(states, colors));

        }

        if (CurrentFragment.FRAGMENT != null) {
            if (CurrentFragment.FRAGMENT.equals(FragmentTitle.HOME_FRAGMENT)) {
                if (bottomNavigationView != null)
                    bottomNavigationView.setSelectedItemId(R.id.nav_home);
            } else if (CurrentFragment.FRAGMENT.equals(FragmentTitle.DAILY_FRAGMENT)) {
                if (bottomNavigationView != null)
                    bottomNavigationView.setSelectedItemId(R.id.nav_daily);
            } else if (CurrentFragment.FRAGMENT.equals(FragmentTitle.OUR_APPS_FRAGMENT)) {
                if (bottomNavigationView != null)
                    bottomNavigationView.setSelectedItemId(R.id.nav_our_apps);
            } else {
                if (bottomNavigationView != null)
                    bottomNavigationView.setSelectedItemId(R.id.nav_home);
            }
        } else {
            if (bottomNavigationView != null)
                bottomNavigationView.setSelectedItemId(R.id.nav_home);
        }

    }

    private final OnMazeBoxGetListener onMazeBoxGetListener = new OnMazeBoxGetListener() {
        @Override
        public void onReceive() {
            new SharedData(MainActivity.this).setGameLives(GameLives.MAXIMUM);
            new SharedData(MainActivity.this).setUndoActions(ControlLimits.UNDO_ACTION);
            new PopupMazeBoxStatus(MainActivity.this, true, null).show();
        }

        @Override
        public void onCancel(String reasonText) {
            new PopupMazeBoxStatus(MainActivity.this, false, reasonText).show();
        }
    };

    private final OnThemeChangeListener onThemeChangeListener = new OnThemeChangeListener() {
        @Override
        public void onChange() {
            resetViewColors();
        }
    };

}