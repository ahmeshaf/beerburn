package com.shit.beerburn;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.shit.beerburn.utils.BeerSharedPreferenceHelper;

import java.util.Arrays;
import java.util.List;

public class ActivityLogin extends AppCompatActivity {
    private String stravaUrl = "https://www.strava.com/oauth/authorize?client_id=7378&response_type=code&redirect_uri=http://54.169.225.176/strava/token_exchange&scope=write&state=mystate&approval_prompt=force";
    private static final String STRAVA_PREFS_NAME = "strava_prefs_name";
    private static final String STRAVA_CLIENT_CODE = "strava_code";
    SharedPreferences storedData;
    private WebView webView;
    Activity activity ;
    private ProgressDialog progDailog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_login);
        storedData = getSharedPreferences(STRAVA_PREFS_NAME, Context.MODE_PRIVATE);

        activity = this;
        progDailog = ProgressDialog.show(activity, "Loading","Please wait...", true);
        progDailog.setCancelable(false);


        webView = (WebView) findViewById(R.id.webViewLogin);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.setWebViewClient(new MyBrowser());
        webView.loadUrl(stravaUrl);
    }

    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            progDailog.show();
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {

            Uri myUri = Uri.parse(url);
            if (myUri.getHost().contains("54.169.225.176")) {
                String code = myUri.getQueryParameter("code");
                BeerSharedPreferenceHelper.getInstance().saveTrackerClientCode(code);
                progDailog.dismiss();
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }
            progDailog.dismiss();
        }
    }
}
