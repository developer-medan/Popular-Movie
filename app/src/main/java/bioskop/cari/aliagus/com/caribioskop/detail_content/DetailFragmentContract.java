package bioskop.cari.aliagus.com.caribioskop.detail_content;

import java.util.List;

import bioskop.cari.aliagus.com.caribioskop.model.Movie;

/**
 * Created by ali on 11/02/18.
 */

public interface DetailFragmentContract {
    interface View {
        void loadDataToView(List<String> listGenres, List<String> listPlayers);

        void showToast();
    }

    interface Presenter {
        void loadData(Movie movie);

        void getDataTrailler(String id);
    }

}
