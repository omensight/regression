package com.alphemsoft.education.regression.data.legacy;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import java.util.ArrayList;

/**
 * Created by Mijael Viricochea on 12/20/2017.
 * This Class is an SQLiteOpenHelper
 */

public class OldDBHelper extends SQLiteOpenHelper {
    private final static String DB_NAME = "regression.db";
    private final static int DB_VERSION = 1;

    private final static String TABLE_REGRESSION = "TABLE_REGRESSION";
    //RegressionUnit entity
    private final static String REGRESSION_ID="regression_id"
        ,REGRESSION_NAME = "regression_name"
        ,REGRESSION_X_DATA = "regression_x_data"
        ,REGRESSION_Y_DATA = "regression_y_data"
        ,REGRESSION_TYPE = "regression_type"
        ,REGRESSION_X_LABEL = "regression_x_label"
        ,REGRESSION_Y_LABEL = "regression_y_label"
        //Create Table RegressionUnit
        ,CREATE_REGRESSION_TABLE = "CREATE TABLE "
            + TABLE_REGRESSION
            + "("
            + REGRESSION_ID+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + REGRESSION_TYPE + " INTEGER, "
            + REGRESSION_NAME + " TEXT,"
            + REGRESSION_X_DATA + " TEXT, "
            + REGRESSION_Y_DATA + " TEXT, "
            + REGRESSION_X_LABEL + " TEXT, "
            + REGRESSION_Y_LABEL + " TEXT "
            + ")";

    public OldDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_REGRESSION_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }

    public void addRegression(String name, ArrayList<DataPair> dataSet, int type, String xLabel, String yLabel){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(REGRESSION_NAME, name);
        values.put(REGRESSION_X_DATA, convertDataSetInOneLine(dataSet, "x"));
        values.put(REGRESSION_Y_DATA, convertDataSetInOneLine(dataSet, "y"));
        values.put(REGRESSION_TYPE, type);
        values.put(REGRESSION_X_LABEL, xLabel);
        values.put(REGRESSION_Y_LABEL, yLabel);
        db.insert(TABLE_REGRESSION, null, values);
        db.close();
    }

    public boolean regressionAlreadyExists(String name){
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT " + REGRESSION_NAME + " FROM " + TABLE_REGRESSION + " WHERE " + REGRESSION_NAME + " = " + "'"+name+"'";
        Cursor cursor = db.rawQuery(query, null);
        int count = 0;
        if (cursor.moveToFirst()){
            do {
                count++;
            }while (cursor.moveToNext());
        }
        return count>0?true:false;
    }

    private String convertDataSetInOneLine(ArrayList<DataPair> dataSet, String xOrY) {
        String aux = "";
        for (int i=0; i<dataSet.size(); i++){
            if (xOrY.equals("x")){
                aux += String.valueOf(dataSet.get(i).getX())+";";
            }else if (xOrY.equals("y")){
                aux += String.valueOf(dataSet.get(i).getY())+";";
            }
        }
        return aux;
    }

    public ArrayList<String> getRegressionNames(){
        ArrayList<String> auxiliarNames = new ArrayList<>();
        String query = "SELECT " + REGRESSION_NAME + " FROM " +TABLE_REGRESSION;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()){
            do {
                auxiliarNames.add(cursor.getString(0));
            }while (cursor.moveToNext());
        }
        return auxiliarNames;
    }



    public ArrayList<RegressionLegacy> getSavedRegressions(){
        ArrayList<RegressionLegacy> regressionLegacies = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_REGRESSION;
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()){
            do {
                RegressionLegacy regressionLegacy = new RegressionLegacy(
                        cursor.getInt(0)
                        ,cursor.getInt(1)
                        ,cursor.getString(2)
                        ,cursor.getString(3)
                        ,cursor.getString(4)
                        ,cursor.getString(5)
                        ,cursor.getString(6));

                regressionLegacies.add(regressionLegacy);
            }while (cursor.moveToNext());
        }
        return regressionLegacies;
    }

    public RegressionLegacy getRegression(String name){
        RegressionLegacy regressionLegacy = null;
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_REGRESSION + " WHERE " + REGRESSION_NAME + " = " + "'"+name+"'";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()){
            do {
                regressionLegacy = new RegressionLegacy(
                        cursor.getInt(0)
                        ,cursor.getInt(1)
                        ,cursor.getString(2)
                        ,cursor.getString(3)
                        ,cursor.getString(4)
                        ,cursor.getString(5)
                        ,cursor.getString(6));
            }while (cursor.moveToNext());
        }
        db.close();
        return regressionLegacy;
    }

    public void updateRegression(int id, ArrayList<DataPair> dataPairs){
        SQLiteDatabase db = getWritableDatabase();
        String xLineData = convertDataSetInOneLine(dataPairs, "x");
        String yLineData = convertDataSetInOneLine(dataPairs, "y");
        String query = "UPDATE " + TABLE_REGRESSION
                + " SET " + REGRESSION_X_DATA + "=" + '"' +xLineData+'"'+" , " +REGRESSION_Y_DATA + " = " + '"'+yLineData+'"'
                + " WHERE " + REGRESSION_ID + "=" +id;
        db.execSQL(query);
        db.close();
    }


    public void deleteRegression(String name) {
        SQLiteDatabase database = getWritableDatabase();
        database.delete(TABLE_REGRESSION, REGRESSION_NAME +"="+'"'+name+'"',null);
    }

    public void renameRegression(String originalName, String newName) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(REGRESSION_NAME,newName);
        db.update(TABLE_REGRESSION,contentValues, REGRESSION_NAME+"="+'"'+originalName+'"',null);
        db.close();
    }
}
