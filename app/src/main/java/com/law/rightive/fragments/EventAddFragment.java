package com.law.rightive.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.law.rightive.R;

public class EventAddFragment extends BottomSheetDialogFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.event_add_bottom_sheet_layout, container, false);

        EditText edit_text_CRN = view.findViewById(R.id.edit_text_CRN);

        edit_text_CRN.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() >= 4) {
                    edit_text_CRN.setInputType(InputType.TYPE_CLASS_NUMBER);
                } else
                    edit_text_CRN.setInputType(InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


        return view;
    }
}
