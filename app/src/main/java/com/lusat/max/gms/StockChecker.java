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
public class StockChecker extends Activity implements View.OnClickListener {

    Button sqlSearch, sqlView, sqlQty;
    EditText sqlName, sqlMrp;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen);
        sqlQty = (Button)findViewById(R.id.bQtySearch);
        sqlSearch = (Button)findViewById(R.id.bSQLSearch);
        sqlName = (EditText)findViewById(R.id.etSQLName);
        sqlSearch.setOnClickListener(this);
        sqlQty.setOnClickListener(this);

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
                    String result = myDataBase.searchResult(name);
                    myDataBase.close();
                    tvResult.setText(result);
                }catch (Exception e){
                   e.printStackTrace();
                }
                break;
            case R.id.bQtySearch:
                try {
                    String name = sqlName.getText().toString();
                    DataBaseHelper myDataBase = new DataBaseHelper(this);
                    TextView tvResult = (TextView)findViewById(R.id.tvResult);
                    TextView textQty = (TextView)findViewById(R.id.tvResultQty);
                    myDataBase.openDataBase();
                    String result = myDataBase.searchQty(name);
                    String totalQty = myDataBase.totalQty(name);
                    myDataBase.close();
                    tvResult.setText(result);
                    textQty.setText(totalQty);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }

    }

}

