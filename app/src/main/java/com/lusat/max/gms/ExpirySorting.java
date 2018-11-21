package com.lusat.max.gms;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import java.io.IOException;

/**
 * Created by Satyendra on 2/27/2016.
 */
public class ExpirySorting extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_product);
        TextView tv = (TextView) findViewById(R.id.tvSQLinfo);
        DataBaseHelper myDataBase = new DataBaseHelper(this);
        myDataBase.openDataBase();
        String data = myDataBase.getExpiredProduct();
        myDataBase.close();
        tv.setText(data);
    }
}
