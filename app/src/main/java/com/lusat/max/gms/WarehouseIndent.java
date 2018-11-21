package com.lusat.max.gms;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by satye on 6/18/2016.
 */
public class WarehouseIndent extends Activity implements View.OnClickListener {

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.warehouse_screen);

    }

    public void onClick(View arg0){
        switch (arg0.getId()){
            case R.id.bSQLSearch:

            case R.id.bQtySearch:

                break;
        }

    }
}
