package com.shit.beerburn.apps;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.shit.beerburn.DashBoardView;
import com.shit.beerburn.app.AppController;
import com.shit.beerburn.utils.BeerSharedPreferenceHelper;
import com.shit.beerburn.utils.Const;
import com.shit.beerburn.utils.DateTimeHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by ahmeshaf on 1/18/2016.
 */
public class StravaManager implements TrackingManager{

    private DashBoardView mDashBoardView = null;
    private String TAG = StravaManager.class.getSimpleName();
    private final String client_id  = "7378";
    private final String client_secret = "40d0be525ec5d47326eebb4f4c50845e76216f16";
    // private final String myToken = "7d7ad3279091236e2d39956e20fb89f1e69416f4";
    // private final String client_code = "804d85a7731dedaa8e6813aebdcc1acf722e319f";
    // private final String client_code_shy = "cf68a378d155bc033771607f0ab8c21638b18abd";

    private static final String STRAVA_CLIENT_CODE = "strava_code";
    private static final String STRAVA_PREFS_NAME = "strava_prefs_name";
    private static final String STRAVA_TOKEN_KEY = "strava_token";
    private static final String STRAVA_LAST_DATE_KEY = "strava_last_date";
    private static final String STRAVA_BEER_KEY = "strava_beer_key";
    private static final String STRAVA_BEER_KEY_TODAY = "strava_beer_key_today";

    private volatile Long mBeerToday;
    private volatile Long mBeerAllTime;
    private volatile String mLastActivityDate;

    private volatile int activityCount;
    static final Object lock=new Object();

    public StravaManager(DashBoardView dashBoardView) {
        this.mDashBoardView = dashBoardView;
        initialize();
    }

    private void initialize() {
        this.mBeerAllTime = BeerSharedPreferenceHelper.getInstance().getBeerAllTime();
        this.mLastActivityDate = BeerSharedPreferenceHelper.getInstance().getLastActivityDate();

        if (!DateTimeHelper.isDayOfDateToday(mLastActivityDate)) {
            mBeerToday = Long.valueOf(0);
        }
        else {
            mBeerToday = BeerSharedPreferenceHelper.getInstance().getBeerToday();
        }
    }

    /**
     * Check of token is available in shared preferences.
     * If available get the last entered date.
     * If both available begin the chain of getting activities.
     */
    @Override
    public void initiate() {
        initialize();
        // boolean silent = settings.getBoolean("silentMode", false);
        this.mDashBoardView.showProgressDialog();
        String token = BeerSharedPreferenceHelper.getInstance().getTrackerToken(STRAVA_TOKEN_KEY);
        if (!token.isEmpty()) {
            // String mLastActivityDate = BeerSharedPreferenceHelper.getInstance().getLastActivityDate();
            if (!mLastActivityDate.isEmpty()) {
                startGettingCaloriesFrom(mLastActivityDate, token);
            }
            else {
                doFromGetLastDate(token);
            }
        }
        else {
            doFromGetToken();
        }
    }

    /**
     * Start the retrieval process from getting the date first.
     * @param token
     */
    private void doFromGetLastDate(String token) {
        // TODO: Ask from server first which date.
        String jan_1_2016 = "2016-01-01T00:00:00Z";
        saveLastActivityDate(jan_1_2016);
        startGettingCaloriesFrom(jan_1_2016, token);
    }

    /**
     * Start getting calories.
     * @param mLastActivityDate
     * @param token
     */
    private void startGettingCaloriesFrom(final String mLastActivityDate, String token) {
        mDashBoardView.showProgressDialog();
        String url = Const.URL_ACTIVITIES_STRAVA;

        String epochStravaTimeStamp = DateTimeHelper.getEpochSecondsOf(mLastActivityDate).toString();

        url += "?" + "after="+epochStravaTimeStamp+"&access_token="+token;

        doJsonArryReq(Const.REQUEST_ACTIVITIES, url, Request.Method.GET);
    }

    private final String ISO_8601_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";

    /**
     * Start the procedure of getting token.
     */
    private void doFromGetToken() {
        //TODO: Getting token through local/host/some other way?
        // return myToken;

        String trackerClientCode = BeerSharedPreferenceHelper.getInstance().getTrackerClientCode(STRAVA_CLIENT_CODE);
        if (trackerClientCode.isEmpty())
            mDashBoardView.startSignInActivity();
        else {
            String url = Const.URL_OAUTH_STRAVA + "?client_id=" + client_id +
                    "&client_secret=" + client_secret + "&code=" + trackerClientCode;

            doJsonObjReq(Const.REQUEST_TOKEN, url, Request.Method.POST);
        }

    }

