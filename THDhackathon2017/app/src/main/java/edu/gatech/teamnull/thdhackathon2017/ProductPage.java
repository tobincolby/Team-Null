package edu.gatech.teamnull.thdhackathon2017;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import edu.gatech.teamnull.thdhackathon2017.model.*;

/**
 * Created by lucas on 9/16/2017.
 */

public class ProductPage extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_page);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.app_title);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent launchAddPage = new Intent(ProductPage.this, BarcodeScannerActivity.class);
                ProductPage.this.startActivity(launchAddPage);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Hardcoded Customer
        Customer customer = new Customer("Colby", 1);

        ProductDBHelper helper = new ProductDBHelper(getApplicationContext());

        SQLiteDatabase rdb = helper.getReadableDatabase();
        String[] projection = {
                Data.ProductEntry.COLUMN_NAME_TITLE,
                Data.ProductEntry.COLUMN_NAME_PRICE,
                Data.ProductEntry.COLUMN_NAME_SKU,
                Data.ProductEntry._ID
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
        final Product[] products = new Product[cursor.getCount()];
        while(cursor.moveToNext()) {
            products[cursor.getPosition()] = new Product(
                    cursor.getInt(cursor.getColumnIndexOrThrow(Data.ProductEntry._ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(Data.ProductEntry.COLUMN_NAME_TITLE)),
                    cursor.getDouble(cursor.getColumnIndexOrThrow(Data.ProductEntry.COLUMN_NAME_PRICE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(Data.ProductEntry.COLUMN_NAME_SKU)));
            String itemTitle = cursor.getString(
                    cursor.getColumnIndexOrThrow(Data.ProductEntry.COLUMN_NAME_TITLE));
            itemTitles[cursor.getPosition()] = itemTitle;
        }
        cursor.close();

        customer.addProduct(new Product());


        ListView productListView = (ListView) findViewById(R.id.product_list_view);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                itemTitles);
        productListView.setAdapter(arrayAdapter);

        //List View has an onclick listener that passes product string to SelectedProductPage
        productListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                Product entry = products[0];
                for (Product product : products) {
                    String title = (String) parent.getAdapter().getItem(position);
                    if (title.equals(product.getTitle())) {
                        entry = product;
                    }
                }
                Intent intent = new Intent(ProductPage.this, SelectedProductPage.class);
                String key = "ProductTitle";
                intent.putExtra(key, entry);
                startActivity(intent);

            }
        });
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.action_settings);
        item.setVisible(false);
        super.onPrepareOptionsMenu(menu);
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            Intent i = new Intent(this, HomePageActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_account) {

        } else if (id == R.id.nav_barcode) {
            Intent i = new Intent(this, BarcodeScannerActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_saved_videos) {
            Intent i = new Intent(this, SavedVideosPage.class);
            startActivity(i);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
