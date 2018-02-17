package bioskop.cari.aliagus.com.caribioskop.detail_content;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.CoordinatorLayout;
import android.util.Log;
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
import bioskop.cari.aliagus.com.caribioskop.main_content.MainContentActivity;
import bioskop.cari.aliagus.com.caribioskop.model.Movie;
import bioskop.cari.aliagus.com.caribioskop.utils.Animated;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ali on 11/02/18.
 */

public class DetailFragment extends BottomSheetDialogFragment implements DetailFragmentContract.View, View.OnClickListener {

    private static final String TAG = DetailFragment.class.getSimpleName();
    public static final int VISIBLE = 1;
    public static final int GONE = 2;
    private static final int ADD_MOVIE = 1;
    private static final int REMOVE_MOVIE = 2;
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
    @BindView(R.id.image_favorite_detail_not_full)
    ImageView imageViewFavoriteDetailNotFull;
    @BindView(R.id.image_favorite_detail_full)
    ImageView imageViewFavoriteDetailFull;
    ProgressDialog progressDialog;
    private Context context;
    private Movie movie;
    private Toast toast;
    private String players;
    boolean isFirstTime = true;
    private Integer position;

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
        Log.d(TAG, "created");
        mPresenter = new DetailFragmentPresenter(this, context);
        initProgressDialog();
        setLayout();
        mPresenter.loadData(movie);
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
    public void loadDataToView(
            List<String> listGenres,
            List<String> listPlayers,
            String duration,
            boolean isFavorite
    ) {
        if (listPlayers != null) {
            players = String.valueOf(listPlayers);
            players = players.replaceAll("\\[", " ");
            players = players.replaceAll("]", " ");
            textViewPlayers.setText(players);
        }
        if (isFavorite) {
            imageViewFavoriteDetailFull.setVisibility(View.VISIBLE);
            imageViewFavoriteDetailNotFull.setVisibility(View.GONE);
        } else {
            imageViewFavoriteDetailFull.setVisibility(View.GONE);
            imageViewFavoriteDetailNotFull.setVisibility(View.VISIBLE);
        }
        textViewTitleContentDetail.setText(movie.getTitle());
        textViewDurationDetail.setText(" " + duration + " min");
        synopsisDetail.setText(movie.getOverView());
        dateRelease.setText(movie.getReleaseDate());
        String genres = listGenres.toString();
        genres = genres.replaceAll("\\[", " ");
        genres = genres.replaceAll("]", " ");
        textViewGenreContentDetail.setText(genres);
        rateMovie.setText(movie.getVoteAverage() + "/10");
        setImageDetail(movie.getPosterPath());
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
        checkProgressDialog();
    }

    public void setMovie(View view) {
        this.movie = (Movie) view.getTag();
        this.position = (Integer) view.getTag(R.integer.key_position);
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @OnClick({
            R.id.image_trailler,
            R.id.image_favorite_detail_not_full,
            R.id.image_favorite_detail_full
    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_trailler:
                mPresenter.getDataTrailler(movie.getId());
                dismissAllowingStateLoss();
                break;
            case R.id.image_favorite_detail_not_full:
                Animated.animatedView(view, GONE);
                Animated.animatedView(imageViewFavoriteDetailFull, VISIBLE);
                mPresenter.addOrRemoveMovie(movie, ADD_MOVIE);
                break;
            case R.id.image_favorite_detail_full:
                Animated.animatedView(view, GONE);
                Animated.animatedView(imageViewFavoriteDetailNotFull, VISIBLE);
                mPresenter.addOrRemoveMovie(movie, REMOVE_MOVIE);
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
        if (isFirstTime) progressDialog.show();
    }

    private void initProgressDialog() {
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (isAdded()) {
            isFirstTime = false;
        }
    }

    private void checkProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        ((MainContentActivity) getActivity()).notifyPosition(position);
    }
}
