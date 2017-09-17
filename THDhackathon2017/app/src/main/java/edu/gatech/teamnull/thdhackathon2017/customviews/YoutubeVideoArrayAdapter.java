package edu.gatech.teamnull.thdhackathon2017.customviews;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import edu.gatech.teamnull.thdhackathon2017.R;
import edu.gatech.teamnull.thdhackathon2017.model.Video;

/**
 * Created by colby on 9/17/17.
 */

public class YoutubeVideoArrayAdapter extends ArrayAdapter<Video> implements View.OnClickListener {

    private ArrayList<Video> dataSet;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView title;
        TextView id;
        ImageView thumbnails;
    }

    public YoutubeVideoArrayAdapter(ArrayList<Video> data, Context context) {
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


    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Video dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag


        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.youtube_list_item, parent, false);
            viewHolder.title = (TextView) convertView.findViewById(R.id.titleText);
            viewHolder.id = (TextView) convertView.findViewById(R.id.videoID);
            viewHolder.thumbnails = (ImageView) convertView.findViewById(R.id.thumbnail);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.title.setText(dataModel.getTitle());
        viewHolder.id.setText(dataModel.getId());
        viewHolder.thumbnails.setOnClickListener(this);
        viewHolder.thumbnails.setTag(position);
        // Return the completed view to render on screen
        convertView.setOnClickListener(this);
        return convertView;
    }
}
