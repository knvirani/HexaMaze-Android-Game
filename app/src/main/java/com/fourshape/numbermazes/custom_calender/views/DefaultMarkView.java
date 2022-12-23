package com.fourshape.numbermazes.custom_calender.views;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fourshape.numbermazes.R;
import com.fourshape.numbermazes.custom_calender.CellConfig;
import com.fourshape.numbermazes.custom_calender.MarkStyle;
import com.fourshape.numbermazes.custom_calender.data_formats.DayData;
import com.fourshape.numbermazes.utils.MakeLog;

public class DefaultMarkView extends BaseMarkView {

    private TextView textView;

    private AbsListView.LayoutParams matchParentParams;
    private int orientation;

    private View sideBar;
    private TextView markTextView;
    private ShapeDrawable circleDrawable;
    private Context context;

    private Typeface robotoRegularSlimTypeface;

    public DefaultMarkView(Context context) {
        super(context);
        this.context = context;
        robotoRegularSlimTypeface = Typeface.createFromAsset(context.getAssets(), "fonts/roboto_regular_slim.ttf");
    }

    public DefaultMarkView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        robotoRegularSlimTypeface = Typeface.createFromAsset(context.getAssets(), "fonts/roboto_regular_slim.ttf");
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

    private void initLayoutWithStyle(MarkStyle style){

        textView = new TextView(getContext());
        textView.setTypeface(robotoRegularSlimTypeface);

        textView.setGravity(Gravity.CENTER);
        matchParentParams = new AbsListView.LayoutParams((int) CellConfig.cellWidth, (int) CellConfig.cellHeight);

        switch (style.getStyle()){

            case MarkStyle.DEFAULT:

            case MarkStyle.BACKGROUND:

                this.setLayoutParams(matchParentParams);
                this.setOrientation(HORIZONTAL);
                textView.setTextColor(Color.WHITE);
                this.setPadding(11, 5, 11, 5);
                textView.setLayoutParams(new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, (float) 1.0));
                textView.setBackground(textView.getContext().getDrawable(R.drawable.bg_game_session_completion));
                this.addView(textView);
                return;

            case MarkStyle.SELECTED_DATE:

                this.setLayoutParams(matchParentParams);
                this.setOrientation(HORIZONTAL);
                textView.setTextColor(context.getColor(R.color.default_selected_date_text));
                this.setPadding(11, 5, 11, 5);
                textView.setLayoutParams(new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, (float) 1.0));
                textView.setBackground(textView.getContext().getDrawable(R.drawable.bg_selected_date_in_calendar));
                this.addView(textView);
                return;

            case MarkStyle.TODAY_DATE_INCOMPLETE:

                this.setLayoutParams(matchParentParams);
                this.setOrientation(HORIZONTAL);
                textView.setTextColor(context.getColor(R.color.default_calendar_game_session_incomplete_marked_date));
                this.setPadding(11, 5, 11, 5);
                textView.setLayoutParams(new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, (float) 1.0));
                textView.setBackground(textView.getContext().getDrawable(R.drawable.bg_today_in_calendar_incomplete));
                this.addView(textView);
                return;

            case MarkStyle.TODAY_DATE_COMPLETE:

                this.setLayoutParams(matchParentParams);
                this.setOrientation(HORIZONTAL);
                textView.setTextColor(context.getColor(R.color.default_calendar_game_session_complete_marked_date));
                this.setPadding(11, 5, 11, 5);
                textView.setLayoutParams(new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, (float) 1.0));
                textView.setBackground(textView.getContext().getDrawable(R.drawable.bg_today_in_calendar_completion));
                this.addView(textView);
                return;

            case MarkStyle.SESSION_COMPLETE:

                this.setLayoutParams(matchParentParams);
                this.setOrientation(HORIZONTAL);
                textView.setTextColor(context.getColor(R.color.default_calendar_game_session_complete_marked_date));
                this.setPadding(11, 5, 11, 5);
                textView.setLayoutParams(new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, (float) 1.0));
                textView.setBackground(textView.getContext().getDrawable(R.drawable.bg_game_session_completion));
                this.addView(textView);
                return;

            case MarkStyle.SESSION_INCOMPLETE:

                this.setLayoutParams(matchParentParams);
                this.setOrientation(HORIZONTAL);
                textView.setTextColor(context.getColor(R.color.default_calendar_game_session_incomplete_marked_date));
                this.setPadding(11, 5, 11, 5);
                textView.setLayoutParams(new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, (float) 1.0));
                textView.setBackground(textView.getContext().getDrawable(R.drawable.bg_game_session_incomplete));
                this.addView(textView);
                return;

            case MarkStyle.DOT:

                this.setLayoutParams(matchParentParams);
                this.setOrientation(VERTICAL);
                textView.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, (float) 2.0));
                this.addView(new PlaceHolderVertical(getContext()));
                this.addView(textView);
                this.addView(new Dot(getContext(), style.getColor()));

                return;

            case MarkStyle.RIGHTSIDEBAR:

                this.setLayoutParams(matchParentParams);
                this.setOrientation(HORIZONTAL);
                textView.setLayoutParams(new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, (float) 3.0));
                this.addView(new PlaceHolderHorizontal(getContext()));
                this.addView(textView);
                PlaceHolderHorizontal barRight = new PlaceHolderHorizontal(getContext());
                barRight.setBackgroundColor(style.getColor());
                this.addView(barRight);

                return;

            case MarkStyle.LEFTSIDEBAR:

                this.setLayoutParams(matchParentParams);
                this.setOrientation(HORIZONTAL);
                textView.setLayoutParams(new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, (float) 3.0));
                PlaceHolderHorizontal barLeft = new PlaceHolderHorizontal(getContext());
                barLeft.setBackgroundColor(style.getColor());
                this.addView(barLeft);
                this.addView(textView);
                this.addView(new PlaceHolderHorizontal(getContext()));

                return;

            default:
                throw new IllegalArgumentException("Invalid Mark Style Configuration!");
        }
    }

    @Override
    public void setDisplayText(DayData day) {

        initLayoutWithStyle(day.getDate().getMarkStyle());
        textView.setText(day.getText());

    }

    static class PlaceHolderHorizontal extends View{

        LayoutParams params;
        public PlaceHolderHorizontal(Context context) {
            super(context);
            params = new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, (float) 1.0);
            this.setLayoutParams(params);
        }

        public PlaceHolderHorizontal(Context context, AttributeSet attrs) {
            super(context, attrs);
            params = new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, (float) 1.0);
            this.setLayoutParams(params);
        }
    }

    static class PlaceHolderVertical extends View{

        LayoutParams params;
        public PlaceHolderVertical(Context context) {
            super(context);
            params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, (float) 1.0);
            this.setLayoutParams(params);
        }

        public PlaceHolderVertical(Context context, AttributeSet attrs) {
            super(context, attrs);
            params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, (float) 1.0);
            this.setLayoutParams(params);
        }
    }

    static class Dot extends RelativeLayout {

        public Dot(Context context, int color) {
            super(context);
            this.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, (float) 1.0));
            View dotView = new View(getContext());
            LayoutParams lp = new LayoutParams(10, 10);
            lp.addRule(CENTER_IN_PARENT,TRUE);
            dotView.setLayoutParams(lp);
            ShapeDrawable dot = new ShapeDrawable(new OvalShape());

            dot.getPaint().setColor(color);
            dotView.setBackground(dot);
            this.addView(dotView);
        }
    }

}
