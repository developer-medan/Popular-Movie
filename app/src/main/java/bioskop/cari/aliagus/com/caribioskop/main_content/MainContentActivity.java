package bioskop.cari.aliagus.com.caribioskop.main_content;

import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import java.util.ArrayList;

import bioskop.cari.aliagus.com.caribioskop.R;
import bioskop.cari.aliagus.com.caribioskop.about.About;
import bioskop.cari.aliagus.com.caribioskop.fragment_content.ContentMovieFragment;
import bioskop.cari.aliagus.com.caribioskop.lib.StringSource;
import bioskop.cari.aliagus.com.caribioskop.toast.ToastFragment;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainContentActivity extends AppCompatActivity implements MainContentContract.View,
        BottomNavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = MainContentActivity.class.getSimpleName();
    private MainContentActivityPresenter mPresenter;
    @BindView(R.id.bottom_navigation_view)
    BottomNavigationView mBottomNavigationView;
    private Menu menuNavBottom;
    boolean isMainContent = true;
    private About about;
    private ToastFragment toastFragment;
    private ContentMovieFragment contentMovieFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_content);
        ButterKnife.bind(this);
        initPresenter();
        initBottomNavigation();
        Log.d(TAG, "created");
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
                                for (int a = 0; a < 4; a++) {
                                    String id = "";
                                    if (a == 0) {
                                        id = "bottom_navigation_now_playing";
                                    } else if (a == 1) {
                                        id = "botton_navigation_popular";
                                    } else if (a == 2) {
                                        id = "bottom_navigation_favorite";
                                    } else if (a == 3) {
                                        id = "botton_navigation_comming_soon";
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

    @Override
    protected void onResume() {
        super.onResume();
        setTitle(R.string.now_playing);
        mBottomNavigationView.setSelectedItemId(R.id.bottom_navigation_now_playing);
    }

    private void attachFagmentToActivity(String urlData, String filter) {
        contentMovieFragment = new ContentMovieFragment();
        contentMovieFragment.setFilter(filter);
        contentMovieFragment.setUrlData(urlData);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_layout_content_activity, contentMovieFragment)
                .commitAllowingStateLoss();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.botton_navigation_comming_soon:
                attachFagmentToActivity(StringSource.GET_COMING_SOON_MOVIE, "soon");
                setTitle(R.string.coming_soon);
                isMainContent = true;
                break;

            case R.id.botton_navigation_popular:
                attachFagmentToActivity(StringSource.GET_POPULAR_MOVIE, "popular");
                setTitle(R.string.popular_movie);
                isMainContent = true;
                break;

            case R.id.bottom_navigation_now_playing:
                attachFagmentToActivity(StringSource.GET_NOW_PLAYING_MOVIE, "now");
                setTitle(R.string.now_playing);
                isMainContent = true;
                break;

            case R.id.bottom_navigation_favorite:
                attachFagmentToActivity(StringSource.GET_TRAILLER, "favorite");
                setTitle(R.string.favorite);
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
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        moveTaskToBack(true);
    }

    @Override
    public void showToastFragment(String message) {
        toastFragment = ToastFragment.getInstance(getApplicationContext());
        toastFragment.setMessage(message);
        toastFragment.show(
                getSupportFragmentManager(),
                toastFragment.getTag()
        );
    }

    @Override
    public void notifyPosition(Integer position) {
        contentMovieFragment.refreshAdapterPosition(position);
    }

    public void resumeActivity() {
        checkToastFragment();
        onResume();
    }

    private void checkToastFragment() {
        if (toastFragment != null && toastFragment.isAdded()) {
            toastFragment.dismissAllowingStateLoss();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        checkToastFragment();
    }

}
