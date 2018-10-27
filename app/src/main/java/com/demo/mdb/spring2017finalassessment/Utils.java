package com.demo.mdb.spring2017finalassessment;

import android.support.annotation.NonNull;
import android.util.Log;

import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by hp on 3/14/2017.
 */

public class Utils {
    /* TODO Part 5
     * implement getRandomPhrase on a thread pool of size 1. Use a callable to make a GET request on
     * this urlString: "https://api.whatdoestrumpthink.com/api/v1/quotes/random". You'll probably
     * need to actually go to the URL to see the JSON structure to know what String you want (don't
     * worry, it's a very simple JSON file.)
     *
     * convertStreamToString has been provided
     *
     * Note: if you can't remember how to use a Callable, you can get partial credit without one!
     */
    static Future<String> getRandomPhrase() throws Exception {
        final Callable<String> callable = new Callable<String>() {
            @Override
            public String call() throws Exception {
                String string = null;
                URL url = new URL("https://api.whatdoestrumpthink.com/api/v1/quotes/random");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                    string = convertStreamToString(in);
                } catch (Exception e){
                    Log.d("REEEEEE", "call: " + e);
                }
                finally {
                    urlConnection.disconnect();
                }
                return string;
            }
        };
        return new Future<String>() {
            @Override
            public boolean cancel(boolean b) {
                return false;
            }

            @Override
            public boolean isCancelled() {
                return false;
            }

            @Override
            public boolean isDone() {
                return false;
            }

            @Override
            public String get() throws InterruptedException, ExecutionException {
                String phrase = null;
                JSONObject jsonObject = null;
                String message = null;
                try {
                    phrase = callable.call();
                    jsonObject = new JSONObject(phrase);
                    message = jsonObject.getString("message");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return message;
            }

            @Override
            public String get(long l, @NonNull TimeUnit timeUnit) throws InterruptedException, ExecutionException, TimeoutException {
                return null;
            }
        };
    }

    static String convertStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }
}
