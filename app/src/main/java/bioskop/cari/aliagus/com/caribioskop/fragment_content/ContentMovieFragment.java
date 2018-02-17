package bioskop.cari.aliagus.com.caribioskop.fragment_content;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import java.util.List;

import bioskop.cari.aliagus.com.caribioskop.R;
import bioskop.cari.aliagus.com.caribioskop.adapter.adapter_content.AdapterContentMovie;
import bioskop.cari.aliagus.com.caribioskop.detail_content.DetailFragment;
import bioskop.cari.aliagus.com.caribioskop.main_content.MainContentActivity;
import bioskop.cari.aliagus.com.caribioskop.model.Movie;
import butterknife.BindView;
import butterknife.ButterKnife;
import dmax.dialog.SpotsDialog;

/**
 * Created by ali on 11/02/18.
 */

public class ContentMovieFragment extends Fragment implements ContentMovieFragmentContract.View,
        AdapterContentMovie.ListenerAdapterContentMovie {

    private static final String TAG = ContentMovieFragment.class.getSimpleName();
    private static final int ADD_MOVIE = 1;
    private static final int REMOVE_MOVIE = 2;
    View view;
    ContentMovieFragmentPresenter mPresenter;
    AdapterContentMovie adapterContentMovie;
    @BindView(R.id.recycler_now_playing)
    RecyclerView recyclerView_now_playing;
    @BindView(R.id.fragment_content_movie_logo)
    RelativeLayout relativeLayoutLogo;
    Context context;
    AlertDialog pDialog;
    private String urlData;
    DetailFragment detailFragment;
    private String filter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_movie_content, null);
        ButterKnife.bind(this, view);
        mPresenter = new ContentMovieFragmentPresenter(this, getContext());
        initProgressDialog();
        mPresenter.loadData(urlData, filter);
        Log.d(TAG, "created");
        return view;
    }

    @Override
    public void loadDataToAdapter(
            List<Movie> movieList,
            List<Integer> listType,
            String message) {
        int colomn = 2;
        if (message != null) {
            showToastFragment(message);
        }
        checkMovieList(movieList);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, colomn);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(context, DividerItemDecoration.VERTICAL);
        adapterContentMovie = new AdapterContentMovie(movieList, listType, this);
        adapterContentMovie.setContext(getContext());
        adapterContentMovie.setFilter(filter);
        recyclerView_now_playing.setHasFixedSize(true);
        recyclerView_now_playing.setLayoutManager(gridLayoutManager);
        //recyclerView_now_playing.addItemDecoration(dividerItemDecoration);
        recyclerView_now_playing.setAdapter(adapterContentMovie);
        checkProgressDialog();
    }

    @Override
    public void showToastFragment(String message) {
        if (getActivity() != null) {
            ((MainContentActivity) getActivity()).showToastFragment(message);
            checkProgressDialog();
        }
    }

    @Override
    public void refreshAdapter(List<Movie> listMovie, List<Integer> listTypes) {
        checkMovieList(listMovie);
        adapterContentMovie.refresh(listMovie, listTypes);
    }

    @Override
    public void refreshAdapterPosition(Integer position) {
        adapterContentMovie.refreshPosition(position);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onHolderClick(View view) {
        detailFragment = new DetailFragment();
        detailFragment.setMovie(view);
        detailFragment.setContext(getContext());
        detailFragment.show(
                getFragmentManager(),
                detailFragment.getTag()
        );
    }

    @Override
    public void onImageFavoriteWhiteNotFull(View view) {
        mPresenter.saveOrRemoveMovieToFavorite(
                view,
                ADD_MOVIE,
                filter
        );
    }

    @Override
    public void onImageFavoriteWhiteFull(View view) {
        mPresenter.saveOrRemoveMovieToFavorite(
                view,
                REMOVE_MOVIE,
                filter);
    }

    private void initProgressDialog() {
        pDialog = new SpotsDialog(getContext(), R.style.ProgressDialogStyle);
        pDialog.setCancelable(true);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pDialog.show();
    }

    public void setUrlData(String urlData) {
        this.urlData = urlData;
    }

    @Override
    public void onPause() {
        super.onPause();
        checkProgressDialog();
    }

    public void checkProgressDialog() {
        if (pDialog != null && pDialog.isShowing()) {
            pDialog.dismiss();
        }
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    private void checkMovieList(List<Movie> movieList) {
        if (movieList.size() == 0) {
            relativeLayoutLogo.setVisibility(View.VISIBLE);
        } else {
            relativeLayoutLogo.setVisibility(View.GONE);
        }
    }
}
