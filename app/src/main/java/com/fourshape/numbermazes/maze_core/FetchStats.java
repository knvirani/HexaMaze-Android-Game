package com.fourshape.numbermazes.maze_core;

import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fourshape.numbermazes.R;
import com.fourshape.numbermazes.custom_calender.DateValidation;
import com.fourshape.numbermazes.custom_calender.data_formats.DateData;
import com.fourshape.numbermazes.game_db.stats.StatsDB;
import com.fourshape.numbermazes.listeners.OnStatsDataFetchListener;
import com.fourshape.numbermazes.utils.AppColor;
import com.fourshape.numbermazes.utils.FormattedData;
import com.fourshape.numbermazes.utils.MakeLog;
import com.fourshape.numbermazes.utils.SharedData;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;

public class FetchStats {

    private static final String TAG = "FetchStats";

    private int statsId;
    private StatsDB statsDB;

    private ArrayList<Integer> dashboardArrayList;
    private ViewGroup viewContainer;
    private AppColor appColor;
    private Context context;

    private OnStatsDataFetchListener onStatsDataFetchListener;

    public FetchStats (Context context, ViewGroup viewContainer, int statsId) {
        this.context = context;
        this.statsDB = new StatsDB(context);
        appColor = new AppColor();
        appColor.setThemeId(new SharedData(context).getAppCurrentTheme());
        this.statsId = statsId;
        this.viewContainer = viewContainer;
        dashboardArrayList = new ArrayList<>();
    }

    public void setOnStatsDataFetchListener (OnStatsDataFetchListener onStatsDataFetchListener) {
        this.onStatsDataFetchListener = onStatsDataFetchListener;
    }

    public void fetchData () {

        new FetchDataInBG().execute();

    }

    private class FetchDataInBG extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPostExecute(Void unused) {

            super.onPostExecute(unused);

            if (onStatsDataFetchListener != null) {
                onStatsDataFetchListener.onFetched();
            }

        }

        @Override
        protected Void doInBackground(Void... voids) {
            fetch();
            return null;
        }
    }

    private void fetch () {

        ArrayList<ArrayList<Object>> arrayLists = statsDB.getWinStreakRawData(statsId);
        ArrayList<Integer> dashboardArrayList = statsDB.getCalculatedData(statsId);

        int currentStreak = 0;
        int previousStreak = 0;

        for (int i=0; i<arrayLists.size(); i++) {

            try {

                if (arrayLists.get(i) != null && arrayLists.get(i+1) != null) {

                    ArrayList<Object> objectArrayList1 = arrayLists.get(i);
                    ArrayList<Object> objectArrayList2 = arrayLists.get(i+1);

                    DateData dateData1 = (DateData) objectArrayList1.get(0);
                    DateData dateData2 = (DateData) objectArrayList2.get(0);

                    if (DateValidation.getDateDifference(dateData1, dateData2) <= 1) {
                        currentStreak++;
                        MakeLog.info(TAG, "Recorded Win Streak.");
                        if (currentStreak > previousStreak)
                            previousStreak = currentStreak;
                    } else {
                        currentStreak = 0;
                        MakeLog.info(TAG, "Recorded Win Streak to Zero.");
                    }

                } else {

                    if (arrayLists.get(i) != null) {

                        ArrayList<Object> objectArrayList1 = arrayLists.get(i);
                        ArrayList<Object> objectArrayList2 = arrayLists.get(i-1);

                        if (DateValidation.getDateDifference((DateData)objectArrayList1.get(0), (DateData)objectArrayList2.get(0)) <= 1) {

                            currentStreak++;
                            MakeLog.info(TAG, "Recorded Win Streak.");
                            if (currentStreak > previousStreak)
                                previousStreak = currentStreak;

                        } else {
                            currentStreak = 0;
                            MakeLog.info(TAG, "Recorded Win Streak to Zero.");
                        }

                    }
                }

            } catch (Exception e) {
                MakeLog.exception(e);
            }

        }

        dashboardArrayList.add(Math.max(currentStreak, previousStreak));

        if (this.dashboardArrayList != null) {
            this.dashboardArrayList.addAll(dashboardArrayList);
        }

    }

    public void fitDataIntoViews () {

        int count = 0;

        for (int value : dashboardArrayList) {

            View dynamicView = LayoutInflater.from(context).inflate(R.layout.dynamic_child_for_stats, null);

            MaterialCardView parentCV = dynamicView.findViewById(R.id.parent_cv);
            TextView statsValueTV = dynamicView.findViewById(R.id.stats_value);
            TextView statsTitleTV = dynamicView.findViewById(R.id.stats_title);

            parentCV.setCardBackgroundColor(parentCV.getContext().getColor(this.appColor.getDynamicStatsCardBackgroundColor()));
            statsValueTV.setTextColor(statsValueTV.getContext().getColor(this.appColor.getDynamicStatsValueTextColor()));
            statsTitleTV.setTextColor(statsTitleTV.getContext().getColor(this.appColor.getDynamicStatsTitleTextColor()));
            statsTitleTV.setBackgroundColor(statsTitleTV.getContext().getColor(this.appColor.getDynamicStatsTitleBackgroundColor()));

            if (count == 1) {
                statsValueTV.setText(FormattedData.getFormattedTime(value));
            } else {
                if (value >= 0)
                    statsValueTV.setText(String.valueOf(value));
                else {
                    statsValueTV.setText("0");
                }
            }

            statsTitleTV.setText(getStatusTitle(count));

            viewContainer.addView(dynamicView);

            count++;

        }



    }

    private String getStatusTitle (int i) {
        if (i==0)
            return "Game Completed";
        else if (i==1)
            return "Best Time";
        else if (i==2)
            return "Perfect Wins";
        else if (i==3)
            return "Total Score";
        else if (i==4)
            return "Best Win Streak";
        else
            return "None";
    }

}
