package com.fourshape.numbermazes.maze_core;

import android.content.Context;
import android.graphics.BlendMode;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.ImageFormat;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.fourshape.numbermazes.R;
import com.fourshape.numbermazes.listeners.OnBoardStartAnimationListener;
import com.fourshape.numbermazes.listeners.OnCellTapListener;
import com.fourshape.numbermazes.listeners.OnCellValueSetForTestListener;
import com.fourshape.numbermazes.listeners.OnGameWonAnimationStatusListener;
import com.fourshape.numbermazes.maze_core.structure.Cell;
import com.fourshape.numbermazes.maze_core.structure.FillType;
import com.fourshape.numbermazes.maze_core.structure.Property;
import com.fourshape.numbermazes.maze_core.structure.Validity;
import com.fourshape.numbermazes.ui_popups.PopupSetNumber;
import com.fourshape.numbermazes.utils.AppColor;
import com.fourshape.numbermazes.utils.MakeLog;
import com.fourshape.numbermazes.utils.SharedData;
import com.fourshape.numbermazes.utils.VariablesControl;

public class BoardView extends View {

    private static final String TAG = "BoardView";

    private float radius, width, height, cornerRadius, borderThickness, triangularHeight, horizontalSpace;

    private Paint borderPaint, shapePaint, letterPaint, connectorLinePaint;
    private Rect letterPaintBounds;

    private Canvas canvas;

    private Typeface letterTypeface;

    private float xCoord, yCoord;

    private Matrix matrix;

    private boolean isAnimationMode = (VariablesControl.IS_PRODUCTION);
    private final int ANIMATION_MILLISECONDS = 50;
    private int animationCounter = 0;
    private final int animationSlab = 790;

    private boolean isWinAnimationMode = false;
    private float winAnimationCounter = 0;
    private final float winAnimationSlab = 2.5f;

    private PopupSetNumber popupSetNumber;
    private boolean isShapeEditMode = false;
    private boolean shouldDraw = false;
    private boolean isWinNotified = false;

    private OnBoardStartAnimationListener onBoardStartAnimationListener;
    private OnCellTapListener onCellTapListener;
    private OnGameWonAnimationStatusListener onGameWonAnimationStatusListener;

    private AppColor appColor;

    public BoardView(Context context) {
        super(context);
        init();
    }

