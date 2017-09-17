package edu.gatech.teamnull.thdhackathon2017;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.twitter.sdk.android.core.DefaultLogger;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterConfig;
import com.twitter.sdk.android.tweetui.TweetTimelineRecyclerViewAdapter;
import com.twitter.sdk.android.tweetui.UserTimeline;

/**
 * Created by karshinlin on 9/17/17.
 */

// Uses an AsyncTask to download a Twitter user's timeline
public class DownloadTwitterTask extends AsyncTask<String, Void, String> {
    final static String CONSUMER_KEY = "yZITfDK7vg5npvQaUzTb0ONWN";
    final static String CONSUMER_SECRET = "2FntJ7AX6yjgA7AYdC7gYF6zdAtZ6vZqoXvlZFjKof4s8mY7bC";

    private Activity theActivity;

    public DownloadTwitterTask(Activity theActivity) {
        this.theActivity = theActivity;
    }

    @Override
    protected String doInBackground(String... screenNames) {
        TwitterConfig config = new TwitterConfig.Builder(theActivity)
                .logger(new DefaultLogger(Log.DEBUG))
                .twitterAuthConfig(new TwitterAuthConfig("yZITfDK7vg5npvQaUzTb0ONWN", "2FntJ7AX6yjgA7AYdC7gYF6zdAtZ6vZqoXvlZFjKof4s8mY7bC"))
                .debug(true)
                .build();
        Twitter.initialize(config);
        return "";
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        final RecyclerView recyclerView = (RecyclerView) theActivity.findViewById(R.id.tweetlist);
        recyclerView.setLayoutManager(new LinearLayoutManager(theActivity));
        final UserTimeline userTimeline = new UserTimeline.Builder()
                .screenName("HomeDepot").maxItemsPerRequest(5)
                .build();
        final TweetTimelineRecyclerViewAdapter adapter =
                new TweetTimelineRecyclerViewAdapter.Builder(theActivity)
                        .setTimeline(userTimeline)
                        .setViewStyle(R.style.tw__TweetLightWithActionsStyle)
                        .build();
        recyclerView.setAdapter(adapter);
    }

}