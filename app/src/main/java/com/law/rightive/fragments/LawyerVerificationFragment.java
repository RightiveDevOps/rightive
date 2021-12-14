package com.law.rightive.fragments;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.law.rightive.R;
import com.law.rightive.utils.DefinedMethods;
import com.law.rightive.utils.FireBaseUtils;
import com.law.rightive.utils.UserUtils;

import java.util.Objects;

public class LawyerVerificationFragment extends BottomSheetDialogFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_lawyer_verification, container, false);
        EditText editTextEmailLabel = view.findViewById(R.id.emailDisabled);
        editTextEmailLabel.setText(UserUtils.getInstance().getEmail());

        Button sendEmailButton = view.findViewById(R.id.sendEmail);
        Button verifyEmailButton = view.findViewById(R.id.verifyEmail);

        sendEmailButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                FireBaseUtils.getInstance().getFireBaseAuth().sendSignInLinkToEmail(UserUtils.getInstance().getEmail(), FireBaseUtils.getInstance().getActionCodeSettings())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
//                                    DefinedMethods.getSnackBar(v, "Email successfully sent on email : " + UserUtils.getInstance().getEmail() + "\nPlease check your inbox").show();
                                    Toast.makeText(getActivity(), "Email successfully sent on email : " + UserUtils.getInstance().getEmail() + "\nPlease verify the email.", Toast.LENGTH_SHORT).show();
                                    sendEmailButton.setVisibility(View.GONE);
                                    verifyEmailButton.setVisibility(View.VISIBLE);
                                } else {
//                                    DefinedMethods.getSnackBar(v, "Error Occurred : \n" + Objects.requireNonNull(task.getException()).getMessage()).show();
                                    Toast.makeText(getActivity(), Objects.requireNonNull(task.getException()).getMessage()+"\nPlease check your input and try again", Toast.LENGTH_SHORT).show();
                                    dismiss();
                                }
                            }
                        });
            }
        });

        verifyEmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_APP_EMAIL);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                String title = "Open Email Application";
                Intent chooser = Intent.createChooser(intent, title);
                try {
                    startActivity(chooser);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(getActivity(), "Error Occurred: \n" + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
        return view;
    }
}