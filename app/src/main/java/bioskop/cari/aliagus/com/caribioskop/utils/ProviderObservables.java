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
    private static final String TAG = ProviderObservables.class.getSimpleName();
    private Context context;
    private DatabaseManagerHelper databaseManagerHelper;
    public static final int MOVIE_CONTENT = 1;
    public static final int LOADING_CONTENT = 2;

    public ProviderObservables(Context context) {
        this.context = context;
        databaseManagerHelper = DatabaseManagerHelper.getInstance(context);
    }

    public Observable<HashMap<String, Object>> getObservableMovie(final String urlData) {
        return Observable.create(new ObservableOnSubscribe<HashMap<String, Object>>() {
            @Override
            public void subscribe(ObservableEmitter<HashMap<String, Object>> emitter) throws Exception {
                boolean isConnect = ConnectionProvider.networkStatus(context);
                if (isConnect) {
                    requestToGetAllMoviesNowPlaying(emitter, urlData);
                }
            }
        }).subscribeOn(Schedulers.io());
    }

    private void requestToGetAllMoviesNowPlaying(final ObservableEmitter<HashMap<String, Object>> emitter, String urlData) {
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
                                List<Movie> listAllMovie = databaseManagerHelper.getAllMovieFromDatabase(
                                        StringSource.colomnMovie
                                );
                                if (listAllMovie.size() == 0) {
                                    databaseManagerHelper.bulkInsertMovieToDatabase(listMovie);
                                }
                                HashMap<String, Object> hasmap = new HashMap<>();
                                hasmap.put("listMovie", listMovie);
                                hasmap.put("listType", listType);
                                emitter.onNext(hasmap);
                                emitter.onComplete();
                            } catch (Exception ex) {
                                emitter.onError(ex);
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
                                emitter.onNext("");
                                emitter.onComplete();
                            } catch (Exception ex) {
                                emitter.onError(ex);
                            }
                        }
                    }
                });
    }
}
