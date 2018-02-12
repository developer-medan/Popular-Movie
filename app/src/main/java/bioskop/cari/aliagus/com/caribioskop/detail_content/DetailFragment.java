package bioskop.cari.aliagus.com.caribioskop.detail_content;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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

/**
 * Created by ali on 11/02/18.
 */

public class DetailFragment extends Fragment implements DetailFragmentContract.View {

    View view;
    DetailFragmentPresenter mPresenter;
    private View movieView;
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
    private Context context;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_detail_layout, null);
        ButterKnife.bind(this, view);
        mPresenter = new DetailFragmentPresenter(this, context);
        mPresenter.loadDataToView(movieView);
        return view;
    }

    @Override
    public void loadDataToView(List<String> listGenres) {
        Movie movie = (Movie) movieView.getTag();
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
        String urlImage = StringSource.GET_IMAGE_MOVIE + StringSource.SIZE_IMAGE_ADAPTER + image;
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
    }

    public void setMovie(View movie) {
        this.movieView = movie;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
