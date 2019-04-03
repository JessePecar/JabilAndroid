package jtr.jabil.jabilgit;

import android.widget.CheckBox;

public class RunCard {

    private String runName, runDate, runData;
    private int maxTemp, minTemp, runLength, runID, runInterval;
    public RunCard(int runID, String runName, int runLength, String runDate, int maxTemp, int minTemp, String runData, int runInterval){

        this.runName = runName;
        this.runLength = runLength;
        this.runDate = runDate;
        this.maxTemp = maxTemp;
        this.minTemp = minTemp;
        this.runData = runData;
        this.runID = runID;
        this.runInterval = runInterval;

    }

    public String getName(){
        return runName;
    }
    public String getDate() { return runDate; }
    public String getData() { return runData; }
    public int getRunTimer(){ return runInterval / 100;}
    public int getMaxTemp(){
        return maxTemp;
    }
    public int getMinTemp(){
        return minTemp;
    }
    public int getRunLength() { return runLength * (runInterval / 100); }

    public int getRunID(){ return runID; }
}
