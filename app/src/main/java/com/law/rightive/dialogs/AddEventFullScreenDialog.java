package com.law.rightive.dialogs;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.divider.MaterialDivider;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.transition.platform.MaterialArcMotion;
import com.google.android.material.transition.platform.MaterialContainerTransform;
import com.google.android.material.transition.platform.MaterialContainerTransformSharedElementCallback;
import com.law.rightive.R;
import com.law.rightive.parents.EventAddAndEdit;
import com.law.rightive.utils.FireBaseUtils;

import java.text.ParseException;
import java.util.Calendar;


import club.cred.synth.views.SynthImageButton;

public class AddEventFullScreenDialog extends AppCompatActivity implements EventAddAndEdit {
    EditText fromDateEditText, fromTimeEditText;
    SynthImageButton event_dialog_close_button, event_dialog_save_button;
    SwitchMaterial switchAllDay;
    MaterialDivider insideDivider;
    LinearLayout fiveNotificationLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_event_fullscreen_dialog);
        setEnterSharedElementCallback(new MaterialContainerTransformSharedElementCallback());
        getWindow().setSharedElementEnterTransition(buildTransitions());
        getWindow().setSharedElementExitTransition(buildTransitions());
        getWindow().setSharedElementReenterTransition(buildTransitions());

        //ELEMENTS
        switchAllDay = findViewById(R.id.switchAllDay);
        insideDivider = findViewById(R.id.insideDivider);
        event_dialog_close_button = findViewById(R.id.calendar_prev_button);
        event_dialog_save_button = findViewById(R.id.event_dialog_save_button);

        event_dialog_close_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        event_dialog_save_button.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                FireBaseUtils.getInstance().addEventToFireStore(v);
            }
        });

        switchAllDay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    fromTimeEditText.setVisibility(View.GONE);
                    insideDivider.setVisibility(View.GONE);
//                    fromDateEditText.setLayoutParams(new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 48, getResources().getDisplayMetrics())));
                } else {
                    fromTimeEditText.setVisibility(View.VISIBLE);
                    insideDivider.setVisibility(View.VISIBLE);
                }
            }
        });
        fromDateEditText = findViewById(R.id.fromDateEditText);
        fromTimeEditText = findViewById(R.id.fromTimeEditText);
        fiveNotificationLayout = findViewById(R.id.fiveNotificationLayout);

        Calendar d = Calendar.getInstance();
        int hour = d.get(Calendar.HOUR_OF_DAY);
        int minute = d.get(Calendar.MINUTE);

        if (fiveNotificationLayout.getChildCount() <= 0) {
            fiveNotificationLayout.setVisibility(View.GONE);
        } else {
            fiveNotificationLayout.setVisibility(View.VISIBLE);
        }

        TextView notificationTextViewUtils = new TextView(this);
        notificationTextViewUtils.setText("qwertyuiopohgfcvbn");
        notificationTextViewUtils.setCompoundDrawables(null,null, this.getResources().getDrawable(R.drawable.ic_baseline_close_24),null);
        fiveNotificationLayout.addView(notificationTextViewUtils);
        fiveNotificationLayout.setVisibility(View.VISIBLE);

        MaterialDatePicker customDatePicker = getMaterialDatePicker();
        MaterialTimePicker customTimePicker = getMaterialTimePicker(hour, minute);

        fromTimeEditText.setText(getDefaultTimeEditText());
        fromDateEditText.setText(getDefaultDateEditText());

        fromDateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDatePicker.show(getSupportFragmentManager(), "ADD_EVENT_DATE_PICKER");
            }
        });
        fromTimeEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customTimePicker.show(getSupportFragmentManager(), "ADD_EVENT_TIME_PICKER");
            }
        });
        customDatePicker.addOnPositiveButtonClickListener(selection -> {
            try {
                fromDateEditText.setText(getFromDateEditText(customDatePicker.getHeaderText()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        });

        customTimePicker.addOnPositiveButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fromTimeEditText.setText(getFromTimeEditText(customTimePicker.getHour(), customTimePicker.getMinute()));
            }
        });

    }

    private MaterialContainerTransform buildTransitions() {
        MaterialContainerTransform materialContainerTransform = new MaterialContainerTransform();
        materialContainerTransform.addTarget(R.id.container);
//        materialContainerTransform.setAllContainerColors(MaterialColors.getColor(findViewById(R.id.container), R.attr.colorSurface));
        materialContainerTransform.setDuration(400);
        materialContainerTransform.setPathMotion(new MaterialArcMotion());
        materialContainerTransform.setInterpolator(new FastOutSlowInInterpolator());
        materialContainerTransform.setFadeMode(MaterialContainerTransform.FADE_MODE_IN);
        return materialContainerTransform;
    }

    @Override
    public boolean isCustom() {
        return true;
    }
}
