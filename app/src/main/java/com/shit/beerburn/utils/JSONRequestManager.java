package com.shit.beerburn.utils;

import android.app.DownloadManager;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.shit.beerburn.app.AppController;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ahmeshaf on 1/18/2016.
 */
public class JSONRequestManager {

    /**
     * Making json object request
     * */

    private static JSONObject returnJSON;
    private static boolean finishedReqJSONObj = false;
    private static final String tag_json_obj = "jobj_req", tag_json_arry = "jarray_req";
    /**
     * Returns a JSON Object upon request.
     * @param url
     * @param method
     * @param headers
     * @param params
     * @return
     */
    public static void getJsonObjReq(final TextView msgResponse, String url, int method,
                                     final HashMap<String,String> headers,
                                     final HashMap<String, String> params) {
        finishedReqJSONObj = false;
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(method,
                url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        // Log.d(TAG, response.toString());

                        try {
                            returnJSON=response;
                            Double calories = response.getDouble("calories");
                            msgResponse.setText(calories.toString());
                        }
                        catch (Exception exp) {
                            //TODO: write good error handler
                        }
                        finally {
                            finishedReqJSONObj = true;
                        }
                        // hideProgressDialog();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // VolleyLog.d(TAG, "Error: " + error.getMessage());
                // hideProgressDialog();
            }
        }) {

            /**
             * Passing some request headers
             * */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return headers;
            }

            @Override
            protected Map<String, String> getParams() {
                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
        // Cancelling request
        // ApplicationController.getInstance().getRequestQueue().cancelAll(tag_json_obj);
    }
}
