package com.law.rightive;

import android.os.Build;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.law.rightive.utils.DefinedMethods;
import com.law.rightive.utils.UserUtils;

import club.cred.synth.views.SynthButton;

public class SignUpActivity extends AppCompatActivity {

    private SynthButton continue_button;
    private EditText input_mobile_number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        continue_button = findViewById(R.id.welcome);
        input_mobile_number = findViewById(R.id.input_OTP);

        continue_button.setEnabled(false);
        continue_button.setTextColor(getResources().getColor(R.color.copper_text_color_30));

        input_mobile_number.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                boolean enabled = input_mobile_number.getText().toString().length() == 10;
                if (enabled) {
                    continue_button.setEnabled(true);
                    continue_button.setTextColor(getResources().getColor(R.color.copper_text_color));
                } else {
                    continue_button.setEnabled(false);
                    continue_button.setTextColor(getResources().getColor(R.color.copper_text_color_30));
                }
            }
        });


        continue_button.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                UserUtils.getInstance().setMobileNumber(input_mobile_number.getText().toString());
                if(PhoneNumberUtils.isGlobalPhoneNumber(UserUtils.getInstance().getMobileNumber()))
                {
                    DefinedMethods.navigateToActivity(SignUpActivity.this, OTPActivity.class, false);
                }
                else
                {
                    Snackbar snackbar = DefinedMethods.getSnackBar(v, "Please enter a correct phone number");
                    snackbar.show();
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}