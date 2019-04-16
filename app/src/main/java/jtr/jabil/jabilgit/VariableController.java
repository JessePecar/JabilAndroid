package jtr.jabil.jabilgit;

import android.app.Application;

public class VariableController  extends Application {
    String displayedRun;
    String displayedName;
    String tempUnit;
    String Day, Month, Year;
    int runID;
    int timer, displayedTimer;
    int min, max;
    boolean showText;

    private static VariableController instance = null;

    protected VariableController(){}

    public static synchronized VariableController myInstance(){
        if(null == instance){
            instance = new VariableController();
        }
        return instance;
    }

}
