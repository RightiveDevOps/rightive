package com.law.rightive;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.law.rightive.utils.DefinedMethods;
import com.law.rightive.utils.FireBaseUtils;
import com.law.rightive.utils.UserUtils;

import club.cred.synth.views.SynthButton;

public class MainActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView singOutText = findViewById(R.id.signOut_textField);

        FirebaseUser firebaseUser = FireBaseUtils.getInstance().getFirebaseUser();
        if (firebaseUser != null) {
            if (UserUtils.getInstance().getUserType() == 1001) {
                if (!firebaseUser.isEmailVerified()) {
                    DefinedMethods.navigateToActivity(MainActivity.this, ProfileActivity.class, true);
                } else {
                    DefinedMethods.navigateToActivity(MainActivity.this, WelcomeLawyerActivity.class, true);
                }
            } else {
                DefinedMethods.navigateToActivity(MainActivity.this, WelcomeClientActivity.class, true);
            }
        }
        SynthButton synthButton_getStarted = findViewById(R.id.get_started_button);
        synthButton_getStarted.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                DefinedMethods.navigateToActivity(MainActivity.this, SignUpActivity.class, true);

//                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);

            }
        });

        singOutText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                FireBaseUtils.getInstance().getFireBaseAuth().signOut();
                DefinedMethods.getSnackBar(v, "Sign out successful!").show();
            }
        });
    }
}