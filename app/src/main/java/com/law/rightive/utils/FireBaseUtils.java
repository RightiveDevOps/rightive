package com.law.rightive.utils;
/*
 * SUCCESS = 0
 * FAILURE = 1
 * */

import android.os.Build;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.O)
public class FireBaseUtils extends AppCompatActivity {

    private static FireBaseUtils fireBaseUtils = null;

    private FireBaseUtils() {
    }

    public static FireBaseUtils getInstance() {
        if (fireBaseUtils == null) {
            fireBaseUtils = new FireBaseUtils();
        }
        return fireBaseUtils;
    }

    private final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private final FirebaseAuth fireBaseAuth = FirebaseAuth.getInstance();
    private FirebaseUser firebaseUser = fireBaseAuth.getCurrentUser();

    private final FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = null;

    public FirebaseFirestore getFirebaseFirestore() {
        return firebaseFirestore;
    }

    public FirebaseDatabase getFirebaseDatabase() {
        return firebaseDatabase;
    }

    public ActionCodeSettings getActionCodeSettings() {
        return actionCodeSettings;
    }

    ActionCodeSettings actionCodeSettings = ActionCodeSettings.newBuilder()
            .setUrl("https://rightive-legal.firebaseapp.com/finishSignUp?cartId=1234")
            .setHandleCodeInApp(true)
            .setIOSBundleId("com.rightive.ios")
            .setAndroidPackageName(
                    "com.rightive.android",
                    true,
                    "12")
            .build();


    public FirebaseUser getFireBaseUser() {
        return firebaseUser;
    }

    public void setFireBaseUser(FirebaseUser firebaseUser) {
        this.firebaseUser = firebaseUser;
    }

    public FirebaseAuth getFireBaseAuth() {
        return fireBaseAuth;
    }

    public List<Object> addDataToFireBaseDB() {

        Task<DocumentReference> collectionReference = firebaseFirestore.collection("USERS").add(UserUtils.getInstance().getUserDetailsMap())
                .addOnSuccessListener(documentReference -> {
                    List<Object> addResult = new ArrayList<>();

                })
                .addOnFailureListener(e -> {

                });

//        collectionReference.getResult();
        return null;
    }

    public void addClientToFireStore(View view) {
        collectionReference = firebaseFirestore.collection(StringUtils.FIRESTORE_CLIENT);
        collectionReference
                .document(FireBaseUtils.getInstance().getFireBaseUser().getUid())
                .set(UserUtils.getInstance().getUserDetailsMap())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(@NonNull Void unused) {
                        DefinedMethods.getSnackBar(view, "User added successfully").show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        DefinedMethods.getSnackBar(view, e.getMessage()).show();
                    }
                });
    }

    public void addLawyerToFireStore(View view) {
        collectionReference = firebaseFirestore.collection(StringUtils.FIRESTORE_LAWYER);
        collectionReference
                .document(FireBaseUtils.getInstance().getFireBaseUser().getUid())
                .set(UserUtils.getInstance().getUserDetailsMap())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(@NonNull Void unused) {
                        DefinedMethods.getSnackBar(view, "User added successfully").show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        DefinedMethods.getSnackBar(view, e.getMessage()).show();
                    }
                });
    }

    public void addLawyerDetailsToFireStore(View view) {
        collectionReference = firebaseFirestore.collection(StringUtils.FIRESTORE_LAWYER);
        collectionReference
                .document("rightive@gmail.com") //FireBaseUtils.getInstance().getFireBaseUser().getUid()
                .set(UserUtils.getInstance().getTestLawyerMap())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(@NonNull Void unused) {
                        DefinedMethods.getSnackBar(view, "Details updated successfully").show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                DefinedMethods.getSnackBar(view, e.getMessage()).show();
            }
        });
    }

    public void addSpecializationsToFireStore(List<String> allAddedSpecializations, View view) {
        collectionReference = firebaseFirestore.collection(StringUtils.FIRESTORE_SPECIALIZATIONS);
        collectionReference
                .document(StringUtils.FIRESTORE_SPECIALIZATIONS_DOC)
                .update(StringUtils.FIRESTORE_SPECIALIZATIONS_SPECS, allAddedSpecializations);
    }

    public void addCaseToFireStore(View view) {
    }

    public void addEventToFireStore(View view) {
    }

    public void addNotificationToFireStore(View view) {
    }
}
