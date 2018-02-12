package bioskop.cari.aliagus.com.caribioskop.main_content;

/**
 * Created by ali on 11/02/18.
 */

public class MainContentActivityPresenter implements MainContentContract.Presenter {

    private MainContentContract.View view;

    public MainContentActivityPresenter(MainContentContract.View view) {
        this.view = view;
    }

}
