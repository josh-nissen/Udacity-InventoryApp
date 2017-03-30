package com.android.nissen.udacity_inventoryapp;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.CharArrayBuffer;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.nissen.udacity_inventoryapp.data.InventoryContract.InventoryEntry;
import com.android.nissen.udacity_inventoryapp.InventoryActivity;
import com.android.nissen.udacity_inventoryapp.data.InventoryDbHelper;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final InventoryDbHelper db = new InventoryDbHelper(this);


        final ListView lv = (ListView) findViewById(R.id.list_1);
        lv.setEmptyView(findViewById(R.id.empty_text));
        ArrayList<String> list = db.getAllData();
        ItemListAdapter arrayAdapter = new ItemListAdapter(
                MainActivity.this, list);
        lv.setAdapter(arrayAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent infoIntent = new Intent(MainActivity.this, InventoryActivity.class);
                String itemSelected = ((TextView) view.findViewById(R.id.text_1)).getText().toString();
                infoIntent.putExtra("listItem", itemSelected);
                startActivity(infoIntent);
            }
        });

        Button add = (Button) findViewById(R.id.btn_add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addIntent = new Intent(MainActivity.this, AddProductActivity.class);
                startActivity(addIntent);
            }
        });

        // Refresh List
        Button refresh = (Button) findViewById(R.id.btn_refresh);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ListView lv = (ListView) findViewById(R.id.list_1);
                lv.setEmptyView(findViewById(R.id.empty_text));
                ArrayList<String> list = db.getAllData();
                ItemListAdapter arrayAdapter = new ItemListAdapter(MainActivity.this, list);
                lv.setAdapter(arrayAdapter);
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Intent infoIntent = new Intent(MainActivity.this, InventoryActivity.class);
                        String itemSelected = ((TextView) view.findViewById(R.id.text_1)).getText().toString();
                        infoIntent.putExtra("listItem", itemSelected);
                        startActivity(infoIntent);
                    }
                });
            }
        });

        // Delete Everything     (╯°□°）╯︵ ┻━┻
        Button delete = (Button) findViewById(R.id.btn_delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.deleteAllEntries();
                Toast.makeText(MainActivity.this, "Refresh the list!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Insert dummy data" menu option
            case R.id.insert_initial_data:
                insertInitialData();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void insertInitialData(){
        InventoryDbHelper db = new InventoryDbHelper(this);
        String productName = "Staples";
        int productPrice = 15;
        int productQty = 300;
        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.staples);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        db.insertData(productName, productQty, productPrice, byteArray);
    }
}
