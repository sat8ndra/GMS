package com.lusat.max.gms;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;

/**
 * Created by satye on 12/12/2015.
 */
public class PriceCheck extends Activity implements View.OnClickListener {

    Button sqlSearch,sqlPrice, sqlView, sqlQty;
    EditText sqlName, sqlMrp;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.price_screen);
        sqlPrice = (Button)findViewById(R.id.bPriceSearch);
        sqlSearch = (Button)findViewById(R.id.bSQLSearch);
        sqlName = (EditText)findViewById(R.id.etSQLName);
        sqlSearch.setOnClickListener(this);
        sqlPrice.setOnClickListener(this);
    }

    public void onClick(View arg0){
        switch (arg0.getId()){
            case R.id.bSQLSearch:
                try {
                    String name = sqlName.getText().toString();
                    DataBaseHelper myDataBase = new DataBaseHelper(this);
                    TextView tvResult = (TextView) findViewById(R.id.tvResult);
                    myDataBase.createDataBase();
                    myDataBase.openDataBase();
                    String result = myDataBase.searchProduct(name);
                    myDataBase.close();
                    tvResult.setText(result);
                }catch (Exception e){
                    e.printStackTrace();
                }
                break;
            case R.id.bPriceSearch:
                try {
                    String name = sqlName.getText().toString();
                    DataBaseHelper myDataBase = new DataBaseHelper(this);
                    TextView tvResult = (TextView) findViewById(R.id.tvResult);
                    myDataBase.createDataBase();
                    myDataBase.openDataBase();
                    String result = myDataBase.searchPrice(name);
                    myDataBase.close();
                    tvResult.setText(result);
                }catch (Exception e){
                    e.printStackTrace();
                }
                break;
        }

    }

}