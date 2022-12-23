package com.fourshape.numbermazes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.fourshape.numbermazes.analytics.TrackScreen;
import com.fourshape.numbermazes.fragments.howtoplay.Step1Fragment;
import com.fourshape.numbermazes.utils.AppColor;
import com.fourshape.numbermazes.utils.ScreenConfiguration;
import com.fourshape.numbermazes.utils.SharedData;
import com.google.android.material.appbar.MaterialToolbar;

public class HowToPlayActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);

        ScreenConfiguration.set(this, this);

        setContentView(R.layout.activity_how_to_play);

        TrackScreen.now(this);

        ConstraintLayout parent = findViewById(R.id.parent_cl);
        MaterialToolbar materialToolbar = findViewById(R.id.material_toolbar);
        FrameLayout frameLayout = findViewById(R.id.fragment);

        AppColor appColor = new AppColor();
        appColor.setThemeId(new SharedData(this).getAppCurrentTheme());

        parent.setBackgroundColor(this.getColor(appColor.getAppBackgroundColor()));
        materialToolbar.setBackgroundColor(this.getColor(appColor.getAppBarBackgroundColor()));
        materialToolbar.setTitleTextColor(this.getColor(appColor.getAppBarTextColor()));
        materialToolbar.setNavigationIconTint(this.getColor(appColor.getAppBarIconTintColor()));
        frameLayout.setBackgroundColor(this.getColor(appColor.getAppBackgroundColor()));

        materialToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment, new Step1Fragment()).commit();

    }
}