package com.fourshape.numbermazes.fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fourshape.numbermazes.GameSessionActivity;
import com.fourshape.numbermazes.R;
import com.fourshape.numbermazes.app_ads.PlacementIds;
import com.fourshape.numbermazes.app_ads.VideoAd;
import com.fourshape.numbermazes.custom_calender.CustomCalendarView;
import com.fourshape.numbermazes.custom_calender.DateValidation;
import com.fourshape.numbermazes.custom_calender.MarkStyle;
import com.fourshape.numbermazes.custom_calender.data_formats.DateData;
import com.fourshape.numbermazes.custom_calender.listeners.OnDateClickListener;
import com.fourshape.numbermazes.custom_calender.listeners.OnMonthChangeListener;
import com.fourshape.numbermazes.custom_calender.utils.CurrentCalendar;
import com.fourshape.numbermazes.game_db.DailyGameDB;
import com.fourshape.numbermazes.listeners.OnBasicVideoAdListener;
import com.fourshape.numbermazes.listeners.OnDailySessionLaunchListener;
import com.fourshape.numbermazes.listeners.OnPolicyRequiredMessageListener;
import com.fourshape.numbermazes.listeners.OnPolicyStatusListener;
import com.fourshape.numbermazes.maze_core.GameCompletion;
import com.fourshape.numbermazes.maze_core.structure.GameLevel;
import com.fourshape.numbermazes.ui_popups.PopupDailyGameSessionLaunch;
import com.fourshape.numbermazes.ui_popups.PopupGameLevel;
import com.fourshape.numbermazes.ui_popups.PopupPolicyRequired;
import com.fourshape.numbermazes.ui_popups.PopupPrivacyPolicy;
import com.fourshape.numbermazes.utils.ActionSnackbar;
import com.fourshape.numbermazes.utils.AppColor;
import com.fourshape.numbermazes.utils.BundleParams;
import com.fourshape.numbermazes.utils.FormattedData;
import com.fourshape.numbermazes.utils.GameType;
import com.fourshape.numbermazes.utils.MakeLog;
import com.fourshape.numbermazes.utils.RandNumber;
import com.fourshape.numbermazes.utils.SharedData;
import com.fourshape.numbermazes.utils.VariablesControl;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.progressindicator.CircularProgressIndicator;

import java.util.ArrayList;
import java.util.Locale;


public class DailyFragment extends Fragment {

    private static final String TAG = "DailyFragment";

    private View mainView;
    private ConstraintLayout parentLL;

    private ImageView bigCalenderIV;
    private TextView monthTitleTV, separateDotTV, yearTitleTV, playTV;

    private MaterialCardView calendarContainerCV, playCV;
    private CustomCalendarView customCalendarView;

    private AppColor appColor;
    private PopupDailyGameSessionLaunch popupDailyGameSessionLaunch;

    private DateData currentSelectedDateData = null;
    private DailyGameDB dailyGameDB;
    private ArrayList<ArrayList<Object>> data;
    private ArrayList<DateData> selectedDatesList;

    private boolean isTodayChallengeCompleted = false;
    private DateData currentDate = null;
    private VideoAd videoAd;

    private CircularProgressIndicator progressIndicator, progressIndicatorInPlayCV;

    public DailyFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mainView = inflater.inflate(R.layout.fragment_daily, container, false);

        popupDailyGameSessionLaunch = new PopupDailyGameSessionLaunch(this.getContext());
        popupDailyGameSessionLaunch.setOnDailySessionLaunchListener(onDailySessionLaunchListener);

        appColor = new AppColor();
        videoAd = new VideoAd().withContext(this.getActivity()).withContext(this.getContext()).setOnBasicVideoAdListener(onBasicVideoAdListener).setPlacementId(PlacementIds.INTERSTITIAL_ON_DAILY_GAME_START_AD);

        parentLL = mainView.findViewById(R.id.content_container);
        bigCalenderIV = mainView.findViewById(R.id.calender_iv);
        monthTitleTV = mainView.findViewById(R.id.month_title);
        separateDotTV = mainView.findViewById(R.id.separate_dot_tv);
        yearTitleTV = mainView.findViewById(R.id.year_title);
        playTV = mainView.findViewById(R.id.play_tv);

        progressIndicatorInPlayCV = mainView.findViewById(R.id.progress_circular_in_play_cv);
        progressIndicator = mainView.findViewById(R.id.progress_circular);
        calendarContainerCV = mainView.findViewById(R.id.calendar_container);
        playCV = mainView.findViewById(R.id.play_cv);
        customCalendarView = mainView.findViewById(R.id.custom_calendar);