    public BoardView (Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    public BoardView (Context context, AttributeSet attributeSet, int defStyleAttr) {
        super(context, attributeSet, defStyleAttr);
        init();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.canvas = canvas;
        drawView();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        float mWidth = MeasureSpec.getSize(widthMeasureSpec);
        float mHeight = MeasureSpec.getSize(heightMeasureSpec);

        float dimension = Math.min(mWidth, mHeight);

        final float PADDING_HORIZONTAL = 16, PADDING_VERTICAL = 16;

        this.width = dimension - PADDING_HORIZONTAL * 2 - 10;
        this.height = dimension - PADDING_VERTICAL * 2;

        this.horizontalSpace = this.width / this.matrix.getMAX_COL() * 0.98f;
        this.triangularHeight = (float)Math.tan(Math.toRadians(30)) * this.horizontalSpace;
        this.radius = (this.horizontalSpace) / (float) Math.cos(Math.toRadians(30)) * 0.92f;

        //MakeLog.info(TAG, "Width: " + this.width + " Height: " + height);
        //MakeLog.info(TAG, "HS: " + this.horizontalSpace);
        //MakeLog.info(TAG, "TriangularHeight: " + this.triangularHeight);
        //MakeLog.info(TAG, "Radius: " + this.radius);

        setMeasuredDimension((int)this.width, (int)this.height);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        boolean isValidTouchEvent = false;

        float x = event.getX(), y = event.getY();
        int action = event.getAction();

        xCoord = -1;
        yCoord = -1;

        if (action == MotionEvent.ACTION_DOWN || action == MotionEvent.ACTION_MOVE) {

            xCoord = x;
            yCoord = y;

            isValidTouchEvent = true;

        }

        return isValidTouchEvent;

    }

    public void reset () {
        isWinNotified = false;
        isWinAnimationMode = false;
        isShapeEditMode = false;
        isAnimationMode = VariablesControl.IS_PRODUCTION;
    }

    public void setOnCellTapListener (OnCellTapListener onCellTapListener) {
        this.onCellTapListener = onCellTapListener;
    }

    public void setOnBoardStartAnimationListener (OnBoardStartAnimationListener onBoardStartAnimationListener) {
        this.onBoardStartAnimationListener = onBoardStartAnimationListener;
    }

    public void setOnGameWonAnimationStatusListener (OnGameWonAnimationStatusListener onGameWonAnimationStatusListener) {
        this.onGameWonAnimationStatusListener = onGameWonAnimationStatusListener;
    }

    public void toggleWinAnimationForTest () {

        if (!isWinAnimationMode)
        {

            for (int row=0; row<matrix.getMAXRow(); row++) {
                for (int col=0; col<matrix.getMAX_COL(); col++) {

                    Cell cell = matrix.getBoard()[row][col];

                    if (cell.getProperty() == Property.RESERVED) {
                        this.matrix.getBoard()[row][col].setCorrectValue(cell.getValue());
                    }

                }
            }

            isWinAnimationMode = true;
        }
        else
            isWinAnimationMode = false;

    }

    public void setMatrix (Matrix matrix) {

        this.matrix = matrix;

        this.horizontalSpace = this.width / this.matrix.getMAX_COL() * 0.98f;
        this.triangularHeight = (float)Math.tan(Math.toRadians(30)) * this.horizontalSpace;
        this.radius = (this.horizontalSpace) / (float) Math.cos(Math.toRadians(30)) * 0.92f;

        //MakeLog.info(TAG, "Width: " + this.width + " Height: " + height);
        //MakeLog.info(TAG, "HS: " + this.horizontalSpace);
        //MakeLog.info(TAG, "TriangularHeight: " + this.triangularHeight);
        //MakeLog.info(TAG, "Radius: " + this.radius);

    }

    private void init () {

        canvas = new Canvas();

        borderPaint = new Paint();
        shapePaint = new Paint();
        letterPaint = new Paint();
        connectorLinePaint = new Paint();

        letterPaintBounds = new Rect();

        radius = 25;
        cornerRadius = 6.5f;
        borderThickness = 13f;

        matrix = new Matrix();

        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setStrokeWidth(borderThickness);
        borderPaint.setAntiAlias(true);

        shapePaint.setAntiAlias(true);
        shapePaint.setStyle(Paint.Style.FILL);

        CornerPathEffect corEffect = new CornerPathEffect(this.cornerRadius);
        borderPaint.setPathEffect(corEffect);
        shapePaint.setPathEffect(corEffect);

        letterTypeface = Typeface.createFromAsset(this.getContext().getAssets(), "fonts/roboto_regular_slim.ttf");

        popupSetNumber = new PopupSetNumber(this.getContext()).setOnCellValueSetForTestListener(onCellValueSetForTestListener);

        appColor = new AppColor();
        appColor.setThemeId(new SharedData(this.getContext()).getAppCurrentTheme());

    }

    private final OnCellValueSetForTestListener onCellValueSetForTestListener = new OnCellValueSetForTestListener() {
        @Override
        public void onSet(int value) {
            matrix.getBoard()[matrix.getSelectedRow()][matrix.getSelectedCol()].setValue(value);
            MakeLog.info(TAG, "Set Value For Text - R: " + matrix.getSelectedRow() + " C: " + matrix.getSelectedCol() + " Value: " + value);
            enableDrawing();
        }
    };

    public void refreshViewColors () {

        if (appColor == null) {
            appColor = new AppColor();
        }

        appColor.setThemeId(new SharedData(this.getContext()).getAppCurrentTheme());

    }

    public void disableGameWinAnimation () {
        isWinAnimationMode = false;
    }

    public void enableGameWinAnimation () {
        isWinAnimationMode = true;
    }

    private void calculatePath () {

        float startX = horizontalSpace;
        float startY = radius * 2;

        // draw hexagon first

        for (int row = 0; row < matrix.getMAXRow(); row++) {

            startY = radius * 1.630f * (row + 1);

            for (int col = 0; col < matrix.getMAX_COL(); col++) {

                Cell currCell = this.matrix.getBoard()[row][col];
                if (currCell.getProperty() == Property.RESERVED) {
                    startX = (horizontalSpace) * (col + 1) + 9.55f;
                    drawTestHexagon(startX, startY, currCell);
                }

                if (isAnimationMode)
                    animationCounter++;

            }
        }

        if (!isAnimationMode) {

            boolean shouldBreak = false;

            // draw connector line first

            if (!isWinAnimationMode) {

                for (int point=matrix.getBeginNumber()+1; point<=matrix.getEndNumber(); point++) {

                    Cell cCell = matrix.getCellFromCorrectValue(point);
                    Cell pCell = matrix.getCellFromCorrectValue(point-1);

                    if (cCell != null && cCell.getFillType() == FillType.PRE_FILLED) {

                        if (pCell != null && pCell.getFillType() == FillType.USER_FILLED) {

                            if (pCell.getValidity() == Validity.VALID) {
                                drawConnectorLine(cCell.getCenterX(), cCell.getCenterY(), pCell.getCenterX(), pCell.getCenterY());
                            }

                        } else {
                            drawConnectorLine(cCell.getCenterX(), cCell.getCenterY(), pCell.getCenterX(), pCell.getCenterY());
                        }

                    } else if (cCell != null && cCell.getFillType() == FillType.USER_FILLED) {

                        if (cCell.getValidity() == Validity.VALID) {
                            drawConnectorLine(cCell.getCenterX(), cCell.getCenterY(), pCell.getCenterX(), pCell.getCenterY());
                        } else {
                            break;
                        }

                    }

                }

            } else {

                for (int point=matrix.getBeginNumber()+1; point<=matrix.getEndNumber(); point++) {

                    Cell cCell = matrix.getCellFromCorrectValue(point);
                    Cell pCell = matrix.getCellFromCorrectValue(point - 1);

                    if (winAnimationCounter > winAnimationSlab*point)
                        drawConnectorLine(cCell.getCenterX(), cCell.getCenterY(), pCell.getCenterX(), pCell.getCenterY());
                    else {
                        break;
                    }

                }

                if (winAnimationCounter < winAnimationSlab*matrix.getEndNumber()+1)
                    winAnimationCounter++;
                else {

                    if (!isWinNotified) {
                        isWinNotified = true;
                        MakeLog.info(TAG, "Win Animation Ends Here");
                        if (onGameWonAnimationStatusListener != null) {
                            onGameWonAnimationStatusListener.onFinish();
                        }
                    }

                }

            }

            if (isShapeEditMode) {

                for (int point=matrix.getBeginNumber()+1; point<=matrix.getEndNumber(); point++) {

                    Cell cCell = matrix.getCellFromCorrectValue(point);
                    Cell pCell = matrix.getCellFromCorrectValue(point - 1);

                    if (cCell != null && pCell != null && pCell.getValue() != -1 && cCell.getValue() != -1 && cCell.getValue() != 0 && pCell.getValue() != 0)
                        drawConnectorLine(cCell.getCenterX(), cCell.getCenterY(), pCell.getCenterX(), pCell.getCenterY());

                }

            }

            for (int row=0; row<matrix.getMAXRow(); row++) {

                for (int col=0; col<matrix.getMAX_COL(); col++) {

                    Cell currCell = this.matrix.getBoard()[row][col];

                    if (currCell.getProperty() == Property.RESERVED) {

                        Cell pCell = null;

                        if (currCell.getFillType() == FillType.PRE_FILLED) {
                            pCell = this.matrix.getPreviousCellForConnectorLine(currCell.getCorrectValue());
                        } else if (currCell.getFillType() == FillType.USER_FILLED && currCell.getValidity() == Validity.VALID) {
                            pCell = this.matrix.getPreviousCellForConnectorLine(currCell.getCorrectValue());
                        }

                        if (pCell != null)
                            drawConnectorLine(currCell.getCenterX(), currCell.getCenterY(), pCell.getCenterX(), pCell.getCenterY());
                        else {
                            shouldBreak = true;
                            break;
                        }

                    }

                }

                if (shouldBreak)
                    break;

            }

            // draw letters second
            if (!isWinAnimationMode) {
                for (int row=0; row<matrix.getMAXRow(); row++) {

                    startY = radius * 1.630f * (row + 1);
                    for (int col=0; col<matrix.getMAX_COL(); col++) {

                        Cell currCell = this.matrix.getBoard()[row][col];
                        if (currCell.getProperty() == Property.RESERVED) {
                            startX = (horizontalSpace) * (col + 1) + 9.55f;
                            drawLetter(startX, startY, currCell);
                        }

                    }
                }
            }


        }

        invalidate();

    }

    private void drawView () {

        if (shouldDraw)
            calculatePath();

        invalidate();

    }

    private void drawLetter (float posX, float posY, Cell cell) {

        final float fontSize = this.width * 0.055f;

        if (!isShapeEditMode) {
            if (cell.getFillType() == FillType.USER_FILLED) {
                if (cell.getValidity() == Validity.VALID || cell.getValidity() == Validity.UNKNOWN) {
                    letterPaint.setColor(this.getContext().getColor(appColor.getUserFilledCellTextColor()));
                } else {
                    letterPaint.setColor(this.getContext().getColor(appColor.getWrongCellTextColor()));
                }
            } else if (cell.getFillType() == FillType.PRE_FILLED) {
                letterPaint.setColor(this.getContext().getColor(appColor.getPrefilledCellTextColor()));
            }
        } else {
            if (cell.getValue() == -1) {
                letterPaint.setColor(this.getContext().getColor(R.color.cell_rank));
            } else {
                letterPaint.setColor(this.getContext().getColor(R.color.white));
            }

        }

        String text;

        try {

            if (isShapeEditMode) {
                if (cell.getValue() == -1) {
                    text = String.valueOf(cell.getRank());
                } else {
                    text = String.valueOf(cell.getValue());
                }
            } else {
                if (cell.getValue() > 0) {
                    text = String.valueOf(cell.getValue());
                } else {
                    text = "";
                }
            }

        } catch (Exception e) {
            MakeLog.exception(e);
            text = "";
        }

        float width, height;
        letterPaint.getTextBounds(text, 0, (int)(text.length()), letterPaintBounds);
        letterPaint.setTypeface(letterTypeface);
        letterPaint.setTextSize(fontSize);
        letterPaint.setAlpha(200);
        letterPaint.setAntiAlias(true);

        width = letterPaint.measureText(text);
        height = letterPaint.measureText(text);

        float x, y;
        x = posX - width / 2;

        if (isShapeEditMode) {
            y = posY + height / 2 + radius * 0.06f + (cell.getValue() <= 0 ? (cell.getRank() >= 10 ? -radius*0.25f : 0) : (cell.getValue() >= 10 ? -radius*0.25f : 0));
        } else {
            y = posY + height / 2 + radius * 0.06f + (cell.getValue() >= 10 ? -radius*0.25f : 0);
        }



        canvas.drawText(text, x, y, letterPaint);

    }

    public int getDimension () {
        return (int) width;
    }

    public Canvas getCanvas () {
        final Canvas myCanvas = canvas;
        return myCanvas;
    }

    private void drawConnectorLine (float startX, float startY, float endX, float endY) {

        connectorLinePaint.setAntiAlias(true);
        connectorLinePaint.setStyle(Paint.Style.STROKE);
        connectorLinePaint.setStrokeWidth(borderThickness);

        if (!isWinAnimationMode)
            connectorLinePaint.setColor(this.getContext().getColor(appColor.getConnectorLineColor()));
        else
            connectorLinePaint.setColor(this.getContext().getColor(appColor.getWinAnimationConnectorLine()));

        CornerPathEffect cornerEffect = new CornerPathEffect(this.cornerRadius);
        connectorLinePaint.setPathEffect(cornerEffect);

        Path path = new Path();
        path.moveTo(startX, startY);
        path.lineTo(endX, endY);
        path.close();

        canvas.drawPath(path, connectorLinePaint);

    }

    private void drawTestHexagon (float startX, float startY, Cell cell) {

        int row = cell.getRowLocation();
        int col = cell.getColLocation();

        Path path = new Path();

        float triangleHeight = (float) (Math.sqrt(3) * radius / 2);
        float centerX = startX, centerY = startY;

        // set centerX and centerY to board cell.
        if (cell.getCenterX() == -1 || cell.getCenterY() == -1) {
            this.matrix.getBoard()[row][col].setCenterXY(centerX, centerY);
            //MakeLog.info(TAG, "Setting XY");
        }

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

        boolean isValidTouch =
                (xCoord != -1 && yCoord != -1)
                &&
                        (xCoord > point2X && xCoord < point6X)
                &&
                        (yCoord < point2Y && yCoord > point3Y)
                ;

        if (isValidTouch) {

            borderPaint.setColor(this.getContext().getColor(appColor.getCellBorderColor()));
            shapePaint.setColor(this.getContext().getColor(appColor.getCellTapBackgroundColor()));

            matrix.selectRC(row, col);

            if (isShapeEditMode) {

                if (popupSetNumber != null) {
                    if (!popupSetNumber.isShowing()) {
                        popupSetNumber.setCell(cell.getRank(), row, col, cell.getValue());
                        popupSetNumber.show();
                        disableDrawing();
                    }
                }

            } else {

                if (cell.getProperty() == Property.RESERVED && cell.getFillType() == FillType.USER_FILLED) {
                    if (cell.getValue() == 0) {
                        if (onCellTapListener != null) {
                            onCellTapListener.onSetCellValue();
                        }
                    } else {
                    /*
                    if (onCellTapListener != null) {
                        onCellTapListener.onRemoveCellValue();
                    }

                     */
                    }
                }

            }


        } else {

            borderPaint.setColor(this.getContext().getColor(this.appColor.getCellBorderColor()));
            
            if (isWinAnimationMode) {

                if (cell.isFirst() || cell.isLast()) {
                    shapePaint.setColor(this.getContext().getColor(this.appColor.getWinAnimationConnectorLine()));
                } else {
                    if (cell.getFillType() == FillType.PRE_FILLED) {
                        shapePaint.setColor(this.getContext().getColor(this.appColor.getPrefilledCellBackgroundColor()));
                    } else if (cell.getFillType() == FillType.USER_FILLED) {

                        if (cell.getValidity() == Validity.INVALID) {
                            shapePaint.setColor(this.getContext().getColor(this.appColor.getWrongCellBackgroundColor()));
                        } else {
                            shapePaint.setColor(this.getContext().getColor(this.appColor.getUserFilledCellBackgroundColor()));
                        }

                    }
                }

            } else {
                if (cell.getFillType() == FillType.PRE_FILLED) {
                    shapePaint.setColor(this.getContext().getColor(this.appColor.getPrefilledCellBackgroundColor()));
                } else if (cell.getFillType() == FillType.USER_FILLED) {

                    if (cell.getValidity() == Validity.INVALID) {
                        shapePaint.setColor(this.getContext().getColor(this.appColor.getWrongCellBackgroundColor()));
                    } else {
                        shapePaint.setColor(this.getContext().getColor(this.appColor.getUserFilledCellBackgroundColor()));
                    }

                }
            }

        }

        canvas.drawPath(path, borderPaint);

        if (isAnimationMode) {
            if (animationCounter > animationSlab*5.9f) {

                canvas.drawPath(path, shapePaint);
                isAnimationMode = false;

                if (onBoardStartAnimationListener != null) {
                    onBoardStartAnimationListener.onFinish();
                }

            }
        } else {
            canvas.drawPath(path, shapePaint);
        }

    }

    public void enableDrawing () {
        shouldDraw = true;
        invalidate();
        MakeLog.info(TAG, "Drawing has been enabled");
    }

    public void disableDrawing () {
        shouldDraw = false;
        MakeLog.info(TAG, "Drawing has been disabled");
    }

    public void enableShapeEditMode () {
        isShapeEditMode = true;
    }

}
