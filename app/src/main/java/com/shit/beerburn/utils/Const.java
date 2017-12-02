package com.shit.beerburn.utils;

public class Const {
	public static final String URL_ACTIVITY_STRAVA = "https://www.strava.com/api/v3/activities/";
	public static final String URL_ACTIVITIES_STRAVA = "https://www.strava.com/api/v3/athlete/activities";
	public static final String URL_OAUTH_STRAVA = "https://www.strava.com/oauth/token";
    public static final String JAN_1_DATE = "2016-01-01T00:00:00Z";

	// public final int REQUEST_ACTIVITIES = 1;
	public static final int REQUEST_CLIENT_CODE = 0;
	public static final int REQUEST_TOKEN = 1;
	public static final int REQUEST_LAST_ACTIVITY_DATE = 2;
	public static final int REQUEST_ACTIVITIES = 3;
	public static final int REQUEST_ACTIVITY = 4;

	public enum TRACKER_TYPE {
		STRAVA, RUN_KEEPER
	}
}
