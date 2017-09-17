package edu.gatech.teamnull.thdhackathon2017.customviews;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubeBaseActivity;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import edu.gatech.teamnull.thdhackathon2017.R;
import edu.gatech.teamnull.thdhackathon2017.SavedVideosPage;
import edu.gatech.teamnull.thdhackathon2017.SelectedProductPage;
import edu.gatech.teamnull.thdhackathon2017.model.Customer;
import edu.gatech.teamnull.thdhackathon2017.model.Video;

/**
 * Created by colby on 9/17/17.
 */

public class YoutubeVideoArrayAdapter extends ArrayAdapter<Video> implements View.OnClickListener {

    private ArrayList<Video> dataSet;
    private YouTubeBaseActivity mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView title;
        TextView id;
        ImageView thumbnails;
    }

    public YoutubeVideoArrayAdapter(ArrayList<Video> data, SelectedProductPage context) {
        super(context, R.layout.youtube_list_item, data);
        this.dataSet = data;
        this.mContext=context;

    }

    public YoutubeVideoArrayAdapter(ArrayList<Video> data, SavedVideosPage context) {
        super(context, R.layout.youtube_list_item, data);
        this.dataSet = data;
        this.mContext=context;

    }

    @Override
    public Video getItem(int position) {
        return dataSet.get(position);
    }

    @Override
    public void onClick(View view) {
        int position=(Integer) view.getTag();
        Object object= getItem(position);
        Video video =(Video) object;

        if (mContext instanceof SelectedProductPage)
            ((SelectedProductPage) mContext).playVideo(video);
        else ((SavedVideosPage) mContext).playVideo(video);
    }




    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Video dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        LayoutInflater inflater = LayoutInflater.from(getContext());
        View rowView = inflater.inflate(R.layout.youtube_list_item, parent, false);
        viewHolder = new ViewHolder();
        viewHolder.title = (TextView) rowView.findViewById(R.id.titleText);
        viewHolder.id = (TextView) rowView.findViewById(R.id.videoID);
        viewHolder.thumbnails = (ImageView) rowView.findViewById(R.id.thumbnail);

        rowView.setTag(position);


        viewHolder.title.setText(dataModel.getTitle());
        viewHolder.id.setText(dataModel.getId());
        viewHolder.thumbnails.setOnClickListener(this);
        viewHolder.thumbnails.setTag(position);
        try {
            new DownloadImageTask(viewHolder.thumbnails).execute(dataModel.getThumbnail().getUrl());
        }catch (Exception e) {
            e.printStackTrace();
        }
        // Return the completed view to render on screen
        rowView.setOnClickListener(this);
        return rowView;
    }


    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}
