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
import bioskop.cari.aliagus.com.caribioskop.lib.StringSource;
import bioskop.cari.aliagus.com.caribioskop.model.Movie;

/**
 * Created by ali on 11/02/18.
 */

public class AdapterContentMovie extends RecyclerView.Adapter<AdapterContentMovie.ViewHolder> implements View.OnClickListener {

    List<Movie> movieList;
    List<Integer> listTypes;
    private static final String TAG = AdapterContentMovie.class.getSimpleName();
    public static final int MOVIE_CONTENT = 1;
    public static final int LOADING_CONTENT = 2;
    private ListenerAdapterContentMovie mListener;
    private View view;
    private Context context;

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

    public void refreshAdapter(List<Movie> movieList, List<Integer> listTypes) {
        this.movieList = movieList;
        this.listTypes = listTypes;
        notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {
        mListener.onHolderClick(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

    public class ViewHolderContent extends ViewHolder {
        ImageView mImageContent;
        TextView mTitleContent;

        public ViewHolderContent(View itemView) {
            super(itemView);
            mImageContent = (ImageView) itemView.findViewById(R.id.image_content);
            mTitleContent = (TextView) itemView.findViewById(R.id.title_content);
        }

        public void bind(Movie movie) {
            String image = movie.getPosterPath();
            String urlImage = StringSource.GET_IMAGE_MOVIE + StringSource.SIZE_IMAGE_ADAPTER + image;
            String title = movie.getTitle();
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
    }

    public class ViewHolderLoading extends ViewHolder {
        public ViewHolderLoading(View itemView) {
            super(itemView);
        }
    }

    public interface ListenerAdapterContentMovie {
        void onHolderClick(View view);
    }
}
