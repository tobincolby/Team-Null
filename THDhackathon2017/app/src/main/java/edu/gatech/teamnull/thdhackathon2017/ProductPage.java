package edu.gatech.teamnull.thdhackathon2017;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * Created by lucas on 9/16/2017.
 */

public class ProductPage extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_page);
    }

    private ListView productListView = (ListView) findViewById(R.id.product_list_view);
    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
            this,
            android.R.layout.product_view,
            productsPurchased );

         productListView.setAdapter(arrayAdapter);

}
