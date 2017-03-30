package com.android.nissen.udacity_inventoryapp.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Josh Nissen on 3/28/2017.
 */

public final class InventoryContract {

    private InventoryContract() {}

    public static class InventoryEntry implements BaseColumns {



        public static final String TABLE_NAME = "catalog";
        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_PRODUCT_NAME = "name";
        public static final String COLUMN_QUANTITY = "quantity";
        public static final String COLUMN_PRICE = "price";
        public static final String COLUMN_IMAGE = "image";

    }
}
