package jtr.jabil.jabilgit;

import android.app.Application;

import com.jjoe64.graphview.series.DataPoint;

import java.util.ArrayList;
import java.util.List;

public class RunVariables  extends Application {

    ArrayList runTemps = new ArrayList();
    ArrayList tempList = new ArrayList();
    List<DataPoint> runDP = new ArrayList<>();

    int maxNum;
    int minNum;
    int currentTemp;
    int dist;
    String runName;
    private static RunVariables instance = null;

    protected RunVariables(){}

    public static synchronized RunVariables myInstance(){
        if(null == instance){
            instance = new RunVariables();
        }
        return instance;
    }

}
