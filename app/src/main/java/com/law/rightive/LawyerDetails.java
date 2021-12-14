package com.law.rightive;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.chip.ChipGroup;
import com.law.rightive.utils.ChipUtils;

import java.util.ArrayList;
import java.util.List;

import club.cred.synth.views.SynthImageButton;

@RequiresApi(api = Build.VERSION_CODES.M)
public class LawyerDetails extends AppCompatActivity {

    EditText editTextChipGroup;
    ChipGroup chipGroup;
    SynthImageButton addChipButton;
    List<String> allAddedSpecializations;
    List<ChipUtils> specializations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lawyer_details);

        editTextChipGroup = findViewById(R.id.editTextChipGroup);
        addChipButton = findViewById(R.id.chipAddImageButton);
        chipGroup = findViewById(R.id.specializationChipGroup);

        specializations = new ArrayList<>();
        allAddedSpecializations = new ArrayList<>();
        addChipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!editTextChipGroup.getText().toString().isEmpty()) {
                    addChip(editTextChipGroup.getText().toString());
                    editTextChipGroup.setText("");
                }
            }
        });
    }

    private void addChip(String text) {
        ChipUtils chip = (ChipUtils) getLayoutInflater().inflate(R.layout.specialization_chip_layout, chipGroup, false);
        chip.setText(text);
        if (!allAddedSpecializations.contains(text)) {
            chip.setCloseIconVisible(true);
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
                        for (int i = 0; i < specializations.size(); i++) {
                            specializations.get(i).setSecondary(true);
                        }
                        if (specializations.size() > 2) {
                            specializations.get(0).setPrimary(true);
                            specializations.get(1).setPrimary(true);
                        }
                    }
                    allAddedSpecializations.add(chip.getText().toString());
                }
            });
            allAddedSpecializations.add(chip.getText().toString());
            chipGroup.addView(chip);
        } else {
            Toast.makeText(LawyerDetails.this, text + " already Added!", Toast.LENGTH_SHORT).show();
        }
    }
}