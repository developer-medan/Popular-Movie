package bioskop.cari.aliagus.com.caribioskop;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import bioskop.cari.aliagus.com.caribioskop.main_content.MainContentActivity;
import bioskop.cari.aliagus.com.caribioskop.toast.ToastFragment;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements MainActivityContract.View {

    private static final String TAG = MainActivity.class.getSimpleName();
    MainActivityPresenter mPresenter;

    @BindView(R.id.mainContainer)
    ConstraintLayout mConstraintLayout;
    private static final int PERMISSION_REQUEST_CODE = 101;
    private AlertDialog pDialog;
    ToastFragment toastFragment;
    @BindView(R.id.version_name)
    TextView textViewAppVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        hideNavigationBar();
        initPresenter();
    }

    private void hideNavigationBar() {
        String versionName = BuildConfig.VERSION_NAME;
        textViewAppVersion.setText(versionName);
        mConstraintLayout = (ConstraintLayout) findViewById(R.id.mainContainer);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mConstraintLayout.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        } else {
            mConstraintLayout.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    }

    private void initPresenter() {
        mPresenter = new MainActivityPresenter(this, getApplicationContext());
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void checkPermission() {
        boolean isPermissionStorageGranted = ActivityCompat
                .checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        boolean isPermissionAccessNetworkState = ActivityCompat
                .checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_NETWORK_STATE) == PackageManager.PERMISSION_GRANTED;
        boolean isPermissionInternet = ActivityCompat
                .checkSelfPermission(MainActivity.this, Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED;

        String[] listPermission = new String[]{
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.ACCESS_NETWORK_STATE,
                android.Manifest.permission.INTERNET
        };
        if (!isPermissionStorageGranted || !isPermissionAccessNetworkState || !isPermissionInternet) {
            requestPermissions(
                    listPermission,
                    PERMISSION_REQUEST_CODE
            );
            return;
        } else {
            mPresenter.getAllGenresMovie();
        }
    }

    private void toMainContent() {
        Intent intent = new Intent(MainActivity.this, MainContentActivity.class);
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            boolean isPermitted = true;
            for (int granted : grantResults) {
                if (granted == PackageManager.PERMISSION_DENIED) {
                    isPermitted = false;
                }
            }
            if (isPermitted) {
                mPresenter.getAllGenresMovie();
            } else {
                moveTaskToBack(true);
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onResume() {
        super.onResume();
        hideNavigationBar();
        checkPermission();
    }

    @Override
    public void jumpToMainContent() {
        toMainContent();
    }

    @Override
    public void showToastFragment(String message) {
        checkToasFragment();
        toastFragment = ToastFragment.getInstance(getApplicationContext());
        toastFragment.setMessage(message);
        toastFragment.show(
                getSupportFragmentManager(),
                toastFragment.getTag()
        );
    }

    public void checkToasFragment() {
        if (toastFragment != null && toastFragment.isAdded()) {
            toastFragment.dismissAllowingStateLoss();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        moveTaskToBack(true);
        finish();
    }

    public void resumeActivity() {
        checkToasFragment();
        onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        checkToasFragment();
    }
}
