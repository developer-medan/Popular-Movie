package bioskop.cari.aliagus.com.caribioskop;

/**
 * Created by ali on 10/02/18.
 */

public interface MainActivityContract {

    interface View {
        void jumpToMainContent();
    }

    interface Presenter {
        void getAllGenresMovie();
    }

}
