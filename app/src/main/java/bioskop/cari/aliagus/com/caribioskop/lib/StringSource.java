package bioskop.cari.aliagus.com.caribioskop.lib;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ali on 11/02/18.
 */

public class StringSource {
    public static final String BASE_URL = "https://api.themoviedb.org/3/";
    public static final String BASE_GET_MOVIE = BASE_URL+"movie/";
    public static final String API_KEY = "api_key=/*key API*/";
    public static final String GET_ALL_GENRES = BASE_URL + "genre/movie/list?" + API_KEY + "&language=en-In";
    public static final String GET_NOW_PLAYING_MOVIE = BASE_URL + "movie/now_playing?" + API_KEY + "&language=en-ID&page=1&region=ID";
    public static final String GET_POPULAR_MOVIE = BASE_URL + "discover/movie?" + API_KEY +
            "&language=en-US&region=ID&sort_by=popularity.desc&include_adult=false&include_video=false&page=1&year=2018";
    public static final String GET_COMING_SOON_MOVIE = BASE_URL + "movie/upcoming?" + API_KEY + "&language=en-US&page=1&region=ID%7CUS";
    public static final String GET_TRAILLER = "/videos?" + API_KEY + "&language=en-US";
    public static final String GET_ALL_PLAYERS = "/credits?" + API_KEY;
    public static final String GET_DETAIL = "?" + API_KEY;
    public static final String GET_IMAGE_MOVIE = "https://image.tmdb.org/t/p/";
    public static final String SIZE_IMAGE_ADAPTER = "w300";
    public static final String SIZE_IMAGE_DETAIL = "w500";

    public static final String LAST_UPDATE = "lastUpdate";
    public static final String DURATION = "duration";

    //keyValue
    public static final String TABLE_KEY_VALUE = "_kv";
    public static final String COL_KEY = "_key";
    public static final String COL_VALUE = "_value";

    public static final String[] colomnKeyValue = new String[]{
            TABLE_KEY_VALUE,
            COL_KEY,
            COL_VALUE
    };

    public static final String CREATE_TABLE_KEY_VALUE = "CREATE TABLE IF NOT EXISTS "
            + colomnKeyValue[0] +
            "("
            + colomnKeyValue[1]
            + " TEXT, "
            + colomnKeyValue[2] +
            " TEXT"
            + ")";
    //movie
    public static final String TABLE_MOVIE = "_npl";
    public static final String COL_NO = "_no";
    public static final String COL_ID = "_id";
    public static final String COL_TITLE = "_title";
    public static final String COL_VOTE_AVERAGE = "_voteAverage";
    public static final String COL_POPULARITY = "_popularity";
    public static final String COL_OVERVIEW = "_overView";
    public static final String COL_POSTER_PATH = "_posterPath";
    public static final String COL_RELEASE_DATE = "_releaseDate";
    public static final String COL_GENRES = "_genres";

    public static final String[] colomnMovieNowPlaying = new String[]{
            TABLE_MOVIE,
            COL_NO,
            COL_ID,
            COL_TITLE,
            COL_VOTE_AVERAGE,
            COL_POPULARITY,
            COL_OVERVIEW,
            COL_POSTER_PATH,
            COL_RELEASE_DATE,
            COL_GENRES
    };
    public static final String CREATE_TABLE_MOVIE_NOW_PLAYING = "CREATE TABLE IF NOT EXISTS "
            + colomnMovieNowPlaying[0] +
            "("
            + colomnMovieNowPlaying[1] +
            " TEXT, "
            + colomnMovieNowPlaying[2] +
            " VARCHAR(10), "
            + colomnMovieNowPlaying[3] +
            " VARCHAR(50), "
            + colomnMovieNowPlaying[4] +
            " VARCHAR(15), "
            + colomnMovieNowPlaying[5] +
            " VARCHAR(15), "
            + colomnMovieNowPlaying[6] +
            " VARCHAR(500), "
            + colomnMovieNowPlaying[7] +
            " VARCHAR(100), "
            + colomnMovieNowPlaying[8] +
            " VARCHAR(15), "
            + colomnMovieNowPlaying[9] +
            " VARCHAR(25)"
            + ")";

