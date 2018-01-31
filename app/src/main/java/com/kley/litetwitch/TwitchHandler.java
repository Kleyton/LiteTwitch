/**
 * Copyright 2018 Jose Clayton
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.kley.litetwitch;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Class used to manage communication with the Twitch API.
 * Created by Kley on 1/30/2018.
 */
public class TwitchHandler {
    private final static String TAG = TwitchHandler.class.getSimpleName();

    private static final String URL_API_ROOT = "https://api.twitch.tv/kraken";
    private static final String URL_API_USERS = "/users";
    // Replace with your own, public information
    private static final String CLIENT_ID = "hdq1rch45i7l69bosgaub3wdmwdxbn";

    private static final String REQUEST_CLIENT_KEY = "Client-ID";
    private static final String REQUEST_ACCEPT_KEY = "Accept";
    private static final String REQUEST_ACCEPT_VALUE = "application/vnd.twitchtv.v5+json";

    public TwitchHandler() {

    }

    public void testAPICall() {
        final AsyncTask<Void, Void,Void> backendCall = new TwitchTask();
        backendCall.execute();
    }

    private static class TwitchTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            URL url = null;
            try {
                url = new URL(URL_API_ROOT + URL_API_USERS + "?login=mystial"); //TODO: Put your account name here to get the id
                //url = new URL("https://api.twitch.tv/kraken/users/<>/follows/channels");
                //url = new URL("https://api.twitch.tv/kraken/users?login=<>");
                HttpsURLConnection httpURLConnection = (HttpsURLConnection) url.openConnection();
                // Set appropriate headers
                httpURLConnection.setRequestProperty(REQUEST_ACCEPT_KEY, REQUEST_ACCEPT_VALUE);
                httpURLConnection.setRequestProperty(REQUEST_CLIENT_KEY, CLIENT_ID);
                httpURLConnection.setRequestMethod("GET");
                InputStream inputStream = httpURLConnection.getInputStream();
                // Read the response... if the InputStream comes back as null that's a not found error
                // TODO: Handle said error, right now it's caught by the try/catch block
                BufferedReader streamReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                StringBuilder responseStrBuilder = new StringBuilder(2048);
                String inputStr;
                while ((inputStr = streamReader.readLine()) != null)
                    responseStrBuilder.append(inputStr);
                Log.i(TAG, "Response of client: " + responseStrBuilder.toString());
                httpURLConnection.disconnect();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                Log.e(TAG, "Connection error for URL: " + url);
            }catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
