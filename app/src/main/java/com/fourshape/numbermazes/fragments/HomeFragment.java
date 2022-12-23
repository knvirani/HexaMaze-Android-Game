package com.fourshape.numbermazes.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.media.Image;
import android.os.Bundle;

import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fourshape.numbermazes.GameSessionActivity;
import com.fourshape.numbermazes.HowToPlayActivity;
import com.fourshape.numbermazes.R;
import com.fourshape.numbermazes.StatisticsActivity;
import com.fourshape.numbermazes.custom_calender.data_formats.DateData;
import com.fourshape.numbermazes.custom_calender.utils.CurrentCalendar;
import com.fourshape.numbermazes.listeners.OnPolicyRequiredMessageListener;
import com.fourshape.numbermazes.listeners.OnPolicyStatusListener;
import com.fourshape.numbermazes.maze_core.structure.GameConfig;
import com.fourshape.numbermazes.maze_core.structure.GameLevel;
import com.fourshape.numbermazes.ui_popups.PopupGameLevel;
import com.fourshape.numbermazes.ui_popups.PopupPolicyRequired;
import com.fourshape.numbermazes.ui_popups.PopupPrivacyPolicy;
import com.fourshape.numbermazes.utils.AppColor;
import com.fourshape.numbermazes.utils.BundleParams;
import com.fourshape.numbermazes.utils.FormattedData;
import com.fourshape.numbermazes.utils.GameType;
import com.fourshape.numbermazes.utils.MakeLog;
import com.fourshape.numbermazes.utils.SharedData;
import com.google.android.material.card.MaterialCardView;
import com.google.gson.Gson;

public class HomeFragment extends Fragment {

    private static final String TAG = "HomeFragment";

    private View mainView;

    private MaterialCardView dailyPlayCV, statsCV, newGameCV, savedGameCV, howtoplayCV;
    private ImageView dailyRightArrowIV, statsIV, timelapseIV, howtoplayIV;
    private TextView appTitleTV, dayTV, monthTV, statsTV, continueTV, savedGameLevelTimeTV, newGameTV, howtoplayTV;
    private LinearLayoutCompat dayTVLL;

    private int dailyDay = -1, dailyMonth = -1, dailyYear = -1;

    private AppColor appColor;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mainView = inflater.inflate(R.layout.fragment_home, container, false);

        appColor = new AppColor();

        dailyPlayCV = mainView.findViewById(R.id.daily_cv);
        statsCV = mainView.findViewById(R.id.stats_cv);
        newGameCV = mainView.findViewById(R.id.new_game_cv);
        savedGameCV = mainView.findViewById(R.id.saved_game_cv);
        howtoplayCV = mainView.findViewById(R.id.howtoplay_cv);

        dailyRightArrowIV = mainView.findViewById(R.id.right_arrow_iv);
        statsIV = mainView.findViewById(R.id.stats_iv);
        timelapseIV = mainView.findViewById(R.id.timelapse_iv);
        howtoplayIV = mainView.findViewById(R.id.howtoplay_iv);

        howtoplayTV = mainView.findViewById(R.id.howtoplay_tv);
        appTitleTV = mainView.findViewById(R.id.app_title_home);
        dayTV = mainView.findViewById(R.id.daily_day_tv);
        monthTV = mainView.findViewById(R.id.daily_month_tv);
        statsTV = mainView.findViewById(R.id.stats_tv);
        continueTV = mainView.findViewById(R.id.saved_game_tv);
        savedGameLevelTimeTV = mainView.findViewById(R.id.saved_game_level_time_tv);
        newGameTV = mainView.findViewById(R.id.new_game_tv);

        dayTVLL = mainView.findViewById(R.id.day_ll);

        setViewsListener();

