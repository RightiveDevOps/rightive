package com.law.rightive.utils;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.law.rightive.R;

import java.util.Objects;

import club.cred.synth.views.SynthButton;

public class DefinedMethods extends AppCompatActivity {

    public static DefinedMethods getInstance()
    {
        return new DefinedMethods();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static Snackbar getSnackBar(View view, String message)
    {
        Snackbar snackbar = Snackbar.make(view, Objects.requireNonNull(message),Snackbar.LENGTH_LONG);
        Typeface gilroylight = view.getResources().getFont(R.font.gilroylight);
        Typeface gilroymedium = view.getResources().getFont(R.font.gilroymedium);

        TextView snackbarActionTextView = (TextView) snackbar.getView().findViewById(com.google.android.material.R.id.snackbar_action);
        snackbarActionTextView.setTextSize( 15 );
        snackbarActionTextView.setTypeface(gilroymedium);

        TextView snackbarTextView = (TextView) snackbar.getView().findViewById(com.google.android.material.R.id.snackbar_text);
        snackbarTextView.setTextSize( 15 );
        snackbarTextView.setMaxLines( 5 );
        snackbarTextView.setTextColor(view.getResources().getColor(R.color.synth_text_color));
        snackbarTextView.setTypeface(gilroylight);

        snackbar.setActionTextColor(Color.WHITE)
                .setBackgroundTint(view.getResources().getColor(R.color.snackbar_background_color));
        return snackbar;
    }
    public static void navigateToActivity(Activity packageContext,  Class<?> cls, boolean endActivity)
    {
        Intent intent = new Intent(packageContext, cls);
        packageContext.startActivity(intent);
        if(endActivity)
        {
            packageContext.finish();
        }
    }

    public static SynthButton getFormattedButton(Activity packageContext, SynthButton synthButton, boolean isEnabled)
    {
        if(isEnabled)
        {
            synthButton.setTextColor(packageContext.getResources().getColor(R.color.copper_text_color));
        }
        else
        {
            synthButton.setTextColor(packageContext.getResources().getColor(R.color.copper_text_color_30));
        }
        return synthButton;
    }
}
