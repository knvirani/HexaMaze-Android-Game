package com.fourshape.numbermazes.custom_calender.views;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.TextView;

import com.fourshape.numbermazes.R;
import com.fourshape.numbermazes.custom_calender.CellConfig;
import com.fourshape.numbermazes.custom_calender.DateValidation;
import com.fourshape.numbermazes.custom_calender.MarkStyleExp;
import com.fourshape.numbermazes.custom_calender.data_formats.DayData;
import com.fourshape.numbermazes.utils.AppColor;
import com.fourshape.numbermazes.utils.FormattedData;
import com.fourshape.numbermazes.utils.MakeLog;
import com.fourshape.numbermazes.utils.SharedData;

public class DefaultCellView extends BaseCellView {

    public TextView textView;
    private AbsListView.LayoutParams matchParentParams;
    private Context context;
    private Typeface robotoRegularSlimTypeface;
    private AppColor appColor;

    public DefaultCellView(Context context) {
        super(context);
        this.context = context;
        robotoRegularSlimTypeface = Typeface.createFromAsset(context.getAssets(), "fonts/roboto_regular_slim.ttf");
        initLayout();
    }

    public DefaultCellView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        robotoRegularSlimTypeface = Typeface.createFromAsset(context.getAssets(), "fonts/roboto_regular_slim.ttf");
        initLayout();
    }

    private int getAttrValue (int attrId) {

        try {
            TypedValue typedValue = new TypedValue();
            context.getTheme().resolveAttribute(attrId, typedValue, true);
            return typedValue.data;
        } catch (Exception e) {
            MakeLog.exception(e);
            return -1;
        }

    }

    private void initLayout(){

        appColor = new AppColor();
        appColor.setThemeId(new SharedData(context).getAppCurrentTheme());

        matchParentParams = new AbsListView.LayoutParams((int) CellConfig.cellWidth, (int) CellConfig.cellHeight);
        this.setLayoutParams(matchParentParams);
        this.setOrientation(VERTICAL);
        textView = new TextView(getContext());
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(14.0f);
        textView.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, (float) 1.0));
        textView.setTypeface(robotoRegularSlimTypeface);
        this.addView(textView);
    }

    @Override
    public void setDisplayText(DayData day) {

        if (day.getText().trim().equals("Sun") || day.getText().trim().equals("Mon") || day.getText().trim().equals("Tue") || day.getText().trim().equals("Wed") || day.getText().trim().equals("Thu") || day.getText().trim().equals("Fri") || day.getText().trim().equals("Sat"))
        {
            textView.setBackgroundColor(context.getColor(this.appColor.getCalendarWeekDayRowBackgroundColor()));
            textView.setTextColor(context.getColor(this.appColor.getCalendarWeekDayTitleColor()));
        } else {

            if (DateValidation.isValidDate(day.getDate())) {
                textView.setTextColor(context.getColor(this.appColor.getCalendarDayColorTitleColor()));
            } else {
                textView.setTextColor(context.getColor(this.appColor.getInvalidCalendarDateTitleColor()));
            }

        }

        textView.setText(day.getText());
        //MakeLog.info("DefaultCellView", day.getText());


    }

    @Override
    protected void onMeasure(int measureWidthSpec,int measureHeightSpec){
        super.onMeasure(measureWidthSpec, measureHeightSpec);
    }

    public boolean setDateChoose() {
        setBackgroundDrawable(MarkStyleExp.choose);
        textView.setTextColor(Color.WHITE);
        return true ;
    }

    public void setDateToday(){
        setBackgroundDrawable(MarkStyleExp.today);
        textView.setTextColor(Color.rgb(105, 75, 125));
    }

    public void setDateNormal() {
        textView.setTextColor(Color.BLACK);
        setBackgroundDrawable(null);
    }

    public void setTextColor(String text, int color) {
        textView.setText(text);
        if (color != 0) {
            textView.setTextColor(color);
        }
    }

}
