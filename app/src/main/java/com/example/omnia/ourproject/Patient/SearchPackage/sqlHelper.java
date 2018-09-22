package com.example.omnia.ourproject.Patient.SearchPackage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zt on 27/03/18.
 */

public class sqlHelper extends SQLiteOpenHelper {

    public static String DB_Name = "DB";
    public static int Version = 1;
    public static String Table_Name = "SearchTable";
    public static String Column_Name = "search";
    private static final String KEY_ID="Id";



    public sqlHelper(Context context) {
        super(context, DB_Name, null, Version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_TABLE = "CREATE TABLE "+Table_Name+"("
                + KEY_ID+" INTEGER PRIMARY KEY,"+Column_Name+" TEXT"+")";
        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void insert(String s) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Column_Name,s);

        db.insert(Table_Name,null,values);
        db.close();
    }

    public List<String> select() {
        List<String> modellist = new ArrayList<>();
        String sql = "SELECT DISTINCT search FROM " + Table_Name;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        try {

            if (cursor.moveToFirst()) {
                do {
                    modellist.add(cursor.getString(cursor.getColumnIndex(Column_Name)));
                } while (cursor.moveToNext());


            }
        } catch (Exception e) {
            Exception m = e;

        }
cursor.close();
        return modellist;
    }


}
