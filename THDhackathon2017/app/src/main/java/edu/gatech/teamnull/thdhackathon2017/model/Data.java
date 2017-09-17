package edu.gatech.teamnull.thdhackathon2017.model;

import android.provider.BaseColumns;

/**
 * Created by karshinlin on 9/16/17.
 */

public class Data {

    private static Product[] list_of_products;
    private static Customer[] list_of_customers;

    private Data() { }

    public static class ProductEntry implements BaseColumns {
        public static final String TABLE_NAME = "Products";
        public static final String _ID = "ID";

        public static final String COLUMN_NAME_PRICE = "Price";
        public static final String COLUMN_NAME_TITLE = "Title";
        public static final String COLUMN_NAME_SKU = "SKU";
    }

//    public static class CustomerEntry implements BaseColumns {
//        public static final String TABLE_NAME = "entry";
//        public static final String COLUMN_NAME_TITLE = "title";
//        public static final String COLUMN_NAME_TITLE2 = ""
//    }


}
