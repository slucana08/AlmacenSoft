package com.dms.almacensoft.utils;

import android.text.Editable;

/**
 * {@link TextWatcher} es una clase de ayuda que controla el ingreso de datos en los EditText
 */

public interface TextWatcher extends android.text.TextWatcher {

    @Override
    default void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    default void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    default void afterTextChanged(Editable s) {
        onChanged(s.toString());
    }

    void onChanged(String s);
}