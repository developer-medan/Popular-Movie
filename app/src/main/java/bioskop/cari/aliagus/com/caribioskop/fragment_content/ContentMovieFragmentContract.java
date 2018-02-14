package bioskop.cari.aliagus.com.caribioskop.fragment_content;

import java.util.List;

import bioskop.cari.aliagus.com.caribioskop.model.Movie;

/**
 * Created by ali on 11/02/18.
 */

public interface ContentMovieFragmentContract {
    interface View {

        void loadDataToAdapter(List<Movie> listMovie, List<Integer> listType);

        void showToastFragment(String message);
    }

    interface Presenter {
        void loadData(String urlData);
    }

}
