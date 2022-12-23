package com.fourshape.numbermazes.fragments.howtoplay;

import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fourshape.numbermazes.R;
import com.fourshape.numbermazes.utils.AppColor;
import com.fourshape.numbermazes.utils.FormattedData;
import com.fourshape.numbermazes.utils.SharedData;

public class Step2Fragment extends Fragment {

    private View mainView;

    private TextView headerTV, descTV;
    private AppColor appColor;
    private ConstraintLayout parent;

    public Step2Fragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mainView = inflater.inflate(R.layout.fragment_step2, container, false);

        headerTV = mainView.findViewById(R.id.content_header_tv);
        descTV = mainView.findViewById(R.id.content_desc_tv);
        parent = mainView.findViewById(R.id.parent_cl);

        if (getContext() == null)
            return mainView;

        appColor = new AppColor();
        appColor.setThemeId(new SharedData(getContext()).getAppCurrentTheme());

        parent.setBackgroundColor(this.getContext().getColor(this.appColor.getAppBackgroundColor()));
        headerTV.setTextColor(this.getContext().getColor(this.appColor.getAppBarTextColor()));
        descTV.setTextColor(this.getContext().getColor(this.appColor.getAppBarTextColor()));

        return mainView;

    }
}