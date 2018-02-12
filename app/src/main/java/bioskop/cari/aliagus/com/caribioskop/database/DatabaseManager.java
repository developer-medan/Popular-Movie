package bioskop.cari.aliagus.com.caribioskop.database;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by ali on 11/02/18.
 */

public class DatabaseManager {

    private static final String TAG = DatabaseManager.class.getSimpleName();
    private DatabaseHelper databaseHelper;
    private SQLiteDatabase sqLiteDatabase;

    public DatabaseManager(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

    public SQLiteDatabase openDatabase(String tag){
        if (sqLiteDatabase == null){
            sqLiteDatabase = databaseHelper.getWritableDatabase();
        } else if (!sqLiteDatabase.isOpen()){
            sqLiteDatabase = databaseHelper.getWritableDatabase();
        }
        return sqLiteDatabase;
    }

}
