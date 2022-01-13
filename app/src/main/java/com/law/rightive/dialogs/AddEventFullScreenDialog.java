package com.law.rightive.dialogs;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.divider.MaterialDivider;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.transition.platform.MaterialArcMotion;
import com.google.android.material.transition.platform.MaterialContainerTransform;
import com.google.android.material.transition.platform.MaterialContainerTransformSharedElementCallback;
import com.law.rightive.R;
import com.law.rightive.utils.FireBaseUtils;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import club.cred.synth.views.SynthImageButton;

public class AddEventFullScreenDialog extends AppCompatActivity {
    EditText fromDateEditText, fromTimeEditText;
    SynthImageButton event_dialog_close_button, event_dialog_save_button;
    SwitchMaterial switchAllDay;
    MaterialDivider insideDivider;

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

        Calendar d = Calendar.getInstance();
        int hour = d.get(Calendar.HOUR_OF_DAY);
        int minute = d.get(Calendar.MINUTE);

        MaterialDatePicker customDatePicker = CustomDateAndTimePicker.getMaterialDatePicker();
        MaterialTimePicker customTimePicker = CustomDateAndTimePicker.getMaterialTimePicker(hour, minute);

        DecimalFormat formatter = new DecimalFormat("00");
        String am_pm = "";
        if (d.get(Calendar.AM_PM) == Calendar.AM)
            am_pm = "AM";
        else if (d.get(Calendar.AM_PM) == Calendar.PM)
            am_pm = "PM";
        hour = (d.get(Calendar.HOUR) == 0) ? 12 : d.get(Calendar.HOUR);
        fromTimeEditText.setText((formatter.format(hour) + ":" + formatter.format(minute) + " " + am_pm).toString());

        SimpleDateFormat sdf = new SimpleDateFormat("EEE, MMM DD, yyyy");
        Date date = new Date();
        String currentDate = sdf.format(date);
        fromDateEditText.setText(currentDate);

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
        customDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @SuppressLint("SetTextI18n")
            @Override
            public void onPositiveButtonClick(Object selection) {
                try {
                    String date2 = sdf.format(new SimpleDateFormat("MMM DD, yyyy").parse(customDatePicker.getHeaderText()));
                    fromDateEditText.setText(date2);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

        customTimePicker.addOnPositiveButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String amPm = "";
                d.set(Calendar.HOUR_OF_DAY, customTimePicker.getHour());
                d.set(Calendar.MINUTE, customTimePicker.getMinute());
                if (d.get(Calendar.AM_PM) == Calendar.AM)
                    amPm = "AM";
                else if (d.get(Calendar.AM_PM) == Calendar.PM)
                    amPm = "PM";
                String strHrsToShow = (d.get(Calendar.HOUR) == 0) ? "12" : formatter.format(d.get(Calendar.HOUR));
                strHrsToShow = strHrsToShow + ":" + formatter.format(d.get(Calendar.MINUTE)) + " " + amPm;
                fromTimeEditText.setText(strHrsToShow);
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

}
