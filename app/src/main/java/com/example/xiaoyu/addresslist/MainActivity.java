package com.example.xiaoyu.addresslist;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends Activity {

    private SearchView searchView;
    private ListView listview;
    private ArrayList<Map<String, String>> arrayList;
    private SimpleAdapter simpleAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    private void init() {
        arrayList = new ArrayList<Map<String, String>>();
        searchView = (SearchView) findViewById(R.id.main_search);
        listview = (ListView) findViewById(R.id.main_list);
        searchView.onActionViewExpanded();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (TextUtils.isEmpty(newText)) {
                    // 清除ListView的过滤
                    listview.clearTextFilter();
                } else {
                    // 使用用户输入的内容对ListView的列表项进行过滤
                    listview.setFilterText(newText);
                }
                return false;
            }
        });
        Map<String, String> item = new HashMap<String, String>();
        item.put("_id", "0");
        item.put("name", "我的分组");
        arrayList.add(item);
        DBOpenHelper helper = new DBOpenHelper(MainActivity.this, "Address.db");
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor c = db.rawQuery("select _id,name from Addresstb", null);
        if (c != null) {
            while (c.moveToNext()) {
                Map<String, String> it = new HashMap<String, String>();
                it.put("_id", c.getString(c.getColumnIndex("_id")));
                it.put("name", c.getString(c.getColumnIndex("name")));
                arrayList.add(it);
            }
            c.close();
        }
        db.close();
        simpleAdapter = new SimpleAdapter(this, arrayList, R.layout.list_item, new String[]{"_id", "name"}, new int[]{R.id.text_id, R.id.text_name});
        listview.setAdapter(simpleAdapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String _id =  ((TextView) view.findViewById(R.id.text_id)).getText().toString();
                if (_id.equals("0")) {
                    Intent intent = new Intent(MainActivity.this, GroupActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(MainActivity.this, ItemActivity.class);
                    intent.putExtra("_id", ((TextView) view.findViewById(R.id.text_id)).getText().toString());
                    startActivity(intent);
                    finish();
                }
            }
        });
        listview.setTextFilterEnabled(true);
    }

    public void address_add(View v) {
        Intent intent = new Intent(MainActivity.this, AddActivity.class);
        startActivity(intent);
        finish();
    }

}
