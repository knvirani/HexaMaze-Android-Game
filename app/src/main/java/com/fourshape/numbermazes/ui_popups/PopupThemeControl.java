package com.fourshape.numbermazes.ui_popups;

import android.content.Context;
import android.content.res.ColorStateList;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.fourshape.numbermazes.R;
import com.fourshape.numbermazes.listeners.OnThemeChangeListener;
import com.fourshape.numbermazes.utils.AppColor;
import com.fourshape.numbermazes.utils.AppThemes;
import com.fourshape.numbermazes.utils.DimPopupWindow;
import com.fourshape.numbermazes.utils.MakeLog;
import com.fourshape.numbermazes.utils.SharedData;
import com.google.android.material.card.MaterialCardView;

public class PopupThemeControl {

    private static final String TAG = "PopupThemeControl";

    private PopupWindow popupWindow;
    private final Context context;
    private View view;
    private boolean shouldDismissDialog = false;

    private MaterialCardView parentCV, theme1CV, theme2CV, innerParentCV;
    ImageView theme1Image, theme2Image;

    private OnThemeChangeListener onThemeChangeListener;
    private AppColor appColor;

    public PopupThemeControl (Context context) {
        this.context = context;
        preparePopup();

    }

    public PopupThemeControl setOnThemeChangeListener (OnThemeChangeListener onThemeChangeListener) {
        this.onThemeChangeListener = onThemeChangeListener;
        return this;
    }

    private void refreshViewColors () {

        appColor.setThemeId(new SharedData(this.context).getAppCurrentTheme());

        parentCV.setCardBackgroundColor(this.view.getContext().getColor(appColor.getPopupCardBackgroundColor()));
        innerParentCV.setCardBackgroundColor(this.view.getContext().getColor(appColor.getPopupCardBackgroundColor()));
        innerParentCV.setStrokeColor(this.view.getContext().getColor(appColor.getSelectedItemBackgroundColor()));

        clearTheme(theme1Image);
        clearTheme(theme2Image);

        if (new SharedData(this.context).getAppCurrentTheme() == AppThemes.DAY) {
            selectTheme(theme1Image);
        } else {
            selectTheme(theme2Image);
        }

    }

    private void preparePopup () {

        if (this.context == null) {
            MakeLog.error(TAG, "Context is NULL.");
            return;
        }

        view = LayoutInflater.from(this.context).inflate(R.layout.popup_view_theme_control, null);

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

        parentCV = view.findViewById(R.id.parent_cv);
        innerParentCV = view.findViewById(R.id.inner_parent_cv);
        theme1Image = view.findViewById(R.id.theme_1_img);
        theme2Image = view.findViewById(R.id.theme_2_img);
        theme1CV = view.findViewById(R.id.theme_1);
        theme2CV = view.findViewById(R.id.theme_2);

        refreshViewColors();

        theme1CV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (shouldDismissDialog)
                    popupWindow.dismiss();

                if (new SharedData(view.getContext()).getAppCurrentTheme() != AppThemes.DAY) {
                    clearTheme(theme2Image);
                    selectTheme(theme1Image);
                    new SharedData(view.getContext()).setCurrentAppTheme(AppThemes.DAY);

                    refreshViewColors();

                    if (onThemeChangeListener != null) {
                        onThemeChangeListener.onChange();
                    }
                }

            }
        });

        theme2CV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (shouldDismissDialog)
                    popupWindow.dismiss();

                if (new SharedData(view.getContext()).getAppCurrentTheme() != AppThemes.DARK) {

                    clearTheme(theme1Image);
                    selectTheme(theme2Image);
                    new SharedData(view.getContext()).setCurrentAppTheme(AppThemes.DARK);

                    refreshViewColors();

                    if (onThemeChangeListener != null) {
                        onThemeChangeListener.onChange();
                    }

                }

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

    private void selectTextViewDecoration (TextView textView) {
        textView.setTextColor(textView.getContext().getColor(appColor.getSelectedItemTextColor()));
        textView.setBackgroundColor(textView.getContext().getColor(appColor.getSelectedItemBackgroundColor()));
    }

    private void clearTextViewDecoration (TextView textView) {
        textView.setTextColor(textView.getContext().getColor(appColor.getUnselectedItemTextColor()));
        textView.setBackgroundColor(textView.getContext().getColor(appColor.getUnselectedItemBackgroundColor()));
    }

    private void selectTheme (ImageView imageView) {
        imageView.setBackgroundColor(imageView.getContext().getColor(appColor.getSelectedItemBackgroundColor()));
        imageView.setImageTintList(ColorStateList.valueOf(imageView.getContext().getColor(appColor.getSelectedItemTextColor())));
    }

    private void clearTheme (ImageView imageView) {
        imageView.setBackgroundColor(imageView.getContext().getColor(appColor.getUnselectedItemBackgroundColor()));
        imageView.setImageTintList(ColorStateList.valueOf(imageView.getContext().getColor(appColor.getUnselectedItemTextColor())));
    }

    public void showAtTop (View anchorView) {

        shouldDismissDialog = true;

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
        popupWindow.showAtLocation(this.view, Gravity.NO_GRAVITY, 0, 0);
        DimPopupWindow.dimBehind(popupWindow);
        MakeLog.info(TAG, "Popup is Showing.");

    }

    public void show (View anchorView) {

        shouldDismissDialog = false;

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
        DimPopupWindow.lightDimBehind(popupWindow);
        MakeLog.info(TAG, "Popup is Showing.");

    }

}
