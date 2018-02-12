package bioskop.cari.aliagus.com.caribioskop.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import bioskop.cari.aliagus.com.caribioskop.lib.StringSource;
import bioskop.cari.aliagus.com.caribioskop.model.Movie;

/**
 * Created by ali on 11/02/18.
 */

public class DatabaseManagerHelper extends DatabaseHelper{

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

    public void bulkInsertMovieToDatabase(List<Movie> movieList) {
        SQLiteDatabase db = databaseManager.openDatabase(TAG);
        String query = "INSERT OR REPLACE INTO "
                + StringSource.colomnMovie[0] +
                "("
                + StringSource.colomnMovie[1] +
                ", "
                + StringSource.colomnMovie[2] +
                ", "
                + StringSource.colomnMovie[3] +
                ", "
                + StringSource.colomnMovie[4] +
                ", "
                + StringSource.colomnMovie[5] +
                ", "
                + StringSource.colomnMovie[6] +
                ", "
                + StringSource.colomnMovie[7] +
                ") "
                + "VALUES (?, ?, ?, ?, ?, ?, ?) ";
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
                + arrayColomn[1] + " = '" + key + "'";
        Cursor cursor = sqLiteDatabase.rawQuery(selectQuery, null);
        Movie movie = new Movie();
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            movie.setId(cursor.getString(cursor.getColumnIndex(arrayColomn[1])));
            movie.setTitle(cursor.getString(cursor.getColumnIndex(arrayColomn[2])));
            movie.setVoteAverage(cursor.getString(cursor.getColumnIndex(arrayColomn[3])));
            movie.setPopularity(cursor.getString(cursor.getColumnIndex(arrayColomn[4])));
            movie.setOverView(cursor.getString(cursor.getColumnIndex(arrayColomn[5])));
            movie.setPosterPath(cursor.getString(cursor.getColumnIndex(arrayColomn[6])));
            movie.setReleaseDate(cursor.getString(cursor.getColumnIndex(arrayColomn[7])));
        } else {
            movie.setId("");
            movie.setTitle("");
            movie.setVoteAverage("");
            movie.setPopularity("");
            movie.setOverView("");
            movie.setPosterPath("");
            movie.setReleaseDate("");
        }
        cursor.close();
        return movie;
    }

    private List<String> getListId(String[] arrayColomn) {
        SQLiteDatabase sqLiteDatabase = databaseManager.openDatabase(TAG);
        String selectQuery = "SELECT * FROM " + arrayColomn[0];
        Cursor cursor = sqLiteDatabase.rawQuery(selectQuery, null);
        List<String> listIdMovie = new ArrayList<>();
        if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    listIdMovie.add(cursor.getString(cursor.getColumnIndex(arrayColomn[1])));
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
        List<String> listId= new ArrayList<>();
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
}
