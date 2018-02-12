package bioskop.cari.aliagus.com.caribioskop.detail_content;

import android.content.Context;
import android.view.View;

import java.util.List;

import bioskop.cari.aliagus.com.caribioskop.database.DatabaseManagerHelper;
import bioskop.cari.aliagus.com.caribioskop.lib.StringSource;
import bioskop.cari.aliagus.com.caribioskop.model.Movie;

/**
 * Created by ali on 11/02/18.
 */

public class DetailFragmentPresenter implements DetailFragmentContract.Presenter {

    private DetailFragmentContract.View view;
    private Context context;
    DatabaseManagerHelper databaseManagerHelper;

    public DetailFragmentPresenter(DetailFragmentContract.View view, Context context) {
        this.view = view;
        this.context = context;
    }

    @Override
    public void loadDataToView(View view) {
        Movie movie = (Movie) view.getTag();
        databaseManagerHelper = DatabaseManagerHelper.getInstance(context);
        List<String> listGenres = databaseManagerHelper.getListGenres(
                StringSource.colomnGenres,
                movie.getGenresList()
        );
        this.view.loadDataToView(listGenres);
    }
}
