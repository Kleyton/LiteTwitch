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

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * Fragment that renders the main screen view for this application.
 * Contains the list of registered channels and their status.
 *
 * Created by Kley on 2/18/2018.
 */

public class MainListFragment extends Fragment {
    private static final String TAG = MainListFragment.class.getSimpleName();

    private ListView channelList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i(TAG, "Inflating view");
        return inflater.inflate(R.layout.main_screen, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        channelList = getView().findViewById(R.id.twitch_channel_list);
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {
                while(TwitchHandler.channelResponse == null) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void nothing) {
                populateList();
            }
        }.execute();
    }

    private void populateList() {
        TwitchChannelAdapter adapter = new TwitchChannelAdapter(TwitchHandler.channelResponse.channels);
        channelList.setAdapter(adapter);
    }

    private class TwitchChannelAdapter extends BaseAdapter {
        private List<TwitchChannel> channels;

        public TwitchChannelAdapter(List<TwitchChannel> channels) {
            this.channels = channels;
        }

        @Override
        public int getCount() {
            return channels.size();
        }

        @Override
        public Object getItem(int i) {
            return channels.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if(view == null) {
                view = getLayoutInflater().inflate(R.layout.twitch_list_item, viewGroup, false);
            }

            final TwitchChannel channel = (TwitchChannel) getItem(i);

            /*
            new AsyncTask<ImageView, Void, Bitmap>() {

                @Override
                protected Bitmap doInBackground(Void... voids) {
                    try {
                        URL url = new URL(channel.iconURL);
                        Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                        icon.setImageBitmap(bmp);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    };
                }

                @Override
                protected void onPostExecute(Bitmap image) {
                    ImageView icon = view.findViewById(R.id.twitch_icon);
                }
            }.execute();
            */
            ImageView icon = view.findViewById(R.id.twitch_icon);
            new ImageDownloaderTask(icon).execute(channel.iconURL);
            TextView status = view.findViewById(R.id.twitch_channel_name);
            status.setText(channel.channelTitle);
            return view;
        }
    }

    class ImageDownloaderTask extends AsyncTask<String, Void, Bitmap> {
        private final WeakReference<ImageView> imageViewReference;

        public ImageDownloaderTask(ImageView imageView) {
            imageViewReference = new WeakReference<ImageView>(imageView);
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            try {
                URL url = new URL(params[0]);
                Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                return bmp;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (isCancelled()) {
                bitmap = null;
            }

            if (imageViewReference != null) {
                ImageView imageView = imageViewReference.get();
                if (imageView != null) {
                    if (bitmap != null) {
                        imageView.setImageBitmap(bitmap);
                    } else {
                        imageView.setImageDrawable(getResources().getDrawable(R.drawable.twitch_icon));
                    }
                }
            }
        }
    }
}
