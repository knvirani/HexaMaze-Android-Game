package com.fourshape.numbermazes.fragments.howtoplay;

import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fourshape.numbermazes.R;
import com.fourshape.numbermazes.fragments.FragmentTitle;
import com.fourshape.numbermazes.utils.AppColor;
import com.fourshape.numbermazes.utils.FormattedData;
import com.fourshape.numbermazes.utils.SharedData;
import com.google.android.material.card.MaterialCardView;

public class Step1Fragment extends Fragment {

    private View mainView;

    private ConstraintLayout parent;
    private TextView headerTV, descTV, nextStepTV;
    private MaterialCardView nextStepCV;

    private AppColor appColor;

    public Step1Fragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mainView = inflater.inflate(R.layout.fragment_step1, container, false);

        parent = mainView.findViewById(R.id.parent_cl);
        headerTV = mainView.findViewById(R.id.content_header_tv);
        descTV = mainView.findViewById(R.id.content_desc_tv);
        nextStepCV = mainView.findViewById(R.id.next_step_cv);
        nextStepTV = mainView.findViewById(R.id.next_step_tv);

        if (getContext() == null)
            return mainView;

        appColor = new AppColor();
        appColor.setThemeId(new SharedData(getContext()).getAppCurrentTheme());

        parent.setBackgroundColor(this.getContext().getColor(this.appColor.getAppBackgroundColor()));
        headerTV.setTextColor(this.getContext().getColor(this.appColor.getAppBarTextColor()));
        descTV.setTextColor(this.getContext().getColor(this.appColor.getAppBarTextColor()));
        nextStepCV.setCardBackgroundColor(this.getContext().getColor(this.appColor.getNextStepCardBackgroundColor()));
        nextStepTV.setTextColor(this.getContext().getColor(this.appColor.getNextStepTextColor()));

        nextStepCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (getActivity() != null) {
                    getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit).addToBackStack(FragmentTitle.STEP_2_FRAGMENT).replace(container.getId(), new Step2Fragment()).commit();
                }

            }
        });

        return mainView;

    }
}