package com.fourshape.numbermazes.maze_core;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.fourshape.numbermazes.R;
import com.fourshape.numbermazes.utils.AppColor;
import com.fourshape.numbermazes.utils.MakeLog;
import com.fourshape.numbermazes.utils.SharedData;
import com.fourshape.numbermazes.utils.VariablesControl;

public class THView extends View {

    private static final String TAG = "THView";
    private float radius, width, height, cornerRadius = 0, borderThickness = 0;

    private Paint borderPaint, shapePaint, letterPaint;
    private Rect letterPaintBounds;

    private Typeface letterTypeface;

    private boolean isAnimationMode = VariablesControl.IS_PRODUCTION;
    private int animationCounter = 0;
    private final float animationSlab = 2.5f;

    private int letterValue;

    private boolean hasStyleSet;
    private AppColor appColor;

    public THView(Context context) {
        super(context);
        init();
    }

    public THView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public THView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init () {

        borderPaint = new Paint();
        shapePaint = new Paint();
        letterPaint = new Paint();

        letterPaintBounds = new Rect();
        letterTypeface = Typeface.createFromAsset(this.getContext().getAssets(), "fonts/roboto_black.ttf");

        hasStyleSet = false;

        appColor = new AppColor();
        appColor.setThemeId(new SharedData(this.getContext()).getAppCurrentTheme());

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawView(canvas);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        float mWidth = MeasureSpec.getSize(widthMeasureSpec);
        float mHeight = MeasureSpec.getSize(heightMeasureSpec);

        float dimension = Math.min(mWidth, mHeight);

        this.width = dimension;
        this.height = dimension;

        setMeasuredDimension((int)dimension, (int)dimension);

    }

    public void refreshViewColors () {

        if (appColor == null) {
            appColor = new AppColor();
        }

        appColor.setThemeId(new SharedData(this.getContext()).getAppCurrentTheme());

    }

    private void setStyle () {

        cornerRadius = this.width/11;
        borderThickness = this.width/12;

        this.radius = this.width/2.5f - borderThickness;

        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setStrokeWidth(borderThickness);
        borderPaint.setAntiAlias(true);

        shapePaint.setAntiAlias(true);
        shapePaint.setStyle(Paint.Style.FILL);

        CornerPathEffect corEffect = new CornerPathEffect(this.cornerRadius);
        borderPaint.setPathEffect(corEffect);
        shapePaint.setPathEffect(corEffect);

        hasStyleSet = true;

    }

    private void drawView (Canvas canvas) {

        if (isAnimationMode)
            animationCounter++;

        if (!hasStyleSet) {
            setStyle();
        }

        Path path = new Path();

        float triangleHeight = (float) (Math.sqrt(3) * radius / 2);

        float centerX = this.width/2, centerY = this.height/2;

        // Start building the shape path
        float point1X = centerX, point1Y = centerY + radius;
        path.moveTo(point1X, point1Y);

        float point2X = centerX - triangleHeight, point2Y = centerY + radius / 2;

        if (isAnimationMode) {
            if (animationCounter > animationSlab)
                path.lineTo(point2X, point2Y);
        } else {
            path.lineTo(point2X, point2Y);
        }

        float point3X = centerX - triangleHeight, point3Y = centerY - radius / 2;

        if (isAnimationMode) {
            if (animationCounter > animationSlab*2)
                path.lineTo(point3X, point3Y);
        } else {
            path.lineTo(point3X, point3Y);
        }

        float point4X = centerX, point4Y = centerY - radius;

        if (isAnimationMode) {
            if (animationCounter > animationSlab*3)
                path.lineTo(point4X, point4Y);
        } else {
            path.lineTo(point4X, point4Y);
        }

        float point5X = centerX + triangleHeight, point5Y = centerY - radius / 2;

        if (isAnimationMode) {
            if (animationCounter > animationSlab*4)
                path.lineTo(point5X, point5Y);
        } else {
            path.lineTo(point5X, point5Y);
        }

        float point6X = centerX + triangleHeight, point6Y = centerY + radius / 2;

        if (isAnimationMode) {
            if (animationCounter > animationSlab*5)
            {
                path.lineTo(point6X, point6Y);
                path.close();
            }
        } else {
            path.lineTo(point6X, point6Y);
            path.close();
        }

        borderPaint.setColor(this.getContext().getColor(appColor.getTargetCellBorderColor()));
        shapePaint.setColor(this.getContext().getColor(appColor.getTargetCellBackgroundColor()));

        canvas.drawPath(path, borderPaint);

        if (isAnimationMode) {
            if (animationCounter > animationSlab * 5.9f) {
                canvas.drawPath(path, shapePaint);
                drawLetter(canvas, centerX, centerY);
                this.isAnimationMode = false;
            }
        } else {
            canvas.drawPath(path, shapePaint);
            drawLetter(canvas, centerX, centerY);
        }

        invalidate();

    }

    private void drawLetter (Canvas canvas, float posX, float posY) {

        final float fontSize = this.width / 3.5f;

        String text = String.valueOf(letterValue);

        float width, height;
        letterPaint.getTextBounds(text, 0, (int)(text.length()), letterPaintBounds);
        letterPaint.setTypeface(letterTypeface);
        letterPaint.setTextSize(fontSize);
        letterPaint.setAntiAlias(true);
        letterPaint.setColor(this.getContext().getColor(appColor.getTargetCellTextColor()));

        width = letterPaint.measureText(text);
        height = letterPaint.measureText(text);

        float x, y;
        x = posX - width / 2;
        y = posY + height / 2 + radius * 0.06f + (letterValue >= 10 ? -radius*0.25f : 0);

        canvas.drawText(text, x, y, letterPaint);

    }

    public void setLetter (int letterValue) {
        this.letterValue = letterValue;
        this.animationCounter = 0;
        this.isAnimationMode = true;
    }

}
