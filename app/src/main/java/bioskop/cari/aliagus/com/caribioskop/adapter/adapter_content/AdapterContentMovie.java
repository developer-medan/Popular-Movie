package bioskop.cari.aliagus.com.caribioskop.adapter.adapter_content;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
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
import bioskop.cari.aliagus.com.caribioskop.database.DatabaseManagerHelper;
import bioskop.cari.aliagus.com.caribioskop.lib.StringSource;
import bioskop.cari.aliagus.com.caribioskop.model.Movie;
import bioskop.cari.aliagus.com.caribioskop.utils.Animated;

/**
 * Created by ali on 11/02/18.
 */

public class AdapterContentMovie extends RecyclerView.Adapter<AdapterContentMovie.ViewHolder> implements View.OnClickListener {

    List<Movie> movieList;
    List<Integer> listTypes;
    private static final String TAG = AdapterContentMovie.class.getSimpleName();
    public static final int MOVIE_CONTENT = 1;
    public static final int LOADING_CONTENT = 2;
    public static final int VISIBLE = 1;
    public static final int GONE = 2;
    private ListenerAdapterContentMovie mListener;
    private View view;
    private Context context;
    private String filter;

    public AdapterContentMovie(
            List<Movie> movieList,
            List<Integer> listTypes,
            ListenerAdapterContentMovie mListener
    ) {
        this.movieList = movieList;
        this.listTypes = listTypes;
        this.mListener = mListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == MOVIE_CONTENT) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.custom_content_layout, null);
            return new ViewHolderContent(view);
        } else {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.custom_loading, null);
            return new ViewHolderLoading(view);
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        switch (viewType) {
            case MOVIE_CONTENT:
                ViewHolderContent viewHolderContent = (ViewHolderContent) holder;
                Movie movie = movieList.get(position);
                viewHolderContent.bind(movie);
                viewHolderContent.itemView.setTag(movie);
                viewHolderContent.itemView.setTag(R.integer.key_position, position);
                viewHolderContent.itemView.setOnClickListener(this);
                break;

            case LOADING_CONTENT:
                ViewHolderLoading viewHolderLoading = (ViewHolderLoading) holder;
                break;
        }
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return listTypes.get(position);
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    public void onClick(View view) {
        mListener.onHolderClick(view);
    }

    public void refresh(List<Movie> listMovie, List<Integer> listTypes) {
        this.movieList = listMovie;
        this.listTypes = listTypes;
        notifyDataSetChanged();
    }

    public void refreshPosition(Integer position) {
        notifyItemChanged(position);
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

    public class ViewHolderContent extends ViewHolder implements View.OnClickListener {
        private final ImageView mImageFavoriteWhiteNotFull;
        private final ImageView mImageFavoriteWhiteFull;
        private ImageView mImageContent;
        private TextView mTitleContent;

        public ViewHolderContent(View itemView) {
            super(itemView);
            mImageContent = (ImageView) itemView.findViewById(R.id.image_content);
            mTitleContent = (TextView) itemView.findViewById(R.id.title_content);
            mImageFavoriteWhiteNotFull = (ImageView) itemView.findViewById(R.id.image_favorite_white_not_full);
            mImageFavoriteWhiteFull = (ImageView) itemView.findViewById(R.id.image_favorite_white_full);
        }

        public void bind(Movie movie) {
            DatabaseManagerHelper databaseManagerHelper = DatabaseManagerHelper.getInstance(context);
            List<String> listId = databaseManagerHelper.getListId(StringSource.colomnFavorites);
            if (listId.contains(movie.getId())) {
                mImageFavoriteWhiteNotFull.setVisibility(View.GONE);
                mImageFavoriteWhiteFull.setVisibility(View.VISIBLE);
            } else {
                mImageFavoriteWhiteFull.setVisibility(View.GONE);
                mImageFavoriteWhiteNotFull.setVisibility(View.VISIBLE);
            }
            String image = movie.getPosterPath();
            String urlImage = StringSource.GET_IMAGE_MOVIE + StringSource.SIZE_IMAGE_ADAPTER + image;
            String title = movie.getTitle();
            mImageFavoriteWhiteNotFull.setTag(movie);
            mImageFavoriteWhiteFull.setTag(movie);
            mImageFavoriteWhiteNotFull.setOnClickListener(this);
            mImageFavoriteWhiteFull.setOnClickListener(this);
            mTitleContent.setText(title);
            mImageContent.setScaleType(ImageView.ScaleType.FIT_XY);
            GlideUrl glideUrlPhotoProfile = new GlideUrl(
                    urlImage,
                    new LazyHeaders.Builder()
                            .build()
            );
            Glide.with(context)
                    .load(glideUrlPhotoProfile)
                    .error(R.drawable.ic_broken_image_gray_24dp)
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .into(mImageContent);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.image_favorite_white_not_full:
                    if (!filter.equals("favorite")) {
                        Animated.animatedView(mImageFavoriteWhiteFull, VISIBLE);
                        Animated.animatedView(view, GONE);
                        mListener.onImageFavoriteWhiteNotFull(view);
                    }
                    break;

                case R.id.image_favorite_white_full:
                    if (filter.equals("favorite")) {
                        Animated.animatedView(view, mListener);
                        notifyItemChanged(getAdapterPosition());
                    } else {
                        Animated.animatedView(mImageFavoriteWhiteNotFull, VISIBLE);
                        Animated.animatedView(view, GONE);
                        mListener.onImageFavoriteWhiteFull(view);
                    }
                    break;
            }
        }
    }

    public class ViewHolderLoading extends ViewHolder {
        public ViewHolderLoading(View itemView) {
            super(itemView);
        }
    }

    public interface ListenerAdapterContentMovie {
        void onHolderClick(View view);

        void onImageFavoriteWhiteNotFull(View view);

        void onImageFavoriteWhiteFull(View view);
    }
}
