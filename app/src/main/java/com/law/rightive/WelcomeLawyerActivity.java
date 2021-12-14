package com.law.rightive;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.law.rightive.utils.DefinedMethods;
import com.law.rightive.utils.FireBaseUtils;
import com.law.rightive.utils.UserUtils;

import java.util.Objects;

@RequiresApi(api = Build.VERSION_CODES.O)
public class WelcomeLawyerActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_lawyer);
        FirebaseAuth auth = FireBaseUtils.getInstance().getFireBaseAuth();
        Intent intent = getIntent();
        String emailLink = intent.getData() != null ? intent.getData().toString() : "";
        String email = UserUtils.getInstance().getEmail();
        AuthCredential credential = EmailAuthProvider.getCredentialWithLink(email, emailLink);

        if (auth.isSignInWithEmailLink(emailLink)) {
            Objects.requireNonNull(auth.getCurrentUser()).linkWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), "Email successfully verified", Toast.LENGTH_SHORT).show();
                        AuthResult result = task.getResult();
                        FireBaseUtils.getInstance().addLawyerToFireStore(getWindow().getDecorView().getRootView());
                    } else {
                        Toast.makeText(getApplicationContext(), "Error Occurred : " + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}