package com.alphemsoft.education.regression.data.legacy;


import java.util.ArrayList;

/**
 * Created by Mijael Viricochea on 12/20/2017.
 */

public class DataPair implements Comparable {
    private Double x;
    private Double y;

    public DataPair(Double x, Double y){
        this.x = x;
        this.y = y;
    }

    public DataPair(){};

    public Double getX() {
        return x;
    }

    public Double getY() {
        return y;
    }

    public void setX(Double x) {
        this.x = x;
    }

    public void setY(Double y) {
        this.y = y;
    }

    public static ArrayList<DataPair> convertToDataPairs(double[] x, double[] y){
        ArrayList<DataPair> auxDataPairs = new ArrayList<>();
        for (int i = 0; i<x.length; i++){
            auxDataPairs.add(new DataPair(x[i],y[i]));
        }
        return auxDataPairs;
    }

    public static double[] getXFromDataPair(ArrayList<DataPair> dataPairs){
        double[] auxX = new double[dataPairs.size()];
        for (int i = 0; i<dataPairs.size(); i++) {
            auxX[i] = dataPairs.get(i).getX();
        }
        return auxX;
    }

    public static double[] getYFromDataPair(ArrayList<DataPair> dataPairs){
        double[] auxY = new double[dataPairs.size()];
        for (int i = 0; i<dataPairs.size(); i++) {
            auxY[i] = dataPairs.get(i).getY();
        }
        return auxY;
    }

    @Override
    public int compareTo(Object o) {
        return Double.compare(x, ((DataPair)o).x);
    }
}
