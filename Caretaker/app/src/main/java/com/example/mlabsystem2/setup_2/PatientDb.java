package com.example.mlabsystem2.setup_2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class PatientDb extends SQLiteOpenHelper{

    public static final String DB_NAME = "Patientlist";
    public static final int DB_VER = 1;
    public static final String DB_TABLE = "Patientnames";
    public static final String DB_COLUMN = "Names";

    public PatientDb(Context context){
        super(context,DB_NAME,null,DB_VER);
      //  boolean b =context.deleteDatabase("Patientlist");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = String.format("CREATE TABLE %s (ID INTEGER PRIMARY KEY AUTOINCREMENT,%s TEXT NOT NULL);",DB_TABLE,DB_COLUMN);
        db.execSQL(query);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String query = String.format("DELETE TABLE IF EXISTS %s",DB_TABLE);
        db.execSQL(query);
        onCreate(db);

    }

    public void insertnewName(String name){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DB_COLUMN,name);
        db.insertWithOnConflict(DB_TABLE,null,values,SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
    }

    public ArrayList<String> getNameList(){
        ArrayList<String> nameList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(DB_TABLE,new String[]{DB_COLUMN},null,null,null,null,null);
        while (cursor.moveToNext()){
            int index = cursor.getColumnIndex(DB_COLUMN);
            nameList.add(cursor.getString(index));
        }
        cursor.close();
        db.close();
        return nameList;
    }


}
