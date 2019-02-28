package jtr.jabil.jabilgit;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class CurrentRunFragment extends Fragment{

    EditText runName;
    TextView maxTemp, minTemp, currentTemp;
    SeekBar lineDial;

    ArrayList runTemps = new ArrayList();
    ArrayList tempList = new ArrayList();
    VariableController vC = new VariableController();
    int maxNum, minNum;
    RunVariables rV = new RunVariables();


    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.activity_new_run, container, false);

        runName = view.findViewById(R.id.runNameSave);
        lineDial = view.findViewById(R.id.currentTempSeek);
        maxTemp = view.findViewById(R.id.maxTempNum);
        minTemp = view.findViewById(R.id.minTempNum);
        currentTemp = view.findViewById(R.id.currentTempNum);

        configTimer();
        configTemps();
        configBar(0);


        return view;
    }
    void configTimer(){
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                configTimer();
                configTemps();
                configBar(0);
            }
        };
        long delay;
        if(vC.myInstance().timer < 100){
            delay = 100;
        }
        else{
            delay = vC.myInstance().timer;
        }
        Timer timer = new Timer();
        timer.schedule(task, delay);
    }
    //TODO:: Figure out how to call this from the activity.
    void configBar(final int i){
        TimerTask task = new TimerTask(){
            @Override
            public void run(){
                if(i < 10){
                    int d = rV.myInstance().dist / 10;
                    lineDial.setProgress(lineDial.getProgress() + d);
                    rV.myInstance().runName = runName.getText().toString();
                    configBar(i + 1);
                }
            }
        };
        long delay = 10;
        Timer timer = new Timer();
        timer.schedule(task, delay);
    }
    void configTemps(){
        minTemp.setText(String.valueOf(rV.myInstance().minNum));
        maxTemp.setText(String.valueOf(rV.myInstance().maxNum));
        currentTemp.setText(String.valueOf(rV.myInstance().currentTemp));
    }

}
