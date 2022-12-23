package com.fourshape.numbermazes;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.fourshape.numbermazes.utils.MakeLog;
import com.fourshape.numbermazes.utils.OpenExternalUrl;
import com.fourshape.numbermazes.utils.ScreenConfiguration;
import com.fourshape.numbermazes.utils.SharedData;
import com.fourshape.numbermazes.utils.VariablesControl;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;

import de.cketti.mailto.EmailIntentBuilder;

public class ContactActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);

        ScreenConfiguration.set(this, this);

        //TrackScreen.now(this, "ContactActivity");

        setContentView(R.layout.activity_contact);

        MaterialToolbar materialToolbar = findViewById(R.id.material_toolbar);

        materialToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        TextInputEditText subjectEditText, messageEditText;
        subjectEditText = findViewById(R.id.email_subject);
        messageEditText = findViewById(R.id.email_message);

        MaterialButton submitMB = findViewById(R.id.submit_feedback);

        MaterialCardView privacyPolicyCV = findViewById(R.id.privacy_policy_cv);

        privacyPolicyCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenExternalUrl.open(view.getContext(), view, VariablesControl.PRIVACY_POLICY_URL);
            }
        });

        submitMB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String subject = (subjectEditText.getText() == null) ? null: subjectEditText.getText().toString();
                String message = (messageEditText.getText() == null) ? null: messageEditText.getText().toString();

                if (subject != null && message != null) {

                    if (subject.length() > 0 && message.length() > 0) {

                        try {

                            EmailIntentBuilder.from(view.getContext())
                                    .to(VariablesControl.CONTACT_EMAIL)
                                    .subject(subject)
                                    .body(message)
                                    .start();

                        } catch (Exception e) {
                            MakeLog.exception(e);
                        }

                    }

                }

            }
        });

    }

}