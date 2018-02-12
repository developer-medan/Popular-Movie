package bioskop.cari.aliagus.com.caribioskop;

import android.content.Context;
import android.util.Log;

import bioskop.cari.aliagus.com.caribioskop.utils.ProviderObservables;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by ali on 10/02/18.
 */

public class MainActivityPresenter implements MainActivityContract.Presenter{

    private MainActivityContract.View view;
    private Context context;
    ProviderObservables providerObservables;

    public MainActivityPresenter(MainActivityContract.View view, Context context) {
        this.view = view;
        this.context = context;
    }

    @Override
    public void getAllGenresMovie() {
        providerObservables = new ProviderObservables(context);
        Observable<String> observable = providerObservables.getObservableGenresMovie();
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String s) {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("testingsss1111", "errer");

                    }

                    @Override
                    public void onComplete() {
                        view.jumpToMainContent();
                    }
                });
    }
}
