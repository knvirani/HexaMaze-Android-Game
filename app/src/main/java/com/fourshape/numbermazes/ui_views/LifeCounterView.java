package com.fourshape.numbermazes.ui_views;

import android.content.res.ColorStateList;
import android.widget.ImageView;

import com.fourshape.numbermazes.R;
import com.fourshape.numbermazes.maze_core.structure.GameLives;
import com.fourshape.numbermazes.utils.AppColor;
import com.fourshape.numbermazes.utils.SharedData;

public class LifeCounterView {

    private ImageView lifeIV1, lifeIV2, lifeIV3;
    private AppColor appColor;
    private int currentTotalLife = -1;

    public LifeCounterView (ImageView lifeIV1, ImageView lifeIV2, ImageView lifeIV3) {
        this.lifeIV1 = lifeIV1;
        this.lifeIV2 = lifeIV2;
        this.lifeIV3 = lifeIV3;
        appColor = new AppColor();
    }

    public void refreshViewColor () {
        appColor.setThemeId(new SharedData(lifeIV1.getContext()).getAppCurrentTheme());
        if (currentTotalLife == 3) {
            this.lifeIV1.setImageTintList(ColorStateList.valueOf(this.lifeIV1.getContext().getColor(appColor.getAvailableLifeIconTintColor())));
            this.lifeIV2.setImageTintList(ColorStateList.valueOf(this.lifeIV1.getContext().getColor(appColor.getAvailableLifeIconTintColor())));
            this.lifeIV3.setImageTintList(ColorStateList.valueOf(this.lifeIV1.getContext().getColor(appColor.getAvailableLifeIconTintColor())));
        } else if (currentTotalLife == 2) {
            this.lifeIV1.setImageTintList(ColorStateList.valueOf(this.lifeIV1.getContext().getColor(appColor.getAvailableLifeIconTintColor())));
            this.lifeIV2.setImageTintList(ColorStateList.valueOf(this.lifeIV1.getContext().getColor(appColor.getAvailableLifeIconTintColor())));
            this.lifeIV3.setImageTintList(ColorStateList.valueOf(this.lifeIV1.getContext().getColor(appColor.getLostLifeIconTintColor())));
        } else if (currentTotalLife == 1) {
            this.lifeIV1.setImageTintList(ColorStateList.valueOf(this.lifeIV1.getContext().getColor(appColor.getAvailableLifeIconTintColor())));
            this.lifeIV2.setImageTintList(ColorStateList.valueOf(this.lifeIV1.getContext().getColor(appColor.getLostLifeIconTintColor())));
            this.lifeIV3.setImageTintList(ColorStateList.valueOf(this.lifeIV1.getContext().getColor(appColor.getLostLifeIconTintColor())));
        } else if (currentTotalLife == 0) {
            this.lifeIV1.setImageTintList(ColorStateList.valueOf(this.lifeIV1.getContext().getColor(appColor.getLostLifeIconTintColor())));
            this.lifeIV2.setImageTintList(ColorStateList.valueOf(this.lifeIV1.getContext().getColor(appColor.getLostLifeIconTintColor())));
            this.lifeIV3.setImageTintList(ColorStateList.valueOf(this.lifeIV1.getContext().getColor(appColor.getLostLifeIconTintColor())));
        }
    }

    public void setLife (int currentTotalLife) {

        this.currentTotalLife = currentTotalLife;
        appColor.setThemeId(new SharedData(lifeIV1.getContext()).getAppCurrentTheme());

        if (currentTotalLife == 3) {
            this.lifeIV1.setImageTintList(ColorStateList.valueOf(this.lifeIV1.getContext().getColor(appColor.getAvailableLifeIconTintColor())));
            this.lifeIV2.setImageTintList(ColorStateList.valueOf(this.lifeIV1.getContext().getColor(appColor.getAvailableLifeIconTintColor())));
            this.lifeIV3.setImageTintList(ColorStateList.valueOf(this.lifeIV1.getContext().getColor(appColor.getAvailableLifeIconTintColor())));
        } else if (currentTotalLife == 2) {
            this.lifeIV1.setImageTintList(ColorStateList.valueOf(this.lifeIV1.getContext().getColor(appColor.getAvailableLifeIconTintColor())));
            this.lifeIV2.setImageTintList(ColorStateList.valueOf(this.lifeIV1.getContext().getColor(appColor.getAvailableLifeIconTintColor())));
            this.lifeIV3.setImageTintList(ColorStateList.valueOf(this.lifeIV1.getContext().getColor(appColor.getLostLifeIconTintColor())));
        } else if (currentTotalLife == 1) {
            this.lifeIV1.setImageTintList(ColorStateList.valueOf(this.lifeIV1.getContext().getColor(appColor.getAvailableLifeIconTintColor())));
            this.lifeIV2.setImageTintList(ColorStateList.valueOf(this.lifeIV1.getContext().getColor(appColor.getLostLifeIconTintColor())));
            this.lifeIV3.setImageTintList(ColorStateList.valueOf(this.lifeIV1.getContext().getColor(appColor.getLostLifeIconTintColor())));
        } else if (currentTotalLife == 0) {
            this.lifeIV1.setImageTintList(ColorStateList.valueOf(this.lifeIV1.getContext().getColor(appColor.getLostLifeIconTintColor())));
            this.lifeIV2.setImageTintList(ColorStateList.valueOf(this.lifeIV1.getContext().getColor(appColor.getLostLifeIconTintColor())));
            this.lifeIV3.setImageTintList(ColorStateList.valueOf(this.lifeIV1.getContext().getColor(appColor.getLostLifeIconTintColor())));
        }

    }

}
