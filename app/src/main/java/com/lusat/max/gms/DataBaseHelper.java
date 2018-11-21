package com.lusat.max.gms;

/**
 * Created by satye on 1/21/2016.
 */
/** INSTRUCTIONS
 *      convert into csv with first row column name
        import into PMS15.sqlite OR PPT15.sqlite
        remove extension .sqlite
        create column _id INTEGER PRIMARY KEY
        EXECUTE 2 FOLLOWING STATEMENT
        CREATE TABLE "android_metadata" ("locale" TEXT DEFAULT 'en_US')
        INSERT INTO "android_metadata" VALUES ('en_US')
 */
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class DataBaseHelper extends SQLiteOpenHelper {
    private static final String LOGCAT = null;
    //The Android's default system path of your application database.
    private static String DB_PATH = "/data/data/com.lusat.max.gms/databases/";
    private static String DB_NAME = "GMS";
    private SQLiteDatabase myDataBase;
    private final Context myContext;
    public static final String KEY_ROWID = "P_CODE";
    public static final String KEY_PACKING = "P_PACKING";
    public static final String KEY_NAME = "P_NAME";
    public static final String PMS_MRP = "MRP";
    public static final String KEY_MRP = "F_MRP";
    public static final String KEY_TRADE = "F_TRADE";
    public static final String KEY_NETRATE = "F_QNT";
    public static final String KEY_LASTDATE = "F_RECDT";
    public static final String KEY_QTY = "P_QNT";
    public static final String KEYFQTY13 = "F_QNT13";
    private static final String PRODUCT_TABLE = "PMS";
    private static final String PURCHASE_TABLE = "PPT";
    private static final String COMPANY_TABLE = "CMS";
    private static final String WAREHOUSE_TABLE = "WAREHOUSE";
    public static final String KEY_COMPANY = "F_ORMFD";
    public static final String KEY_BATCH = "BAT_NO";
    public static final String KEY_EXPDT = "F_EXPDT";
    public static final String PMS_PURRATE = "F_NETPUR";
    public static final String PPT_F_PCODE = "F_PCODE";
    public static final String CMS_CCODE = "C_CODE";
    public static final String CMS_CNAME = "C_NAME";
    public String sCompany = "";
    /**
     * Constructor
     * Takes and keeps a reference of the passed context in order to access to the application assets and resources.
     * @param context
     */
    public DataBaseHelper(Context context) {
        super(context, DB_NAME, null, 1);
        this.myContext = context;
        Log.d(LOGCAT, "Created");
    }
    /**
     * Creates a empty database on the system and rewrites it with your own database.
     * */
    public void createDataBase() throws IOException {

        boolean dbExist = checkDataBase();

        if(dbExist){
            //do nothing - database already exist
        }else{

            //By calling this method and empty database will be created into the default system path
            //of your application so we are gonna be able to overwrite that database with our database.
            this.getReadableDatabase();

            try {

                copyDataBase();

            } catch (IOException e) {

                throw new Error("*****Error copying database*********");

            }
        }

    }

    /**
     * Check if the database already exist to avoid re-copying the file each time you open the application.
     * @return true if it exists, false if it doesn't
     */
    private boolean checkDataBase(){

        SQLiteDatabase checkDB = null;

        try{
            String myPath = DB_PATH + DB_NAME;
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

        }catch(SQLiteException e){

            //database does't exist yet.

        }

        if(checkDB != null){

            checkDB.close();

        }

        return checkDB != null ? true : false;
    }

    /**
     * Copies your database from your local assets-folder to the just created empty database in the
     * system folder, from where it can be accessed and handled.
     * This is done by transfering bytestream.
     * */
    private void copyDataBase() throws IOException{

        //Open your local db as the input stream
        InputStream myInput = myContext.getAssets().open(DB_NAME);

        // Path to the just created empty db
        String outFileName = DB_PATH + DB_NAME;

        //Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(outFileName);

        //transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer))>0){
            myOutput.write(buffer, 0, length);
        }

        //Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();

    }


    public void openDataBase() throws SQLException {
        //Open the database
        String myPath = DB_PATH + DB_NAME;
        myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
    }
    @Override
    public synchronized void close() {
        if(myDataBase != null)
            myDataBase.close();
        super.close();
    }
    @Override
    public void onCreate(SQLiteDatabase database) {

    }
    @Override
    public void onUpgrade(SQLiteDatabase database, int version_old,
                          int current_version) {

    }

    public ArrayList<HashMap<String, String>> getAllProducts() {
        ArrayList<HashMap<String, String>> proList;
        proList = new ArrayList<HashMap<String, String>>();
        String selectQuery = "SELECT  * FROM PPT";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                //Id, Company,Name,Price
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("Id", cursor.getString(0));
                map.put("P_CODE", cursor.getString(4));
                map.put("P_NAME", cursor.getString(5));
                map.put("P_PACKING", cursor.getString(6));
                map.put("BAT_NO", cursor.getString(7));
                map.put("P_QNT", cursor.getString(8));
                map.put("F_QNT", cursor.getString(9));
                map.put("F_RECDATE", cursor.getString(13));
                map.put("F_EXPDT", cursor.getString(15));
                map.put("F_MRP", cursor.getString(16));
                map.put("F_TRADE", cursor.getString(17));
                map.put("F_QNT13", cursor.getString(43));
                map.put("F_ORMFD", cursor.getString(85));

                proList.add(map);
            } while (cursor.moveToNext());
        }
        return proList;
    }
    public ArrayList<HashMap<String,String>>warehouseSearch(String name) {
        ArrayList<HashMap<String, String>> warehouselist;
        warehouselist = new ArrayList<HashMap<String, String>>();
        //used in WAREHOUSE TABLE SEARCHING AND JAVA LIST ACTIVITY
        String[] columns = new String[]{KEY_ROWID, KEY_NAME };
        Cursor c = myDataBase.query(WAREHOUSE_TABLE, columns, "P_NAME LIKE " + "'" + name + "%'", null, null, null, null);
        if (c.moveToFirst()) {
            do {
                //product and pcode
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("P_CODE", c.getString(0));
                map.put("P_NAME", c.getString(1));
                warehouselist.add(map);
            } while (c.moveToNext());
        }
        return warehouselist;
    }



    public String getData() {
        String[] columns = new String[]{KEY_ROWID, KEY_NAME, PMS_MRP};
        Cursor c = myDataBase.query(PRODUCT_TABLE, columns, null, null, null, null, null);
        String result = "";
        int iRow = c.getColumnIndex(KEY_ROWID);
        int iName = c.getColumnIndex(KEY_NAME);
        int iMrp = c.getColumnIndex(PMS_MRP);
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){
            result = result + c.getString(iRow) + " " + c.getString(iName) + " " + c.getString(iMrp) +"\n";
        }
        return result;
    }

    public String searchResult(String name) {
        String[] columns = new String[]{KEY_ROWID, PMS_MRP, KEY_NAME };
        Cursor c = myDataBase.query(PRODUCT_TABLE, columns, "P_NAME LIKE " + "'" + name + "%'", null, null, null, null);
        String result = "";
        int iRow = c.getColumnIndex(KEY_ROWID);
        int iName = c.getColumnIndex(KEY_NAME);
        int iMrp = c.getColumnIndex(PMS_MRP);
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){
            result = result + c.getString(iRow) + "  " + c.getString(iName) + "  " + c.getString(iMrp)  +"\n";
        }
        return result;
    }
    public String searchProduct(String name) {
        String[] columns = new String[]{KEY_TRADE, PMS_PURRATE,KEY_NAME,KEY_ROWID, PMS_MRP };
        Cursor c = myDataBase.query(PRODUCT_TABLE, columns, "P_NAME LIKE " + "'" + name + "%'", null, null, null, null);
        String result = "";

        int iTrade = c.getColumnIndex(KEY_TRADE);
        int iPurrate = c.getColumnIndex(PMS_PURRATE);
        int iName = c.getColumnIndex(KEY_NAME);
        int iRow = c.getColumnIndex(KEY_ROWID);
        int iMrp = c.getColumnIndex(PMS_MRP);
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){
            result = result + c.getString(iTrade) + "  " + c.getString(iPurrate) + "  " + c.getString(iName) + "  " + c.getString(iMrp) + "  " + c.getString(iRow) +"\n";
        }
        return result;
    }

    public String searchPrice(String name) throws Exception {
        String[] columns = new String[]{KEY_NAME,KEY_NETRATE,PPT_F_PCODE,KEY_LASTDATE,KEY_MRP,KEY_TRADE };

        Cursor c = myDataBase.query(PURCHASE_TABLE, columns, "P_CODE LIKE " + "'P" + name + "'", null, null, null, null);

        String result = "";

        int iTrade = c.getColumnIndex(KEY_TRADE);
        int iRate = c.getColumnIndex(KEY_NETRATE);
        int iName = c.getColumnIndex(KEY_NAME);
        int iMrp = c.getColumnIndex(KEY_MRP);
        int iDate = c.getColumnIndex(KEY_LASTDATE);
        int iCompany = c.getColumnIndex(PPT_F_PCODE);


        for (c.moveToLast(); !c.isBeforeFirst(); c.moveToPrevious()){
                sCompany = "" + c.getString(iCompany);
                String[] columns2 = new String[]{CMS_CCODE,CMS_CNAME };
                Cursor c1 = myDataBase.query(COMPANY_TABLE, columns2, "C_CODE LIKE " + "'" + sCompany + "'", null, null, null, null);
                int iCMSCompany = c1.getColumnIndex(CMS_CNAME);
                result = result + c.getString(iTrade) + "  " + c.getString(iRate) + "  " + c.getString(iName) + "  " + c.getString(iMrp) + "  " + c.getString(iDate) + "\n";
                for (c1.moveToFirst(); !c1.isAfterLast(); c1.moveToNext()){
                result = result + c1.getString(iCMSCompany) + "" +"\n"+"\n";
                }

        }
        return result;
    }


    public String getProductName() {
        String[] columns = new String[]{KEY_NAME};
        Cursor c = myDataBase.query(PRODUCT_TABLE, columns, null, null, null, null, null);
        String result = "";
        int iName = c.getColumnIndex(KEY_NAME);
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){
            result = result + " " + c.getString(iName) + " " +"\n";
        }
        return result;
    }
    public String searchQty(String name) {
        String[] columns = new String[]{KEY_ROWID, KEY_NAME, KEY_MRP,KEY_QTY,KEYFQTY13};
        Cursor c = myDataBase.query(PURCHASE_TABLE, columns, "P_CODE LIKE " + "'P" + name + "'", null, null, null, null);
        String result = "";
        int iRow = c.getColumnIndex(KEY_ROWID);
        int iName = c.getColumnIndex(KEY_NAME);
        int iMrp = c.getColumnIndex(KEY_MRP);
        int iQty = c.getColumnIndex(KEY_QTY);
        int iFqnt13 = c.getColumnIndex(KEYFQTY13);
        int rowQty = 0;
        for (c.moveToLast(); !c.isBeforeFirst(); c.moveToPrevious()){
            rowQty = c.getInt(iQty) - c.getInt(iFqnt13);
            if(rowQty>0) {
                result = result + c.getString(iRow) + "  " + c.getString(iName) + "  " + c.getString(iMrp) + "  " + Integer.toString(rowQty) + "\n";
            }
        }
        return result;
    }
    public String totalQty(String name) {
        String[] columns = new String[]{KEY_QTY,KEYFQTY13};
        Cursor c = myDataBase.query(PURCHASE_TABLE, columns, "P_CODE LIKE " + "'P" + name + "'", null, null, null, null);
        int Qty = 0;
        int iQty = c.getColumnIndex(KEY_QTY);
        int iFqnt13 = c.getColumnIndex(KEYFQTY13);
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){
            int x = c.getInt(iQty) - c.getInt(iFqnt13);
            if(x<0){
                x=0;
            }
            Qty = Qty + x;
        }
        return Integer.toString(Qty);
    }
    public String getExpiredProduct() {
        String expFormat = "F_EXPDT LIKE '03/16' OR F_EXPDT LIKE '02/16' OR F_EXPDT LIKE '01/16' OR F_EXPDT LIKE '12/15' OR F_EXPDT LIKE '11/15' OR F_EXPDT LIKE '10/15' OR F_EXPDT LIKE '09/15' OR F_EXPDT LIKE '08/15' OR F_EXPDT LIKE '07/15' OR F_EXPDT LIKE '06/15' OR F_EXPDT LIKE '04/16'";
        String[] columns = new String[]{KEY_ROWID,KEY_NAME,KEY_PACKING,KEY_EXPDT, KEY_MRP,KEY_BATCH,KEY_COMPANY,KEY_QTY,KEYFQTY13};
        Cursor c = myDataBase.query(PURCHASE_TABLE, columns, expFormat, null, null, null, null);
        String result = "";
        int iName = c.getColumnIndex(KEY_NAME);
        int iPacking = c.getColumnIndex(KEY_PACKING);
        int iExpirydate = c.getColumnIndex(KEY_EXPDT);
        int iMrp = c.getColumnIndex(KEY_MRP);
        int iBatch = c.getColumnIndex(KEY_BATCH);
        int iCompany = c.getColumnIndex(KEY_COMPANY);
        int iQty = c.getColumnIndex(KEY_QTY);
        int iFqnt13 = c.getColumnIndex(KEYFQTY13);
        int rowQty = 0;
        for (c.moveToLast(); !c.isBeforeFirst(); c.moveToPrevious()){
            rowQty = c.getInt(iQty) - c.getInt(iFqnt13);
            if(rowQty>0) {
                result = result + c.getString(iExpirydate) + "  " + c.getString(iName) + "  " + c.getString(iPacking) + "  " + c.getString(iMrp) + "  " + c.getString(iBatch) + "  " + c.getString(iCompany) + "  " + Integer.toString(rowQty) + "\n";
            }
        }
        return result;
    }


    // Add your public helper methods to access and get content from the database.
    // You could return cursors by doing "return myDataBase.query(....)" so it'd be easy
    // to you to create adapters for your views.
}