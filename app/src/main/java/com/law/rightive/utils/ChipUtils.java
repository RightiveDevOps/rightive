package com.law.rightive.utils;

import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Build;
import android.util.AttributeSet;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;

import com.google.android.material.chip.Chip;
import com.law.rightive.R;

@RequiresApi(api = Build.VERSION_CODES.M)
public class ChipUtils extends Chip {
    private boolean isPrimary;
    private boolean isSecondary;
    Context context;

    public ChipUtils(Context context) {
        super(context);
        this.context = context;
    }

    public ChipUtils(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public ChipUtils(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    public void setPrimary(boolean flag) {
        isPrimary = flag;
        if (isPrimary) {
            isSecondary = false;
            this.setChipBackgroundColor(ColorStateList.valueOf(context.getColor(R.color.chip_primary_selected)));
        } else {
            isPrimary = false;
            isSecondary = false;
            this.setChipBackgroundColor(ColorStateList.valueOf(context.getColor(R.color.chip_deselected)));
        }
    }

    public void setSecondary(boolean flag) {
        isSecondary = flag;
        if (isSecondary) {
            isPrimary = false;
            this.setChipBackgroundColor(ColorStateList.valueOf(context.getColor(R.color.chip_secondary_selected)));
        } else {
            isPrimary = false;
            isSecondary = false;
            this.setChipBackgroundColor(ColorStateList.valueOf(context.getColor(R.color.chip_deselected)));
        }
    }

    public boolean isPrimary() {
        return this.isPrimary;
    }
    public boolean isSecondary() {
        return this.isSecondary;
    }

}
