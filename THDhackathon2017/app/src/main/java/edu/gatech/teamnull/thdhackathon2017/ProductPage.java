package edu.gatech.teamnull.thdhackathon2017;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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
        Product current = new Product();
        customer.addProduct(current);

        //add Product to SQLite database
        ProductDBHelper helper = new ProductDBHelper(getApplicationContext());
        SQLiteDatabase db = helper.getWritableDatabase();
        helper.write(db, current);
        Product current1 = new Product("Wrench", 12, "ASdf4e");
        customer.addProduct(current1);
        helper.write(db, current1);
        SQLiteDatabase rdb = helper.getReadableDatabase();
        String[] projection = {
                Data.ProductEntry.COLUMN_NAME_TITLE
        };
        String sortOrder =
                Data.ProductEntry.COLUMN_NAME_TITLE + " DESC";
        Cursor cursor = rdb.query(
                Data.ProductEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                sortOrder
        );
        String[] itemTitles = new String[cursor.getCount()];
        while(cursor.moveToNext()) {
            String itemTitle = cursor.getString(
                    cursor.getColumnIndexOrThrow(Data.ProductEntry.COLUMN_NAME_TITLE));
            itemTitles[cursor.getPosition()] = itemTitle;
        }
        cursor.close();

        ListView productListView = (ListView) findViewById(R.id.product_list_view);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                itemTitles);
        productListView.setAdapter(arrayAdapter);
    }







}
