package com.law.rightive;

import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.law.rightive.utils.DefinedMethods;
import com.law.rightive.utils.FireBaseUtils;
import com.law.rightive.utils.UserUtils;

import java.util.concurrent.TimeUnit;

import club.cred.synth.views.SynthButton;

public class OTPActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private String verificationId;
    private String code;
    private String mobile_number;
    private EditText editTextOTP;
    private TextView OTP_hint, resend_OTP;
    private View parentLayout;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private SynthButton continue_OTP_button;

    private FirebaseUser firebaseUser;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        mAuth = FireBaseUtils.getInstance().getFireBaseAuth();

        continue_OTP_button = findViewById(R.id.continue_OTP_button);
        editTextOTP = findViewById(R.id.input_OTP);
        OTP_hint = findViewById(R.id.OTP_hint_label);
        resend_OTP = findViewById(R.id.resend_OTP_label);

        parentLayout = findViewById(android.R.id.content);

        continue_OTP_button.setEnabled(false);
        continue_OTP_button.setTextColor(getResources().getColor(R.color.copper_text_color_30));

        mobile_number = UserUtils.getInstance().getMobileNumber();

        OTP_hint.setText("enter the 6 digit OTP sent on\r\n" + mobile_number + " to proceed");

        editTextOTP.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                boolean enabled = editTextOTP.getText().toString().length() == 6;
                if (enabled) {
                    continue_OTP_button.setEnabled(true);
                    continue_OTP_button.setTextColor(getResources().getColor(R.color.copper_text_color));
                } else {
                    continue_OTP_button.setEnabled(false);
                    continue_OTP_button.setTextColor(getResources().getColor(R.color.copper_text_color_30));
                }
            }
        });

        continue_OTP_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyCode(editTextOTP.getText().toString());
            }
        });

        resend_OTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhoneAuthOptions options =
                        PhoneAuthOptions.newBuilder(mAuth)
                                .setPhoneNumber(mobile_number)
                                .setTimeout(60L, TimeUnit.SECONDS)
                                .setActivity(OTPActivity.this)
                                .setCallbacks(mCallBack)
                                .setForceResendingToken(mResendToken)
                                .build();
                PhoneAuthProvider.verifyPhoneNumber(options);
            }
        });
    }

    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationId = s;
            mResendToken = forceResendingToken;
        }

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
            mAuth.signInWithCredential(credential)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                code = phoneAuthCredential.getSmsCode();
                                if (code != null) {
                                    editTextOTP.setText(code);
                                }
                            } else {
                                Toast.makeText(OTPActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(OTPActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    };

    private void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithCredential(credential);
    }

    private void signInWithCredential(PhoneAuthCredential credential) {
        // inside this method we are checking if
        // the code entered is correct or not.
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FireBaseUtils.getInstance().setFireBaseUser(task.getResult().getUser());
                            DefinedMethods.navigateToActivity(OTPActivity.this, ProfileActivity.class, true);
                        } else {
                            // if the code is not correct then we are
                            // displaying an error message to the user.
//                            Toast.makeText(OTPActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();

                            Snackbar snackbar = DefinedMethods.getSnackBar(parentLayout, task.getException().getMessage());
                            snackbar.show();
                        }
                    }
                });
    }

    private void sendVerificationCode() {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(mobile_number)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(OTPActivity.this)
                        .setCallbacks(mCallBack)
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    @Override
    protected void onStart() {
        super.onStart();
        sendVerificationCode();
    }
}