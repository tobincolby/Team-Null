package edu.gatech.teamnull.thdhackathon2017.model;

/**
 * Created by karshinlin on 9/16/17.
 */

import android.os.AsyncTask;
import android.util.Log;

import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.ResourceId;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.Thumbnail;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

/**
 * Print a list of videos matching a search term.
 *
 */
public class Search extends AsyncTask<Void, Void, Void> {

    /**
     * Define a global variable that identifies the name of a file that
     * contains the developer's API key.
     */
    private static final String PROPERTIES_FILENAME = "edu/gatech/teamnull/thdhackathon2017/youtube.properties";

    private static final long NUMBER_OF_VIDEOS_RETURNED = 10;

    private String apiKey;

    /**
     * Define a global instance of a Youtube object, which will be used
     * to make YouTube Data API requests.
     */
    private static YouTube youtube;
    private static String inputQuery;

    private boolean queryDone = false;

    private List<SearchResult> results;

    private List<Video> videos;

    /**
     * Read YouTube properties file to get API key
     *
     */
    public Search (String inputQuery) {
        // Read the developer key from the properties file.
        //Properties properties = new Properties();
            //InputStream in = new FileInputStream("/" + PROPERTIES_FILENAME);
            //properties.load(in);
        apiKey = "AIzaSyCyZYbZPxJhH-yze8kJpKx_7wDsg6Cz8Pw";
        this.inputQuery = inputQuery;
    }

    protected Void doInBackground(Void... params) {
        try {
            // This object is used to make YouTube Data API requests. The last
            // argument is required, but since we don't need anything
            // initialized when the HttpRequest is initialized, we override
            // the interface and provide a no-op function.
            youtube = new YouTube.Builder(new NetHttpTransport(), new JacksonFactory(), new HttpRequestInitializer() {
                public void initialize(HttpRequest request) throws IOException {
                }
            }).setApplicationName("youtube-cmdline-search-sample").build();

            // Define the API request for retrieving search results.
            YouTube.Search.List search = youtube.search().list("id,snippet");

            search.setKey(apiKey);
            search.setQ(inputQuery);

            // Restrict the search results to only include videos. See:
            // https://developers.google.com/youtube/v3/docs/search/list#type
            search.setType("video");

            // To increase efficiency, only retrieve the fields that the
            // application uses.
            search.setFields("items(id/kind,id/videoId,snippet/title,snippet/thumbnails/default/url)");
            search.setMaxResults(NUMBER_OF_VIDEOS_RETURNED);

            // Call the API and print results.
            SearchListResponse searchResponse = search.execute();
            List<SearchResult> searchResultList = searchResponse.getItems();
            if (searchResultList != null) {
                results = searchResultList;
                videos = prettyPrint(searchResultList.iterator(), inputQuery);
                queryDone = true;
            } else {
                Log.d("FINE", "NO RESULTS");
            }
        } catch (GoogleJsonResponseException e) {
            Log.d("Error", "There was a service error: " + e.getDetails().getCode() + " : "
                    + e.getDetails().getMessage());
        } catch (IOException e) {
            Log.d("Error", "There was an IO error: " + e.getCause() + " : " + e.getMessage());
        } catch (Throwable t) {
            t.printStackTrace();
        }

        return null;
    }

    public boolean isQueryDone() {
        return queryDone;
    }

    public List<SearchResult> getResults() {
        return results;
    }

    public List<Video> getVideos() { return videos; }

    /*
     * Prints out all results in the Iterator. For each result, print the
     * title, video ID, and thumbnail.
     *
     * @param iteratorSearchResults Iterator of SearchResults to print
     *
     * @param query Search query (String)
     */
    private static ArrayList<Video> prettyPrint(Iterator<SearchResult> iteratorSearchResults, String query) {

//        Log.d("Fine", "\n=============================================================");
//        Log.d("Fine",
//                "   First " + NUMBER_OF_VIDEOS_RETURNED + " videos for search on \"" + query + "\".");
//        Log.d("Fine", "=============================================================\n");
//
//        if (!iteratorSearchResults.hasNext()) {
//            Log.d("Fine", " There aren't any results for your query.");
//        }
        ArrayList<Video> listOfVideos = new ArrayList<>();
        while (iteratorSearchResults.hasNext()) {

            SearchResult singleVideo = iteratorSearchResults.next();
            ResourceId rId = singleVideo.getId();

            // Confirm that the result represents a video. Otherwise, the
            // item will not contain a video ID.
            if (rId.getKind().equals("youtube#video")) {
                Thumbnail thumbnail = singleVideo.getSnippet().getThumbnails().getDefault();
                Video vid = new Video(singleVideo.getSnippet().getTitle(), thumbnail, rId.getVideoId());
                listOfVideos.add(vid);
//                Log.d("Fine", " Video Id" + rId.getVideoId());
//                Log.d("Fine", " Title: " + singleVideo.getSnippet().getTitle());
                Log.d("Fine", " Thumbnail: " + thumbnail.getUrl());
//                Log.d("Fine", "\n-------------------------------------------------------------\n");
            }
        }
        return listOfVideos;
    }
}