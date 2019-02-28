package jtr.jabil.jabilgit;

import android.app.Application;

public class VariableController  extends Application {
    String displayedRun;
    String displayedName;
    int runID;
    int timer;
    int min, max;
    private static VariableController instance = null;

    protected VariableController(){}

    public static synchronized VariableController myInstance(){
        if(null == instance){
            instance = new VariableController();
        }
        return instance;
    }

}
