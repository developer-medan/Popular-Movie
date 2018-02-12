package bioskop.cari.aliagus.com.caribioskop.lib;

/**
 * Created by ali on 11/02/18.
 */

public class StringSource {
    public static final String BASE_URL = "https://api.themoviedb.org/3/";
    public static final String API_KEY = "api_key=e685a7fe7a4fe13cbd60d0d7432ad5b0";
    public static final String GET_ALL_GENRES = BASE_URL + "genre/movie/list?" + API_KEY + "&language=en-In";
    public static final String GENRE_ID = "28";
    public static final String GET_MOVIE_LIST_BY_GENRE_ID = BASE_URL +
            "genre/" + GENRE_ID + "/movies?" + API_KEY +
            "&language=en-US&include_adult=false&sort_by=created_at.asc";
    public static final String MOVIE_ID = "3";
    public static final String LEANGUAGE = "&language=en-US";
    public static final String GET_DETAIL_MOVIE_BY_ID = BASE_URL + "movie/" + MOVIE_ID + "?" + API_KEY + LEANGUAGE;
    /*BASE_URL + "/movie/" + input id movie id + "?" + API_KEY+ "&language=en-US"*/
    public static final String GET_NOW_PLAYING_MOVIE = BASE_URL+"movie/now_playing?" + API_KEY + "&language=en-US&page=1&region=ID";
    public static final String GET_POPULAR_MOVIE = BASE_URL + "discover/movie?" + API_KEY +
            "&language=en-US&region=ID&sort_by=popularity.desc&include_adult=false&include_video=false&page=1&year=2018";

    //https://image.tmdb.org/t/p/w500/kqjL17yufvn9OVLyXYpvtyrFfak.jpg
    public static final String GET_IMAGE_MOVIE = "https://image.tmdb.org/t/p/";
    public static final String SIZE_IMAGE_ADAPTER = "w300";
    public static final String SIZE_IMAGE_DETAIL = "w500";
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
    public static final String TABLE_MOVIE = "_mv";
    public static final String COL_ID = "_id";
    public static final String COL_TITLE = "_title";
    public static final String COL_VOTE_AVERAGE = "_voteAverage";
    public static final String COL_POPULARITY = "_popularity";
    public static final String COL_OVERVIEW = "_overView";
    public static final String COL_POSTER_PATH = "_posterPath";
    public static final String COL_RELEASE_DATE = "_releaseDate";

    public static final String[] colomnMovie = new String[]{
            TABLE_MOVIE,
            COL_ID,
            COL_TITLE,
            COL_VOTE_AVERAGE,
            COL_POPULARITY,
            COL_OVERVIEW,
            COL_POSTER_PATH,
            COL_RELEASE_DATE
    };
    public static final String CREATE_TABLE_MOVIE = "CREATE TABLE IF NOT EXISTS "
            + colomnMovie[0] +
            "("
            + colomnMovie[1] +
            " VARCHAR(10), "
            + colomnMovie[2] +
            " VARCHAR(50), "
            + colomnMovie[3] +
            " VARCHAR(15), "
            + colomnMovie[4] +
            " VARCHAR(15), "
            + colomnMovie[5] +
            " VARCHAR(500), "
            + colomnMovie[6] +
            " VARCHAR(100), "
            + colomnMovie[7] +
            " VARCHAR(15)"
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
}
