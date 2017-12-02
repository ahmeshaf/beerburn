package com.shit.beerburn;

/**
 * Created by ahmeshaf on 2/6/2016.
 */
public interface DashBoardView {
    void updateDashBoard();
    void setBeerCountAllTime(String beerAllTimeText);
    String getBeerCountAllTime();
    void setBeerCountToday(String beerTodayText);
    String getBeerCountToday();
    void setLastActivityDate(String date);
    String getLastActivityDate();
    void startSignInActivity();
    void showProgressDialog();
    void hideProgressDialog();
    void toastErrorMessage(String msg);

}
