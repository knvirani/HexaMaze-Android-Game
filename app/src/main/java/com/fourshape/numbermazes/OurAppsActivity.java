package com.fourshape.numbermazes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.fourshape.numbermazes.analytics.TrackScreen;
import com.fourshape.numbermazes.rv_adapter.ContentAdapter;
import com.fourshape.numbermazes.rv_adapter.ContentModal;
import com.fourshape.numbermazes.rv_adapter.ContentType;
import com.fourshape.numbermazes.utils.AppColor;
import com.fourshape.numbermazes.utils.ScreenConfiguration;
import com.fourshape.numbermazes.utils.SharedData;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.progressindicator.CircularProgressIndicator;

import java.util.ArrayList;

public class OurAppsActivity extends AppCompatActivity {

    private AppBarLayout appBarLayout;
    private MaterialToolbar materialToolbar;
    private RecyclerView recyclerView;
    private CircularProgressIndicator progressIndicator;
    private AppColor appColor;
    private ConstraintLayout constraintLayout;

    private ArrayList<ContentModal> contentModalArrayList;
    private ContentAdapter contentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);

        ScreenConfiguration.set(this, this);

        setContentView(R.layout.activity_our_apps);

        TrackScreen.now(this);

        appBarLayout = findViewById(R.id.app_bar_layout);
        materialToolbar = findViewById(R.id.material_toolbar);
        recyclerView = findViewById(R.id.recycler_view);
        progressIndicator = findViewById(R.id.progress_circular);
        constraintLayout = findViewById(R.id.parent);

        appColor = new AppColor();
        appColor.setThemeId(new SharedData(this).getAppCurrentTheme());

        appBarLayout.setBackgroundColor(getColor(this.appColor.getAppBarBackgroundColor()));
        materialToolbar.setBackgroundColor(getColor(appColor.getAppBarBackgroundColor()));
        materialToolbar.setNavigationIconTint(getColor(this.appColor.getAppBarIconTintColor()));
        materialToolbar.setTitleTextColor(getColor(this.appColor.getAppBarTextColor()));
        recyclerView.setBackgroundColor(getColor(this.appColor.getAppBackgroundColor()));
        constraintLayout.setBackgroundColor(getColor(this.appColor.getAppBackgroundColor()));
        progressIndicator.setIndicatorColor(getColor(this.appColor.getProgressCircularIndicatorColor()));

        materialToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        contentModalArrayList = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(linearLayoutManager);
        contentAdapter = new ContentAdapter(contentModalArrayList);
        recyclerView.setAdapter(contentAdapter);

        addDeveloperInfo();

        setData("Killer Sudoku", "com.fourshape.killersudoku", R.drawable.app_killersudoku);
        setData("Classic Offline Sudoku", "com.fourshape.sudoku", R.drawable.app_classic_sudoku);
        setData("16x16 Classic Sudoku", "com.fourshape.a16x16sudoku", R.drawable.app_16x16_sudoku);
        setData("6x6 Classic Sudoku", "com.fourshape.a6x6sudoku", R.drawable.app_6x6_sudoku);
        setData("Gujarati Classic Sudoku", "com.fourshape.gujarati.sudoku", R.drawable.app_gujarati_sudoku);
        setData("PhotoByte: Unlimited Images Compressor", "com.fourshape.photobyte", R.drawable.app_photobyte);
        setData("SendsDirect for WhatsApp", "com.fourshape.sendsdirect", R.drawable.app_sendsdirect);
        setData("GujMCQ: For GSSSB and GPSSB Exams", "com.fourshape.a4mcqplus", R.drawable.app_gujmcq);
        setData("Learn Gujarati Grammar", "com.fourshape.learngujaratigrammar", R.drawable.app_learn_gujarati_grammar);

        contentAdapter.notifyDataSetChanged();

        progressIndicator.setVisibility(View.GONE);

    }

    private void setData (String appTitle, String appPackage, int appLogoDrawableId) {

        contentModalArrayList.add(new ContentModal(ContentType.APPLICATION, appLogoDrawableId, appTitle, appPackage));
        contentModalArrayList.add(new ContentModal(ContentType.DIVIDER));

    }

    private void addDeveloperInfo () {

        contentModalArrayList.add(new ContentModal(ContentType.DEVELOPER_INFO));
        contentModalArrayList.add(new ContentModal(ContentType.DIVIDER));

    }

}