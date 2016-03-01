package cegepsth.taskplanner.utils;

/**
 * Created by Maxim on 12/6/2015.
 * This is used to make call to the webservice used to access our MySQL database server
 */

import com.loopj.android.http.*;


public class WebServiceClient {

    private static final String BASE_URL = "https://fishingspotonthefly.com/FishingSpotOnTheFly/taskplanner/";

    private static AsyncHttpClient client = new AsyncHttpClient();

    public static final String TAG_ID = "id";
    public static final String TAG_NAME = "name";
    public static final String TAG_EMAIL = "email";
    public static final String TAG_TOKEN = "token";
    public static final String TAG_MESSAGE = "message";
    public static final String TAG_SUCCESS = "success";


    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.post(getAbsoluteUrl(url), params, responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }

}
