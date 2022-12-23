package com.fourshape.numbermazes.ui_popups;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.appcompat.widget.LinearLayoutCompat;

import com.fourshape.numbermazes.R;
import com.fourshape.numbermazes.utils.AppColor;
import com.fourshape.numbermazes.utils.DimPopupWindow;
import com.fourshape.numbermazes.utils.MakeLog;
import com.fourshape.numbermazes.utils.SharedData;
import com.google.android.material.card.MaterialCardView;

import org.w3c.dom.Text;

public class PopupSoundControl {

    private static final String TAG = "PopupSoundControl";

    private PopupWindow popupWindow;
    private Context context;
    private View view;

    private MaterialCardView parentCV, innerParentCV;
    private LinearLayoutCompat offLL, onLL;
    private TextView soundOnTextView, soundOffTextView;

    private boolean isSoundOn = true;

    private int soundOnTextColor, soundOnBgColor, soundOffTextColor, soundOffBgColor;

    private AppColor appColor;

    public PopupSoundControl(Context context) {
        this.context = context;
        preparePopupWindow();

    }

    @SuppressLint("ResourceType")
    private void preparePopupWindow () {

        if (this.context == null) {
            MakeLog.error(TAG, "Context is NULL.");
            return;
        }

        view = LayoutInflater.from(this.context).inflate(R.layout.popup_view_volume_control, null);

        if (view == null) {
            MakeLog.error(TAG, "View is NULL.");
            return;
        }

        appColor = new AppColor();
        appColor.setThemeId(new SharedData(this.context).getAppCurrentTheme());

        int width = MaterialCardView.LayoutParams.WRAP_CONTENT;
        int height = MaterialCardView.LayoutParams.WRAP_CONTENT;
        boolean focusable = false;

        popupWindow = new PopupWindow(view, width, height, focusable);

        offLL = view.findViewById(R.id.left_part);
        onLL = view.findViewById(R.id.right_part);
        parentCV = view.findViewById(R.id.parent_cv);
        innerParentCV = view.findViewById(R.id.inner_parent_cv);
        soundOnTextView = view.findViewById(R.id.on_sound);
        soundOffTextView = view.findViewById(R.id.off_sound);

        parentCV.setCardBackgroundColor(view.getContext().getColor(appColor.getPopupCardBackgroundColor()));
        innerParentCV.setCardBackgroundColor(view.getContext().getColor(appColor.getPopupCardBackgroundColor()));
        innerParentCV.setStrokeColor(view.getContext().getColor(appColor.getSelectedItemBackgroundColor()));

        soundOnTextColor = view.getContext().getColor(appColor.getSelectedItemTextColor());
        soundOnBgColor = view.getContext().getColor(appColor.getSelectedItemBackgroundColor());
        soundOffTextColor = view.getContext().getColor(appColor.getUnselectedItemTextColor());
        soundOffBgColor = view.getContext().getColor(appColor.getUnselectedItemBackgroundColor());

        if (new SharedData(this.context).getGameSoundStatus()) {

            soundOffTextView.setSelected(false);
            soundOffTextView.setTextColor(soundOffTextColor);
            soundOffTextView.setCompoundDrawableTintList(ColorStateList.valueOf(soundOffTextColor));
            soundOffTextView.setBackgroundColor(soundOffBgColor);

            soundOnTextView.setSelected(true);
            soundOnTextView.setTextColor(soundOnTextColor);
            soundOnTextView.setCompoundDrawableTintList(ColorStateList.valueOf(soundOnTextColor));
            soundOnTextView.setBackgroundColor(soundOnBgColor);

        } else {

            soundOnTextView.setSelected(false);
            soundOnTextView.setTextColor(soundOffTextColor);
            soundOnTextView.setCompoundDrawableTintList(ColorStateList.valueOf(soundOffTextColor));
            soundOnTextView.setBackgroundColor(soundOffBgColor);

            soundOffTextView.setSelected(true);
            soundOffTextView.setTextColor(soundOnTextColor);
            soundOffTextView.setCompoundDrawableTintList(ColorStateList.valueOf(soundOnTextColor));
            soundOffTextView.setBackgroundColor(soundOnBgColor);

        }

        soundOnTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleViewSelection(soundOnTextView, soundOffTextView);
                new SharedData(view.getContext()).toggleGameSound(true);
            }
        });

        soundOffTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleViewSelection(soundOnTextView, soundOffTextView);
                new SharedData(view.getContext()).toggleGameSound(false);
            }
        });

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

    @SuppressLint("ResourceType")
    private void toggleViewSelection (TextView view1, TextView view2) {

        if (view1.getCurrentTextColor() == soundOnTextColor) {

            view1.setSelected(false);
            view1.setTextColor(soundOffTextColor);
            view1.setCompoundDrawableTintList(ColorStateList.valueOf(soundOffTextColor));
            view1.setBackgroundColor(soundOffBgColor);

            view2.setSelected(true);
            view2.setTextColor(soundOnTextColor);
            view2.setCompoundDrawableTintList(ColorStateList.valueOf(soundOnTextColor));
            view2.setBackgroundColor(soundOnBgColor);

        } else {

            view2.setSelected(false);
            view2.setTextColor(soundOffTextColor);
            view2.setCompoundDrawableTintList(ColorStateList.valueOf(soundOffTextColor));
            view2.setBackgroundColor(soundOffBgColor);

            view1.setSelected(true);
            view1.setTextColor(soundOnTextColor);
            view1.setCompoundDrawableTintList(ColorStateList.valueOf(soundOnTextColor));
            view1.setBackgroundColor(soundOnBgColor);

        }

    }

    public void showAtTop (View anchorView) {

        if (this.popupWindow == null) {
            MakeLog.error(TAG, "PopupWindow is NULL");
            return;
        }

        if (this.view == null) {
            MakeLog.error(TAG, "HolderView is NULL");
            return;
        }

        if (anchorView == null) {
            MakeLog.error(TAG, "AnchorView is NULL");
            return;
        }

        popupWindow.showAsDropDown(anchorView);
        popupWindow.setOverlapAnchor(true);
        popupWindow.setAttachedInDecor(true);
        popupWindow.showAtLocation(this.view, Gravity.NO_GRAVITY, 0, 0);
        DimPopupWindow.dimBehind(popupWindow);
        MakeLog.info(TAG, "Popup is Showing.");

    }


    public void show (View anchorView) {

        if (this.popupWindow == null) {
            MakeLog.error(TAG, "PopupWindow is NULL");
            return;
        }

        if (this.view == null) {
            MakeLog.error(TAG, "HolderView is NULL");
            return;
        }

        if (anchorView == null) {
            MakeLog.error(TAG, "AnchorView is NULL");
            return;
        }

        popupWindow.setOverlapAnchor(true);
        popupWindow.setAttachedInDecor(true);
        popupWindow.showAtLocation(this.view, Gravity.BOTTOM, 0, 150);
        DimPopupWindow.dimBehind(popupWindow);
        MakeLog.info(TAG, "Popup is Showing.");

    }

}
