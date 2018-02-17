package bioskop.cari.aliagus.com.caribioskop.about;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.CoordinatorLayout;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.TextView;

import bioskop.cari.aliagus.com.caribioskop.BuildConfig;
import bioskop.cari.aliagus.com.caribioskop.R;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ali on 12/02/18.
 */

public class About extends BottomSheetDialogFragment {

    private View view;
    @BindView(R.id.container_about)
    ConstraintLayout constraintLayoutContainerFragment;
    private Context context;
    @BindView(R.id.version_name_about)
    TextView textViewVersionName;

    @Override
    public void setupDialog(Dialog dialog, int style) {
        view = View.inflate(getContext(), R.layout.fragment_about, null);
        ButterKnife.bind(this, view);
        dialog.setContentView(view);
        setLayout();
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
        View parent = (View) view.getParent();
        parent.setFitsSystemWindows(true);
        BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(parent);
        view.measure(0, 0);
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int screenHeight = displaymetrics.heightPixels;
        bottomSheetBehavior.setPeekHeight(screenHeight);
        layoutParams.height = screenHeight;
        parent.setLayoutParams(layoutParams);
        textViewVersionName.setText(BuildConfig.VERSION_NAME);
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
