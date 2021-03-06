package com.android.nissen.udacity_inventoryapp.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.android.nissen.udacity_inventoryapp.data.InventoryContract.InventoryEntry;

import java.util.ArrayList;

/**
 * Created by Josh Nissen on 3/28/2017.
 */

public class InventoryDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    static final String DATABASE_NAME = "Inventory.db";

    public InventoryDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_TABLE = "CREATE TABLE " + InventoryContract.InventoryEntry.TABLE_NAME + " (" +
                InventoryContract.InventoryEntry.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                InventoryContract.InventoryEntry.COLUMN_PRODUCT_NAME + " TEXT NOT NULL, " +
                InventoryContract.InventoryEntry.COLUMN_QUANTITY + " INTEGER NOT NULL, " +
                InventoryContract.InventoryEntry.COLUMN_PRICE + " INTEGER NOT NULL, " +
                InventoryContract.InventoryEntry.COLUMN_IMAGE + " BLOB);";
        sqLiteDatabase.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + InventoryContract.InventoryEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    // Insert data in the table
    public boolean insertData(String productName, int quantity, int price, byte[] image) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(InventoryContract.InventoryEntry.COLUMN_PRODUCT_NAME, productName);
        contentValues.put(InventoryContract.InventoryEntry.COLUMN_QUANTITY, quantity);
        contentValues.put(InventoryContract.InventoryEntry.COLUMN_PRICE, price);
        contentValues.put(InventoryContract.InventoryEntry.COLUMN_IMAGE, image);
        db.insert(InventoryContract.InventoryEntry.TABLE_NAME, null, contentValues);
        return true;
    }

    // Get data from the table
    public Cursor getData(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor result = db.rawQuery("SELECT * from " + InventoryContract.InventoryEntry.TABLE_NAME +
                " WHERE name=\"" + name + "\"", null);
        return result;
    }

    // Delete all table entries
    public int deleteAllEntries() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(InventoryContract.InventoryEntry.TABLE_NAME, null, null);
    }

    // Delete one table entry
    public boolean deleteData(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(InventoryContract.InventoryEntry.TABLE_NAME, "name=?", new String[]{name}) > 0;
    }

    // Update data in the table
    public void updateData(String name, int quantity, int change) {
        SQLiteDatabase db = this.getWritableDatabase();
        String strSQL = "UPDATE " + InventoryContract.InventoryEntry.TABLE_NAME + " SET quantity = "
                + (quantity + change) + " WHERE name = \"" + name + "\"";
        db.execSQL(strSQL);
    }

    public ArrayList<String> getAllData() {
        ArrayList<String> productList = new ArrayList<String>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor getList = db.rawQuery("SELECT * FROM " + InventoryContract.InventoryEntry.TABLE_NAME, null);
        getList.moveToFirst();
        while (getList.isAfterLast() == false) {
            String productName = getList.getString(getList.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_PRODUCT_NAME));
            int quantity = getList.getInt(getList.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_QUANTITY));
            int price = getList.getInt(getList.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_PRICE));
            productList.add(productName + "\n" + "Quantity: " + quantity + "\n" + "Price: $" + price);
            getList.moveToNext();
        }
        return productList;
    }
}
