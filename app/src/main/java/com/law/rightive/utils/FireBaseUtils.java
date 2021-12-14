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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiresApi(api = Build.VERSION_CODES.O)
public class FireBaseUtils extends AppCompatActivity {

    private static FireBaseUtils fireBaseUtils = null;


    private final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private final FirebaseAuth fireBaseAuth = FirebaseAuth.getInstance();
    private FirebaseUser firebaseUser = fireBaseAuth.getCurrentUser();

    private final FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = null;

    public FirebaseFirestore getFirebaseFirestore() {
        return firebaseFirestore;
    }

    private FireBaseUtils() {
    }

    public FirebaseDatabase getFirebaseDatabase() {
        return firebaseDatabase;
    }

    public ActionCodeSettings getActionCodeSettings() {
        return actionCodeSettings;
    }

    ActionCodeSettings actionCodeSettings =
            ActionCodeSettings.newBuilder()
                    // URL you want to redirect back to. The domain (www.example.com) for this
                    // URL must be whitelisted in the Firebase Console.
                    .setUrl("https://rightive-legal.firebaseapp.com/finishSignUp?cartId=1234")
                    // This must be true
                    .setHandleCodeInApp(true)
                    .setIOSBundleId("com.rightive.ios")
                    .setAndroidPackageName(
                            "com.rightive.android",
                            true, /* installIfNotAvailable */
                            "12"    /* minimumVersion */)
                    .build();


    public FirebaseUser getFirebaseUser() {
        return firebaseUser;
    }

    public void setFireBaseUtils(FirebaseUser firebaseUser) {
        this.firebaseUser = firebaseUser;
    }

    public FirebaseAuth getFireBaseAuth() {
        return fireBaseAuth;
    }

    public static FireBaseUtils getInstance() {
        if (fireBaseUtils == null) {
            fireBaseUtils = new FireBaseUtils();
        }
        return fireBaseUtils;
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
        collectionReference.document(FireBaseUtils.getInstance().getFirebaseUser().getUid()).set(UserUtils.getInstance().getUserDetailsMap()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(@NonNull Void unused) {
                DefinedMethods.getSnackBar(view, "User added successfully").show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                DefinedMethods.getSnackBar(view, e.getMessage()).show();
            }
        });
    }

    public void addLawyerToFireStore(View view) {
        collectionReference = firebaseFirestore.collection(StringUtils.FIRESTORE_LAWYER);
        collectionReference.document(FireBaseUtils.getInstance().getFirebaseUser().getUid()).set(UserUtils.getInstance().getUserDetailsMap()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(@NonNull Void unused) {
                DefinedMethods.getSnackBar(view, "User added successfully").show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                DefinedMethods.getSnackBar(view, e.getMessage()).show();
            }
        });
    }

    public void addCaseToFireStore(View view) {
    }

    public void addEventToFireStore(View view) {
    }

    public void addNotificationToFireStore(View view) {
    }
}
