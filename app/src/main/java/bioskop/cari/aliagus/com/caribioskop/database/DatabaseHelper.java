package bioskop.cari.aliagus.com.caribioskop.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import bioskop.cari.aliagus.com.caribioskop.lib.StringSource;

/**
 * Created by ali on 11/02/18.
 */

public class DatabaseHelper extends SQLiteOpenHelper{

    //database version
    public static final int DATABASE_VERSION = 5;
    //database name
    public static final String DATABASE_NAME = "kv.db";

    private static DatabaseHelper instance;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static synchronized DatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseHelper(context);
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(StringSource.CREATE_TABLE_KEY_VALUE);
        db.execSQL(StringSource.CREATE_TABLE_MOVIE_NOW_PLAYING);
        db.execSQL(StringSource.CREATE_TABLE_MOVIE_POPULAR);
        db.execSQL(StringSource.CREATE_TABLE_MOVIE_COMING_SOON);
        db.execSQL(StringSource.CREATE_TABLE_GENRES);
        db.execSQL(StringSource.CREATE_TABLE_MOVIE_FAVORITE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + StringSource.TABLE_KEY_VALUE);
        db.execSQL("DROP TABLE IF EXISTS " + StringSource.TABLE_MOVIE);
        db.execSQL("DROP TABLE IF EXISTS " + StringSource.TABLE_POPULAR);
        db.execSQL("DROP TABLE IF EXISTS " + StringSource.TABLE_COMING_SOON);
        db.execSQL("DROP TABLE IF EXISTS " + StringSource.TABLE_GENRES);
        db.execSQL("DROP TABLE IF EXISTS " + StringSource.TABLE_FAVORITE);
        onCreate(db);
    }
}
