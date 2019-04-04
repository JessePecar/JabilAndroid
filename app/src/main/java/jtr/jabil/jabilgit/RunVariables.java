package jtr.jabil.jabilgit;

import android.app.Application;

import com.github.mikephil.charting.data.Entry;
import com.jjoe64.graphview.series.DataPoint;

import java.util.ArrayList;
import java.util.List;

public class RunVariables  extends Application {

    ArrayList runTemps = new ArrayList();
    ArrayList tempList = new ArrayList();
    List<Entry> runDP = new ArrayList<>();
    List<Entry> fillerMax = new ArrayList<>();
    List<Entry> fillerMin = new ArrayList<>();

    int maxNum;
    int minNum;
    int currentTemp;
    int dist;
    String runName;
    boolean keepRunning;
    private static RunVariables instance = null;

    protected RunVariables(){}

    public static synchronized RunVariables myInstance(){
        if(null == instance){
            instance = new RunVariables();
        }
        return instance;
    }

}
