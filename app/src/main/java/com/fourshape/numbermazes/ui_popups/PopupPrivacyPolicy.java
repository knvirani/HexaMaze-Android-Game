package com.fourshape.numbermazes.ui_popups;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;

import com.fourshape.numbermazes.R;
import com.fourshape.numbermazes.listeners.OnPolicyStatusListener;
import com.fourshape.numbermazes.utils.AppColor;
import com.fourshape.numbermazes.utils.DimPopupWindow;
import com.fourshape.numbermazes.utils.MakeLog;
import com.fourshape.numbermazes.utils.OpenExternalUrl;
import com.fourshape.numbermazes.utils.SharedData;
import com.fourshape.numbermazes.utils.TestDevices;
import com.fourshape.numbermazes.utils.VariablesControl;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.ump.ConsentDebugSettings;
import com.google.android.ump.ConsentForm;
import com.google.android.ump.ConsentInformation;
import com.google.android.ump.ConsentRequestParameters;
import com.google.android.ump.FormError;
import com.google.android.ump.UserMessagingPlatform;

import org.jetbrains.annotations.NotNull;

public class PopupPrivacyPolicy {

    private static final String TAG = "PopupPrivacyPolicy";

    private PopupWindow popupWindow;
    private final Context context;
    private View view;
    private final Activity activity;

    private OnPolicyStatusListener onPolicyStatusListener;
    private ConsentInformation consentInformation;

    private AppColor appColor;

    public PopupPrivacyPolicy (Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
        appColor = new AppColor();
        appColor.setThemeId(new SharedData(context).getAppCurrentTheme());
        preparePopup();
    }

