package com.fourshape.numbermazes.ui_views;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.LinearLayoutCompat;

import com.fourshape.numbermazes.R;
import com.fourshape.numbermazes.utils.AppColor;
import com.fourshape.numbermazes.utils.SharedData;
import com.google.android.material.card.MaterialCardView;

public class ShareGameView {

    public static View getView (Context context, Bitmap mazePuzzleBitmap) {

        View view = LayoutInflater.from(context).inflate(R.layout.view_share_game, null);

        LinearLayoutCompat parent = view.findViewById(R.id.parent_ll);
        View dividerView1 = view.findViewById(R.id.divider_view_1);
        TextView appTitleTextView = view.findViewById(R.id.app_title_tv);
        ImageView puzzleIV = view.findViewById(R.id.puzzle_iv);
        MaterialCardView appIconContainerCV = view.findViewById(R.id.app_icon_container_cv);

        AppColor appColor = new AppColor();
        appColor.setThemeId(new SharedData(context).getAppCurrentTheme());

        parent.setBackgroundColor(context.getColor(appColor.getShareViewBackgroundColor()));
        dividerView1.setBackgroundColor(context.getColor(appColor.getNormalDividerColor()));
        appTitleTextView.setTextColor(context.getColor(appColor.getShareViewAppTitleTextColor()));

        if (mazePuzzleBitmap != null) {
            puzzleIV.setImageBitmap(mazePuzzleBitmap);
        }

        return view;

    }

}
