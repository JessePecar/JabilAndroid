package jtr.jabil.jabilgit;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class NewRunActivity extends AppCompatActivity {
    EditText runName;
    TextView maxTemp, minTemp, currentTemp;
    Button saveButton, cancelButton;
    SeekBar lineDial;
    ArrayList runTemps = new ArrayList();
    ArrayList tempList = new ArrayList();
    VariableController vC = new VariableController();
    int maxNum, minNum;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_run);
        runName = findViewById(R.id.runNameSave);
        currentTemp = findViewById(R.id.currentTempNum);
        maxTemp = findViewById(R.id.maxTempNum);
        minTemp = findViewById(R.id.minTempNum);
        lineDial = findViewById(R.id.currentTempSeek);
        getSupportActionBar().setTitle("New Run");
        maxNum = minNum = 0;

        maxTemp.setText(String.valueOf(maxNum));

        minTemp.setText(String.valueOf(minNum));

        configureSave();
        configureCancel();
        configureTimer();
    }

    private void configureSave(){
        saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                RunDatabase rDB = new RunDatabase(getBaseContext());

                Date date = new Date();


                getIntent().putExtra("NEW_RUN_NAME", runName.getText().toString());
                getIntent().putExtra("RUN_MAX_TEMP", maxTemp.getText().toString());
                getIntent().putExtra("RUN_MIN_TEMP", minTemp.getText().toString());
                setResult(900, getIntent());

                int printRand;
                if(runTemps.isEmpty()){
                    printRand = 900;
                }else{
                    printRand = (int)runTemps.get(runTemps.size() - 1);
                    for(int i =0; i < runTemps.size(); i++){
                        if((int)runTemps.get(i) < minNum){
                            minNum = (int)runTemps.get(i);
                        }else if((int)runTemps.get(i) > maxNum){
                            maxNum = (int)runTemps.get(i);
                        }
                    }
                    rDB.addRun(runName.getText().toString(),
                            runTemps.size(),
                            date.toString(),
                            maxNum,
                            minNum,
                            runTemps.toString());
                }
                Toast.makeText(getBaseContext(), runTemps.toString(), Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }
    private void configureCancel(){
        cancelButton = findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                finish();
            }
        });
    }
    private void configureTimer(){
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                generateRandomNum();
                configureTimer();
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
    private void generateRandomNum(){
        Random rand = new Random();
        System.out.print("Generating new Temp");

        tempList.add(rand.nextInt((120 - 32) +1) +32);
        if(tempList.size() > 1){
            transit(0, ((int)tempList.get(tempList.size() - 1) - (int)tempList.get(tempList.size() - 2)));
        }
        if(tempList.size() == 10){
            enterValue();
        }

    }
    private void transit(final int i, final int dist){
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if(i < 10){
                    int d = dist / 10;
                    lineDial.setProgress(lineDial.getProgress() + d);

                    int j = i + 1;
                    transit(j, dist);
                }
            }
        };
        long delay = 10;

        Timer timer = new Timer();
        timer.schedule(task, delay);
    }
    private void enterValue(){
        int aveAdd = 0;

        for(int i =0; i < 10; i++){
            aveAdd += (int)tempList.get(i);
        }
        aveAdd /= 10;

        runTemps.add(aveAdd);

        aveAdd = 0;
        tempList.clear();

        if((int)runTemps.get(runTemps.size() - 1) < minNum){
            minNum = (int)runTemps.get(runTemps.size() - 1);
        }else if((int)runTemps.get(runTemps.size() - 1) > maxNum){
            maxNum = (int)runTemps.get(runTemps.size() - 1);
            if(minNum == 0){
                minNum = maxNum;
            }
        }

        currentTemp.setText(String.valueOf((int)runTemps.get(runTemps.size() - 1)));

        maxTemp.setText(String.valueOf(maxNum));

        minTemp.setText(String.valueOf(minNum));
    }
}
