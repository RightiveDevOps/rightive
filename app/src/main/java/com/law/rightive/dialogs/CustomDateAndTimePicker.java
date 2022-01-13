package com.law.rightive.dialogs;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

public class CustomDateAndTimePicker {

    public static MaterialDatePicker getMaterialDatePicker()
    {
        MaterialDatePicker.Builder materialDateBuilder = MaterialDatePicker.Builder.datePicker();
        materialDateBuilder.setTitleText("SELECT A DATE");
        MaterialDatePicker materialDatePicker = materialDateBuilder.build();
        return materialDatePicker;
    }

    public static MaterialTimePicker getMaterialTimePicker(int hour, int minute)
    {
        MaterialTimePicker.Builder materialTimeBuilder = new MaterialTimePicker.Builder();
        materialTimeBuilder
                .setTitleText("SELECT YOUR TIME")
                .setHour(hour)
                .setMinute(minute)
                .setTimeFormat(TimeFormat.CLOCK_12H);
        return materialTimeBuilder.build();
    }

}