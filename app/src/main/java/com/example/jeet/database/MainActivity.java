package com.example.jeet.database;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;

public class MainActivity extends Activity {

    DBAdapter myDb;
    public void openDB() {
        myDb = new DBAdapter(this);
        myDb.open();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    public void onClick_AddRecord(View v) {

        Intent i = new Intent(this, ItemLayout.class);
        startActivity(i);
    }
    public void viewbutton(View v) {
        Intent i = new Intent(this, ViewLayout.class);
        startActivity(i);
    }
    public void onClick_ClearAll(View v) {
        openDB();
        myDb.deleteAll();
    }
}


