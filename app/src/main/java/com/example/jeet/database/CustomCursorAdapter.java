package com.example.jeet.database;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.view.View;
import android.widget.SimpleCursorAdapter;


public class CustomCursorAdapter extends SimpleCursorAdapter {

        public CustomCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to,int flags) {
        super(context, layout, c, from, to,0);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        super.bindView(view, context, cursor);
        if(cursor.getPosition() % 2 == 0) {
            view.setBackgroundColor(Color.BLUE);
        }else{
            view.setBackgroundColor(Color.RED);
        }
    }
}