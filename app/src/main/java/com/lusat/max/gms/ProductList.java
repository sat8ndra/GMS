package com.lusat.max.gms;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * Created by satye on 6/26/2016.
 */
public class ProductList extends ListActivity {
    String products[] = {"megapen"};
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setListAdapter(new ArrayAdapter<String>(ProductList.this, android.R.layout.simple_list_item_1 , products));
    }
    @Override


    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        String cheese = products[position];
        Class ourClass = null;
        try {
            ourClass = Class.forName("com.lusat.max.gms." + cheese);
            Intent ourIntent = new Intent(ProductList.this,ourClass);
            startActivity(ourIntent);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
}