    //popular
    public static final String TABLE_POPULAR = "_mpo";
    public static final String[] colomnMoviePopular = new String[]{
            TABLE_POPULAR,
            COL_NO,
            COL_ID,
            COL_TITLE,
            COL_VOTE_AVERAGE,
            COL_POPULARITY,
            COL_OVERVIEW,
            COL_POSTER_PATH,
            COL_RELEASE_DATE,
            COL_GENRES
    };
    public static final String CREATE_TABLE_MOVIE_POPULAR = "CREATE TABLE IF NOT EXISTS "
            + colomnMoviePopular[0] +
            "("
            + colomnMoviePopular[1] +
            " TEXT, "
            + colomnMoviePopular[2] +
            " VARCHAR(10), "
            + colomnMoviePopular[3] +
            " VARCHAR(50), "
            + colomnMoviePopular[4] +
            " VARCHAR(15), "
            + colomnMoviePopular[5] +
            " VARCHAR(15), "
            + colomnMoviePopular[6] +
            " VARCHAR(500), "
            + colomnMoviePopular[7] +
            " VARCHAR(100), "
            + colomnMoviePopular[8] +
            " VARCHAR(15), "
            + colomnMoviePopular[9] +
            " VARCHAR(25)"
            + ")";

    //comingsoon
    public static final String TABLE_COMING_SOON = "_cms";
    public static final String[] colomnMovieComingSoon = new String[]{
            TABLE_COMING_SOON,
            COL_NO,
            COL_ID,
            COL_TITLE,
            COL_VOTE_AVERAGE,
            COL_POPULARITY,
            COL_OVERVIEW,
            COL_POSTER_PATH,
            COL_RELEASE_DATE,
            COL_GENRES
    };
    public static final String CREATE_TABLE_MOVIE_COMING_SOON = "CREATE TABLE IF NOT EXISTS "
            + colomnMovieComingSoon[0] +
            "("
            + colomnMovieComingSoon[1] +
            " TEXT, "
            + colomnMovieComingSoon[2] +
            " VARCHAR(10), "
            + colomnMovieComingSoon[3] +
            " VARCHAR(50), "
            + colomnMovieComingSoon[4] +
            " VARCHAR(15), "
            + colomnMovieComingSoon[5] +
            " VARCHAR(15), "
            + colomnMovieComingSoon[6] +
            " VARCHAR(500), "
            + colomnMovieComingSoon[7] +
            " VARCHAR(100), "
            + colomnMovieComingSoon[8] +
            " VARCHAR(15), "
            + colomnMovieComingSoon[9] +
            " VARCHAR(25)"
            + ")";

    //favorite
    public static final String TABLE_FAVORITE = "_fvr";
    public static final String[] colomnFavorites = new String[]{
            TABLE_FAVORITE,
            COL_NO,
            COL_ID,
            COL_TITLE,
            COL_VOTE_AVERAGE,
            COL_POPULARITY,
            COL_OVERVIEW,
            COL_POSTER_PATH,
            COL_RELEASE_DATE,
            COL_GENRES
    };
    public static final String CREATE_TABLE_MOVIE_FAVORITE = "CREATE TABLE IF NOT EXISTS "
            + colomnFavorites[0] +
            "("
            + colomnFavorites[1] +
            " TEXT, "
            + colomnFavorites[2] +
            " VARCHAR(10), "
            + colomnFavorites[3] +
            " VARCHAR(50), "
            + colomnFavorites[4] +
            " VARCHAR(15), "
            + colomnFavorites[5] +
            " VARCHAR(15), "
            + colomnFavorites[6] +
            " VARCHAR(500), "
            + colomnFavorites[7] +
            " VARCHAR(100), "
            + colomnFavorites[8] +
            " VARCHAR(15), "
            + colomnFavorites[9] +
            " VARCHAR(25)"
            + ")";

    //genres
    public static final String COL_NAME = "_name";
    public static final String TABLE_GENRES = "_genres";
    public static final String COL_ID_GENRE = "_id";
    public static final String[] colomnGenres = new String[]{
            TABLE_GENRES,
            COL_ID_GENRE,
            COL_NAME
    };

    public static final String CREATE_TABLE_GENRES = "CREATE TABLE IF NOT EXISTS "
            + colomnGenres[0] +
            "("
            + colomnGenres[1]
            + " TEXT, "
            + colomnGenres[2] +
            " TEXT"
            + ")";

    public static final List<String[]> LIST_ALL_TABLE() {
        List<String[]> listStringArrays = new ArrayList<>();
        listStringArrays.add(colomnMovieNowPlaying);
        listStringArrays.add(colomnMoviePopular);
        listStringArrays.add(colomnMovieComingSoon);
        return listStringArrays;
    }
}