    private void preparePopup () {

        if (this.context == null) {
            MakeLog.error(TAG, "Context is NULL.");
            return;
        }

        view = LayoutInflater.from(this.context).inflate(R.layout.popup_privacy_policy, null);

        if (view == null) {
            MakeLog.error(TAG, "View is NULL.");
            return;
        }

        int width = LinearLayoutCompat.LayoutParams.WRAP_CONTENT;
        int height = LinearLayoutCompat.LayoutParams.WRAP_CONTENT;
        boolean focusable = false;

        MaterialCardView parentCV;
        LinearLayoutCompat contentCV;
        CircularProgressIndicator progressIndicator;

        parentCV = view.findViewById(R.id.parent_cv);
        contentCV = view.findViewById(R.id.content_cv);
        progressIndicator = view.findViewById(R.id.progress_circular);

        TextView policyDescTV = view.findViewById(R.id.policy_description_text);
        policyDescTV.setMovementMethod(LinkMovementMethod.getInstance());
        policyDescTV.setText(getTncLine());

        MaterialButton agreeMB, notAgreeMB;
        agreeMB = view.findViewById(R.id.agree_btn);
        notAgreeMB = view.findViewById(R.id.reject_btn);

        parentCV.setCardBackgroundColor(this.view.getContext().getColor(appColor.getPopupCardBackgroundColor()));
        contentCV.setBackgroundColor(this.view.getContext().getColor(appColor.getPopupCardBackgroundColor()));
        progressIndicator.setIndicatorColor(this.view.getContext().getColor(appColor.getProgressCircularIndicatorColor()));
        policyDescTV.setTextColor(this.view.getContext().getColor(appColor.getPopupBodyTextColor()));
        agreeMB.setBackgroundTintList(ColorStateList.valueOf(this.view.getContext().getColor(appColor.getPopupPrimaryButtonBackgroundColor())));
        agreeMB.setTextColor(this.view.getContext().getColor(appColor.getPopupPrimaryButtonTextColor()));
        notAgreeMB.setBackgroundTintList(ColorStateList.valueOf(this.view.getContext().getColor(appColor.getPopupSecondaryButtonBackgroundColor())));
        notAgreeMB.setTextColor(this.view.getContext().getColor(appColor.getPopupSecondaryButtonTextColor()));

        agreeMB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                contentCV.setVisibility(View.INVISIBLE);
                progressIndicator.setVisibility(View.VISIBLE);

                askForGDPRConsent();

            }
        });

        notAgreeMB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                popupWindow.dismiss();
                if (onPolicyStatusListener != null) {
                    new SharedData(context).policyStatus(false);
                    onPolicyStatusListener.onRejected();
                }
            }
        });

        popupWindow = new PopupWindow(view, width, height, focusable);

    }

    public void show () {

        if (this.popupWindow == null) {
            MakeLog.error(TAG, "PopupWindow is NULL");
            return;
        }

        if (this.view == null) {
            MakeLog.error(TAG, "HolderView is NULL");
            return;
        }

        popupWindow.showAtLocation(this.view, Gravity.CENTER, 0, 0);
        DimPopupWindow.dimBehind(popupWindow);
        MakeLog.info(TAG, "Popup is Showing.");

    }

    private void askForGDPRConsent(){

        if (view.getContext() == null)
            return;

        ConsentDebugSettings debugSettings = new ConsentDebugSettings.Builder(view.getContext())
                .setDebugGeography(ConsentDebugSettings.DebugGeography.DEBUG_GEOGRAPHY_EEA)
                .addTestDeviceHashedId(TestDevices.ADMOB_TEST_DEVICE)
                .build();

        // Set tag for underage of consent. false means users are not underage.
        ConsentRequestParameters params = new ConsentRequestParameters
                .Builder()
                //.setConsentDebugSettings(debugSettings)
                .setTagForUnderAgeOfConsent(false)
                .build();

        if (view.getContext() == null) {
            afterFormActionStuff();
            return;
        }

        if (consentInformation == null)
            consentInformation = UserMessagingPlatform.getConsentInformation(view.getContext());

        if (this.activity == null) {
            afterFormActionStuff();
            return;
        }

        consentInformation.requestConsentInfoUpdate(this.activity, params, new ConsentInformation.OnConsentInfoUpdateSuccessListener() {
            @Override
            public void onConsentInfoUpdateSuccess() {

                if (consentInformation.isConsentFormAvailable()) {
                    // load form here.
                    MakeLog.info(TAG, "Consent Form is Available");
                    loadForm();
                } else {
                    MakeLog.info(TAG, "Consent Form is not Available");
                    if (onPolicyStatusListener != null) {
                        popupWindow.dismiss();
                        onPolicyStatusListener.onAccepted();
                    }
                }

            }
        }, new ConsentInformation.OnConsentInfoUpdateFailureListener() {
            @Override
            public void onConsentInfoUpdateFailure(@NonNull @NotNull FormError formError) {

                MakeLog.info(TAG, "Consent form error: " + formError.getMessage());
                MakeLog.info(TAG, "Consent form error code: " + formError.getErrorCode());

                afterFormActionStuff();

            }
        });

    }

    private void afterFormActionStuff () {
        if (onPolicyStatusListener != null) {
            popupWindow.dismiss();
            new SharedData(context).policyStatus(true);
            onPolicyStatusListener.onAccepted();
        }
    }

    private void loadForm() {

        if (view.getContext() == null) {
            afterFormActionStuff();
            return;
        }

        UserMessagingPlatform.loadConsentForm(view.getContext(), new UserMessagingPlatform.OnConsentFormLoadSuccessListener() {
            @Override
            public void onConsentFormLoadSuccess(@NonNull @NotNull ConsentForm consentForm) {

                if (consentInformation.getConsentStatus() == ConsentInformation.ConsentStatus.REQUIRED) {

                    if (activity == null) {
                        afterFormActionStuff();
                        return;
                    }

                    consentForm.show(activity, new ConsentForm.OnConsentFormDismissedListener() {
                        @Override
                        public void onConsentFormDismissed(@Nullable @org.jetbrains.annotations.Nullable FormError formError) {
                            // handle the dismissal by re-loading the form.
                            loadForm();
                        }
                    });
                } else if (consentInformation.getConsentStatus() ==  ConsentInformation.ConsentStatus.OBTAINED || consentInformation.getConsentStatus() == ConsentInformation.ConsentStatus.NOT_REQUIRED){

                    afterFormActionStuff();

                } else {
                    // do nothing with unknown status.
                    afterFormActionStuff();
                }

            }
        }, new UserMessagingPlatform.OnConsentFormLoadFailureListener() {
            @Override
            public void onConsentFormLoadFailure(@NonNull @NotNull FormError formError) {
                afterFormActionStuff();
            }
        });

    }


    public PopupPrivacyPolicy setOnPolicyStatusListener (OnPolicyStatusListener onPolicyStatusListener) {
        this.onPolicyStatusListener = onPolicyStatusListener;
        return this;
    }

    private SpannableString getTncLine () {

        String originalString = "By using this app, you're agree with our privacy policy and TnC. Read full policy here.";

        String partToClick = "Read full policy here";

        int startPos = originalString.indexOf(partToClick);
        int endPos = originalString.lastIndexOf(partToClick) + partToClick.length();

        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                MakeLog.info(TAG, "Clicked");
                OpenExternalUrl.open(PopupPrivacyPolicy.this.view.getContext(), view, VariablesControl.PRIVACY_POLICY_URL);
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(context.getColor(appColor.getPopupLinkColor()));
            }
        };

        SpannableString spannableString = new SpannableString(originalString);
        spannableString.setSpan(clickableSpan, startPos, endPos, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return spannableString;

    }

}
