package edu.gatech.teamnull.thdhackathon2017;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayer.Provider;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.api.services.youtube.model.Thumbnail;
import com.google.api.services.youtube.model.Activity;

import java.util.ArrayList;

import edu.gatech.teamnull.thdhackathon2017.customviews.YoutubeVideoArrayAdapter;
import edu.gatech.teamnull.thdhackathon2017.model.Config;
import edu.gatech.teamnull.thdhackathon2017.model.Customer;
import edu.gatech.teamnull.thdhackathon2017.model.Data;
import edu.gatech.teamnull.thdhackathon2017.model.Product;
import edu.gatech.teamnull.thdhackathon2017.model.ProductDBHelper;
import edu.gatech.teamnull.thdhackathon2017.model.Video;
import edu.gatech.teamnull.thdhackathon2017.model.VideoDBHelper;


public class SavedVideosPage extends YouTubeBaseActivity
        implements YouTubePlayer.OnInitializedListener {



    private static final int RECOVERY_REQUEST = 1;
    private YouTubePlayerView youTubeView;
    private MyPlayerStateChangeListener playerStateChangeListener;
    private MyPlaybackEventListener playbackEventListener;
    private YouTubePlayer player;
    private boolean wasRestored = true;
    private FloatingActionButton fab;
    private FloatingActionButton delete;
    private boolean videoHidden = true;

    private Video currentlyPlaying = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_videos_page);
        Intent intent = getIntent();
        final Product product = (Product) intent.getSerializableExtra("ProductTitle");
        Toolbar toolbar = findViewById(R.id.toolbar);
        setActionBar(toolbar);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setTitle("DIYTube Saved Videos");

        VideoDBHelper helper = new VideoDBHelper(getApplicationContext());

        SQLiteDatabase rdb = helper.getReadableDatabase();
        String[] projection = {
                Data.VideoEntry.COLUMN_NAME_TITLE,
                Data.VideoEntry.COLUMN_NAME_THUMBNAIL,
                Data.VideoEntry.COLUMN_NAME_ID,
                Data.VideoEntry.COLUMN_NAME_DATE
        };
        String sortOrder =
                Data.VideoEntry.COLUMN_NAME_DATE + " DESC";
        Cursor cursor = rdb.query(
                Data.VideoEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                sortOrder
        );
        ArrayList<Video> videos = new ArrayList<>();
        while(cursor.moveToNext()) {
            Thumbnail tn = new Thumbnail();
            tn.setUrl(cursor.getString(cursor.getColumnIndexOrThrow(Data.VideoEntry.COLUMN_NAME_THUMBNAIL)));
            videos.add(new Video(
                    cursor.getString(cursor.getColumnIndexOrThrow(Data.VideoEntry.COLUMN_NAME_TITLE)),
                    tn,
                    cursor.getString(cursor.getColumnIndexOrThrow(Data.VideoEntry.COLUMN_NAME_ID))));
        }
        cursor.close();

