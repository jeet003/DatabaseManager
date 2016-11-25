package com.example.jeet.database;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.app.Activity;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


public class ItemLayout extends Activity  {

    final String blockCharacterSet = "<>@";
    final String pattern1 = "^[987]([0-9]?){9}$";
    InputFilter filter1 = new InputFilter() {

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

            if (source != null && blockCharacterSet.contains(("" + source))) {
                return "";
            }
            return null;
        }
    };
    InputFilter filter = new InputFilter() {
        public CharSequence filter(CharSequence source, int start, int end,
                                   Spanned dest, int dstart, int dend) {
            for (int i = start; i < end; i++) {
                if (!Character.isLetter(source.charAt(i)) && !Character.isSpaceChar(source.charAt(i))) {
                    return "";
                }
            }
            return null;
        }
    };
    DBAdapter myDb;
    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }
    public void setupUI(View view) {

        //Set up touch listener for non-text box views to hide keyboard.
        try {
            if (!(view instanceof EditText)) {

                view.setOnTouchListener(new View.OnTouchListener() {

                    public boolean onTouch(View v, MotionEvent event) {
                        hideSoftKeyboard(ItemLayout.this);
                        return false;
                    }

                });
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }


        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {

            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {

                View innerView = ((ViewGroup) view).getChildAt(i);
                try {
                    setupUI(innerView);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_layout);
        setupUI(findViewById(R.id.item_layout));

        final EditText nameInput = (EditText) findViewById(R.id.nameInput);
        final EditText addressInput = (EditText) findViewById(R.id.addressInput);
        final EditText phoneInput = (EditText) findViewById(R.id.phoneInput);

        addressInput.setFilters(new InputFilter[]{filter1});
        nameInput.setFilters(new InputFilter[]{filter});
        phoneInput.setFilters(new InputFilter[]{new RegexInputFilter(pattern1)});

        final  SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        final TextView fromDateEtxt = (TextView) findViewById(R.id.etxt_fromdate);
        fromDateEtxt.requestFocus();
        Calendar newCalendar = Calendar.getInstance();
        final  DatePickerDialog fromDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                fromDateEtxt.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        fromDateEtxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(view == fromDateEtxt) {
                    fromDatePickerDialog.show();

                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void openDB() {
        myDb = new DBAdapter(this);
        myDb.open();
    }

    public void closeDB() {
        myDb.close();
    }


    public void addButtonClicked(View v) {
        EditText nameInput = (EditText) findViewById(R.id.nameInput);
        EditText addressInput = (EditText) findViewById(R.id.addressInput);
        final EditText phoneInput = (EditText) findViewById(R.id.phoneInput);
        TextView fromDateEtxt=(TextView) findViewById(R.id.etxt_fromdate);

        String nameInput1=nameInput.getText().toString();
        String addressInput1=addressInput.getText().toString();
        String phoneInput1=phoneInput.getText().toString();


        if(nameInput1.matches("") || addressInput1.matches("") || phoneInput1.matches("") || fromDateEtxt.getText().toString().matches("") )
        {
            AlertDialog.Builder builder2=new AlertDialog.Builder(ItemLayout.this);
            builder2.setMessage("Incorrect Data!! Try again...");
            builder2.setCancelable(false).setPositiveButton("OK",new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    return;
                }

            });
            builder2.show();

        }
        else{
            openDB();
            myDb.insertRow(nameInput1,addressInput1 , phoneInput1,fromDateEtxt.getText().toString());
            nameInput.setText("");
            addressInput.setText("");
            phoneInput.setText("");
            fromDateEtxt.setText("");

        AlertDialog.Builder builder2=new AlertDialog.Builder(ItemLayout.this);
        builder2.setMessage("Added Successfully");
        builder2.setCancelable(false).setPositiveButton("OK",new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                onBackPressed();
            }

        });
        builder2.show();
    }}
}
