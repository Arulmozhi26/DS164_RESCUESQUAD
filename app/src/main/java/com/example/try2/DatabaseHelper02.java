package com.example.try2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper02 extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseHelper";

    private static final String TABLE_NAME = "people_tab";
    private static final String COL1 = "ID";
    private static final String COL2 = "TITLE";
    private static final String COL3 = "DESCR";
    private static final String COL4 = "LATLNG";
    private static final String COL5 = "TRACK";

    public DatabaseHelper02(Context context) {
        super(context, TABLE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL2 +" TEXT,"+COL3+" TEXT, "+COL4+" TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean addData(String item1,String item2,String item3,String item4) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL2, item1);
        contentValues.put(COL3,item2);
        contentValues.put(COL4,item3);
        contentValues.put(COL5,item4);

        long result = db.insert(TABLE_NAME, null, contentValues);
        Log.d(TAG, "addData: Adding " + item1 +" to " + TABLE_NAME);



        //if date as inserted incorrectly it will return -1
        if (result== -1 ) {
            return false;
        } else {
            return true;
        }
    }


    public Cursor getData(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME;
        Cursor data = db.rawQuery(query, null);
        return data;
    }


    public Cursor getItemID(String name){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT " + COL1 + " FROM " + TABLE_NAME +
                " WHERE " + COL2 + " = '" + name + "'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }


    public boolean updateName(int ID, String TITLE,String DESCR){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL2, TITLE);
        contentValues.put(COL3,DESCR);
        db.update(TABLE_NAME,contentValues,"ID=?",new String[]{String.valueOf(ID)});
        return true;
    }


    public int deleteName(int ID){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME,"ID=?",new String[]{String.valueOf(ID)});
    }

}


