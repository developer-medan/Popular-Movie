package bioskop.cari.aliagus.com.caribioskop.popular;

/**
 * Created by ali on 11/02/18.
 */

public class PopularFragmentPresenter implements PopularFragmentContract.Presenter {

    private PopularFragmentContract.View view;

    public PopularFragmentPresenter(PopularFragmentContract.View view) {
        this.view = view;
    }
}
