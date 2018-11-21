package com.lusat.max.gms;

/**
 * Created by satye on 1/21/2016.
 */
import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import java.io.IOException;

/**
 * Created by satye on 12/12/2015.
 */
public class AllProduct extends Activity {

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_product);
        TextView tv = (TextView) findViewById(R.id.tvSQLinfo);
        DataBaseHelper myDataBase = new DataBaseHelper(this);
        try {
            myDataBase.createDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }
        myDataBase.openDataBase();
        String data = myDataBase.getProductName();
        myDataBase.close();
        tv.setText(data);
    }
}
