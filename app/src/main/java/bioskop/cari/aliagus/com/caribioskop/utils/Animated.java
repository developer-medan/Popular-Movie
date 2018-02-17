package bioskop.cari.aliagus.com.caribioskop.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.view.View;

import bioskop.cari.aliagus.com.caribioskop.adapter.adapter_content.AdapterContentMovie;

/**
 * Created by ali on 16/02/18.
 */

public class Animated {


    private static final int VISIBLE = 1;

    public static final void animatedView(final View view, final int visibility) {
        float alpa;
        float y;
        if (visibility == VISIBLE) {
            alpa = 1.0f;
            y = 0;
        } else {
            alpa = 0.0f;
            y = view.getHeight();
        }
        view.animate()
                .translationY(y)
                .alpha(alpa)
                .setDuration(300)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        if (visibility == VISIBLE) {
                            view.setVisibility(View.VISIBLE);
                        } else {
                            view.setVisibility(View.GONE);
                        }
                    }
                });
    }

    public static final void animatedView(final View view,
                                          final AdapterContentMovie.ListenerAdapterContentMovie mListener
    ) {
        view.animate()
                .translationY(view.getHeight())
                .alpha(0.0f)
                .setDuration(300)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        view.setVisibility(View.GONE);
                        mListener.onImageFavoriteWhiteFull(view);
                    }
                });
    }
}

