package com.law.rightive;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.law.rightive.fragments.LawyerVerificationFragment;
import com.law.rightive.utils.DefinedMethods;
import com.law.rightive.utils.FireBaseUtils;
import com.law.rightive.utils.StringUtils;
import com.law.rightive.utils.UserUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import club.cred.synth.views.SynthButton;

public class ProfileActivity extends AppCompatActivity {

    private EditText first;
    private EditText last;
    private RadioGroup radio_group;
    private RadioButton radio_client;
    private RadioButton radio_lawyer;
    private RadioButton radio_userId;
    private EditText emailEditText;
    private CheckBox terms_conditions;
    private SynthButton agree_continue_button;
    private LottieAnimationView emailSent;

    private String userTypeString;
    private int selectedId;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        first = findViewById(R.id.first);
        last = findViewById(R.id.last);
        radio_group = findViewById(R.id.radio_group);
        emailEditText = findViewById(R.id.email);
        terms_conditions = findViewById(R.id.terms_conditions);

        emailSent = findViewById(R.id.emailSent);

        agree_continue_button = findViewById(R.id.welcome);
        agree_continue_button.setEnabled(false);
        agree_continue_button.setTextColor(getResources().getColor(R.color.copper_text_color_30));

        radio_client = findViewById(R.id.radio_client);
        radio_lawyer = findViewById(R.id.radio_lawyer);

        radio_client.setId(UserUtils.CLIENT);
        radio_lawyer.setId(UserUtils.LAWYER);

        radio_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                selectedId = checkedId;
                radio_userId = (RadioButton) findViewById(checkedId);
                userTypeString = radio_userId != null ? radio_userId.getText().toString() : "";
                emailEditText.setVisibility(View.GONE);
                if (checkedId == 1001) {
                    emailEditText.setVisibility(View.VISIBLE);
                }
            }
        });

        first.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                agree_continue_button.setEnabled(false);
                if (first.getText().length() > 0 && terms_conditions.isChecked()) {
                    agree_continue_button.setEnabled(true);
                    agree_continue_button.setTextColor(getResources().getColor(R.color.copper_text_color));
                } else {
                    agree_continue_button.setEnabled(false);
                    agree_continue_button.setTextColor(getResources().getColor(R.color.copper_text_color_30));
                }
            }
        });
        emailEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                agree_continue_button.setEnabled(false);
                if (emailEditText.getText().length() > 0 && terms_conditions.isChecked()) {
                    agree_continue_button.setEnabled(true);
                    agree_continue_button.setTextColor(getResources().getColor(R.color.copper_text_color));
                } else {
                    agree_continue_button.setEnabled(false);
                    agree_continue_button.setTextColor(getResources().getColor(R.color.copper_text_color_30));
                }
            }
        });
        terms_conditions.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                boolean flag = false;
                if (isChecked) {
                    if ((first.getText().length() > 0) && (radio_group.getCheckedRadioButtonId() != 0)) {
                        flag = true;
                        if (selectedId == 1001) {
                            flag = emailEditText.getText().length() > 0;
                        }
                    }
                }
                if (flag) {
                    agree_continue_button.setEnabled(true);
                    agree_continue_button.setTextColor(getResources().getColor(R.color.copper_text_color));
                } else {
                    agree_continue_button.setEnabled(false);
                    agree_continue_button.setTextColor(getResources().getColor(R.color.copper_text_color_30));
                }
            }
        });

        agree_continue_button.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                setUserInContext();
                if (UserUtils.getInstance().getUserType() == 1001) {
                    if (emailEditText.getText().length() > 0 && !android.util.Patterns.EMAIL_ADDRESS.matcher(emailEditText.getText()).matches()) {
                        Snackbar snackbar = DefinedMethods.getSnackBar(v, "Please enter a valid email address");
                        snackbar.setAction("OK", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                snackbar.dismiss();
                            }
                        }).show();
                    } else {
                        if (FireBaseUtils.getInstance().getFirebaseUser() != null && !FireBaseUtils.getInstance().getFirebaseUser().isEmailVerified()) {
                            LawyerVerificationFragment lawyerVerificationFragment = new LawyerVerificationFragment();
                            lawyerVerificationFragment.show(getSupportFragmentManager(), "Lawyer Verification Drawer");
                            lawyerVerificationFragment.setCancelable(false);
                        } else {
                            DefinedMethods.navigateToActivity(ProfileActivity.this, LawyerDetails.class, true);
                        }
                    }
                } else {
                    FireBaseUtils.getInstance().addClientToFireStore(v);
                    DefinedMethods.navigateToActivity(ProfileActivity.this, WelcomeClientActivity.class, true);
                }
            }
        });
    }

    private void setUserInContext() {
        UserUtils.getInstance().setEmail(emailEditText.getText().toString());
        UserUtils.getInstance().setUserType(selectedId);
        UserUtils.getInstance().setFirstName(first.getText().toString());
        UserUtils.getInstance().setLastName(last.getText().toString());
        UserUtils.getInstance().setUserTypeString(userTypeString);
        UserUtils.getInstance().setEmail(emailEditText.getText().toString());
    }


}