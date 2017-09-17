package edu.gatech.teamnull.thdhackathon2017.customviews;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.vision.text.Text;

import java.util.ArrayList;

import edu.gatech.teamnull.thdhackathon2017.R;
import edu.gatech.teamnull.thdhackathon2017.ViewReviewsActivity;
import edu.gatech.teamnull.thdhackathon2017.model.Review;

/**
 * Created by colby on 9/17/17.
 */

public class ReviewAdapter extends ArrayAdapter<Review> implements View.OnClickListener{

    ViewReviewsActivity context;

    ArrayList<Review> dataset;

    private class ViewHolder {
        TextView name;
        TextView stars;
        TextView review;
    }

    public ReviewAdapter(ViewReviewsActivity activity, ArrayList<Review> reviews) {
        super(activity, R.layout.review_list_item);
        dataset = reviews;
        context = activity;
    }

    @Override
    public void onClick(View v) {

        int position=(Integer) v.getTag();
        Object object= getItem(position);
        Review dataModel=(Review) object;

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Review review = dataset.get(position);
        ViewHolder holder=new ViewHolder();
        View rowView;
        rowView = LayoutInflater.from(getContext()).inflate(R.layout.review_list_item, null);
        holder.name=(TextView) rowView.findViewById(R.id.customerName);
        holder.name.setText(review.getAuthor().getName());
        holder.stars= (TextView) rowView.findViewById(R.id.stars);
        holder.stars.setText(review.getRating());
        holder.review = (TextView) rowView.findViewById(R.id.reviewText);
        holder.review.setText(review.getText());
        rowView.setOnClickListener(this);
        rowView.setTag(position);
        return rowView;
    }
}
