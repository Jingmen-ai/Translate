package com.youdao.sdk.ydtranslatedemo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.database.Cursor;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
class ManageSQL {

    //MySQLite mySQLite;

    /*public void creatSQL(){
        mySQLite=new MySQLite(this, "MyWeather.db3",1);
    }*/
    //insert the temperature and humidity datas
    public void insertT(SQLiteDatabase db, String data0, String data1,String data2){
        //db.execSQL("insert into word values (null,?,?)", new String[]{data0,data1});
        ContentValues values=new ContentValues();
        values.put("English",data0);
        values.put("Chinese",data1);
        values.put("setence",data2);
        db.insert("word",null,values);
        values.clear();
    }
    public void deleteT(SQLiteDatabase db,int i){
        String str=String.valueOf(i);
        db.delete("word","id=?",new String[]{str});
    }
    public void deleteAll(SQLiteDatabase db){
        db.execSQL("delete from word");
        db.execSQL("DELETE FROM sqlite_sequence WHERE name = 'word'");
    }
    public void change(SQLiteDatabase db, String data0, String data1,String data2,int i){
        ContentValues values=new ContentValues();
        values.put("English",data0);
        values.put("Chinese",data1);
        values.put("setence",data2);
        String str=String.valueOf(i);
        db.update("word",values,"id=?",new String[]{str});
        values.clear();
    }
    public Cursor find(SQLiteDatabase db,String str){
        Cursor cursor;
        String string=str;
        cursor=db.rawQuery("SELECT * FROM word WHERE English LIKE ?",new String[]{"%"+str+"%"});

        return cursor;
    }
}

public class Book extends Activity {
    ListView listView;
    MySQLite mySQLite;
    ManageSQL manageSQL;
    String data0, data1,data2,data3;
    Cursor cursor;
    int position;
    public int getPosition(){
        return position;
    }
    public void setPosition(int position){
        this.position=position;
    }
    Configuration mConfiguration;
    int ori;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);
        Button BT=(Button)findViewById(R.id.button3);
        Button BT1=(Button)findViewById(R.id.button4);