        return mainView;
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshViewsColor();
        setDataInViews();
    }

    @SuppressLint("SetTextI18n")
    private void setDataInViews () {

        if (getContext() == null)
            return;

        if (new SharedData(getContext()).getSavedGeneralGame() != null) {

            GameConfig gameConfig = new Gson().fromJson(new SharedData(getContext()).getSavedGeneralGame(), GameConfig.class);

            if (gameConfig != null) {

                savedGameLevelTimeTV.setText(getGameLevel(gameConfig.getGameLevel()) + " \u2022 " + FormattedData.getFormattedTime(gameConfig.getGameSeconds()));

                savedGameLevelTimeTV.setTextColor(this.getContext().getColor(this.appColor.getHomePrimaryBtnTextColor()));
                continueTV.setTextColor(this.getContext().getColor(this.appColor.getHomePrimaryBtnTextColor()));
                savedGameCV.setCardBackgroundColor(this.getContext().getColor(this.appColor.getHomePrimaryBtnBackgroundColor()));

                newGameCV.setCardBackgroundColor(this.getContext().getColor(this.appColor.getHomeSecondaryBtnBackgroundColor()));
                newGameTV.setTextColor(this.getContext().getColor(this.appColor.getHomeSecondaryBtnTextColor()));

            } else {

                newGameTV.setTextColor(this.getContext().getColor(this.appColor.getHomePrimaryBtnTextColor()));
                newGameCV.setCardBackgroundColor(this.getContext().getColor(this.appColor.getHomePrimaryBtnBackgroundColor()));

                savedGameCV.setCardBackgroundColor(this.getContext().getColor(this.appColor.getHomeSecondaryBtnBackgroundColor()));
                continueTV.setTextColor(this.getContext().getColor(this.appColor.getHomeSecondaryBtnTextColor()));
                savedGameLevelTimeTV.setTextColor(this.getContext().getColor(this.appColor.getHomeSecondaryBtnTextColor()));

                savedGameCV.setVisibility(View.INVISIBLE);

            }

        } else {

            newGameTV.setTextColor(this.getContext().getColor(this.appColor.getHomePrimaryBtnTextColor()));
            newGameCV.setCardBackgroundColor(this.getContext().getColor(this.appColor.getHomePrimaryBtnBackgroundColor()));

            savedGameCV.setCardBackgroundColor(this.getContext().getColor(this.appColor.getHomeSecondaryBtnBackgroundColor()));
            continueTV.setTextColor(this.getContext().getColor(this.appColor.getHomeSecondaryBtnTextColor()));
            savedGameLevelTimeTV.setTextColor(this.getContext().getColor(this.appColor.getHomeSecondaryBtnTextColor()));

            savedGameCV.setVisibility(View.INVISIBLE);

        }

        DateData cDate = CurrentCalendar.getCurrentDateData();

        if (new SharedData(getContext()).getSavedDailyGame() != null) {

            GameConfig gameConfig = new Gson().fromJson(new SharedData(getContext()).getSavedDailyGame(), GameConfig.class);

            if (gameConfig != null) {

                dailyDay = gameConfig.getDay();
                dailyMonth = gameConfig.getMonth();
                dailyYear = gameConfig.getYear();

                if (dailyDay != -1 && dailyMonth != -1 && dailyYear != -1) {

                    if (dailyDay == cDate.getDay() && dailyMonth == cDate.getMonth() && dailyYear == cDate.getYear()) {

                        if (timelapseIV != null) {
                            timelapseIV.setVisibility(View.VISIBLE);
                        }

                        dayTV.setText(String.valueOf(dailyDay));
                        monthTV.setText(FormattedData.getMonth(dailyMonth));

                    } else {
                        timelapseIV.setVisibility(View.GONE);
                        dayTV.setText(String.valueOf(cDate.getDay()));
                        monthTV.setText(FormattedData.getMonth(cDate.getMonth()));
                    }

                } else {
                    timelapseIV.setVisibility(View.GONE);
                    dayTV.setText(String.valueOf(cDate.getDay()));
                    monthTV.setText(FormattedData.getMonth(cDate.getMonth()));
                }

            } else {
                timelapseIV.setVisibility(View.GONE);
                dayTV.setText(String.valueOf(cDate.getDay()));
                monthTV.setText(FormattedData.getMonth(cDate.getMonth()));
            }

        } else {
            timelapseIV.setVisibility(View.GONE);
            dayTV.setText(String.valueOf(cDate.getDay()));
            monthTV.setText(FormattedData.getMonth(cDate.getMonth()));
        }

    }

    public String getGameLevel (int gameLevel) {
        if (gameLevel == GameLevel.EASY) {
            return "EASY";
        } else if (gameLevel == GameLevel.MEDIUM) {
            return "MEDIUM";
        } else if (gameLevel == GameLevel.HARD) {
            return "HARD";
        } else if (gameLevel == GameLevel.LEGEND) {
            return "LEGEND";
        } else {
            return "NONE";
        }
    }

    private void setViewsListener () {

        if (howtoplayCV != null) {
            howtoplayCV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(view.getContext(), HowToPlayActivity.class));
                }
            });
        }

        if (newGameCV != null) {
            newGameCV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (!new SharedData(view.getContext()).getPolicyStatus()) {
                        new PopupPrivacyPolicy(view.getContext(), getActivity()).setOnPolicyStatusListener(onPolicyStatusListener).show();
                    } else {
                        new PopupGameLevel(view.getContext(), getActivity()).show();
                    }

                }
            });
        }

        if (savedGameCV != null) {
            savedGameCV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(view.getContext(), GameSessionActivity.class);
                    intent.putExtra(BundleParams.GAME_TYPE, GameType.SAVED_GENERAL);
                    view.getContext().startActivity(intent);

                }
            });
        }

        if (statsCV != null) {
            statsCV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    view.getContext().startActivity(new Intent(view.getContext(), StatisticsActivity.class));
                }
            });
        }

        if (dailyPlayCV != null) {
            dailyPlayCV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (!new SharedData(view.getContext()).getPolicyStatus()) {
                        new PopupPrivacyPolicy(view.getContext(), getActivity()).setOnPolicyStatusListener(onPolicyStatusListener).show();
                    } else {

                        Intent intent = new Intent(view.getContext(), GameSessionActivity.class);

                        if (timelapseIV.getVisibility() == View.VISIBLE) {

                            intent.putExtra(BundleParams.GAME_TYPE, GameType.SAVED_DAILY);

                            intent.putExtra(BundleParams.DAILY_GAME_DAY, dailyDay);
                            intent.putExtra(BundleParams.DAILY_GAME_MONTH, dailyMonth);
                            intent.putExtra(BundleParams.DAILY_GAME_YEAR, dailyYear);

                        } else {

                            intent.putExtra(BundleParams.GAME_TYPE, GameType.DAILY);

                            DateData cDate = CurrentCalendar.getCurrentDateData();
                            intent.putExtra(BundleParams.DAILY_GAME_DAY, cDate.getDay());
                            intent.putExtra(BundleParams.DAILY_GAME_MONTH, cDate.getMonth());
                            intent.putExtra(BundleParams.DAILY_GAME_YEAR, cDate.getYear());

                        }

                        view.getContext().startActivity(intent);

                    }

                }
            });
        }

    }

    private void refreshViewsColor () {

        if (getContext() == null) {
            MakeLog.error(TAG, "Null Context before color refresh.");
            return;
        }

        appColor.setThemeId(new SharedData(this.getContext()).getAppCurrentTheme());

        if (mainView != null) {
            mainView.setBackgroundColor(this.getContext().getColor(appColor.getAppBackgroundColor()));
        }

        if (appTitleTV != null) {
            appTitleTV.setTextColor(this.getContext().getColor(appColor.getHomeAppTitleColor()));
        }

        if (dailyPlayCV != null) {
            dailyPlayCV.setCardBackgroundColor(this.getContext().getColor(appColor.getHomeDailyPlayCardBackgroundColor()));
        }

        if (dayTVLL != null) {
            dayTVLL.setBackgroundColor(this.getContext().getColor(appColor.getHomeDailyDayTextBackgroundColor()));
        }

        if (dayTV != null) {
            dayTV.setTextColor(this.getContext().getColor(appColor.getHomeDailyDayTextColor()));
        }

        if (monthTV != null) {
            monthTV.setTextColor(this.getContext().getColor(appColor.getHomeDailyMonthTextColor()));
        }

        if (dailyRightArrowIV != null) {
            dailyRightArrowIV.setImageTintList(ColorStateList.valueOf(this.getContext().getColor(appColor.getHomeDailyPlayRightArrowTintColor())));
        }

        if (statsCV != null) {
            statsCV.setCardBackgroundColor(this.getContext().getColor(appColor.getHomeStatsCardBackgroundColor()));
        }

        if (statsIV != null) {
            statsIV.setImageTintList(ColorStateList.valueOf(this.getContext().getColor(appColor.getHomeStatsCardIconTintColor())));
        }

        if (statsTV != null) {
            statsTV.setTextColor(this.getContext().getColor(this.appColor.getHomeStatsCardTextColor()));
        }

        if (savedGameCV != null) {
            savedGameCV.setCardBackgroundColor(this.getContext().getColor(this.appColor.getHomeSecondaryBtnBackgroundColor()));
        }

        if (continueTV != null) {
            continueTV.setTextColor(this.getContext().getColor(this.appColor.getHomeSecondaryBtnTextColor()));
        }

        if (savedGameLevelTimeTV != null) {
            savedGameLevelTimeTV.setTextColor(this.getContext().getColor(appColor.getHomeSecondaryBtnTextColor()));
        }

        if (newGameCV != null) {
            newGameCV.setCardBackgroundColor(this.getContext().getColor(this.appColor.getHomePrimaryBtnBackgroundColor()));
        }

        if (newGameTV != null) {
            newGameTV.setTextColor(this.getContext().getColor(this.appColor.getHomePrimaryBtnTextColor()));
        }

        if (timelapseIV != null) {
            timelapseIV.setImageTintList(ColorStateList.valueOf(this.getContext().getColor(this.appColor.getHomeDailyDayTextBackgroundColor())));
        }

        if (howtoplayCV != null) {
            howtoplayCV.setCardBackgroundColor(this.getContext().getColor(this.appColor.getHowtoplayCardBackgroundColor()));
        }

        if (howtoplayTV != null) {
            howtoplayTV.setTextColor(this.getContext().getColor(this.appColor.getAppBarTextColor()));
        }

        if (howtoplayIV != null) {
            howtoplayIV.setImageTintList(ColorStateList.valueOf(this.getContext().getColor(this.appColor.getAppBarTextColor())));
        }

    }

    private final OnPolicyStatusListener onPolicyStatusListener = new OnPolicyStatusListener() {
        @Override
        public void onAccepted() {
            new PopupGameLevel(mainView.getContext(), getActivity()).show();
        }

        @Override
        public void onRejected() {
            new PopupPolicyRequired(getContext()).setOnPolicyRequiredMessageListener(onPolicyRequiredMessageListener).show();
        }
    };

    private final OnPolicyRequiredMessageListener onPolicyRequiredMessageListener = new OnPolicyRequiredMessageListener() {
        @Override
        public void onOkay() {
            new PopupPrivacyPolicy(getContext(), getActivity()).setOnPolicyStatusListener(onPolicyStatusListener).show();
        }

        @Override
        public void onExitApp() {

            if (getActivity() != null)
                getActivity().finish();

        }
    };

}