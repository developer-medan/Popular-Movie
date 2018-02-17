package bioskop.cari.aliagus.com.caribioskop.utils;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import bioskop.cari.aliagus.com.caribioskop.database.DatabaseManagerHelper;
import bioskop.cari.aliagus.com.caribioskop.lib.StringSource;
import bioskop.cari.aliagus.com.caribioskop.model.Movie;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by ali on 11/02/18.
 */

public class ProviderObservables {
    public static final int MOVIE_CONTENT = 1;
    public static final int LOADING_CONTENT = 2;
    private static final String TAG = ProviderObservables.class.getSimpleName();
    private Context context;
    private DatabaseManagerHelper databaseManagerHelper;

    public ProviderObservables(Context context) {
        this.context = context;
        databaseManagerHelper = DatabaseManagerHelper.getInstance(context);
    }

    public Observable<List<String>> getObservableTrailer(final String id) {
        return Observable.create(new ObservableOnSubscribe<List<String>>() {
            @Override
            public void subscribe(final ObservableEmitter<List<String>> emitter) throws Exception {
                boolean isConnect = ConnectionProvider.networkStatus(context);
                if (isConnect) {
                    requestToGetAllTrailer(emitter, id);
                } else {
                    emitter.onError(new Throwable("You Have No Internet Connection..."));
                }
            }
        }).subscribeOn(Schedulers.io());
    }

