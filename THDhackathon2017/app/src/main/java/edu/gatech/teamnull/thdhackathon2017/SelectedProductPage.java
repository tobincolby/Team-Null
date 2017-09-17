package edu.gatech.teamnull.thdhackathon2017;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayer.Provider;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.api.services.youtube.model.SearchResult;
import edu.gatech.teamnull.thdhackathon2017.model.Video;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import edu.gatech.teamnull.thdhackathon2017.customviews.YoutubeVideoArrayAdapter;
import edu.gatech.teamnull.thdhackathon2017.model.Config;
import android.content.Intent;
import android.os.Bundle;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

import edu.gatech.teamnull.thdhackathon2017.model.Product;
import edu.gatech.teamnull.thdhackathon2017.model.Search;


public class SelectedProductPage extends YouTubeBaseActivity
        implements YouTubePlayer.OnInitializedListener {



    private static final int RECOVERY_REQUEST = 1;
    private YouTubePlayerView youTubeView;
    private MyPlayerStateChangeListener playerStateChangeListener;
    private MyPlaybackEventListener playbackEventListener;
    private YouTubePlayer player;
    private boolean wasRestored = true;
    private FloatingActionButton fab;

    private TextView productTitleLbl;
    private TextView productPriceLbl;
    private TextView productSkuLbl;

    private boolean videoHidden = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_product_page);
        Intent intent = getIntent();
        Product product = (Product) intent.getSerializableExtra("ProductTitle");

        productTitleLbl = (TextView) findViewById(R.id.product_title_label);
        productTitleLbl.setText(product.getTitle());

        productPriceLbl = (TextView) findViewById(R.id.product_price_label);
        productPriceLbl.setText("$" + product.getPrice());

        productSkuLbl = (TextView) findViewById(R.id.product_sku_label);
        productSkuLbl.setText(product.getSku());

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectedProductPage.this.stopVideo();
            }
        });
        fab.setVisibility(View.GONE);
        showInfo();

        Search mySearch = new Search(product.getTitle() + " tool tutorial", this);

        mySearch.execute();


        youTubeView = (YouTubePlayerView) findViewById(R.id.youtube_view);
        youTubeView.setVisibility(View.GONE);
        youTubeView.initialize(Config.YOUTUBE_API_KEY, this);

        playerStateChangeListener = new MyPlayerStateChangeListener();
        playbackEventListener = new MyPlaybackEventListener();
    }

    public void hideInfo() {
        productPriceLbl.setVisibility(View.GONE);
        productSkuLbl.setVisibility(View.GONE);
        productTitleLbl.setVisibility(View.GONE);
    }

    public void showInfo() {
        productPriceLbl.setVisibility(View.VISIBLE);
        productSkuLbl.setVisibility(View.VISIBLE);
        productTitleLbl.setVisibility(View.VISIBLE);
    }

    public void stopVideo() {
        fab.setVisibility(View.GONE);
        slideToTop(youTubeView);
        //youTubeView.setVisibility(View.GONE);
        showInfo();
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

    public void updateUI(ArrayList<edu.gatech.teamnull.thdhackathon2017.model.Video> results) {
        YoutubeVideoArrayAdapter adapter = new YoutubeVideoArrayAdapter(results, this);
        ListView list = (ListView) findViewById(R.id.videoList);
        list.setAdapter(adapter);
    }

    public void playVideo(Video video) {
        //youTubeView.setVisibility(View.VISIBLE);
        if (videoHidden) {
            slideDown(youTubeView);
        }

        fab.setVisibility(View.VISIBLE);
        fab.bringToFront();
        hideInfo();
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
            showMessage("Playing");
        }

        @Override
        public void onPaused() {
            // Called when playback is paused, either due to user action or call to pause().
            showMessage("Paused");
        }

        @Override
        public void onStopped() {
            // Called when playback stops for a reason other than being paused.
            showMessage("Stopped");
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
