package com.fourshape.numbermazes.fragments.stats;

import android.os.Bundle;

import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.fourshape.numbermazes.R;
import com.fourshape.numbermazes.game_db.stats.StatsId;
import com.fourshape.numbermazes.listeners.OnStatsDataFetchListener;
import com.fourshape.numbermazes.maze_core.FetchStats;
import com.fourshape.numbermazes.utils.AppColor;
import com.fourshape.numbermazes.utils.SharedData;
import com.google.android.material.progressindicator.CircularProgressIndicator;

public class EasyLevelStatsFragment extends Fragment {

    private FetchStats fetchStats;
    private View mainView;
    private FrameLayout parent;
    private LinearLayoutCompat viewsContainer;
    private CircularProgressIndicator progressIndicator;
    private final static int STATS_ID = StatsId.EASY;

    private AppColor appColor;

    public EasyLevelStatsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mainView = inflater.inflate(R.layout.fragment_easy_level_stats, container, false);

        parent = mainView.findViewById(R.id.parent);
        viewsContainer = mainView.findViewById(R.id.dynamic_views_container);
        progressIndicator = mainView.findViewById(R.id.progress_circular);

        appColor = new AppColor();

        if (getContext() != null) {

            appColor.setThemeId(new SharedData(getContext()).getAppCurrentTheme());
            parent.setBackgroundColor(getContext().getColor(this.appColor.getAppBackgroundColor()));
            viewsContainer.setBackgroundColor(getContext().getColor(this.appColor.getAppBackgroundColor()));
            progressIndicator.setIndicatorColor(getContext().getColor(this.appColor.getProgressCircularIndicatorColor()));

            fetchStats = new FetchStats(getContext(), viewsContainer, STATS_ID);
            fetchStats.setOnStatsDataFetchListener(onStatsDataFetchListener);

            fetchStats.fetchData();

        }

        return mainView;

    }

    private final OnStatsDataFetchListener onStatsDataFetchListener = new OnStatsDataFetchListener() {
        @Override
        public void onFetched() {

            fetchStats.fitDataIntoViews();
            progressIndicator.setVisibility(View.GONE);
            viewsContainer.setVisibility(View.VISIBLE);

        }
    };

}