        customCalendarView.setOnMonthChangeListener(new OnMonthChangeListener() {
            @Override
            public void onMonthChange(int year, int month) {
                monthTitleTV.setText(FormattedData.getMonth(month));
                yearTitleTV.setText(String.format(Locale.getDefault(), "%d", year));
            }
        });

        customCalendarView.setOnDateClickListener(new OnDateClickListener() {
            @Override
            public void onDateClick(View view, DateData date) {

                if (date.getYear() != 0) {
                    if (playTV != null) {
                        String newText = "Play Game\n" + date.getDay() + " " + FormattedData.getMonth(date.getMonth()) + ", " + date.getYear();
                        playTV.setText(newText);
                    }
                }

                MakeLog.info(TAG, "Date Selected");

                unMarkPreviouslySelectedDate();

                currentSelectedDateData = date;

                registerSelectedDate(date);
                unMarkAllDates();
                markPlayedDates();
                markCurrentSelectedDate();

            }
        });

        setViewsListener();

        selectedDatesList = new ArrayList<>();
        currentDate = CurrentCalendar.getCurrentDateData();
        dailyGameDB = new DailyGameDB(getContext());

        return mainView;
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshViewsColor();
        makeProgressVisible();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                currentDate = CurrentCalendar.getCurrentDateData();
                setDataInViews();
            }
        }, 1000);

    }

    private void hideContent () {
        if (parentLL != null) {
            parentLL.setVisibility(View.INVISIBLE);
        }
    }

    private void makeProgressVisible () {

        if (progressIndicator != null)
            progressIndicator.setVisibility(View.VISIBLE);

        if (progressIndicatorInPlayCV != null) {
            progressIndicatorInPlayCV.setVisibility(View.INVISIBLE);
        }

        if (playTV != null) {
            playTV.setVisibility(View.VISIBLE);
        }

        if (parentLL != null)
            parentLL.setVisibility(View.INVISIBLE);



    }

    private void makeContentVisible () {

        if (progressIndicator != null)
            progressIndicator.setVisibility(View.GONE);

        if (parentLL != null)
            parentLL.setVisibility(View.VISIBLE);

    }

    private void refreshViewsColor () {

        if (getContext() == null) {
            return;
        }

        appColor.setThemeId(new SharedData(getContext()).getAppCurrentTheme());

        if (parentLL != null) {
            parentLL.setBackgroundColor(this.getContext().getColor(this.appColor.getAppBackgroundColor()));
        }

        if (monthTitleTV != null) {
            monthTitleTV.setTextColor(this.getContext().getColor(this.appColor.getDailyPageMonthTextColor()));
        }

        if (yearTitleTV != null) {
            yearTitleTV.setTextColor(this.getContext().getColor(this.appColor.getDailyPageYearTextColor()));
        }

        if (separateDotTV != null) {
            separateDotTV.setTextColor(this.getContext().getColor(this.appColor.getDailyPageYearTextColor()));
        }

        if (playCV != null) {
            playCV.setCardBackgroundColor(this.getContext().getColor(this.appColor.getPrimaryBtnBackgroundColor()));
        }

        if (playTV != null) {
            playTV.setTextColor(this.getContext().getColor(this.appColor.getPrimaryBtnTextColor()));
        }

        if (calendarContainerCV != null) {
            calendarContainerCV.setCardBackgroundColor(this.getContext().getColor(this.appColor.getAppBackgroundColor()));
            calendarContainerCV.setStrokeColor(this.getContext().getColor(this.appColor.getCalendarWeekDayRowBackgroundColor()));
        }

        if (progressIndicator != null) {
            progressIndicator.setIndicatorColor(this.getContext().getColor(this.appColor.getProgressCircularIndicatorColor()));
        }

        if (progressIndicatorInPlayCV != null) {
            progressIndicatorInPlayCV.setIndicatorColor(this.getContext().getColor(this.appColor.getPrimaryBtnTextColor()));
        }

    }

    private void setViewsListener () {

        if (playCV != null) {
            playCV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (!new SharedData(view.getContext()).getPolicyStatus()) {
                        new PopupPrivacyPolicy(getContext(), getActivity()).setOnPolicyStatusListener(onPolicyStatusListener).show();
                        return;
                    }

                    playCV.setEnabled(false);

                    if (DateValidation.isValidDate(currentSelectedDateData) && currentSelectedDateData.getYear() == 0) {

                        new ActionSnackbar().show(mainView, false, "Select correct date.");
                        playCV.setEnabled(true);

                    } else if (!DateValidation.isValidDate(currentSelectedDateData)) {

                        new ActionSnackbar().show(mainView, false, "Select correct date.");
                        playCV.setEnabled(true);

                    } else {

                        int recordId = getMatchedRecordId(currentSelectedDateData);

                        if (recordId == -1) {

                            int gameLevel = RandNumber.get(GameLevel.EASY, GameLevel.LEGEND);

                            Intent intent = new Intent(view.getContext(), GameSessionActivity.class);
                            intent.putExtra(BundleParams.GAME_LEVEL, gameLevel);
                            intent.putExtra(BundleParams.GAME_TYPE, GameType.DAILY);

                            if (currentSelectedDateData != null) {

                                intent.putExtra(BundleParams.DAILY_GAME_DAY, currentSelectedDateData.getDay());
                                intent.putExtra(BundleParams.DAILY_GAME_MONTH, currentSelectedDateData.getMonth());
                                intent.putExtra(BundleParams.DAILY_GAME_YEAR, currentSelectedDateData.getYear());

                                if (new SharedData(view.getContext()).isFirstTime()) {
                                    startActivity(intent);
                                    new SharedData(view.getContext()).setFirstTime();
                                }
                                else
                                    startAd(intent);

                                playCV.setEnabled(true);

                            } else {

                                new ActionSnackbar().show(mainView, false, "First, Select the date from board.");
                                playCV.setEnabled(true);

                            }

                        } else {

                            playCV.setEnabled(true);

                            if (popupDailyGameSessionLaunch != null) {
                                if (!popupDailyGameSessionLaunch.isShowing()) {

                                    popupDailyGameSessionLaunch.setSearchData(recordId, currentSelectedDateData);
                                    popupDailyGameSessionLaunch.show();

                                }
                            }

                        }

                    }

                }
            });
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        unMarkPreviouslySelectedDate();
        hideContent();
    }

    private final OnDailySessionLaunchListener onDailySessionLaunchListener = new OnDailySessionLaunchListener() {
        @Override
        public void onResume(Intent intent) {

            if (!new SharedData(getContext()).isFirstTime())
                startAd(intent);
            else {
                new SharedData(getContext()).setFirstTime();
                startActivity(intent);
            }

        }

        @Override
        public void onRestart(Intent intent) {

            if (!new SharedData(getContext()).isFirstTime())
                startAd(intent);
            else {
                new SharedData(getContext()).setFirstTime();
                startActivity(intent);
            }

        }
    };

    private void startAd (Intent intent) {

        if (playCV != null) {
            playCV.setEnabled(false);
        }

        if (playTV != null) {
            playTV.setVisibility(View.INVISIBLE);
        }

        if (progressIndicatorInPlayCV != null) {
            progressIndicatorInPlayCV.setVisibility(View.VISIBLE);
        }

        videoAd.withContext(getContext()).withActivity(getActivity()).setTargetActivityIntent(intent);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                videoAd.load();
            }
        }, 1000);

    }

    private final OnPolicyStatusListener onPolicyStatusListener = new OnPolicyStatusListener() {
        @Override
        public void onAccepted() {
            // new PopupGameLevel(mainView.getContext(), getActivity()).show();
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

    private final OnBasicVideoAdListener onBasicVideoAdListener = new OnBasicVideoAdListener() {
        @Override
        public void onShowRequest() {

        }

        @Override
        public void onProceedAfter() {

            startActivity(videoAd.getTargetActivityIntent());

        }
    };

    private void unMarkAllSelectedDates () {

        if (this.selectedDatesList == null)
            return;;

        for (DateData dateData : this.selectedDatesList) {
            if (customCalendarView != null) {
                customCalendarView.unMarkDate(dateData);
            }
        }

        selectedDatesList.clear();

    }

    private void registerSelectedDate (DateData dateData) {
        if (selectedDatesList == null) {
            selectedDatesList = new ArrayList<>();
        }

        selectedDatesList.add(dateData);
    }

    private void unMarkAllDates () {

        if (this.data != null) {
            for (ArrayList<Object> data : this.data) {
                customCalendarView.unMarkDate((DateData)data.get(1));
            }
        }

    }

    private void markCurrentSelectedDate () {

        if (currentSelectedDateData == null)
            return;

        customCalendarView.unMarkDate(currentSelectedDateData);
        customCalendarView.markDate(currentSelectedDateData.setMarkStyle(new MarkStyle().setStyle(MarkStyle.SELECTED_DATE)));

    }

    private void unMarkPreviouslySelectedDate () {

        if (currentSelectedDateData == null)
            return;

        if (customCalendarView != null)
            customCalendarView.unMarkDate(currentSelectedDateData);

    }

    private void markPlayedDates () {

        if (currentSelectedDateData == null) {
            return;
        }

        if (this.data != null) {
            markAllDates(this.data);
        }

    }

    private void setDataInViews() {
        customCalendarView.travelTo(CurrentCalendar.getCurrentDateData());
        new FetchDBRecords(dailyGameDB).execute();
    }

    private void resetPreviousMarkToDefault (DateData dateData) {

        if (this.data == null)
            return;;

        for (ArrayList<Object> data : this.data) {
            if (((DateData)data.get(1)).equals(dateData)) {

                if ((int)data.get(2) == GameCompletion.COMPLETED) {
                    customCalendarView.markDate(((DateData)data.get(1)).setMarkStyle(new MarkStyle().setStyle(MarkStyle.SESSION_COMPLETE)));
                } else {
                    customCalendarView.markDate(((DateData)data.get(1)).setMarkStyle(new MarkStyle().setStyle(MarkStyle.SESSION_INCOMPLETE)));
                }

            }
        }

    }


    private void markAllDates (ArrayList<ArrayList<Object>> data) {

        if (data != null) {

            MakeLog.info(TAG, "Non-NULL Data");

            this.data = data;

            for (ArrayList<Object> arrayList : data) {
                if ((int)arrayList.get(2) == GameCompletion.COMPLETED) {

                    if (((DateData)arrayList.get(1)).equals(CurrentCalendar.getCurrentDateData())) {
                        isTodayChallengeCompleted = true;
                    } else {
                        customCalendarView.markDate(((DateData)arrayList.get(1)).setMarkStyle(new MarkStyle().setStyle(MarkStyle.SESSION_COMPLETE)));
                    }

                } else {

                    if (((DateData)arrayList.get(1)).equals(CurrentCalendar.getCurrentDateData())) {
                        isTodayChallengeCompleted = false;
                    } else {
                        customCalendarView.markDate(((DateData)arrayList.get(1)).setMarkStyle(new MarkStyle().setStyle(MarkStyle.SESSION_INCOMPLETE)));
                    }

                }
            }

            if (isTodayChallengeCompleted) {
                customCalendarView.markDate(CurrentCalendar.getCurrentDateData().setMarkStyle(new MarkStyle().setStyle(MarkStyle.TODAY_DATE_COMPLETE)));
            } else {
                customCalendarView.markDate(CurrentCalendar.getCurrentDateData().setMarkStyle(new MarkStyle().setStyle(MarkStyle.TODAY_DATE_INCOMPLETE)));
            }

        } else {
            MakeLog.info(TAG, "NULL Data");
        }

        makeViewsNormal();

    }


    private void makeViewsNormal () {

        if (calendarContainerCV != null) {
            if (calendarContainerCV.getVisibility() != View.VISIBLE)
                calendarContainerCV.setVisibility(View.VISIBLE);
        }

        if (progressIndicator != null) {
            if (progressIndicator.getVisibility() == View.VISIBLE)
                progressIndicator.setVisibility(View.GONE);
        }

    }

    private int getMatchedRecordId (DateData dateData) {

        if (this.data != null && dateData != null) {

            for (ArrayList<Object> data : this.data) {

                if (((DateData)data.get(1)).getDay() == dateData.getDay() && ((DateData)data.get(1)).getMonth() == dateData.getMonth() && ((DateData)data.get(1)).getYear() == dateData.getYear()) {

                    if ((int)data.get(2) == GameCompletion.COMPLETED) {
                        return -1;
                    } else {
                        return (int)data.get(0);
                    }

                }

            }

        }

        return -1;

    }

    private class FetchDBRecords extends AsyncTask<Void, Void, ArrayList<ArrayList<Object>>> {

        private final DailyGameDB dailyGameDB;

        public FetchDBRecords (DailyGameDB dailyGameDB) {
            this.dailyGameDB = dailyGameDB;
        }

        @Override
        protected void onPostExecute(ArrayList<ArrayList<Object>> arrayLists) {

            markAllDates(arrayLists);

            super.onPostExecute(arrayLists);

            makeContentVisible();

        }

        @Override
        protected ArrayList<ArrayList<Object>> doInBackground(Void... voids) {

            try {

                ArrayList<ArrayList<Object>> data = dailyGameDB.getAllRecords();
                dailyGameDB.closeDB();

                MakeLog.info(TAG, "I'm executed.");

                return data;

            } catch (Exception e) {
                MakeLog.exception(e);
            }

            return null;
        }
    }


}