mConfiguration=  this.getResources().getConfiguration();
ori = mConfiguration.orientation;
        final List<Map<String, String>> listItemsList=new ArrayList<Map<String,String>>();
        final List<Map<String, String>> listItemsList2=new ArrayList<Map<String,String>>();
        final TextView TV11=(TextView)findViewById(R.id.textView12);
        final ListView LL=(ListView)findViewById(R.id.listview1) ;
        final Button BThid=(Button)findViewById(R.id.button7) ;

            final Button BTfind=(Button)findViewById(R.id.button5);
            final EditText ETX=(EditText)findViewById(R.id.textView11);

        data0="data0";
        data1="data1";
        data2="data2";
        listView=(ListView)findViewById(R.id.listView);
        mySQLite=new MySQLite(this, "wordbank.db", null,1);





        BT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                AlertDialog.Builder builder = new AlertDialog.Builder(Book.this);
                builder.setTitle("添加单词");
                View view1 = getLayoutInflater().inflate(R.layout.addword,null);
                final EditText ET1=(EditText) view1.findViewById(R.id.text1);
                final EditText ET2=(EditText) view1.findViewById(R.id.text2);
                final EditText ET3=(EditText) view1.findViewById(R.id.text3);
                builder.setView(view1);
                builder.setPositiveButton("添加", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String str1=ET1.getText().toString().trim();
                        String str2=ET2.getText().toString().trim();
                        String str3=ET3.getText().toString().trim();
                        manageSQL=new ManageSQL();
                        manageSQL.insertT(mySQLite.getReadableDatabase(), str1, str2,str3);

                        onCreate(null);
                    }
                });
                builder.show();
            }
        });


        cursor=mySQLite.getReadableDatabase().rawQuery("select  * from word" , null);

        if (ori == mConfiguration.ORIENTATION_LANDSCAPE) {
            //横屏
            SimpleAdapter adapter=new SimpleAdapter(this, listItemsList, R.layout.land_item,
                    new String[]{"data0","data1","data2","data3"},
                    new int[]{R.id.dataview1,R.id.dataview2,R.id.dataview3,R.id.dataview4});
            if(cursor.moveToFirst()){
                do {
                    Map<String,String> map=new HashMap<String, String>();
                    map.put("data0", cursor.getString(0));//数据库中第一列的内容显示在listview的左边
                    map.put("data1", cursor.getString(1));
                    map.put("data2", cursor.getString(2));
                    map.put("data3", cursor.getString(3));//数据库的第三列内容显示在listview的右边
                    listItemsList.add(map);
                }while (cursor.moveToNext());
            }
            listView.setAdapter(adapter);
            //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//强制为竖屏
        } else if (ori == mConfiguration.ORIENTATION_PORTRAIT) {
            //竖屏
            SimpleAdapter adapter=new SimpleAdapter(this, listItemsList, R.layout.activity_listview,
                    new String[]{"data0","data1","data2"},
                    new int[]{R.id.dataview1,R.id.dataview2,R.id.dataview3});
            if(cursor.moveToFirst()){
                do {
                    Map<String,String> map=new HashMap<String, String>();
                    map.put("data0", cursor.getString(0));//数据库中第一列的内容显示在listview的左边
                    map.put("data1", cursor.getString(1));
                    map.put("data2", cursor.getString(2));//数据库的第三列内容显示在listview的右边
                    listItemsList.add(map);
                }while (cursor.moveToNext());
            }
            listView.setAdapter(adapter);
            //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//强制为横屏
        }


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Book.this);
                builder.setTitle("查看单词");
                View view1 = getLayoutInflater().inflate(R.layout.worddetail,null);
                final EditText ET1=(EditText) view1.findViewById(R.id.text1);
                final EditText ET2=(EditText) view1.findViewById(R.id.text2);
                final EditText ET3=(EditText) view1.findViewById(R.id.text3);
                Cursor cursor=mySQLite.getReadableDatabase().rawQuery("select  * from word" , null);
                String str=listItemsList.get(i).toString();
                //int a=Integer.parseInt(str);
                setPosition(i);
                int y=getPosition()+1;
                cursor.move(y);
                ET1.setText(cursor.getString(1));
                ET2.setText(cursor.getString(2));
                ET3.setText(cursor.getString(3));
                builder.setNegativeButton("删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(Book.this,String.valueOf(getPosition())+"号单词已删除",Toast.LENGTH_LONG).show();
                        int y=getPosition();
                        manageSQL=new ManageSQL();
                        manageSQL.deleteT(mySQLite.getWritableDatabase(),y);
                        onCreate(null);
                    }
                });
                builder.setPositiveButton("修改", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        int y=getPosition();
                        Toast.makeText(Book.this,String.valueOf(getPosition())+"号单词已修改",Toast.LENGTH_LONG).show();
                        String str1=ET1.getText().toString().trim();
                        String str2=ET2.getText().toString().trim();
                        String str3=ET3.getText().toString().trim();
                        manageSQL=new ManageSQL();
                        manageSQL.change(mySQLite.getWritableDatabase(),str1,str2,str3,y);
                        onCreate(null);
                    }
                });
                setPosition(Integer.parseInt(cursor.getString(0)));
                builder.setView(view1);

                builder.show();

            }
        });
        BT1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder=new AlertDialog.Builder(Book.this);
                builder.setTitle("清空确认");
                builder.setMessage("是否清空整个单词本内容，此操作不可逆");
                builder.setPositiveButton("确认清空", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        manageSQL=new ManageSQL();
                        manageSQL.deleteAll(mySQLite.getWritableDatabase());
                        onCreate(null);
                    }
                });
                builder.setNegativeButton("返回", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.show();
            }
        });
        BTfind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str=ETX.getText().toString().trim();
                Cursor cursor2;
                listItemsList2.clear();
                manageSQL=new ManageSQL();
                cursor2=manageSQL.find(mySQLite.getWritableDatabase(),str);
                SimpleAdapter adapter1=new SimpleAdapter(Book.super.getApplicationContext(),listItemsList2, R.layout.activity_listview,
                        new String[]{"data0","data1","data2"},
                        new int[]{R.id.dataview1,R.id.dataview2,R.id.dataview3});
                if(cursor2.moveToFirst()){
                    do {
                        Map<String,String> map=new HashMap<String, String>();
                        map.put("data0", cursor2.getString(0));//数据库中第一列的内容显示在listview的左边
                        map.put("data1", cursor2.getString(1));
                        map.put("data2", cursor2.getString(2));//数据库的第三列内容显示在listview的右边
                        listItemsList2.add(map);
                    }while (cursor2.moveToNext());
                }
                LL.setAdapter(adapter1);
                TV11.setVisibility(View.VISIBLE);
                LL.setVisibility(View.VISIBLE);
                BThid.setVisibility(View.VISIBLE);
                BThid.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        listItemsList2.clear();
                        LL.setAdapter(null); //清空listview
                        TV11.setVisibility(View.GONE);
                        LL.setVisibility(View.GONE);
                        BThid.setVisibility(View.GONE);
                    }
                });

            }
        });
    }
}
