package com.law.rightive;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.law.rightive.utils.ChipUtils;
import com.law.rightive.utils.DefinedMethods;
import com.law.rightive.utils.FireBaseUtils;
import com.law.rightive.utils.StringUtils;
import com.law.rightive.utils.UserUtils;

import java.util.ArrayList;
import java.util.List;

import club.cred.synth.views.SynthButton;
import club.cred.synth.views.SynthImageButton;

@RequiresApi(api = Build.VERSION_CODES.M)
public class LawyerDetails extends AppCompatActivity {

    EditText editTextChipGroup;
    ChipGroup chipGroup;
    SynthImageButton addChipButton;
    List<String> allAddedSpecializations;
    List<ChipUtils> specializations;
    SynthButton saveDetails_button;
    LottieAnimationView chipLoadingAnimView;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lawyer_details);

        editTextChipGroup = findViewById(R.id.editTextChipGroup);
        addChipButton = findViewById(R.id.chipAddImageButton);
        chipGroup = findViewById(R.id.specializationChipGroup);

        saveDetails_button = findViewById(R.id.saveDetails_button);

        chipLoadingAnimView = findViewById(R.id.chipLoadingAnimView);
        chipLoadingAnimView.setVisibility(View.VISIBLE);

        specializations = new ArrayList<>();
        allAddedSpecializations = new ArrayList<>();

        fetchSpecializations();
        addChipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!editTextChipGroup.getText().toString().isEmpty()) {
                    addChip(editTextChipGroup.getText().toString(), false);
                    editTextChipGroup.setText("");
                }
            }
        });

        saveDetails_button.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                List<String> listPrimary = new ArrayList<>();
                List<String> listSecondary = new ArrayList<>();
                for (int i = 0; i < specializations.size(); i++) {
                    if (specializations.get(i).isPrimary())
                        listPrimary.add(specializations.get(i).getText().toString());
                    else if (specializations.get(i).isSecondary())
                        listSecondary.add(specializations.get(i).getText().toString());
                }
                UserUtils.getInstance().setPrimarySpecializations(listPrimary);
                UserUtils.getInstance().setSecondarySpecializations(listSecondary);
                FireBaseUtils.getInstance().addLawyerDetailsToFireStore(v);
                FireBaseUtils.getInstance().addSpecializationsToFireStore(allAddedSpecializations, v);
//                DefinedMethods.navigateToActivity(LawyerDetails.this, WelcomeLawyerActivity.class, true);
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void fetchSpecializations() {
        FireBaseUtils
                .getInstance()
                .getFirebaseFirestore()
                .collection(StringUtils.FIRESTORE_SPECIALIZATIONS)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                        assert queryDocumentSnapshots != null;
                        if (!queryDocumentSnapshots.isEmpty()) {
                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                            ArrayList<String> specs = (ArrayList<String>) list.get(0).get(StringUtils.FIRESTORE_SPECIALIZATIONS_SPECS);
                            allAddedSpecializations.addAll(specs);
                            for(int i=0;i<allAddedSpecializations.size();i++)
                            {
                                addChip(allAddedSpecializations.get(i), true);
                            }
                            chipLoadingAnimView.setVisibility(View.GONE);
                        } else {
                            Toast.makeText(LawyerDetails.this, "No data found in Database", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void addChip(String text, boolean fromFireBase) {
        ChipUtils chip = (ChipUtils) getLayoutInflater().inflate(R.layout.specialization_chip_layout, chipGroup, false);
        chip.setText(text);
        chip.setOnCloseIconClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chipGroup.removeView(chip);
                allAddedSpecializations.remove(chip.getText().toString());
                if (specializations.contains(chip) && specializations.size() > 2) {
                    specializations.remove(chip);
                    specializations.get(0).setPrimary(true);
                    specializations.get(1).setPrimary(true);

                } else {
                    specializations.remove(chip);
                }
            }
        });
        chip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (specializations.size() < 2) {
                        //primary
                        chip.setPrimary(true);
                        specializations.add(chip);
                    } else if (specializations.size() < 7 && !chip.isPrimary()) {
                        //secondary
                        chip.setSecondary(true);
                        specializations.add(chip);
                    } else {
                        //toast
                        Toast.makeText(LawyerDetails.this, "Cannot add more than 7 specializations", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    chip.setPrimary(false);
                    chip.setSecondary(false);
                    specializations.remove(chip);
                    for (int i = 0; i < specializations.size() && specializations.size() > 2; i++) {
                        specializations.get(i).setSecondary(true);
                    }
                    if (specializations.size() > 2) {
                        specializations.get(0).setPrimary(true);
                        specializations.get(1).setPrimary(true);
                    }
                }
            }
        });
        if (fromFireBase) {
            chip.setCloseIconVisible(false);
            chipGroup.addView(chip);
        } else if (!allAddedSpecializations.contains(text)) {
            chip.setCloseIconVisible(true);
            allAddedSpecializations.add(chip.getText().toString());
            chipGroup.addView(chip);
        } else {
            Toast.makeText(LawyerDetails.this, text + " already Added!", Toast.LENGTH_SHORT).show();
        }
    }
}