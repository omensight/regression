package com.alphemsoft.education.regression.data.legacy;


import java.util.ArrayList;

/**
 * Created by Mijael Viricochea on 12/20/2017.
 */

public class RegressionLegacy implements Comparable<RegressionLegacy> {

    private int id = 0;
    private int type = 0;
    private String name = "";
    private String xData = "";
    private String yData = "";
    private String xLabel = "";
    private String yLabel = "";

    public RegressionLegacy(int id, int type, String name, String xData, String yData, String xLabel, String yLabel){
        this.id = id;
        this.type = type;
        this.name = name;
        this.xData = xData;
        this.yData = yData;
        this.xLabel = xLabel;
        this.yLabel = yLabel;
    }

    public String getName() {
        return name;
    }

    public int getType() {
        return type;
    }

    public ArrayList<DataPair> getDataPairs(){
        ArrayList<DataPair> auxDataPairs = new ArrayList<>();
        while (xData.length()>0||yData.length()>0){
            Double x=0.0,y=0.0;
            if (xData.length()>0){
                x = Double.valueOf(xData.substring(0,xData.indexOf(';')));
            }
            if (xData.length()>0){
                y = Double.valueOf(yData.substring(0,yData.indexOf(';')));
            }
            auxDataPairs.add(new DataPair(x,y));
            xData = xData.substring(xData.indexOf(';')+1);
            yData = yData.substring(yData.indexOf(';')+1);
        }
        return auxDataPairs;
    }

    public int getId() {
        return id;
    }

    public String getXLabel() {
        return xLabel;
    }

    public String getYLabel() {
        return yLabel;
    }

    @Override
    public int compareTo(RegressionLegacy regressionLegacy) {
        int last =this.name.compareTo(regressionLegacy.name);
        return last == 0 ? this.name.compareTo(regressionLegacy.name):last;
    }


    public void setName(String name) {
        this.name = name;
    }
}
