package edu.gatech.teamnull.thdhackathon2017;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import edu.gatech.teamnull.thdhackathon2017.model.*;

/**
 * Created by lucas on 9/16/2017.
 */

public class ProductPage extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_page);
        Customer customer = new Customer("Colby", 1);
        customer.addProduct(new Product());
        ListView productListView = (ListView) findViewById(R.id.product_list_view);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                customer.getProductsByName());
        productListView.setAdapter(arrayAdapter);
    }







}
