package com.MohamedTaha.Imagine.Quran.ui.activities;

import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.MohamedTaha.Imagine.Quran.R;
import com.MohamedTaha.Imagine.Quran.helper.HelperClass;
import com.MohamedTaha.Imagine.Quran.helper.SharedPerefrenceHelper;
import com.MohamedTaha.Imagine.Quran.interactor.NavigationDrawarInteractor;
import com.MohamedTaha.Imagine.Quran.notification.NotificationHelper;
import com.MohamedTaha.Imagine.Quran.presenter.NavigationDrawarPresenter;
import com.MohamedTaha.Imagine.Quran.ui.fragments.FragmentSound;
import com.MohamedTaha.Imagine.Quran.ui.fragments.GridViewFragment;
import com.MohamedTaha.Imagine.Quran.ui.fragments.PartsFragment;
import com.MohamedTaha.Imagine.Quran.view.NavigationDrawarView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

import static com.MohamedTaha.Imagine.Quran.interactor.SplashInteractor.FIRST_TIME;

public class NavigationDrawaberActivity extends AppCompatActivity implements NavigationDrawarView {
    @BindView(R.id.nav_view)
    BottomNavigationView navView;
    @BindView(R.id.toobar)
    Toolbar toobar;
    @BindString(R.string.about)
    String aboutString;
    @BindString(R.string.shareApp)
    String shareApp;
    @BindString(R.string.notSupport)
    String notSupport;
    @BindString(R.string.exit_app)
    String exit_app;

    private int current_fragment;
    public static MaterialSearchView searchView;
    public static final String NOTIFICATION_OPEN = "notificationOpen";
    String appPackageName;
    int notificationId;
    private NavigationDrawarPresenter presenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawaber);
        ButterKnife.bind(this);
        presenter = new NavigationDrawarInteractor(this);
        appPackageName = getPackageName();

        //For Settings Notifications
        NotificationHelper.sendNotificationEveryHalfDay(getApplicationContext());
        NotificationHelper.enableBootRecieiver(getApplicationContext());

        //for close Notification
        notificationId = getIntent().getIntExtra(NOTIFICATION_OPEN, 1);
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancel(notificationId);

        searchView = (MaterialSearchView) findViewById(R.id.search_view);
       navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navView.setSelectedItemId(R.id.read_quran);
        setSupportActionBar(toobar);
        //for change color text toolbar
        toobar.setTitleTextColor(Color.parseColor("#FFFFFF"));
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            int id = item.getItemId();
            if (id == current_fragment) {
                return false;
            }
            switch (id) {
                case R.id.read_quran:
                    GridViewFragment gridViewFragment = new GridViewFragment();
                    HelperClass.replece(gridViewFragment, getSupportFragmentManager(), R.id.frameLayout);
                    break;
                case R.id.read_parts:
                    PartsFragment partsFragment = new PartsFragment();
                    HelperClass.replece(partsFragment, getSupportFragmentManager(), R.id.frameLayout);
                    break;
                case R.id.sound_quran:
                    FragmentSound fragmentSound = new FragmentSound();
                    HelperClass.replece(fragmentSound, getSupportFragmentManager(), R.id.frameLayout);
                    break;
            }
            current_fragment = id;
            return true;
        }
    };

    @Override
    public void onBackPressed() {
        presenter.exitApp(searchView, navView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        MenuItem SearchItem = menu.findItem(R.id.action_search);
        searchView.setMenuItem(SearchItem);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_share:
                presenter.shareApp(aboutString, appPackageName);
                break;
            case R.id.action_send_us:
                presenter.sendUs();
                break;
            case R.id.action_use_way:
                SharedPerefrenceHelper.putFirstTime(getApplicationContext(), FIRST_TIME, false);
                HelperClass.startActivity(getApplicationContext(), SplashActivity.class);
                break;
            case R.id.action_settings:
                HelperClass.startActivity(getApplicationContext(), SettingsActivity.class);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                }
                break;
            case R.id.action_rate:
                presenter.actionRate(appPackageName);
                break;
            default:

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showMessageExitApp() {
        HelperClass.customToast(this, exit_app);
    }

    @Override
    public void exitApp() {
        HelperClass.closeApp(getApplicationContext());
    }

    @Override
    public void getDefault() {
        navView.setSelectedItemId(R.id.read_quran);
    }

    @Override
    public void getShareApp(Intent intent) {
        startActivity(Intent.createChooser(intent, shareApp));
    }

    @Override
    public void getSendUs(Intent intentEmail) {
        if (intentEmail.resolveActivity(getPackageManager()) != null) {
            startActivity(intentEmail);
        } else {
            HelperClass.customToast(this, notSupport);
        }
    }

    @Override
    public void getRateApp(Intent rateApp) {
        startActivity(rateApp);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }
}