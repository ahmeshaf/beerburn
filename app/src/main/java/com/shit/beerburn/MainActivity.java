package com.shit.beerburn;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;


import com.shit.beerburn.apps.StravaManager;
import com.shit.beerburn.utils.BeerSharedPreferenceHelper;
import com.shit.beerburn.utils.DateTimeHelper;

import java.util.Locale;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, DashBoardView {

    private String TAG = MainActivity.class.getSimpleName();
    private static final String STRAVA_PREFS_NAME = "strava_prefs_name";

    TextView lastDateTextView;
    TextView beerAllTimeTextView;
    TextView beerTodayTextView;

    SharedPreferences storedData;
    SwipeRefreshLayout mSwipeRefreshLayout;
    private DashBoardPresenter dashBoardPresenter;
    private BeerSharedPreferenceHelper mBSPHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        // getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        //         WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        lastDateTextView = (TextView) findViewById(R.id.ldateTextView);
        beerAllTimeTextView = (TextView) findViewById(R.id.bearTextView);
        beerTodayTextView = (TextView) findViewById(R.id.todayBearTextView);


        storedData = getSharedPreferences(STRAVA_PREFS_NAME, Context.MODE_PRIVATE);
        mBSPHelper = new BeerSharedPreferenceHelper(storedData);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshView);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.beer_golden);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        updateDashBoard();

        dashBoardPresenter = new DashBoardPresenter(this);
        /**
         * Showing Swipe Refresh animation on activity create
         * As animation won't start on onCreate, post runnable is used
         */
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
               // mSwipeRefreshLayout.setRefreshing(true);
                dashBoardPresenter.onRefresh();
            }
        });
    }

    @Override
    public void updateDashBoard() {
        Long beerAllTime = mBSPHelper.getBeerAllTime();
        Long beerToday = mBSPHelper.getBeerToday();
        String isoLastDate = mBSPHelper.getLastActivityDate();

        setBeerCountAllTime(String.format(Locale.ENGLISH, "%d ml", beerAllTime));
        setBeerCountToday(String.format(Locale.ENGLISH, "%d ml", beerToday));

        String formattedDate = DateTimeHelper.getLocalFormattedDate(isoLastDate);
        setLastActivityDate(formattedDate);
    }

    private int REQUEST_1 = 1;
    @Override
    public void startSignInActivity() {
        Intent intent = new Intent(this, ActivityLogin.class);
        startActivityForResult(intent, REQUEST_1);
    }

    @Override
    public void showProgressDialog() {
        mSwipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void hideProgressDialog() {
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void toastErrorMessage(String msg) {
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(this, msg, duration);
        toast.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        /**
         * requestCode : an integer code passed to the called activity set by caller activity
         * resultCode : an integer code returned from the called activity
         * data : an intent containing data set by the called activity
         */
        if (resultCode == RESULT_OK) {
            dashBoardPresenter.onRefresh();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Start Logging
    }

    @Override
    protected void onPause() {
        super.onPause();
        // End Logging
    }


    @Override
    public void onRefresh() {
        dashBoardPresenter.onRefresh();
        // updateScreen();
    }

    @Override
    public void setBeerCountAllTime(String beerAllTimeText) {
        beerAllTimeTextView.setText(beerAllTimeText);
    }

    @Override
    public String getBeerCountAllTime() {
        return beerAllTimeTextView.getText().toString();
    }

    @Override
    public void setBeerCountToday(String beerTodayText) {
        beerTodayTextView.setText(beerTodayText);
    }

    @Override
    public String getBeerCountToday() {
        return beerTodayTextView.getText().toString();
    }

    @Override
    public void setLastActivityDate(String date) {
        lastDateTextView.setText(date);
    }

    @Override
    public String getLastActivityDate() {
        return lastDateTextView.getText().toString();
    }
}
