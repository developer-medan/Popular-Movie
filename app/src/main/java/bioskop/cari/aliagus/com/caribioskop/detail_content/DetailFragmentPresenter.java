package bioskop.cari.aliagus.com.caribioskop.detail_content;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import bioskop.cari.aliagus.com.caribioskop.database.DatabaseManagerHelper;
import bioskop.cari.aliagus.com.caribioskop.lib.StringSource;
import bioskop.cari.aliagus.com.caribioskop.model.Movie;
import bioskop.cari.aliagus.com.caribioskop.utils.ProviderObservables;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by ali on 11/02/18.
 */

public class DetailFragmentPresenter implements DetailFragmentContract.Presenter {

    private DetailFragmentContract.View view;
    private Context context;
    DatabaseManagerHelper databaseManagerHelper;
    ProviderObservables providerObservables;

    public DetailFragmentPresenter(DetailFragmentContract.View view, Context context) {
        this.view = view;
        this.context = context;
    }

    @Override
    public void loadData(Movie movie) {
        providerObservables = new ProviderObservables(context);
        databaseManagerHelper = DatabaseManagerHelper.getInstance(context);
        final List<String> listGenres = databaseManagerHelper.getListGenres(
                StringSource.colomnGenres,
                movie.getGenresList()
        );
        List<String> listId = databaseManagerHelper.getListId(StringSource.colomnFavorites);
        final boolean isFavorite = listId.contains(movie.getId());
        Observable<JSONObject> observableListPlayers = providerObservables.getObservablesListPlayers(movie.getId());
        observableListPlayers
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<JSONObject>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(JSONObject jsonObjectData) {
                        List<String> listPlayers = new ArrayList<>();
                        String duration = "0";
                        try {
                            listPlayers = (List<String>) jsonObjectData.get("listPlayers");
                            duration = jsonObjectData.getString("duration");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        view.loadDataToView(listGenres, listPlayers, duration, isFavorite);
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.loadDataToView(listGenres, null, "0", isFavorite);
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    @Override
    public void getDataTrailler(String id) {
        providerObservables = new ProviderObservables(context);
        Observable<List<String>> observableTrailer = providerObservables.getObservableTrailer(id);
        observableTrailer.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<String>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<String> keyList) {
                        if (keyList.size() != 0) {
                            for (String key : keyList) {
                                Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + key));
                                Intent webIntent = new Intent(Intent.ACTION_VIEW,
                                        Uri.parse("http://www.youtube.com/watch?v=" + key));
                                try {
                                    context.startActivity(appIntent);
                                    break;
                                } catch (ActivityNotFoundException ex) {
                                    context.startActivity(webIntent);
                                    break;
                                }
                            }
                        } else {
                            onError(new Throwable(""));
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.showToast();
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    @Override
    public void addOrRemoveMovie(Movie movie, int codeMovie) {
        providerObservables = new ProviderObservables(context);
        Observable<HashMap<String, Object>> observable = providerObservables.saveMovieToFavoriteDatabase(
                movie,
                codeMovie,
                ""
        );
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HashMap<String, Object>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(HashMap<String, Object> map) {
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }
}
