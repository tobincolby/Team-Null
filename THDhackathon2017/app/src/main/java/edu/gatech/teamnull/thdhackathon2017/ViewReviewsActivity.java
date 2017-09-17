package edu.gatech.teamnull.thdhackathon2017;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import edu.gatech.teamnull.thdhackathon2017.customviews.ReviewAdapter;
import edu.gatech.teamnull.thdhackathon2017.model.Product;
import edu.gatech.teamnull.thdhackathon2017.model.Review;

public class ViewReviewsActivity extends AppCompatActivity {

    private Product thisProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_reviews);

        thisProduct = (Product) getIntent().getSerializableExtra("ProductTitle");

        ListView myListView = (ListView) findViewById(R.id.reviewList);
        ArrayList<Review> reviews = new ArrayList<>();
        ReviewAdapter adapter = new ReviewAdapter(this, reviews);

        myListView.setAdapter(adapter);

    }
}
