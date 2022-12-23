package com.fourshape.numbermazes.custom_calender.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

import com.fourshape.numbermazes.custom_calender.adapters.CalendarAdapter;
import com.fourshape.numbermazes.custom_calender.data_formats.MonthData;

public class MonthView extends GridView {

    private MonthData monthData;
    private CalendarAdapter adapter;

    public MonthView(Context context) {
        super(context);
        this.setNumColumns(7);
    }

    public MonthView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setNumColumns(7);
    }

    public MonthView displayMonth(MonthData monthData){
        adapter = new CalendarAdapter(getContext(), 1, monthData.getData());
        return this;
    }

}
