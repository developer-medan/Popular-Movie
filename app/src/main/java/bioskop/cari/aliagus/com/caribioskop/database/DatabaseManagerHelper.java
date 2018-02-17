package bioskop.cari.aliagus.com.caribioskop.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import bioskop.cari.aliagus.com.caribioskop.lib.StringSource;
import bioskop.cari.aliagus.com.caribioskop.model.Movie;

/**
 * Created by ali on 11/02/18.
 */

public class DatabaseManagerHelper extends DatabaseHelper {

    private static final String TAG = DatabaseManagerHelper.class.getSimpleName();
    DatabaseManager databaseManager;
    private static DatabaseManagerHelper instance;
    private List<Movie> allMovieFromDatabase;

    public DatabaseManagerHelper(Context context) {
        super(context);
        databaseManager = new DatabaseManager(this);
    }

    public static synchronized DatabaseManagerHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseManagerHelper(context);
        }
        return instance;
    }

    public void bulkInsertMovieToDatabase(List<Movie> movieList, String[] arrayStringColomn) {
        SQLiteDatabase db = databaseManager.openDatabase(TAG);
        String query = "INSERT OR REPLACE INTO "
                + arrayStringColomn[0] +
                "("
                + arrayStringColomn[2] +
                ", "
                + arrayStringColomn[3] +
                ", "
                + arrayStringColomn[4] +
                ", "
                + arrayStringColomn[5] +
                ", "
                + arrayStringColomn[6] +
                ", "
                + arrayStringColomn[7] +
                ", "
                + arrayStringColomn[8] +
                ", "
                + arrayStringColomn[9] +
                ") "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?) ";
        try {
            db.beginTransaction();
            SQLiteStatement stmt = db.compileStatement(query);
            for (Movie movie : movieList) {
                stmt.clearBindings();
                stmt.bindString(1, movie.getId());
                stmt.bindString(2, movie.getTitle());
                stmt.bindString(3, movie.getVoteAverage());
                stmt.bindString(4, movie.getPopularity());
                stmt.bindString(5, movie.getOverView());
                stmt.bindString(6, movie.getPosterPath());
                stmt.bindString(7, movie.getReleaseDate());
                stmt.bindString(8, String.valueOf(movie.getGenresList()));
                stmt.execute();
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    public List<Movie> getAllMovieFromDatabase(String[] arrayColomn) {
        SQLiteDatabase sqLiteDatabase = databaseManager.openDatabase(TAG);
        String selectQuery = "SELECT * FROM " + arrayColomn[0];
        Cursor cursor = sqLiteDatabase.rawQuery(selectQuery, null);
        allMovieFromDatabase = new ArrayList<>();
        List<String> listIdCurrency = getListId(arrayColomn);

        if (cursor.getCount() > 0) {
            for (String key : listIdCurrency) {
                Movie movie = getMovieByKey(
                        arrayColomn,
                        key
                );
                allMovieFromDatabase.add(movie);
            }
        }
        return allMovieFromDatabase;
    }

    private Movie getMovieByKey(String[] arrayColomn, String key) {
        SQLiteDatabase sqLiteDatabase = databaseManager.openDatabase(TAG);
        String selectQuery = "SELECT * FROM " + arrayColomn[0] + " WHERE "
                + arrayColomn[2] + " = '" + key + "'";
        Cursor cursor = sqLiteDatabase.rawQuery(selectQuery, null);
        Movie movie = new Movie();
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            String genres = cursor.getString(cursor.getColumnIndex(arrayColomn[9]));
            genres = genres.replaceAll("\\[", "");
            genres = genres.replaceAll("]", "");
            genres = genres.trim();
            String[] genresArray = genres.split(",");
            List<String> listGenres = new ArrayList<>(Arrays.asList(genresArray));
            movie.setId(cursor.getString(cursor.getColumnIndex(arrayColomn[2])));
            movie.setTitle(cursor.getString(cursor.getColumnIndex(arrayColomn[3])));
            movie.setVoteAverage(cursor.getString(cursor.getColumnIndex(arrayColomn[4])));
            movie.setPopularity(cursor.getString(cursor.getColumnIndex(arrayColomn[5])));
            movie.setOverView(cursor.getString(cursor.getColumnIndex(arrayColomn[6])));
            movie.setPosterPath(cursor.getString(cursor.getColumnIndex(arrayColomn[7])));
            movie.setReleaseDate(cursor.getString(cursor.getColumnIndex(arrayColomn[8])));
            movie.setGenresList(listGenres);
        } else {
            movie.setId("");
            movie.setTitle("");
            movie.setVoteAverage("");
            movie.setPopularity("");
            movie.setOverView("");
            movie.setPosterPath("");
            movie.setReleaseDate("");
            movie.setGenresList(new ArrayList<String>());
        }
        cursor.close();
        return movie;
    }

    public List<String> getListId(String[] arrayColomn) {
        SQLiteDatabase sqLiteDatabase = databaseManager.openDatabase(TAG);
        String selectQuery = "SELECT * FROM " + arrayColomn[0];
        Cursor cursor = sqLiteDatabase.rawQuery(selectQuery, null);
        List<String> listIdMovie = new ArrayList<>();
        if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    listIdMovie.add(cursor.getString(cursor.getColumnIndex(arrayColomn[2])));
                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        return listIdMovie;
    }

    public void bulkInsertGenresToDatabase(List<JSONObject> listJsonGenres) {
        SQLiteDatabase db = databaseManager.openDatabase(TAG);
        String query = "INSERT OR REPLACE INTO "
                + StringSource.colomnGenres[0] +
                "("
                + StringSource.colomnGenres[1] +
                ", "
                + StringSource.colomnGenres[2] +
                ") "
                + "VALUES (?, ?) ";
        try {
            db.beginTransaction();
            SQLiteStatement stmt = db.compileStatement(query);
            for (JSONObject genres : listJsonGenres) {
                stmt.clearBindings();
                stmt.bindString(1, String.valueOf(genres.getInt("id")));
                stmt.bindString(2, genres.getString("name"));
                stmt.execute();
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    public List<String> getListIdGenres(String[] arrayColomn) {
        SQLiteDatabase sqLiteDatabase = databaseManager.openDatabase(TAG);
        String selectQuery = "SELECT * FROM " + arrayColomn[0];
        Cursor cursor = sqLiteDatabase.rawQuery(selectQuery, null);
        List<String> listId = new ArrayList<>();
        if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    listId.add(cursor.getString(cursor.getColumnIndex(arrayColomn[1])));
                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        return listId;
    }

    public List<String> getListGenres(String[] arrayStrings, List<String> genresListId) {
        List<String> listGenres = new ArrayList<>();
        for (int a = 0; a < genresListId.size(); a++) {
            SQLiteDatabase sqLiteDatabase = databaseManager.openDatabase(TAG);
            String selectQuery = "SELECT * FROM " + arrayStrings[0] + " WHERE "
                    + arrayStrings[1] + " = '" + genresListId.get(a) + "'";
            Cursor cursor = sqLiteDatabase.rawQuery(selectQuery, null);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                listGenres.add(cursor.getString(cursor.getColumnIndex(arrayStrings[2])));
            }
            cursor.close();
        }
        return listGenres;
    }

    public String getRowTbKV(
            String[] arrayStrings,
            String key
    ) {
        SQLiteDatabase db = databaseManager.openDatabase(TAG);
        String selectQuery = "SELECT * FROM " + arrayStrings[0] + " WHERE "
                + arrayStrings[1] + " = '" + key + "'";
        Cursor cursor = db.rawQuery(selectQuery, null);
        String value = "";
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            value = cursor.getString(cursor.getColumnIndex(arrayStrings[2]));
        }
        cursor.close();
        return value;
    }

    public void insertToTbKV(
            String[] arrayStrings,
            String key,
            String value
    ) {
        SQLiteDatabase db = databaseManager.openDatabase(TAG);
        ContentValues cv = new ContentValues();
        cv.put(arrayStrings[1], key);
        cv.put(arrayStrings[2], value);
        String result = getRowTbKV(arrayStrings, key);
        if (result.equals("")) {
            db.insert(arrayStrings[0], null, cv);
        } else if (!result.equals("") && !result.equals(value)) {
                Long time = Long.valueOf(result);
                Long now = Long.valueOf(value);
                Long timeNow = now - time;
                if (timeNow >= 600000) {
                    deleteRowTableById();
                    String where = arrayStrings[1] + " = ?";
                    String[] args = {key};
                    db.update(arrayStrings[0], cv, where, args);
            }
        }

    }

    private void deleteRowTableById() {
        List<String[]> listAllTable = StringSource.LIST_ALL_TABLE();
        for (String[] arrayStrings : listAllTable) {
            List<String> listId = getListId(arrayStrings);
            if (listId.size() > 0) {
                for (String id : listId) {
                    deleteRow(id, arrayStrings);
                    Log.d(TAG, "deleted " + id +" " + arrayStrings[0]);
                }
            }
        }
    }

    public void deleteRow(String id, String[] arrayStrings) {
        SQLiteDatabase sqLiteDatabase = databaseManager.openDatabase(TAG);
        String where = arrayStrings[2] + "= ?";
        String[] args = {id};
        sqLiteDatabase.delete(arrayStrings[0], where, args);
    }

    public void insertMovieToDatabase(
            String[] arrayStrings,
            List<Movie> listMovie
    ) {
        List<String> listId = getListId(arrayStrings);
        SQLiteDatabase sqLiteDatabase = databaseManager.openDatabase(TAG);
        for (Movie movie : listMovie) {
            if (!listId.contains(movie.getId())) {
                ContentValues contentValues = new ContentValues();
                contentValues.put(arrayStrings[2], movie.getId());
                contentValues.put(arrayStrings[3], movie.getTitle());
                contentValues.put(arrayStrings[4], movie.getVoteAverage());
                contentValues.put(arrayStrings[5], movie.getPopularity());
                contentValues.put(arrayStrings[6], movie.getOverView());
                contentValues.put(arrayStrings[7], movie.getPosterPath());
                contentValues.put(arrayStrings[8], movie.getReleaseDate());
                contentValues.put(arrayStrings[9], String.valueOf(movie.getGenresList()));
                sqLiteDatabase.insert(arrayStrings[0], null, contentValues);
            }
        }
    }
}
