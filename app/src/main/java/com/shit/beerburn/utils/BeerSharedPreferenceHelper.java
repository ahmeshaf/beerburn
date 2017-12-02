package com.shit.beerburn.utils;

import android.content.SharedPreferences;

/**
 * Created by ahmeshaf on 2/7/2016.
 */
public class BeerSharedPreferenceHelper {
    // private static final String JAN_1_2016 = "01-01-2016T00:00:00Z";
    private SharedPreferences mSharedPreferences;
    private static BeerSharedPreferenceHelper beerPrefHelperInstance;
    private static final String STRAVA_TOKEN_KEY = "strava_token";
    private static final String STRAVA_CLIENT_CODE = "strava_code";
    private static final String STRAVA_LAST_DATE_KEY = "strava_last_date";
    private static final String STRAVA_BEER_KEY = "strava_beer_key";
    private static final String STRAVA_BEER_KEY_TODAY = "strava_beer_key_today";

    public BeerSharedPreferenceHelper(){}

    public BeerSharedPreferenceHelper(SharedPreferences sharedPreferences) {
        // this.mSharedPreferences = mSharedPreferences;
        if(mSharedPreferences==null) {
            mSharedPreferences = sharedPreferences;
            beerPrefHelperInstance = this;
        }
    }

    public static synchronized BeerSharedPreferenceHelper getInstance(){
        return beerPrefHelperInstance;
    }

    public Const.TRACKER_TYPE getTrackerType() {
        // TODO: return the tracking type from the shared preferences.
        return Const.TRACKER_TYPE.STRAVA;
    }

    public String getTrackerClientCode(String stravaClientCode) {
        return this.mSharedPreferences.getString(stravaClientCode, "");
    }

    public String getTrackerToken(String trackerKey) {
        return this.mSharedPreferences.getString(trackerKey, "");
    }

    public Long getBeerAllTime() {

        return this.mSharedPreferences.getLong(STRAVA_BEER_KEY, 0);
    }

    public Long getBeerToday() {
        return this.mSharedPreferences.getLong(STRAVA_BEER_KEY_TODAY, 0);
    }

    public String getLastActivityDate() {
        return this.mSharedPreferences.getString(STRAVA_LAST_DATE_KEY, Const.JAN_1_DATE);
    }

    public void saveBeerAllTime(Long value) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putLong(STRAVA_BEER_KEY, value);
        editor.apply();
    }

    public void saveBeerToday(Long value) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putLong(STRAVA_BEER_KEY_TODAY, value);
        editor.apply();
    }

    public void saveLastActivityDate(String isoDate) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(STRAVA_LAST_DATE_KEY, isoDate);
        editor.apply();
    }

    public void saveTrackerToken(String trackerTokenKey, String token) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(trackerTokenKey, token);
        editor.apply();
    }

    public String getTrackerToken() {
        // TODO: Send the selectewd tracker's token
        return getTrackerToken(STRAVA_TOKEN_KEY);
    }

    public static void setFake(BeerSharedPreferenceHelper fakBsp) {
        beerPrefHelperInstance = fakBsp;
    }


    public String getTrackerClientCode() {
        return mSharedPreferences.getString(STRAVA_CLIENT_CODE, "");
    }

    public void saveTrackerClientCode(String clientCode) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(STRAVA_CLIENT_CODE, clientCode);
        editor.apply();
    }
}
