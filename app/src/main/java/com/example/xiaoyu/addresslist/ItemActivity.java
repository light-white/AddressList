package com.example.xiaoyu.addresslist;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

/**
 * Created by NT on 2016/6/5.
 */
public class ItemActivity extends Activity {

    private TextView tv_name;
    private TextView tv_cellphone;
    private TextView tv_homephone;
    private TextView tv_companyphone;
    private TextView tv_email;
    private TextView tv_company;
    private TextView tv_cellgroup;
    private String _id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);
        init();
        _id = getIntent().getStringExtra("_id");
        DBOpenHelper helper = new DBOpenHelper(ItemActivity.this, "Address.db");
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.query("Addresstb", new String[]{"name", "cellphone", "homephone", "companyphone", "email", "company", "cellgroup"}, "_id=?", new String[]{_id}, null, null, null);
        while (cursor.moveToNext()) {
            tv_name.setText(cursor.getString(cursor.getColumnIndex("name")));
            tv_cellphone.setText(cursor.getString(cursor.getColumnIndex("cellphone")));
            tv_homephone.setText(cursor.getString(cursor.getColumnIndex("homephone")));
            tv_companyphone.setText(cursor.getString(cursor.getColumnIndex("companyphone")));
            tv_email.setText(cursor.getString(cursor.getColumnIndex("email")));
            tv_company.setText(cursor.getString(cursor.getColumnIndex("company")));
            tv_cellgroup.setText(cursor.getString(cursor.getColumnIndex("cellgroup")));

            db.close();
        }
    }

    private void init() {
        tv_name = (TextView) findViewById(R.id.item_name);
        tv_cellphone = (TextView) findViewById(R.id.item_cellphone);
        tv_homephone = (TextView) findViewById(R.id.item_homephone);
        tv_companyphone = (TextView) findViewById(R.id.item_companyphone);
        tv_email = (TextView) findViewById(R.id.item_email);
        tv_company = (TextView) findViewById(R.id.item_company);
        tv_cellgroup = (TextView) findViewById(R.id.item_cellgroup);
    }

    public void ItemUpdate(View v) {
        Intent intent = new Intent(ItemActivity.this, UpdateActivity.class);
        intent.putExtra("_id",_id);
        startActivity(intent);
        finish();
    }

    public void ItemDelete(View v) {
        dialog();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Intent intent = new Intent(ItemActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
        return true;
    }

    protected void dialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ItemActivity.this);
        builder.setMessage("确认删除吗？");
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                DBOpenHelper helper = new DBOpenHelper(ItemActivity.this, "Address.db");
                SQLiteDatabase db = helper.getWritableDatabase();
                db.delete("Addresstb", "_id=?", new String[]{_id});
                Intent intent = new Intent(ItemActivity.this, MainActivity.class);
                startActivity(intent);
                ItemActivity.this.finish();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

}
