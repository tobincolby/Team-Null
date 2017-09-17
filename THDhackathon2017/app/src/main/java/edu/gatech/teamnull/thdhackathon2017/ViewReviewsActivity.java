package edu.gatech.teamnull.thdhackathon2017;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import java.util.ArrayList;

import edu.gatech.teamnull.thdhackathon2017.customviews.ReviewAdapter;
import edu.gatech.teamnull.thdhackathon2017.model.Data;
import edu.gatech.teamnull.thdhackathon2017.model.Product;
import edu.gatech.teamnull.thdhackathon2017.model.Review;
import edu.gatech.teamnull.thdhackathon2017.model.ReviewDBHelper;

public class ViewReviewsActivity extends AppCompatActivity {

    private Product thisProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_reviews);

        thisProduct = (Product) getIntent().getSerializableExtra("ProductTitle");

        ListView myListView = (ListView) findViewById(R.id.reviewList);

        ReviewDBHelper helper = new ReviewDBHelper(getApplicationContext());

        SQLiteDatabase rdb = helper.getReadableDatabase();
        String[] projection = {
                Data.ReviewEntry.COLUMN_NAME_TITLE,
                Data.ReviewEntry.COLUMN_NAME_TEXT,
                Data.ReviewEntry.COLUMN_NAME_RATING,
                Data.ReviewEntry.COLUMN_NAME_REVIEWER,
                Data.ReviewEntry.COLUMN_NAME_SKU
        };
        // Filter results WHERE "title" = 'My Title'
        String selection = Data.ReviewEntry.COLUMN_NAME_SKU + " = ?";
        String[] selectionArgs = { thisProduct.getSku() };
        String sortOrder =
                Data.ReviewEntry.COLUMN_NAME_TITLE + " DESC";
        Cursor cursor = rdb.query(
                Data.ReviewEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
        ArrayList<Review> reviews = new ArrayList<>();
        while(cursor.moveToNext()) {
            reviews.add(new Review(
                    cursor.getString(cursor.getColumnIndexOrThrow(Data.ReviewEntry.COLUMN_NAME_TEXT)),
                    cursor.getString(cursor.getColumnIndexOrThrow(Data.ReviewEntry.COLUMN_NAME_REVIEWER)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(Data.ReviewEntry.COLUMN_NAME_RATING)),
                    cursor.getString(cursor.getColumnIndexOrThrow(Data.ReviewEntry.COLUMN_NAME_SKU)),
                    cursor.getString(cursor.getColumnIndexOrThrow(Data.ReviewEntry.COLUMN_NAME_TITLE))));
        }
        cursor.close();

        ReviewAdapter adapter = new ReviewAdapter(this, reviews);

        myListView.setAdapter(adapter);

    }
}
