package edu.gatech.teamnull.thdhackathon2017;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.view.Menu;
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

import java.text.NumberFormat;
import java.util.ArrayList;

import edu.gatech.teamnull.thdhackathon2017.customviews.YoutubeVideoArrayAdapter;
import edu.gatech.teamnull.thdhackathon2017.model.Config;
import edu.gatech.teamnull.thdhackathon2017.model.Customer;
import edu.gatech.teamnull.thdhackathon2017.model.Product;
import edu.gatech.teamnull.thdhackathon2017.model.Search;
import edu.gatech.teamnull.thdhackathon2017.model.Video;


public class SelectedProductPage extends YouTubeBaseActivity
        implements YouTubePlayer.OnInitializedListener {



    private static final int RECOVERY_REQUEST = 1;
    private YouTubePlayerView youTubeView;
    private MyPlayerStateChangeListener playerStateChangeListener;
    private MyPlaybackEventListener playbackEventListener;
    private YouTubePlayer player;
    private boolean wasRestored = true;
    private FloatingActionButton fab;
    private FloatingActionButton save;

    private TextView tutorials;

    private TextView productTitleLbl;
    private TextView productPriceLbl;
    private TextView productSkuLbl;
    private TableLayout infoTable;

    private boolean videoHidden = true;
    private Button reviewButton;
    private Button viewReviews;

    private Video currentlyPlaying = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_product_page);
        Intent intent = getIntent();
        final Product product = (Product) intent.getSerializableExtra("ProductTitle");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setActionBar(toolbar);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setTitle("DIY Tool Vids");

        viewReviews = (Button) findViewById(R.id.viewReviews);
        viewReviews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SelectedProductPage.this, ViewReviewsActivity.class);
                String key = "ProductTitle";
                i.putExtra(key, product);
                SelectedProductPage.this.startActivity(i);
            }
        });
        tutorials = (TextView) findViewById(R.id.tutorials);
        reviewButton = (Button) findViewById(R.id.reviewButton);
        reviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SelectedProductPage.this, ReviewProductActivity.class);
                String key = "ProductTitle";
                i.putExtra(key, product);
                SelectedProductPage.this.startActivity(i);
            }
        });

        productTitleLbl = (TextView) findViewById(R.id.product_title_label);
        productTitleLbl.setText(product.getTitle());

        productPriceLbl = (TextView) findViewById(R.id.product_price_label);
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        productPriceLbl.setText(formatter.format(product.getPrice()));

        productSkuLbl = (TextView) findViewById(R.id.product_sku_label);
        productSkuLbl.setText(product.getSku());

        infoTable = (TableLayout) findViewById(R.id.infoHolder);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectedProductPage.this.stopVideo();
            }
        });
        fab.setVisibility(View.GONE);

        save = (FloatingActionButton) findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentlyPlaying != null) {
                    Customer.addSavedVideo(currentlyPlaying);
                    final Toast savedToast = Toast.makeText(getApplicationContext(), "Vidoe Saved", Toast.LENGTH_SHORT);
                    savedToast.show();
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            savedToast.cancel();
                        }
                    }, 1000);
                }
            }
        });
        save.setVisibility(View.GONE);

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
        reviewButton.setVisibility(View.GONE);
        viewReviews.setVisibility(View.GONE);
        infoTable.setVisibility(View.GONE);
        tutorials.setVisibility(View.GONE);
    }

    public void showInfo() {
        productPriceLbl.setVisibility(View.VISIBLE);
        productSkuLbl.setVisibility(View.VISIBLE);
        productTitleLbl.setVisibility(View.VISIBLE);
        reviewButton.setVisibility(View.VISIBLE);
        viewReviews.setVisibility(View.VISIBLE);
        infoTable.setVisibility(View.VISIBLE);
        tutorials.setVisibility(View.VISIBLE);
    }



    public void stopVideo() {
        fab.setVisibility(View.GONE);
        save.setVisibility(View.GONE);
        slideToTop(youTubeView);
        //youTubeView.setVisibility(View.GONE);
        showInfo();
        while (player.hasNext()) {
            player.next();
        }
    }

    public void slideToTop(View view){
        TranslateAnimation animate = new TranslateAnimation(0,0,0,-view.getHeight() - reviewButton.getHeight());
        animate.setDuration(500);
        animate.setFillAfter(true);
        view.startAnimation(animate);
        view.setVisibility(View.GONE);
        videoHidden = true;
    }
    public void slideDown(View view){
        hideInfo();
        view.setVisibility(View.VISIBLE);
        TranslateAnimation animate = new TranslateAnimation(0,0,-view.getHeight() - reviewButton.getHeight(),0);
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
        currentlyPlaying = video;

        fab.setVisibility(View.VISIBLE);
        fab.bringToFront();
        save.setVisibility(View.VISIBLE);
        save.bringToFront();


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
            //showMessage("Paused");
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
