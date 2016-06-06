package com.example.xiaoyu.addresslist;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by NT on 2016/6/5.
 */
public class GroupActivity extends Activity {

    private ListView listView;
    private ArrayList<Map<String, String>> arrayList;
    private SimpleAdapter simpleAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
        init();
    }

    private void init() {
        listView = (ListView) findViewById(R.id.group_list);
        arrayList = new ArrayList<Map<String, String>>();
        DBOpenHelper helper = new DBOpenHelper(GroupActivity.this, "Address.db");
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor c = db.rawQuery("select _id,cellgroup from Grouptb", null);
        if (c != null) {
            String[] columnNames = c.getColumnNames();
            while (c.moveToNext()) {
                    Map<String, String> it = new HashMap<String, String>();
                    it.put("_id", c.getString(c.getColumnIndex("_id")));
                    it.put("name", c.getString(c.getColumnIndex("cellgroup")));
                    arrayList.add(it);
            }
        }
        simpleAdapter = new SimpleAdapter(this, arrayList, R.layout.list_item, new String[]{"_id", "name"}, new int[]{R.id.text_id, R.id.text_name});
        listView.setAdapter(simpleAdapter);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                long_change(((TextView) view.findViewById(R.id.text_id)).getText().toString(),((TextView) view.findViewById(R.id.text_name)).getText().toString());
                return false;
            }
        });
    }

    public void group_add(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("新建组别");
        final EditText input = new EditText(this);
        builder.setView(input);
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                String str = input.getText().toString();
                DBOpenHelper helper = new DBOpenHelper(GroupActivity.this, "Address.db");
                SQLiteDatabase db = helper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put("cellgroup", str);
                db.insert("Grouptb", null, values);
                onCreate(null);
            }
        });
        builder.create().show();
    }

    public void long_change(final String _id, final String name) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("编辑或删除");
        builder.setPositiveButton("删除", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                DBOpenHelper helper = new DBOpenHelper(GroupActivity.this, "Address.db");
                SQLiteDatabase db = helper.getWritableDatabase();
                db.delete("Grouptb", "_id=?", new String[]{_id});
                ContentValues values = new ContentValues();
                values.put("cellgroup", "");
                db.update("Addresstb", values, "cellgroup=?", new String[]{name});
                onCreate(null);
            }
        });
        builder.setNegativeButton("编辑", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                AlertDialog.Builder builder = new AlertDialog.Builder(GroupActivity.this);
                builder.setTitle("编辑组别");
                final EditText input = new EditText(GroupActivity.this);
                builder.setView(input);
                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        String str = input.getText().toString();
                        DBOpenHelper helper = new DBOpenHelper(GroupActivity.this, "Address.db");
                        SQLiteDatabase db = helper.getWritableDatabase();
                        ContentValues values = new ContentValues();
                        values.put("cellgroup", str);
                        db.update("Grouptb", values, "_id=?", new String[]{_id});
                        db.update("Addresstb", values, "cellgroup=?", new String[]{name});
                        onCreate(null);
                    }
                });
                builder.create().show();
            }
        });
        builder.create().show();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Intent intent = new Intent(GroupActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
        return true;
    }

}
