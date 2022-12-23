package com.fourshape.numbermazes.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fourshape.numbermazes.R;
import com.fourshape.numbermazes.rv_adapter.ContentAdapter;
import com.fourshape.numbermazes.rv_adapter.ContentModal;
import com.fourshape.numbermazes.rv_adapter.ContentType;
import com.fourshape.numbermazes.utils.AppColor;
import com.fourshape.numbermazes.utils.SharedData;
import com.google.android.material.progressindicator.CircularProgressIndicator;

import java.util.ArrayList;

public class OurAppsFragment extends Fragment {

    private View mainView;
    private RecyclerView recyclerView;
    private CircularProgressIndicator progressIndicator;
    private ArrayList<ContentModal> contentModalArrayList;
    private ContentAdapter contentAdapter;

    private AppColor appColor;

    public OurAppsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mainView = inflater.inflate(R.layout.fragment_our_apps, container, false);

        appColor = new AppColor();
        recyclerView = mainView.findViewById(R.id.recycler_view);
        progressIndicator = mainView.findViewById(R.id.progress_circular);

        if (getContext() == null)
            return mainView;

        appColor.setThemeId(new SharedData(getContext()).getAppCurrentTheme());
        mainView.setBackgroundColor(this.getContext().getColor(this.appColor.getAppBackgroundColor()));

        contentModalArrayList = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());

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

        return mainView;
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