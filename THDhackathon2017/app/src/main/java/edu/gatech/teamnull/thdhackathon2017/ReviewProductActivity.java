package edu.gatech.teamnull.thdhackathon2017;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toolbar;

import edu.gatech.teamnull.thdhackathon2017.model.Customer;
import edu.gatech.teamnull.thdhackathon2017.model.Product;
import edu.gatech.teamnull.thdhackathon2017.model.Review;
import edu.gatech.teamnull.thdhackathon2017.model.ReviewDBHelper;

public class ReviewProductActivity extends AppCompatActivity {

    private Product thisProduct;
    private EditText reviewText;
    private RatingBar stars;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_product);
        Intent intent = getIntent();
        thisProduct = (Product) intent.getSerializableExtra("ProductTitle");

        reviewText = (EditText) findViewById(R.id.reviewText);
        stars = (RatingBar) findViewById(R.id.stars);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setActionBar(toolbar);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setTitle("DIY Tool Vids");

        TextView productName = (TextView) findViewById(R.id.productName);
        productName.setText(thisProduct.getTitle());

        Button submit = (Button) findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Customer customer = new Customer("Colby", 0);
                Review review = new Review(reviewText.getText().toString(), Customer.customerName, (int) stars.getRating(), thisProduct.getSku(), thisProduct.getTitle());
                ReviewDBHelper helper = new ReviewDBHelper(getApplicationContext());
                SQLiteDatabase db = helper.getWritableDatabase();
                helper.write(db, review);
                //save the review
                Intent switchToProductList = new Intent(ReviewProductActivity.this, ProductPage.class);
                ReviewProductActivity.this.startActivity(switchToProductList);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        } else if(id==android.R.id.home)
        {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
