package edu.gatech.teamnull.thdhackathon2017;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import edu.gatech.teamnull.thdhackathon2017.model.Customer;
import edu.gatech.teamnull.thdhackathon2017.model.Product;
import edu.gatech.teamnull.thdhackathon2017.model.Review;

public class ReviewProductActivity extends AppCompatActivity {

    private Product thisProduct;
    private EditText reviewText;
    private EditText stars;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_product);
        Intent intent = getIntent();
        thisProduct = (Product) intent.getSerializableExtra("ProductTitle");

        reviewText = (EditText) findViewById(R.id.reviewText);
        stars = (EditText) findViewById(R.id.stars);

        TextView productName = (TextView) findViewById(R.id.productName);
        productName.setText(thisProduct.getTitle());

        Button submit = (Button) findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Customer customer = new Customer("Colby", 0);
                Review review = new Review(reviewText.getText().toString(), customer, Integer.parseInt(stars.getText().toString()));
                //save the review
                finish();
            }
        });

    }
}
