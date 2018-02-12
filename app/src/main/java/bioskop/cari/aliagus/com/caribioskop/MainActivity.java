package bioskop.cari.aliagus.com.caribioskop;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import bioskop.cari.aliagus.com.caribioskop.main_content.MainContentActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import dmax.dialog.SpotsDialog;

public class MainActivity extends AppCompatActivity implements MainActivityContract.View{

    private static final String TAG = MainActivity.class.getSimpleName();
    MainActivityPresenter mPresenter;

    @BindView(R.id.mainContainer)
    ConstraintLayout mConstraintLayout;
    private static final int PERMISSION_REQUEST_CODE = 101;
    private AlertDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        hideNavigationBar();
        initPresenter();
        initProgressDialog();
    }

    private void hideNavigationBar() {
        mConstraintLayout = (ConstraintLayout)findViewById(R.id.mainContainer);
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
            pDialog.show();
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
                pDialog.show();
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
        checkPermission();
    }

    @Override
    public void jumpToMainContent() {
        toMainContent();
        if (pDialog.isShowing()) {
            pDialog.dismiss();
        }
    }

    private void initProgressDialog() {
        pDialog = new SpotsDialog(MainActivity.this, R.style.ProgressDialogStyle);
        pDialog.setCancelable(true);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pDialog.show();
    }
}
