package com.rohg007.android.instiflo.utils;

import android.app.DatePickerDialog;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class DateSetter implements View.OnClickListener, View.OnFocusChangeListener, DatePickerDialog.OnDateSetListener {

    private EditText mEditText;
    private Calendar myCalendar;
    private SimpleDateFormat mSimpleDateFormat;

    public DateSetter(EditText mEditText) {
        this.mEditText = mEditText;
        mEditText.setOnFocusChangeListener(this);
        mEditText.setOnClickListener(this);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        myCalendar.set(Calendar.YEAR,year);
        myCalendar.set(Calendar.MONTH,month);
        myCalendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);

        if(mSimpleDateFormat==null)
            mSimpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        mEditText.setText(mSimpleDateFormat.format(myCalendar.getTime()));
    }

    @Override
    public void onClick(View v) {
        showPicker(v);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus)
            showPicker(v);
    }

    private void showPicker(View v){

        if(myCalendar==null)
            myCalendar = Calendar.getInstance();

        new DatePickerDialog(v.getContext(),this,myCalendar.get(Calendar.YEAR),
                myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }
}
