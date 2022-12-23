package com.fourshape.numbermazes.custom_calender.adapters;

import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.fourshape.numbermazes.custom_calender.CustomCalendarView;
import com.fourshape.numbermazes.custom_calender.data_formats.DateData;
import com.fourshape.numbermazes.custom_calender.data_formats.MonthData;
import com.fourshape.numbermazes.custom_calender.fragments.MonthFragment;
import com.fourshape.numbermazes.custom_calender.utils.CalendarUtil;

import org.jetbrains.annotations.NotNull;

public class CalendarViewAdapter extends FragmentStatePagerAdapter {

    private DateData date;

    private int dateCellId;
    private int markCellId;
    private boolean hasTitle = true;

    private Context context;
    private int mCurrentPosition = -1;

    public CalendarViewAdapter(@NonNull @NotNull FragmentManager fm) {
        super(fm);
    }

    public CalendarViewAdapter setDate(DateData date){
        this.date = date;
        return this;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public CalendarViewAdapter setDateCellId(int dateCellRes){
        this.dateCellId =  dateCellRes;
        return this;
    }

    public CalendarViewAdapter setMarkCellId(int markCellId){
        this.markCellId = markCellId;
        return this;
    }

    @NonNull
    @NotNull
    @Override
    public Fragment getItem(int position) {
        int year = CalendarUtil.position2Year(position);
        int month = CalendarUtil.position2Month(position);

        MonthFragment fragment = new MonthFragment();
        fragment.setTitle(hasTitle);
        MonthData monthData = new MonthData(new DateData(year, month, month / 2), hasTitle);
        fragment.setData(monthData, dateCellId, markCellId);
        return fragment;
    }

    @Override
    public int getCount() {
        return 1000;
    }

    public CalendarViewAdapter setTitle(boolean hasTitle){
        this.hasTitle = hasTitle;
        return this;
    }

    @Override
    public void setPrimaryItem(@NonNull ViewGroup container, int position, @NotNull Object object) {
        super.setPrimaryItem(container, position, object);
        ((CustomCalendarView) container).measureCurrentView(position);
    }
}
