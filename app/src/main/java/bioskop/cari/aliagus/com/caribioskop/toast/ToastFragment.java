package bioskop.cari.aliagus.com.caribioskop.toast;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.CoordinatorLayout;
import android.view.View;
import android.widget.TextView;

import bioskop.cari.aliagus.com.caribioskop.MainActivity;
import bioskop.cari.aliagus.com.caribioskop.R;
import bioskop.cari.aliagus.com.caribioskop.main_content.MainContentActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ali on 11/02/18.
 */

public class ToastFragment extends BottomSheetDialogFragment implements View.OnClickListener {

    private View view;
    private static ToastFragment toastFragment;
    @BindView(R.id.container_toast)
    ConstraintLayout constraintLayoutToast;
    @BindView(R.id.txt_cancel)
    TextView textViewCancel;
    @BindView(R.id.txt_refresh)
    TextView textViewRefresh;
    @BindView(R.id.text_message)
    TextView textViewMessage;
    private String message;

    public static ToastFragment getInstance(Context context) {
        if (toastFragment == null) {
            toastFragment = new ToastFragment();
        }
        return toastFragment;
    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        bundle.putString("message", message);
        super.onSaveInstanceState(bundle);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (savedInstanceState != null && message == null) {
            message = savedInstanceState.getString("message");
        }
        return super.onCreateDialog(savedInstanceState);
    }

    @Override
    public void setupDialog(Dialog dialog, int style) {
        view = View.inflate(getContext(), R.layout.fragment_toast, null);
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
        constraintLayoutToast.post(new Runnable() {
            @Override
            public void run() {
                int heightCoodinatorLayoutCountainer = constraintLayoutToast
                        .getHeight();
                ((BottomSheetBehavior) behavior).setPeekHeight(heightCoodinatorLayoutCountainer);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!message.equals("")) {
            textViewMessage.setText(message);
        }
    }

    @OnClick({
            R.id.txt_refresh,
            R.id.txt_cancel
    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txt_refresh:
                if (getActivity().getClass().equals(MainActivity.class)) {
                    ((MainActivity) getActivity()).resumeActivity();
                } else if (getActivity().getClass().equals(MainContentActivity.class)) {
                    ((MainContentActivity) getActivity()).resumeActivity();
                }
                break;

            case R.id.txt_cancel:
                dismissAllowingStateLoss();
                getActivity().onBackPressed();
                break;
        }
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
