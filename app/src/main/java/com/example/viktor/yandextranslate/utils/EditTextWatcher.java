package com.example.viktor.yandextranslate.utils;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

/**
 * Created by Viktor on 27.04.2017.
 */

public class EditTextWatcher implements TextWatcher {

    private Spinner spinnerLeft;
    private Spinner spinnerRight;
    private Button buttonSwapLng;
    private EditText editText;
    private boolean visibleSpinner = false;

    public EditTextWatcher(Spinner spinnerLeft, Spinner spinnerRight, Button buttonSwapLng, EditText editText) {
        this.spinnerLeft = spinnerLeft;
        this.spinnerRight = spinnerRight;
        this.buttonSwapLng = buttonSwapLng;
        this.editText = editText;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (editText.getText().length() != 0) {
            if (!visibleSpinner) {
                spinnerLeft.setVisibility(View.VISIBLE);
                spinnerRight.setVisibility(View.VISIBLE);
                buttonSwapLng.setVisibility(View.VISIBLE);
                visibleSpinner = true;
            }
        } else {
            visibleSpinner = false;
            spinnerLeft.setVisibility(View.INVISIBLE);
            spinnerRight.setVisibility(View.INVISIBLE);
            buttonSwapLng.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
