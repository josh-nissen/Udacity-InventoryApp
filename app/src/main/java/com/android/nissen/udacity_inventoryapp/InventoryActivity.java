package com.android.nissen.udacity_inventoryapp;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.nissen.udacity_inventoryapp.data.InventoryContract;
import com.android.nissen.udacity_inventoryapp.data.InventoryContract.InventoryEntry;
import com.android.nissen.udacity_inventoryapp.data.InventoryDbHelper;

import static java.security.AccessController.getContext;

/**
 * Created by Josh Nissen on 3/28/2017.
 */

public class InventoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inventory_edit);

        final InventoryDbHelper db = new InventoryDbHelper(this);
        Intent getListName = getIntent();
        String productName = getListName.getExtras().getString("listItem");
        int pos = productName.indexOf("\nQuantity");
        final String subProductName = productName.substring(0, pos);


        final Cursor cur = db.getData(subProductName);

        if (cur.moveToFirst()) {

            // Set Product Name
            TextView tName = (TextView) findViewById(R.id.text_name);
            tName.setText(" " + subProductName);

            // Set Price
            int price = cur.getInt(cur.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_PRICE));
            TextView tPrice = (TextView) findViewById(R.id.text_price);
            tPrice.setText("$" + price);

            // Set Quantity
            int quantity = cur.getInt(cur.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_QUANTITY));
            TextView tQuantity = (TextView) findViewById(R.id.text_quantity);
            tQuantity.setText(String.valueOf(quantity));
        }

        // Decrease quantity by 1
        Button btnTrack = (Button) findViewById(R.id.track);
        btnTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cur.moveToFirst()) {
                    int quantity = cur.getInt(cur.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_QUANTITY));
                    if (quantity > 0) {
                        db.updateData(subProductName, quantity, -1);
                        quantity = cur.getInt(cur.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_QUANTITY));
                        TextView tQuantity = (TextView) findViewById(R.id.text_quantity);
                        tQuantity.setText(String.valueOf(quantity));
                    } else {
                        Toast.makeText(InventoryActivity.this, "It's empty! Order Now!", Toast.LENGTH_SHORT).show();
                    }
                }
                Intent intent = getIntent();
                overridePendingTransition(0, 0);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                finish();
                overridePendingTransition(0, 0);
                startActivity(intent);
            }
        });

        // Increase quantity by 1
        Button btnReceive = (Button) findViewById(R.id.receive);
        btnReceive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cur.moveToFirst()) {
                    int quantity = cur.getInt(cur.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_QUANTITY));
                    db.updateData(subProductName, quantity, 1);
                    quantity = cur.getInt(cur.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_QUANTITY));
                    TextView tQuantity = (TextView) findViewById(R.id.text_quantity);
                    tQuantity.setText(String.valueOf(quantity));
                }
                Intent intent = getIntent();
                overridePendingTransition(0, 0);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                finish();
                overridePendingTransition(0, 0);
                startActivity(intent);
            }
        });

        // Order Now
        Button orderNow = (Button) findViewById(R.id.order);
        orderNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String productName = "";
                if (cur.moveToFirst()) {
                    productName = cur.getString(cur.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_PRODUCT_NAME));
                }
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("message/rfc822");
                intent.putExtra(Intent.EXTRA_TEXT, "In need of some " + productName);
                startActivity(Intent.createChooser(intent, "Send Email"));
            }
        });

        Button sell_ = (Button) findViewById(R.id.sell);
        sell_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cur.moveToFirst()) {
                    int quantity = cur.getInt(cur.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_QUANTITY));
                    if (quantity > 0) {
                        db.updateData(subProductName, quantity, -1);
                        quantity = cur.getInt(cur.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_QUANTITY));
                        TextView tQuantity = (TextView) findViewById(R.id.text_quantity);
                        tQuantity.setText(String.valueOf(quantity));
                    } else {
                        Toast.makeText(InventoryActivity.this, "It's empty! Order Now!", Toast.LENGTH_SHORT).show();
                    }
                }
                Intent intent = getIntent();
                overridePendingTransition(0, 0);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                finish();
                overridePendingTransition(0, 0);
                startActivity(intent);
            }
        });

        // delete row
        Button delete_ = (Button) findViewById(R.id.delete_data);
        delete_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                if (db.deleteData(subProductName)) {
                                    Intent returnHome = new Intent(InventoryActivity.this, MainActivity.class);
                                    startActivity(returnHome);
                                    Toast.makeText(InventoryActivity.this, "Deleted!", Toast.LENGTH_SHORT).show();
                                }
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                break;
                        }
                    }
                };
                android.support.v7.app.AlertDialog.Builder ab = new android.support.v7.app.AlertDialog.Builder(InventoryActivity.this);
                ab.setMessage("Are you sure you want to delete?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
            }
        });

        // Convert byte array to bitmap and display the image
        ImageView img = (ImageView) findViewById(R.id.imageView);
        byte[] image = cur.getBlob(cur.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_IMAGE));
        Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
        img.setImageBitmap(bitmap);
    }
    
    @Override
public void onRestart(){
super.onRestart();
finish();
startAvtivity(getIntent());
}

}
