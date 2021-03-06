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
    private static final String URL_API_FOLLOWS = "/follows";
    private static final String URL_API_CHANNELS = "/channels";
    // Replace with your own, public information
    private static final String CLIENT_ID = "hdq1rch45i7l69bosgaub3wdmwdxbn";

    private static final String REQUEST_CLIENT_KEY = "Client-ID";
    private static final String REQUEST_ACCEPT_KEY = "Accept";
    private static final String REQUEST_ACCEPT_VALUE = "application/vnd.twitchtv.v5+json";

    //Singleton for application wide requests
    private static TwitchHandler INSTANCE;

    public static TwitchHandler getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new TwitchHandler();
        }
        return INSTANCE;
    }

    private TwitchHandler() {
        //TODO: Do we require any setup over here?
    }

    public void testAPICall(String accountName) {
        final AsyncTask<String, Void,Void> backendCall = new TwitchTask();
        backendCall.execute(accountName);
    }

    private static class TwitchTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... voids) {
            URL url = null;
            try {
                url = new URL(URL_API_ROOT + URL_API_USERS + "?login=" + voids[0]); //Note: Put your account name here to get the id
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
                Log.i(TAG, "Response#1 of client: " + responseStrBuilder.toString());
                TwitchResponseParser.UserResponse response = TwitchResponseParser.UserResponse.fromResponse(responseStrBuilder.toString());
                httpURLConnection.disconnect();

                // Get the list of follows for the user...
                String id = response.getUserId();
                if(id != null) {
                    url = new URL(URL_API_ROOT + URL_API_USERS + "/" + id + URL_API_FOLLOWS + URL_API_CHANNELS);
                    httpURLConnection = (HttpsURLConnection) url.openConnection();
                    // Set appropriate headers
                    httpURLConnection.setRequestProperty(REQUEST_ACCEPT_KEY, REQUEST_ACCEPT_VALUE);
                    httpURLConnection.setRequestProperty(REQUEST_CLIENT_KEY, CLIENT_ID);
                    httpURLConnection.setRequestMethod("GET");
                    inputStream = httpURLConnection.getInputStream();
                    // Read the response... if the InputStream comes back as null that's a not found error
                    // TODO: Handle said error, right now it's caught by the try/catch block
                    streamReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                    responseStrBuilder = new StringBuilder(2048);
                    while ((inputStr = streamReader.readLine()) != null)
                        responseStrBuilder.append(inputStr);
                    //Log.i(TAG, "Response#2 of client: " + responseStrBuilder.toString());
                    TwitchResponseParser.ChannelResponse channelResponse = TwitchResponseParser.ChannelResponse.fromResponse(responseStrBuilder.toString());
                    Log.i(TAG, "Channels: " + channelResponse);
                    TwitchHandler.channelResponse = channelResponse;
                }
                else {
                    Log.e(TAG, "User id " + id + " does not exist");
                }

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

    // Find somewhere to fit this... probably need a callback mechanism
    public static TwitchResponseParser.ChannelResponse channelResponse;
}
