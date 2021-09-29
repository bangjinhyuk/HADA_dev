package com.hada.pillmanagement;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class minusPopup extends Activity {
    private ListView listView;
    private PillListViewAdapter pillListViewAdapter;

    List<Item> items= new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_minus_popup);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        listView = findViewById(R.id.pilllistview);

        Database database;
        SQLiteDatabase db;
        database = new Database(minusPopup.this, "pill.db", null, 1);
        db = database.getWritableDatabase();
        database.onCreate(db);

        pillListViewAdapter = new PillListViewAdapter(minusPopup.this);
        listView.setAdapter(pillListViewAdapter);
        listView.setClickable(false);
        Cursor c = db.query("mytable",null,null,null,null,null,null,null);
        System.out.println(c.getCount()+"=>>>>>>>>>");
        while(c.moveToNext()){
            pillListViewAdapter.addItem(c.getLong(c.getColumnIndex("_id")),c.getString(c.getColumnIndex("name")),c.getString(c.getColumnIndex("date")),c.getString(c.getColumnIndex("day")));
        }
        pillListViewAdapter.notifyDataSetChanged();



    }

}