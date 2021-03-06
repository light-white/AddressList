package com.example.xiaoyu.addresslist;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by NT on 2016/6/5.
 */
public class AddActivity extends Activity {

    private Button bt_cancel;
    private Button bt_ensure;

    private EditText ed_name;
    private EditText ed_cellphone;
    private EditText ed_homephone;
    private EditText ed_companyphone;
    private EditText ed_email;
    private EditText ed_company;
    private Spinner sp_group;
    private ArrayList<String> arrayList;
    private ArrayAdapter<String> arrayAdapter;
    private String group;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        init();
    }

    private void init() {
        bt_cancel = (Button) findViewById(R.id.add_cancel);
        bt_ensure = (Button) findViewById(R.id.add_ensure);
        ed_name = (EditText) findViewById(R.id.add_name);
        ed_cellphone = (EditText) findViewById(R.id.add_cellphone);
        ed_homephone = (EditText) findViewById(R.id.add_homephone);
        ed_companyphone = (EditText) findViewById(R.id.add_companyphone);
        ed_email = (EditText) findViewById(R.id.add_email);
        ed_company = (EditText) findViewById(R.id.add_company);
        sp_group = (Spinner) findViewById(R.id.add_cellgroup);
        arrayList = new ArrayList<String>();
        DBOpenHelper helper = new DBOpenHelper(AddActivity.this, "Address.db");
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        Cursor c = db.rawQuery("select cellgroup from Grouptb", null);
        if (c != null) {
            arrayList.add("选择分组");
            String[] columnNames = c.getColumnNames();
            while (c.moveToNext())
                for (String name : columnNames)
                    arrayList.add(c.getString(c.getColumnIndex(name)));
            c.close();
        }
        db.close();
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arrayList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_group.setAdapter(arrayAdapter);
        sp_group.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                group = arrayList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        Button.OnClickListener listener = new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddActivity.this,MainActivity.class);
                switch (v.getId()) {
                    case R.id.add_cancel:
                        startActivity(intent);
                        finish();
                        break;
                    case R.id.add_ensure:
                        if(ed_name.getText().toString().trim().equals("")){
                            Toast.makeText(AddActivity.this, "姓名不能为空", Toast.LENGTH_SHORT).show();
                            break;
                        }
                        if(ed_cellphone.getText().toString().trim().equals("")){
                            Toast.makeText(AddActivity.this, "手机号不能为空", Toast.LENGTH_SHORT).show();
                            break;
                        }
                        DBOpenHelper helper = new DBOpenHelper(AddActivity.this, "Address.db");
                        SQLiteDatabase db = helper.getWritableDatabase();
                        ContentValues values = new ContentValues();
                        values.put("name", ed_name.getText().toString().trim());
                        values.put("cellphone", ed_cellphone.getText().toString().trim());
                        if(!ed_homephone.getText().toString().trim().equals(""))
                            values.put("homephone",ed_homephone.getText().toString().trim());
                        if(!ed_companyphone.getText().toString().trim().equals(""))
                            values.put("companyphone",ed_companyphone.getText().toString().trim());
                        if(!ed_email.getText().toString().trim().equals(""))
                            values.put("email",ed_email.getText().toString().trim());
                        if(!ed_company.getText().toString().trim().equals(""))
                            values.put("company",ed_company.getText().toString().trim());
                        if(!group.equals("选择分组"))
                            values.put("cellgroup",group);
                        db.insert("Addresstb",null,values);
                        db.close();
                        startActivity(intent);
                        finish();
                        break;
                }

            }
        };
        bt_ensure.setOnClickListener(listener);
        bt_cancel.setOnClickListener(listener);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Intent intent = new Intent(AddActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
        return true;
    }
}

