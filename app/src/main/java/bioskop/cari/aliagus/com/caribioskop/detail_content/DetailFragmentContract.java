package bioskop.cari.aliagus.com.caribioskop.detail_content;

import java.util.List;

/**
 * Created by ali on 11/02/18.
 */

public interface DetailFragmentContract {
    interface View {

        void loadDataToView(List<String> listGenres);
    }

    interface Presenter {
        void loadDataToView(android.view.View genresList);
    }

}
