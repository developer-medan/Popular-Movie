package bioskop.cari.aliagus.com.caribioskop.fragment_content;

import android.content.Context;

import java.util.HashMap;
import java.util.List;

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

public class ContentMovieFragmentPresenter implements ContentMovieFragmentContract.Presenter {

    private static final String TAG = ContentMovieFragmentPresenter.class.getSimpleName();
    private ContentMovieFragmentContract.View view;
    private Context context;
    private ProviderObservables providerObservables;


    public ContentMovieFragmentPresenter(ContentMovieFragmentContract.View view, Context context) {
        this.view = view;
        this.context = context;
        providerObservables = new ProviderObservables(context);
    }

    @Override
    public void loadData(String urlData) {
        Observable<HashMap<String, Object>> observable = providerObservables.getObservableMovie(urlData);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HashMap<String, Object>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(HashMap<String, Object> map) {
                        view.loadDataToAdapter(
                                (List<Movie>) map.get("listMovie"),
                                (List<Integer>) map.get("listType")
                        );
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e != null) view.showToastFragment(e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
