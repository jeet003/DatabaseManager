package com.example.jeet.database;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

/**
 * Created by jeet on 25/05/2016.
 */
public class ViewLayout extends Activity {

    DBAdapter myDb;
    public void openDB() {
        myDb = new DBAdapter(this);
        myDb.open();
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId()==R.id.listViewFromDB) {
            menu.setHeaderTitle("MENU");
            String[] menuItems = getResources().getStringArray(R.array.menu);
            for (int i = 0; i<menuItems.length; i++) {
                menu.add(Menu.NONE, i, i, menuItems[i]);
            }
        }
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        int menuItemIndex = item.getItemId();
        if(menuItemIndex==0)
        {
            updateItemForId(info.id);
            AlertDialog.Builder builder2=new AlertDialog.Builder(ViewLayout.this);
            builder2.setMessage("Deleted Successfully");
            builder2.setCancelable(false).setPositiveButton("OK",new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    return;
                }

            });
            builder2.show();
        }
        else if(menuItemIndex==1)
        {
            populateListViewFromDB();
            Toast.makeText(getApplicationContext(), "Not Deleted ", Toast.LENGTH_LONG).show();

        }
        return true;
    }
    public void back(View v){
        onBackPressed();
    }
    public void closeDB() {
        myDb.close();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewlist);
        populateListViewFromDB();

            }
    @Override
    public void onContentChanged() {
        super.onContentChanged();
        View empty = findViewById(R.id.empty);
        ListView list = (ListView) findViewById(R.id.listViewFromDB);
        list.setEmptyView(empty);
    }
    @Override
    protected void onDestroy() {
        closeDB();
        super.onDestroy();

    }

    public void populateListViewFromDB() {
        openDB();
        ListView myList = (ListView) findViewById(R.id.listViewFromDB);
        Cursor cursor = myDb.getAllRows();
        startManagingCursor(cursor);
        String[] fromFieldNames = new String[]
                { DBAdapter.KEY_NAME, DBAdapter.KEY_ADDRESS, DBAdapter.KEY_PHONE, DBAdapter.KEY_DOB};
        int[] toViewIDs = new int[]
                {R.id.i_name,     R.id.i_address,           R.id.i_phone,       R.id.i_dob};
        CustomCursorAdapter myCursorAdapter =
                new CustomCursorAdapter(this, R.layout.item_layout_view, cursor, fromFieldNames, toViewIDs,0);
        myList.setAdapter(myCursorAdapter);
        registerForContextMenu(myList);
    }

    private void updateItemForId(long idInDB) {
        myDb.deleteRow(idInDB);
        populateListViewFromDB();
    }
}

