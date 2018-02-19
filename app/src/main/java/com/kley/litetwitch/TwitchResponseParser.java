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

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

/**
 * Takes the JSON response from an API request to twitch and strips it down to accessible elements.
 * Based on <a href="https://dev.twitch.tv/docs/v5">Twitch V5 API</a>
 * <p/>
 * Created by Kley on 2/18/2018.
 */

public class TwitchResponseParser {
    private final static String TAG = TwitchResponseParser.class.getSimpleName();

    // Used to easily parse responses..
    private static Gson gson = new GsonBuilder().
            registerTypeAdapter(UserResponse.class, new UserDeserializer()).
            create();

    //Singleton for application wide requests
    // Maybe this only makes sense within the handler?
    private static TwitchResponseParser INSTANCE;

    public static TwitchResponseParser getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new TwitchResponseParser();
            gson = new Gson();
        }
        return INSTANCE;
    }

    // TODO: Maybe the whole content below needs to be its own class?

    // Deserialize a single user response from an API request to Twitch, which returns a list of users
    private static class UserDeserializer implements JsonDeserializer<UserResponse> {
        @Override
        public UserResponse deserialize(JsonElement je, Type type, JsonDeserializationContext jdc)
                throws JsonParseException {
            // Get the "content" element from the parsed JSON
            JsonArray listOfUsers = je.getAsJsonObject().getAsJsonArray(UserResponse.usersTag);
            JsonElement user = listOfUsers.get(0);

            // Deserialize it. You use a new instance of Gson to avoid infinite recursion
            // to this deserializer
            return new Gson().fromJson(user, UserResponse.class);
        }
    }

    /**
     * Response from a GET to /user/
     * Partially complete since we only need some of the data.
     */
    public static class UserResponse {
        // The tag that's used to represent the array of users from a request
        public static String usersTag = "users";
        // Tags for properties of a user element
        private String _id;
        private String bio;
        private String display_name;
        private String logo;
        private String name;

        private UserResponse() {}

        public static UserResponse fromResponse(String jsonResponse) {
            UserResponse userResponse = gson.fromJson(jsonResponse, UserResponse.class);
            Log.i(TAG, "Response = " + userResponse);
            return userResponse;
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            builder.append("\n")
            .append("id: ").append(_id).append("\n")
            .append("bio: ").append(bio).append("\n")
            .append("display name: ").append(display_name).append("\n")
            .append("logo: ").append(logo).append("\n")
            .append("name: ").append(name).append("\n");
            return builder.toString();
        }

        public String getUserId() { return _id ; }
        public String getUserBio() { return bio; }
        public String getUserDisplayName() { return display_name; }
        public String getUserLogo() { return logo; }
        public String getUserName() { return name; }
    }
}
