package com.fourshape.numbermazes.ui_popups;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.fourshape.numbermazes.R;
import com.fourshape.numbermazes.listeners.OnCellValueSetForTestListener;
import com.fourshape.numbermazes.utils.DimPopupWindow;
import com.fourshape.numbermazes.utils.MakeLog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class PopupSetNumber {

    private static final String TAG = "PopupSetNumber";

    private PopupWindow popupWindow;
    private View view;

    private TextView headerTV, rcAddressTV;
    private TextInputEditText enterNumberET;
    private MaterialButton setNumberMB;

    private int cellRank, cellRow, cellCol, cellValue;

    private OnCellValueSetForTestListener onCellValueSetForTestListener;
    private boolean isPressed = false;

    public PopupSetNumber (Context context) {
        this.view = LayoutInflater.from(context).inflate(R.layout.popup_set_shape_number, null);
        prepare();
    }

    public void setCell (int cellRank, int cellRow, int cellCol, int cellValue) {
        this.cellRank = cellRank;
        this.cellRow = cellRow;
        this.cellCol = cellCol;
        this.cellValue = cellValue;
        headerTV.setText("Cell Rank: " + String.valueOf(this.cellRank));
        rcAddressTV.setText("Row: " + String.valueOf(this.cellRow) + " Col: " + String.valueOf(this.cellCol));
        enterNumberET.setText(String.valueOf(this.cellValue));
    }

    private void prepare () {

        headerTV = this.view.findViewById(R.id.popup_header);
        rcAddressTV = this.view.findViewById(R.id.rc_address);
        enterNumberET = this.view.findViewById(R.id.shape_cell_value_edit_text);
        setNumberMB = this.view.findViewById(R.id.set_cell_value_mb);

        headerTV.setText("Cell Rank: " + String.valueOf(this.cellRank));
        rcAddressTV.setText("Row: " + String.valueOf(this.cellRow) + " Col: " + String.valueOf(this.cellCol));
        enterNumberET.setText(String.valueOf(this.cellValue));

        setViewListeners();
        prepareWindow();

    }

    private void prepareWindow () {

        int width = FrameLayout.LayoutParams.WRAP_CONTENT;
        int height = FrameLayout.LayoutParams.WRAP_CONTENT;

        popupWindow = new PopupWindow(this.view, width, height, false);

        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {

                if (!isPressed) {
                    if (onCellValueSetForTestListener != null) {
                        onCellValueSetForTestListener.onSet(-1);
                    }
                }

            }
        });

    }

    public void show () {

        if (this.popupWindow == null) {
            MakeLog.error(TAG, "PopupWindow is NULL");
            return;
        }

        if (this.popupWindow.isShowing())
            return;

        if (this.view == null) {
            MakeLog.error(TAG, "HolderView is NULL");
            return;
        }

        isPressed = false;

        popupWindow.showAtLocation(this.view, Gravity.CENTER, 0, 0);
        DimPopupWindow.dimBehind(popupWindow);
        MakeLog.info(TAG, "Popup is Showing.");

    }

    public boolean isShowing () {
        return popupWindow.isShowing();
    }

    private void setViewListeners () {

        setNumberMB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (onCellValueSetForTestListener != null) {

                    isPressed = true;

                    try {

                        int num = Integer.parseInt(enterNumberET.getText().toString().trim());
                        onCellValueSetForTestListener.onSet(num);

                    } catch (Exception e) {
                        MakeLog.exception(e);
                        onCellValueSetForTestListener.onSet(-1);
                    }

                    if (popupWindow != null) {
                        if (popupWindow.isShowing()) {
                            popupWindow.dismiss();
                        }
                    }

                }

            }
        });

    }

    public PopupSetNumber setOnCellValueSetForTestListener (OnCellValueSetForTestListener onCellValueSetForTestListener) {
        this.onCellValueSetForTestListener = onCellValueSetForTestListener;
        return this;
    }

}
