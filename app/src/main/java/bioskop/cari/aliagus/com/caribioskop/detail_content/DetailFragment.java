package bioskop.cari.aliagus.com.caribioskop.detail_content;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.CoordinatorLayout;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;

import java.util.List;

import bioskop.cari.aliagus.com.caribioskop.R;
import bioskop.cari.aliagus.com.caribioskop.lib.StringSource;
import bioskop.cari.aliagus.com.caribioskop.model.Movie;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ali on 11/02/18.
 */

public class DetailFragment extends BottomSheetDialogFragment implements DetailFragmentContract.View, View.OnClickListener {

    private View view;
    private DetailFragmentPresenter mPresenter;
    @BindView(R.id.image_content_detail)
    ImageView imageContentDetail;
    @BindView(R.id.title_content_detail)
    TextView textViewTitleContentDetail;
    @BindView(R.id.duration_content_detail)
    TextView textViewDurationDetail;
    @BindView(R.id.genre_content_detail)
    TextView textViewGenreContentDetail;
    @BindView(R.id.text_synopsis_detail)
    TextView synopsisDetail;
    @BindView(R.id.date_content_detail)
    TextView dateRelease;
    @BindView(R.id.rate_content_rate)
    TextView rateMovie;
    @BindView(R.id.container_detail)
    RelativeLayout relativeLayoutContainerDetail;
    @BindView(R.id.image_trailler)
    ImageView imageViewTrailler;
    @BindView(R.id.text_players_detail)
    TextView textViewPlayers;
    @BindView(R.id.progress_image_loading)
    ProgressBar progressBarImageLoading;
    private Context context;
    private Movie movie;
    private Toast toast;
    private String players;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null && movie == null) {
            movie = (Movie) savedInstanceState.getSerializable("movie");
            players = savedInstanceState.getString("players");
        }
        context = getContext();
    }

    @Override
    public void setupDialog(Dialog dialog, int style) {
        view = View.inflate(getContext(), R.layout.fragment_detail_layout, null);
        ButterKnife.bind(this, view);
        dialog.setContentView(view);
        mPresenter = new DetailFragmentPresenter(this, context);
        setLayout();
    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putSerializable("movie", movie);
        bundle.putString("players", textViewPlayers.getText().toString());
    }

    private void setLayout() {
        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) ((View) view.getParent()).getLayoutParams();
        layoutParams.setMargins(0, 0, 0, 0);
        final CoordinatorLayout.Behavior behavior = layoutParams.getBehavior();
        assert behavior != null;
        ((BottomSheetBehavior) behavior).setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                ((BottomSheetBehavior) behavior).setState(BottomSheetBehavior.STATE_COLLAPSED);
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                //nothing
            }
        });
        relativeLayoutContainerDetail.post(new Runnable() {
            @Override
            public void run() {
                int heightCoodinatorLayoutCountainer = relativeLayoutContainerDetail
                        .getHeight();
                ((BottomSheetBehavior) behavior).setPeekHeight(heightCoodinatorLayoutCountainer);
            }
        });
    }

    @Override
    public void loadDataToView(List<String> listGenres, List<String> listPlayers) {
        if (listPlayers != null) {
            players = String.valueOf(listPlayers);
            players = players.replaceAll("\\[", " ");
            players = players.replaceAll("]", " ");
            textViewPlayers.setText(players);
        }
        setImageDetail(movie.getPosterPath());
        textViewTitleContentDetail.setText(movie.getTitle());
        synopsisDetail.setText(movie.getOverView());
        dateRelease.setText(movie.getReleaseDate());
        String genres = listGenres.toString();
        genres = genres.replaceAll("\\[", " ");
        genres = genres.replaceAll("]", " ");
        textViewGenreContentDetail.setText(genres);
        rateMovie.setText(movie.getVoteAverage());
    }

    private void setImageDetail(String image) {
        String urlImage = StringSource.GET_IMAGE_MOVIE + StringSource.SIZE_IMAGE_DETAIL + image;
        imageContentDetail.setScaleType(ImageView.ScaleType.FIT_XY);
        GlideUrl glideUrlPhotoProfile = new GlideUrl(
                urlImage,
                new LazyHeaders.Builder()
                        .build()
        );
        Glide.with(getContext())
                .load(glideUrlPhotoProfile)
                .error(R.drawable.ic_broken_image_gray_24dp)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(imageContentDetail);
        progressBarImageLoading.setVisibility(View.GONE);
    }

    public void setMovie(View view) {
        this.movie = (Movie) view.getTag();
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @OnClick({
            R.id.image_trailler
    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_trailler:
                mPresenter.getDataTrailler(movie.getId());
                dismissAllowingStateLoss();
                break;
        }
    }

    @Override
    public void showToast() {
        toast = Toast.makeText(context, R.string.error_trailler, Toast.LENGTH_SHORT);
        toast.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.loadData(movie);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (isAdded()) {
            //dismissAllowingStateLoss();
        }
    }
}
