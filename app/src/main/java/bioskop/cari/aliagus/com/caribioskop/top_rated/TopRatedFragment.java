package bioskop.cari.aliagus.com.caribioskop.top_rated;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import bioskop.cari.aliagus.com.caribioskop.R;
import butterknife.ButterKnife;

/**
 * Created by ali on 11/02/18.
 */

public class TopRatedFragment extends Fragment implements TopRatedFragmentContract.View {

    View view;
    TopRatedFragmentPresenter mPresenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_top_rated, null);
        mPresenter = new TopRatedFragmentPresenter(this);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
