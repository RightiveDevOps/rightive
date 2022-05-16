package com.law.rightive.parents;

import android.view.View;
import android.widget.CompoundButton;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.law.rightive.dialogs.CustomDateAndTimePicker;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public interface EventAddAndEdit{

    boolean isCustom();
    Calendar d = Calendar.getInstance();
    default MaterialDatePicker getMaterialDatePicker() {
        return CustomDateAndTimePicker.getMaterialDatePicker();
    }

    default MaterialTimePicker getMaterialTimePicker(int hour, int minute) {
        return CustomDateAndTimePicker.getMaterialTimePicker(hour, minute);
    }

    default String getDefaultTimeEditText() {

        int _hour;
        DecimalFormat formatter = new DecimalFormat("00");
        String am_pm = "";
        if (d.get(Calendar.AM_PM) == Calendar.AM)
            am_pm = "AM";
        else if (d.get(Calendar.AM_PM) == Calendar.PM)
            am_pm = "PM";
        _hour = (d.get(Calendar.HOUR) == 0) ? 12 : d.get(Calendar.HOUR);
        return (formatter.format(_hour) + ":" + formatter.format(d.get(Calendar.MINUTE)) + " " + am_pm).toString();
    }

    default String getFromTimeEditText(int _hour, int _minute)
    {
        DecimalFormat formatter = new DecimalFormat("00");
        String amPm = "";
        d.set(Calendar.HOUR_OF_DAY, _hour);
        d.set(Calendar.MINUTE, _minute);
        if (d.get(Calendar.AM_PM) == Calendar.AM)
            amPm = "AM";
        else if (d.get(Calendar.AM_PM) == Calendar.PM)
            amPm = "PM";
        String strHrsToShow = (d.get(Calendar.HOUR) == 0) ? "12" : formatter.format(d.get(Calendar.HOUR));
        strHrsToShow = strHrsToShow + ":" + formatter.format(d.get(Calendar.MINUTE)) + " " + amPm;
        return strHrsToShow;
    }
    default String getDefaultDateEditText() {
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, MMM DD, yyyy", Locale.getDefault());
        Date date = new Date();
        return sdf.format(date);
    }
    default String getFromDateEditText(String _date) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, MMM dd, yyyy", Locale.getDefault());
        return sdf.format(Objects.requireNonNull(new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).parse(_date)));
    }
}
