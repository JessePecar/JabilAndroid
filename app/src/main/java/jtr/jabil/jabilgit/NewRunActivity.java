package jtr.jabil.jabilgit;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.series.DataPoint;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class NewRunActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{
    Button saveButton, cancelButton;
    ArrayList tempList = new ArrayList();
    VariableController vC = new VariableController();
    RunVariables rV = new RunVariables();
    boolean stopTimer = false;
    int counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run);
        getSupportActionBar().setTitle("New Run");

        BottomNavigationView navigator = findViewById(R.id.navigation2);
        navigator.setOnNavigationItemSelectedListener(this);

        rV.myInstance().currentTemp = 0;
        rV.myInstance().minNum = 0;
        rV.myInstance().maxNum = 0;

        configureSave();
        configureCancel();

        loadFragment(new CurrentRunFragment());
        System.out.println("Running Timer");
        configureTimer();
        System.out.println("Timer is running");
    }
    private void configureTimer(){
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                System.out.println("Running a new temperature");
                generateRandomNum();
                if(!stopTimer){
                    configureTimer();
                }
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
        System.out.println("Generating new Temp");

        tempList.add(rand.nextInt((120 - 32) +1) +32);

        //Send to the UI
        if(tempList.size() > 1){
            System.out.println(tempList.size());
            rV.myInstance().dist = ((int)tempList.get(tempList.size() - 1) - (int)tempList.get(tempList.size() - 2));
        }
        if(tempList.size() == 10){
            System.out.println(tempList.size());
            enterValue();
        }

    }
    private void enterValue(){
        int aveAdd = 0;

        for(int i =0; i < 10; i++){
            aveAdd += (int)tempList.get(i);
        }
        aveAdd /= 10;

        rV.myInstance().runTemps.add(aveAdd);
        rV.myInstance().currentTemp = aveAdd;
        //Temp fix, not very efficient(I think)
        //Got to make the graph scrollable
        rV.myInstance().runDP.add(new DataPoint(counter, aveAdd));



        counter++;
        aveAdd = 0;
        tempList.clear();

        if((int)rV.myInstance().runTemps.get(rV.myInstance().runTemps.size() - 1) < rV.myInstance().minNum){
            rV.myInstance().minNum = (int)rV.myInstance().runTemps.get(rV.myInstance().runTemps.size() - 1);
        }else if((int)rV.myInstance().runTemps.get(rV.myInstance().runTemps.size() - 1) > rV.myInstance().maxNum){
            rV.myInstance().maxNum = (int)rV.myInstance().runTemps.get(rV.myInstance().runTemps.size() - 1);
            if(rV.myInstance().minNum == 0){
                rV.myInstance().minNum = rV.myInstance().maxNum;
            }
        }


    }



    private void configureSave(){
        saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                RunDatabase rDB = new RunDatabase(getBaseContext());

                Date date = new Date();
                String fullDate;
                fullDate = "dd/MM/yy";
                DateFormat newFormat = new SimpleDateFormat(fullDate);
                String formattedDate = newFormat.format(date);

                /*
                * This is where I will pull from run variables, and take the saved data from the 2 tabs.
                * */
                getIntent().putExtra("NEW_RUN_NAME", rV.myInstance().runName);
                getIntent().putExtra("RUN_MAX_TEMP", rV.myInstance().maxNum);
                getIntent().putExtra("RUN_MIN_TEMP", rV.myInstance().minNum);
                setResult(900, getIntent());

                int printRand;
                if(rV.myInstance().runTemps.isEmpty()){
                    printRand = 900;
                }else{
                    printRand = (int)rV.myInstance().runTemps.get(rV.myInstance().runTemps.size() - 1);
                    for(int i =0; i < rV.myInstance().runTemps.size(); i++){
                        if((int)rV.myInstance().runTemps.get(i) < rV.myInstance().minNum){
                            rV.myInstance().minNum = (int)rV.myInstance().runTemps.get(i);
                        }else if((int)rV.myInstance().runTemps.get(i) > rV.myInstance().maxNum){
                            rV.myInstance().maxNum = (int)rV.myInstance().runTemps.get(i);
                        }
                    }

                    rDB.addRun(rV.myInstance().runName,
                            rV.myInstance().runTemps.size(),
                            formattedDate,
                            rV.myInstance().maxNum,
                            rV.myInstance().minNum,
                            rV.myInstance().runTemps.toString());
                }

                Toast.makeText(getBaseContext(), "New Temps saved as: " + rV.myInstance().runTemps.toString(), Toast.LENGTH_LONG).show();
                rV.myInstance().runTemps.clear();
                stopTimer = true;
                finish();
            }
        });
    }
    private void configureCancel(){
        cancelButton = findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                stopTimer = true;
                finish();
            }
        });
    }


    public boolean loadFragment(Fragment fragment){
        if(fragment != null){
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item){
        Fragment fragment = null;
        switch(item.getItemId()){
            case R.id.navigator_current_new_run:
                fragment = new CurrentRunFragment();
                break;
            case R.id.navigator_current_graphed_run:
                fragment = new CurrentRunGraph();
                break;
        }
        return loadFragment(fragment);
    }

}