    private void requestToGetAllTrailer(final ObservableEmitter<List<String>> emitter, String id) {
        Request request = new Request.Builder()
                .url(StringSource.BASE_GET_MOVIE + id + StringSource.GET_TRAILLER)
                .build();
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(1000 * 180, TimeUnit.MILLISECONDS)
                .readTimeout(1000 * 180, TimeUnit.MILLISECONDS)
                .writeTimeout(1000 * 180, TimeUnit.MILLISECONDS)
                .build();
        client.newCall(request)
                .enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        emitter.onError(new Throwable(""));
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.code() == 200) {
                            try {
                                String responseMessage = response.body().string();
                                JSONObject jsonObject = new JSONObject(responseMessage);
                                JSONArray jsonArray = jsonObject.getJSONArray("results");
                                List<String> listStringKey = new ArrayList<>();
                                for (int a = 0; a < jsonArray.length(); a++) {
                                    JSONObject jsonObjectItem = jsonArray.getJSONObject(a);
                                    String key = jsonObjectItem.getString("key");
                                    listStringKey.add(key);
                                }
                                emitter.onNext(listStringKey);
                                emitter.onComplete();
                            } catch (Exception ex) {
                                emitter.onError(new Throwable(""));
                            }
                        }
                    }
                });
    }

    public Observable<HashMap<String, Object>> getObservableMovie(final String urlData, final String filter) {
        return Observable.create(new ObservableOnSubscribe<HashMap<String, Object>>() {
            @Override
            public void subscribe(ObservableEmitter<HashMap<String, Object>> emitter) throws Exception {
                boolean isConnect = ConnectionProvider.networkStatus(context);
                String message = "Please check your Internet Connection...";
                if (isConnect && !filter.equals("favorite")) {
                    requestToGetAllMovies(emitter, urlData, filter);
                } else {
                    String[] arrayStringColomn = new String[0];
                    if (filter.equals("now")) {
                        arrayStringColomn = StringSource.colomnMovieNowPlaying;
                    } else if (filter.equals("popular")) {
                        arrayStringColomn = StringSource.colomnMoviePopular;
                    } else if (filter.equals("soon")) {
                        arrayStringColomn = StringSource.colomnMovieComingSoon;
                    } else if (filter.equals("favorite")) {
                        arrayStringColomn = StringSource.colomnFavorites;
                        message = null;
                    }
                    List<Movie> listAllMovie = databaseManagerHelper.getAllMovieFromDatabase(
                            arrayStringColomn
                    );
                    List<Integer> listType = new ArrayList<>();
                    for (Movie movie : listAllMovie) {
                        listType.add(MOVIE_CONTENT);
                    }
                    HashMap<String, Object> hasmap = new HashMap<>();
                    hasmap.put("listMovie", listAllMovie);
                    hasmap.put("listType", listType);
                    hasmap.put("message", message);
                    emitter.onNext(hasmap);
                    emitter.onComplete();
                }
            }
        }).subscribeOn(Schedulers.io());
    }

    private void requestToGetAllMovies(
            final ObservableEmitter<HashMap<String, Object>> emitter,
            String urlData,
            final String filter
    ) {
        final Request request = new Request.Builder()
                .url(urlData)
                .addHeader("Accept", "application/json")
                .addHeader("Content-Type", "application/json")
                .build();

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(1000 * 180, TimeUnit.MILLISECONDS)
                .readTimeout(1000 * 180, TimeUnit.MILLISECONDS)
                .writeTimeout(1000 * 180, TimeUnit.MILLISECONDS)
                .build();
        client.newCall(request)
                .enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        emitter.onError(e);
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String resonseMessage = response.body().string();
                        int code = response.code();
                        if (code == 200) {
                            try {
                                //todo get json response
                                JSONObject jsonObjectResult = new JSONObject(resonseMessage);
                                JSONArray jsonArrayMovie = jsonObjectResult.getJSONArray("results");
                                List<Movie> listMovie = new ArrayList<>();
                                List<Integer> listType = new ArrayList<>();
                                for (int a = 0; a < jsonArrayMovie.length(); a++) {
                                    JSONObject jsonObjectItem = jsonArrayMovie.getJSONObject(a);
                                    List<String> listIdGenres = new ArrayList<>();
                                    if (jsonObjectItem.has("genre_ids")) {
                                        JSONArray jsonArrayIdGenres = jsonObjectItem.getJSONArray("genre_ids");
                                        for (int i = 0; i < jsonArrayIdGenres.length(); i++) {
                                            listIdGenres.add(String.valueOf(jsonArrayIdGenres.get(i)));
                                        }
                                    }
                                    Movie movie = new Movie();
                                    movie.setId(String.valueOf(jsonObjectItem.getInt("id")));
                                    movie.setPopularity(String.valueOf(jsonObjectItem.get("popularity")));
                                    movie.setTitle(jsonObjectItem.getString("title"));
                                    movie.setOverView(jsonObjectItem.getString("overview"));
                                    movie.setReleaseDate(jsonObjectItem.getString("release_date"));
                                    movie.setPosterPath(jsonObjectItem.getString("poster_path"));
                                    movie.setVoteAverage(String.valueOf(jsonObjectItem.get("vote_average")));
                                    movie.setGenresList(listIdGenres);
                                    listMovie.add(movie);
                                    listType.add(MOVIE_CONTENT);
                                }

                                String[] arrayStringColomn = new String[0];
                                if (filter.equals("now")) {
                                    arrayStringColomn = StringSource.colomnMovieNowPlaying;
                                } else if (filter.equals("popular")) {
                                    arrayStringColomn = StringSource.colomnMoviePopular;
                                } else if (filter.equals("soon")) {
                                    arrayStringColomn = StringSource.colomnMovieComingSoon;
                                }

                                List<Movie> listAllMovie = databaseManagerHelper.getAllMovieFromDatabase(
                                        arrayStringColomn
                                );
                                if (listAllMovie.size() == 0) {
                                    databaseManagerHelper.bulkInsertMovieToDatabase(listMovie, arrayStringColomn);
                                } else {
                                    databaseManagerHelper.insertMovieToDatabase(
                                            arrayStringColomn,
                                            listMovie
                                    );
                                }
                                HashMap<String, Object> hasmap = new HashMap<>();
                                hasmap.put("listMovie", listMovie);
                                hasmap.put("listType", listType);
                                emitter.onNext(hasmap);
                                emitter.onComplete();
                            } catch (Exception ex) {
                                emitter.onError(new Throwable(""));
                            }
                        }
                    }
                });
    }

    public Observable<String> getObservableGenresMovie() {
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                boolean isConnect = ConnectionProvider.networkStatus(context);
                if (isConnect) {
                    requestToGetAllGenres(emitter);
                } else {
                    emitter.onError(new Throwable("You Have No Internet Connection..."));
                }
            }
        }).subscribeOn(Schedulers.io());
    }

    private void requestToGetAllGenres(final ObservableEmitter emitter) {
        final Request request = new Request.Builder()
                .url(StringSource.GET_ALL_GENRES)
                .addHeader("Accept", "application/json")
                .addHeader("Content-Type", "application/json")
                .build();

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(1000 * 180, TimeUnit.MILLISECONDS)
                .readTimeout(1000 * 180, TimeUnit.MILLISECONDS)
                .writeTimeout(1000 * 180, TimeUnit.MILLISECONDS)
                .build();
        client.newCall(request)
                .enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        emitter.onError(e);
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String responMessage = response.body().string();
                        if (response.code() == 200) {
                            try {
                                JSONObject jsonObject = new JSONObject(responMessage);
                                JSONArray jsonArray = jsonObject.getJSONArray("genres");
                                List<JSONObject> listJsonGenres = new ArrayList<>();
                                for (int a = 0; a < jsonArray.length(); a++) {
                                    JSONObject jsonObjectItem = jsonArray.getJSONObject(a);
                                    listJsonGenres.add(jsonObjectItem);
                                }
                                List<String> listId = databaseManagerHelper.getListIdGenres(StringSource.colomnGenres);
                                if (listId.size() == 0) {
                                    databaseManagerHelper.bulkInsertGenresToDatabase(listJsonGenres);
                                }
                                Long time = System.currentTimeMillis();
                                String currentDateString = String.valueOf(time);
                                databaseManagerHelper.insertToTbKV(
                                        StringSource.colomnKeyValue,
                                        StringSource.LAST_UPDATE,
                                        currentDateString
                                );
                                emitter.onNext("");
                                emitter.onComplete();
                            } catch (Exception ex) {
                                emitter.onError(new Throwable(""));
                            }
                        }
                    }
                });
    }

    public Observable<JSONObject> getObservablesListPlayers(final String id) {
        return Observable.create(new ObservableOnSubscribe<JSONObject>() {
            @Override
            public void subscribe(ObservableEmitter<JSONObject> emitter) throws Exception {
                boolean isConnect = ConnectionProvider.networkStatus(context);
                if (isConnect) {
                    requestToGetAllPlayers(emitter, id);
                } else {
                    emitter.onError(new Throwable("You Have No Internet Connection..."));
                }
            }
        }).subscribeOn(Schedulers.io());
    }

    private void requestToGetAllPlayers(final ObservableEmitter<JSONObject> emitter, final String id) {
        final Request request = new Request.Builder()
                .url(StringSource.BASE_GET_MOVIE + id + StringSource.GET_ALL_PLAYERS)
                .addHeader("Accept", "application/json")
                .addHeader("Content-Type", "application/json")
                .build();

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(1000 * 180, TimeUnit.MILLISECONDS)
                .readTimeout(1000 * 180, TimeUnit.MILLISECONDS)
                .writeTimeout(1000 * 180, TimeUnit.MILLISECONDS)
                .build();
        client.newCall(request)
                .enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        emitter.onError(e);
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String responMessage = response.body().string();
                        if (response.code() == 200) {
                            try {
                                JSONObject jsonObject = new JSONObject(responMessage);
                                JSONArray jsonArray = jsonObject.getJSONArray("cast");
                                List<String> listPlayers = new ArrayList<>();
                                JSONObject jsonObjectData = new JSONObject();
                                for (int a = 0; a < jsonArray.length(); a++) {
                                    JSONObject jsonObjectItem = jsonArray.getJSONObject(a);
                                    String player = "";
                                    if (!jsonObjectItem.getString("character").equals("")) {
                                        String name = jsonObjectItem.getString("name");
                                        String character = jsonObjectItem.getString("character");
                                        player = name + " as " + character;
                                    } else {
                                        player = jsonObjectItem.getString("name");
                                    }
                                    listPlayers.add(player);
                                }
                                String duration = databaseManagerHelper.getRowTbKV(
                                        StringSource.colomnKeyValue,
                                        id
                                );
                                if (duration.equals("")) {
                                    requestToGetDuration(
                                            id,
                                            emitter,
                                            listPlayers
                                    );
                                } else {
                                    jsonObjectData.put("listPlayers", listPlayers);
                                    jsonObjectData.put("duration", duration);
                                    emitter.onNext(jsonObjectData);
                                    emitter.onComplete();
                                }
                            } catch (Exception ex) {
                                emitter.onError(new Throwable(""));
                            }
                        }
                    }
                });
    }

    public void requestToGetDuration(
            final String id,
            final ObservableEmitter<JSONObject> emitter,
            final List<String> listPlayers
    ) {
        final Request request = new Request.Builder()
                .url(StringSource.BASE_GET_MOVIE + id + StringSource.GET_DETAIL)
                .build();

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(1000 * 180, TimeUnit.MILLISECONDS)
                .readTimeout(1000 * 180, TimeUnit.MILLISECONDS)
                .writeTimeout(1000 * 180, TimeUnit.MILLISECONDS)
                .build();
        client.newCall(request)
                .enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String responMessage = response.body().string();
                        JSONObject jsonObjectData = new JSONObject();
                        if (response.code() == 200) {
                            try {
                                JSONObject jsonObject = new JSONObject(responMessage);
                                String runtimeTemp = String.valueOf(jsonObject.get("runtime"));
                                String duration = "0";
                                if (!runtimeTemp.equals("null")) {
                                    int runtime = jsonObject.getInt("runtime");
                                    duration = String.valueOf(runtime);
                                    databaseManagerHelper.insertToTbKV(
                                            StringSource.colomnKeyValue,
                                            id,
                                            duration
                                    );
                                }
                                jsonObjectData.put("listPlayers", listPlayers);
                                jsonObjectData.put("duration", duration);
                                emitter.onNext(jsonObjectData);
                                emitter.onComplete();
                            } catch (Exception ex) {
                                emitter.onError(new Throwable(""));
                            }
                        }
                    }
                });
    }

    public Observable<HashMap<String, Object>> saveMovieToFavoriteDatabase(
            final Movie movie,
            final int movieCode,
            final String filter
    ) {
        return Observable.create(new ObservableOnSubscribe<HashMap<String, Object>>() {
            @Override
            public void subscribe(ObservableEmitter<HashMap<String, Object>> emitter) throws Exception {
                List<Movie> list = new ArrayList<>();
                list.add(movie);
                if (movieCode == 1) {
                    databaseManagerHelper.insertMovieToDatabase(
                            StringSource.colomnFavorites,
                            list
                    );
                } else {
                    databaseManagerHelper.deleteRow(
                            movie.getId(),
                            StringSource.colomnFavorites
                    );
                }
                if (filter.equals("favorite")) {
                    List<Movie> listAllMovie = databaseManagerHelper.getAllMovieFromDatabase(
                            StringSource.colomnFavorites
                    );
                    List<Integer> listType = new ArrayList<>();
                    for (Movie movie : listAllMovie) {
                        listType.add(MOVIE_CONTENT);
                    }
                    HashMap<String, Object> hasmap = new HashMap<>();
                    hasmap.put("listMovie", listAllMovie);
                    hasmap.put("listType", listType);
                    emitter.onNext(hasmap);
                    emitter.onComplete();
                }
            }
        }).subscribeOn(Schedulers.io());
    }
}
