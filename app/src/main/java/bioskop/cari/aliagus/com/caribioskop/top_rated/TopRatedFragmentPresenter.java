package bioskop.cari.aliagus.com.caribioskop.top_rated;

/**
 * Created by ali on 11/02/18.
 */

public class TopRatedFragmentPresenter implements TopRatedFragmentContract.Presenter {
    private TopRatedFragmentContract.View view;

    public TopRatedFragmentPresenter(TopRatedFragmentContract.View view) {
        this.view = view;
    }
}
