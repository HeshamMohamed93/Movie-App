package com.example.hesham.fimovie;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.sql.SQLException;

/**
 * Created by hesham on 09/10/15.
 */
public class DBAdapter {
    static final String KEY_ROWID = "_id";
    static final String KEY_TITLE = "title";
    static final String KEY_Date = "date";
    static final String KEY_Poster = "poster";
    static final String KEY_Vote = "vote";
    static final String KEY_Desc = "desc";
    static final String TAG = "DBAdapter";
    static final String DATABASE_NAME = "MyDB";
    static final String DATABASE_TABLE = "Movies";
    static final int DATABASE_VERSION = 6;

    static final String DATABASE_CREATE =
            "create table " +DATABASE_TABLE+" (_id integer primary key , "
                    + "title text not null, date text not null, poster text not null, vote text not null, desc text not null);";

    final Context context;
    DatabaseHelper DBHelper;
    SQLiteDatabase db;

    public DBAdapter(Context ctx) {
        this.context = ctx;
        DBHelper = new DatabaseHelper(context);
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DATABASE_CREATE);
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
        {
            Log.w(TAG, "Upgrading database from version " + oldVersion + "to"
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS contacts");
            onCreate(db);
        }
    }
    public DBAdapter open() throws SQLException
    {
        db= DBHelper.getWritableDatabase();
        return this;
    }

    public void close()
    {
        DBHelper.close();
    }

    public long insertContact(String id, String title, String date , String poster , String vote, String desc)
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_ROWID, id);
        initialValues.put(KEY_TITLE, title);
        initialValues.put(KEY_Date, date);
        initialValues.put(KEY_Poster, poster);
        initialValues.put(KEY_Vote, vote);
        initialValues.put(KEY_Desc, desc);
        return db.insert(DATABASE_TABLE, null, initialValues);
    }

    public boolean deleteContact(String rowId)
    {
        return db.delete(DATABASE_TABLE, KEY_ROWID+ "="+ rowId, null) > 0;
    }

    public Cursor getAllContacts()
    {
        return db.query(DATABASE_TABLE, new String[]{KEY_ROWID, KEY_TITLE,
                KEY_Date, KEY_Poster, KEY_Vote, KEY_Desc}, null, null, null, null, null);
    }



}
