package com.fourshape.numbermazes.custom_calender.views;

import android.content.Context;
import android.util.AttributeSet;

import com.fourshape.numbermazes.custom_calender.data_formats.DateData;
import com.fourshape.numbermazes.custom_calender.listeners.OnDateClickListener;

public abstract class BaseMarkView extends BaseCellView {

    private OnDateClickListener clickListener;
    private DateData date;

    public BaseMarkView(Context context) {
        super(context);
    }

    public BaseMarkView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
}