//        updateUI(Customer.getMySavedVideos());
        updateUI(videos);


        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SavedVideosPage.this.stopVideo();
            }
        });
        fab.setVisibility(View.GONE);

        delete = (FloatingActionButton) findViewById(R.id.delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentlyPlaying != null) {
                    VideoDBHelper helper1 = new VideoDBHelper(getApplicationContext());

                    SQLiteDatabase rdb1 = helper1.getReadableDatabase();
                    String selection = Data.VideoEntry.COLUMN_NAME_ID + " LIKE ?";
                    String[] selectionArgs = { currentlyPlaying.getId() };
                    rdb1.delete(Data.VideoEntry.TABLE_NAME, selection, selectionArgs);

                    Customer.deleteSavedVideo(currentlyPlaying);
                    //recreate();
                    final Toast deleteToast = Toast.makeText(getApplicationContext(), "Video Removed", Toast.LENGTH_SHORT);
                    deleteToast.show();
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            deleteToast.cancel();
                        }
                    }, 1000);
                    finish();
                }
            }
        });
        delete.setVisibility(View.GONE);

        youTubeView = (YouTubePlayerView) findViewById(R.id.youtube_view);
        youTubeView.setVisibility(View.GONE);
        youTubeView.initialize(Config.YOUTUBE_API_KEY, this);

        playerStateChangeListener = new MyPlayerStateChangeListener();
        playbackEventListener = new MyPlaybackEventListener();
    }


    public void stopVideo() {
        fab.setVisibility(View.GONE);
        delete.setVisibility(View.GONE);
        slideToTop(youTubeView);
        //youTubeView.setVisibility(View.GONE);
        while (player.hasNext()) {
            player.next();
        }
    }

    public void slideToTop(View view){
        TranslateAnimation animate = new TranslateAnimation(0,0,0,-view.getHeight());
        animate.setDuration(500);
        animate.setFillAfter(true);
        view.startAnimation(animate);
        view.setVisibility(View.GONE);
        videoHidden = true;
    }
    public void slideDown(View view){
        view.setVisibility(View.VISIBLE);
        TranslateAnimation animate = new TranslateAnimation(0,0,-view.getHeight(),0);
        animate.setDuration(500);
        animate.setFillAfter(true);
        view.startAnimation(animate);
        videoHidden = false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onInitializationSuccess(Provider provider, YouTubePlayer player, boolean wasRestored) {
        this.player = player;
        player.setPlayerStateChangeListener(playerStateChangeListener);
        player.setPlaybackEventListener(playbackEventListener);
        this.wasRestored = wasRestored;
    }

    @Override
    public void onInitializationFailure(Provider provider, YouTubeInitializationResult errorReason) {
        if (errorReason.isUserRecoverableError()) {
            errorReason.getErrorDialog(this, RECOVERY_REQUEST).show();
        } else {
            String error = String.format(getString(R.string.player_error), errorReason.toString());
            Toast.makeText(this, error, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RECOVERY_REQUEST) {
            // Retry initialization if user performed a recovery action
            getYouTubePlayerProvider().initialize(Config.YOUTUBE_API_KEY, this);
        }
    }

    public void updateUI(ArrayList<Video> results) {
        YoutubeVideoArrayAdapter adapter = new YoutubeVideoArrayAdapter(results, this);
        ListView list = (ListView) findViewById(R.id.videoList);
        list.setAdapter(adapter);
    }

    public void playVideo(Video video) {
        //youTubeView.setVisibility(View.VISIBLE);
        if (videoHidden) {
            slideDown(youTubeView);
        }
        currentlyPlaying = video;

        fab.setVisibility(View.VISIBLE);
        fab.bringToFront();
        delete.setVisibility(View.VISIBLE);
        delete.bringToFront();
        currentlyPlaying = video;
        if (!wasRestored) {
            player.cueVideo(video.getId());
            if (player.hasNext()) {
                player.next();
            }
        }
    }

    protected Provider getYouTubePlayerProvider() {
        return youTubeView;
    }

    private void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    private final class MyPlaybackEventListener implements YouTubePlayer.PlaybackEventListener {

        @Override
        public void onPlaying() {
            // Called when playback starts, either due to user action or call to play().
            // Optimized to only show Toast for 1 sec
            final Toast playToast = Toast.makeText(getApplicationContext(), "Playing", Toast.LENGTH_SHORT);
            playToast.show();
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    playToast.cancel();
                }
            }, 1000);
        }

        @Override
        public void onPaused() {
            // Called when playback is paused, either due to user action or call to pause().
            // Optimized to only show Toast for 1 sec
            final Toast pauseToast = Toast.makeText(getApplicationContext(), "Paused", Toast.LENGTH_SHORT);
            pauseToast.show();
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    pauseToast.cancel();
                }
            }, 1000);
        }

        @Override
        public void onStopped() {
            // Called when playback stops for a reason other than being paused.
            // Optimized to only show Toast for 1 sec
            final Toast stopToast = Toast.makeText(getApplicationContext(), "Stopped", Toast.LENGTH_SHORT);
            stopToast.show();
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    stopToast.cancel();
                }
            }, 1000);
        }

        @Override
        public void onBuffering(boolean b) {
            // Called when buffering starts or ends.
        }

        @Override
        public void onSeekTo(int i) {
            // Called when a jump in playback position occurs, either
            // due to user scrubbing or call to seekRelativeMillis() or seekToMillis()
        }
    }

    private final class MyPlayerStateChangeListener implements YouTubePlayer.PlayerStateChangeListener {

        @Override
        public void onLoading() {
            // Called when the player is loading a video
            // At this point, it's not ready to accept commands affecting playback such as play() or pause()
        }

        @Override
        public void onLoaded(String s) {
            // Called when a video is done loading.
            // Playback methods such as play(), pause() or seekToMillis(int) may be called after this callback.
        }

        @Override
        public void onAdStarted() {
            // Called when playback of an advertisement starts.
        }

        @Override
        public void onVideoStarted() {
            // Called when playback of the video starts.
        }

        @Override
        public void onVideoEnded() {
            // Called when the video reaches its end.
        }

        @Override
        public void onError(YouTubePlayer.ErrorReason errorReason) {
            // Called when an error occurs.
        }
    }
}
