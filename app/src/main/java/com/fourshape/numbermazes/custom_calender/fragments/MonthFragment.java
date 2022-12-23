package com.fourshape.numbermazes.custom_calender.fragments;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;

import com.fourshape.numbermazes.R;
import com.fourshape.numbermazes.custom_calender.adapters.CalendarAdapter;
import com.fourshape.numbermazes.custom_calender.data_formats.MonthData;
import com.fourshape.numbermazes.custom_calender.views.MonthView;

public class MonthFragment extends Fragment {

    private MonthData monthData;
    private int cellView = -1;
    private int markView = -1;
    private boolean hasTitle = true;

    public void setData(MonthData monthData, int cellView, int markView) {
        this.monthData = monthData;
        this.cellView = cellView;
        this.markView = markView;
    }

    public void setTitle(boolean hasTitle) {
        this.hasTitle = hasTitle;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {

        LinearLayout ret = new LinearLayout(getContext());

        ret.setOrientation(LinearLayout.VERTICAL);
        ret.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        ret.setGravity(Gravity.CENTER);

        /*
        View titleView = inflater.inflate(R.layout.calendar_month_year_title, null);
        TextView monthTitleTV, yearTitleTV;

        monthTitleTV = titleView.findViewById(R.id.month_title);
        yearTitleTV = titleView.findViewById(R.id.year_title);

         */

        if ((monthData != null) && (monthData.getDate() != null)) {

            /*
            if (hasTitle) {
                monthTitleTV.setText(getMonth(monthData.getDate().getMonth()));
                yearTitleTV.setText(String.format(Locale.getDefault(), "%d", monthData.getDate().getYear()));
            }

             */

            MonthView monthView = new MonthView(getContext());
            monthView.setAdapter(new CalendarAdapter(getContext(), 1, monthData.getData()).setCellViews(cellView, markView));
            //((ViewGroup)titleView).addView(monthView);

            ret.addView(monthView);

        }

        return ret;
    }

    private static String getMonth (int date) {
        if (date == 1)
            return "Jan";
        else if (date == 2)
            return "Feb";
        else if (date == 3)
            return "Mar";
        else if (date == 4)
            return "Apr";
        else if (date == 5)
            return "May";
        else if (date == 6)
            return "Jun";
        else if (date == 7)
            return "Jul";
        else if (date == 8)
            return "Aug";
        else if (date == 9)
            return "Sep";
        else if (date == 10)
            return "Oct";
        else if (date == 11)
            return "Nov";
        else if (date == 12)
            return "Dec";
        else
            return "Nom";
    }

}
