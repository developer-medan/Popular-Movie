package bioskop.cari.aliagus.com.caribioskop.now_playing;

import java.util.List;

import bioskop.cari.aliagus.com.caribioskop.model.Movie;

/**
 * Created by ali on 11/02/18.
 */

public interface NowPlayingContract {
    interface View {

        void loadDataToAdapter(List<Movie> listMovie, List<Integer> listType);
    }

    interface Presenter {
        void loadData(String urlData);
    }

}
