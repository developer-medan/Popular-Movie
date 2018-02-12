package bioskop.cari.aliagus.com.caribioskop.main_content;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import java.util.ArrayList;

import bioskop.cari.aliagus.com.caribioskop.R;
import bioskop.cari.aliagus.com.caribioskop.about.About;
import bioskop.cari.aliagus.com.caribioskop.database.InspectionDatabase;
import bioskop.cari.aliagus.com.caribioskop.detail_content.DetailFragment;
import bioskop.cari.aliagus.com.caribioskop.lib.StringSource;
import bioskop.cari.aliagus.com.caribioskop.now_playing.NowPlayingFragment;
import bioskop.cari.aliagus.com.caribioskop.top_rated.TopRatedFragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainContentActivity extends AppCompatActivity implements MainContentContract.View,
        View.OnClickListener, BottomNavigationView.OnNavigationItemSelectedListener {

    MainContentActivityPresenter mPresenter;
    @BindView(R.id.bottom_navigation_view)
    BottomNavigationView mBottomNavigationView;
    private Menu menuNavBottom;
    boolean isMainContent = true;
    private About about;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_content);
        ButterKnife.bind(this);
        initPresenter();
        initBottomNavigation();
    }

    private void initBottomNavigation() {
        menuNavBottom = mBottomNavigationView.getMenu();
        bottomNavigationViowGetTreeObserver(menuNavBottom);
        mBottomNavigationView.setOnNavigationItemSelectedListener(this);
    }

    private void bottomNavigationViowGetTreeObserver(final Menu menuNavBottom) {
        final ArrayList<View> menuItems = new ArrayList<>(3);
        mBottomNavigationView.getViewTreeObserver()
                .addOnGlobalLayoutListener(
                        new ViewTreeObserver.OnGlobalLayoutListener() {
                            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                            @Override
                            public void onGlobalLayout() {
                                mBottomNavigationView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                                for (int a = 0; a < 3; a++) {
                                    String id = "";
                                    if (a == 0) {
                                        id = "bottom_navigation_now_playing";
                                    } else if (a == 1) {
                                        id = "botton_navigation_top_rated";
                                    } else if (a == 2) {
                                        id = "botton_navigation_popular";
                                    }
                                    MenuItem menuItem = menuNavBottom.findItem(getResources().getIdentifier(id, "id", getPackageName()));
                                    mBottomNavigationView.findViewsWithText(menuItems, menuItem.getTitle(), View.FIND_VIEWS_WITH_TEXT);
                                }
                                for (View menuItem : menuItems) {
                                    ((TextView) menuItem).setTypeface(Typeface.create("sans-serif-condensed", Typeface.NORMAL));
                                }
                            }
                        }
                );
    }

    private void initPresenter() {
        mPresenter = new MainContentActivityPresenter(this);
    }

    @OnClick({
    })
    public void onClick(View view) {
        Intent intent = new Intent(MainContentActivity.this, InspectionDatabase.class);
        startActivity(intent);
        switch (view.getId()) {
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        attachFagmentToActivity(StringSource.GET_NOW_PLAYING_MOVIE);
    }

    private void attachFagmentToActivity(String urlData) {
        NowPlayingFragment nowPlayingFragment = new NowPlayingFragment();
        nowPlayingFragment.setUrlData(urlData);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_layout_content_activity, nowPlayingFragment)
                .commitAllowingStateLoss();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.botton_navigation_top_rated:
             //   attachFagmentToActivity();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_layout_content_activity, new TopRatedFragment())
                        .commitAllowingStateLoss();
                setTitle(R.string.top_rated_movie);
                isMainContent = true;
                break;

            case R.id.botton_navigation_popular:
                attachFagmentToActivity(StringSource.GET_POPULAR_MOVIE);
            /*getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_layout_content_activity, new PopularFragment())
                        .commitAllowingStateLoss();*/
                setTitle(R.string.popular_movie);
                isMainContent = true;
            break;

            case R.id.bottom_navigation_now_playing:
                attachFagmentToActivity(StringSource.GET_NOW_PLAYING_MOVIE);
                setTitle(R.string.now_playing);
                isMainContent = true;
                break;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.about:
                about = new About();
                about.setContext(getApplicationContext());
                about.show(
                        getSupportFragmentManager(),
                        about.getTag()
                );
                break;

            case R.id.test:
                Intent intent = new Intent(MainContentActivity.this, InspectionDatabase.class);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //if (isMainContent) {
            moveTaskToBack(true);
        /*} else {
            attachFagmentToActivity();
            isMainContent = true;
        }*/
    }

    public void showDetailMovie(View view) {
        isMainContent = false;
        DetailFragment detailFragment = new DetailFragment();
        detailFragment.setMovie(view);
        detailFragment.setContext(getApplicationContext());
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_layout_content_activity, detailFragment)
                .commitAllowingStateLoss();
    }

}