    private static final String tag_json_obj = "jobj_req", tag_json_arry = "jarray_req";
    /**
     * Returns a JSON Object upon request.
     * @param url: This is the url to which request is sent
     * @param method: Type of HTTP request.
     */
    public void doJsonObjReq(final int myRequestType, String url, int method) {
        // finishedReqJSONObj = false;
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(method,
                url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Log.d(TAG, response.toString());

                        try {
                            doJsonResponseHandling(myRequestType, response);
                        }
                        catch (Exception exp) {
                            //TODO: write good error handler
                            exp.getStackTrace();
                        }
                        finally {
                            // finishedReqJSONObj = true;
                        }
                        // hideProgressDialog();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // throw;
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                NetworkResponse response = error.networkResponse;
                handleErrorResponse(myRequestType, error);
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }

    private void doJsonArryReq(final int myRequestType, String url, int method) {
        // showProgressDialog();
        JsonArrayRequest req = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // Log.d(TAG, response.toString());
                        doJsonArrayResponseHandling(myRequestType, response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                NetworkResponse response = error.networkResponse;
                handleErrorResponse(myRequestType, error);
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(req,
                tag_json_arry);

        // Cancelling request
        // ApplicationController.getInstance().getRequestQueue().cancelAll(tag_json_arry);
    }

    private void handleErrorResponse(int myRequestType, VolleyError error){
        String text = "Error! Error! Error!";
        mDashBoardView.hideProgressDialog();
        mDashBoardView.toastErrorMessage(text);

    }

    private void doJsonResponseHandling(int myRequestType, JSONObject response) {

        if (myRequestType == Const.REQUEST_ACTIVITY) {
            try {
                synchronized (lock) {
                    String activityId = response.getString("id");
                    Long calories = response.getLong("calories");
                    String startDateStandard = response.getString("start_date");
                    String startDateLocal = response.getString("start_date_local");
                    updateBeerNDateToday(calories, startDateLocal);
                    activityIdList.remove(activityId);
                }

                if (activityIdList.size() > 0) {
                    doActivityRequests(activityIdList.get(0));
                }
                else {
                    if (activityCount == 0)
                        doInterestingStuffWithCalories();
                }

            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else if (myRequestType == Const.REQUEST_TOKEN) {
            try {
                String token = response.getString("access_token");
                BeerSharedPreferenceHelper.getInstance().saveTrackerToken(STRAVA_TOKEN_KEY, token);
                initiate();
            }
            catch (Exception exp) {
                exp.getStackTrace();
            }

        }
    }

    private void updateBeerNDateToday(Long calories, String startDateLocal) {
        synchronized (lock) {
            if (DateTimeHelper.isDayOfDateToday(startDateLocal)) {
                mBeerToday += (calories/44)*100;
            }

            if (DateTimeHelper.getEpochSecondsOf(mLastActivityDate) <
                    DateTimeHelper.getEpochSecondsOf(startDateLocal)) {
                mLastActivityDate = startDateLocal;
            }

            activityCount--;
            mBeerAllTime = mBeerAllTime + (calories/44)*100;
        }
    }

    private ArrayList<String> activityIdList = new ArrayList<>();

    private void doJsonArrayResponseHandling(int myRequestType, JSONArray response) {
        if (myRequestType == Const.REQUEST_ACTIVITIES) {
            int arraySize = response.length();
            activityCount = arraySize;

            for (int index = 0; index < arraySize; index++) {
                try {
                    JSONObject currJson = response.getJSONObject(index);
                    String activityId = currJson.getString("id");
                    activityIdList.add(activityId);
                }
                catch (JSONException exp) {

                }
            }
            if (activityIdList.size() > 0) {
                doActivityRequests(activityIdList.get(0));
            }
            else
                saveAndUpdateTextViews();
            // doInterestingStuffWithCalories(totalCalories);
        }
    }

    private void doActivityRequests(String activityId) {
        String token = BeerSharedPreferenceHelper.getInstance().getTrackerToken(STRAVA_TOKEN_KEY);
        String url = Const.URL_ACTIVITY_STRAVA + activityId + "?access_token=" + token;
        doJsonObjReq(Const.REQUEST_ACTIVITY, url, Request.Method.GET);
    }

    /**
     * Exit function of the strava manager. We need to upload this info.
     */
    private void doInterestingStuffWithCalories() {
        // TODO: Some high level things we can do with this now. For now just display.
        saveAndUpdateTextViews();
    }

    /**
     * Save the beer, date and update the text views in the main activity.
     */
    private void saveAndUpdateTextViews() {
        saveBeerAllTime(mBeerAllTime);
        saveBeerToday(mBeerToday);
        saveLastActivityDate(mLastActivityDate);
        mDashBoardView.hideProgressDialog();
    }

    /**
     * Save the all time beer count to the shared preferences and set the value on
     * main activity.
     * @param value: all time beer count
     */
    private void saveBeerAllTime(Long value) {
        BeerSharedPreferenceHelper.getInstance().saveBeerAllTime(value);
        mDashBoardView.setBeerCountAllTime(String.format(Locale.ENGLISH, "%d ml", value));
    }

    /**
     * Save today's beer count to the shared preferences and set the value in the main activity
     * @param value: today's beer count.
     */
    private void saveBeerToday(Long value) {
        BeerSharedPreferenceHelper.getInstance().saveBeerToday(value);
        mDashBoardView.setBeerCountToday(String.format(Locale.ENGLISH, "%d ml", value));
    }

    /**
     * Save the last activity date to the shared preferences and set the value in the main
     * activity.
     * @param activityStartDate
     */
    private void saveLastActivityDate(String activityStartDate) {
        BeerSharedPreferenceHelper.getInstance().saveLastActivityDate(activityStartDate);
        mDashBoardView.setLastActivityDate(DateTimeHelper.getLocalFormattedDate(activityStartDate));
    }

}
