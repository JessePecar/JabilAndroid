package jtr.jabil.jabilgit;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareClassic;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.provider.Settings;
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
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class NewRunActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{
    Button saveButton, cancelButton;
    ArrayList tempList = new ArrayList();
    VariableController vC = new VariableController();
    RunVariables rV = new RunVariables();
    boolean stopTimer = false;
    private final Handler handle = new Handler();
    int counter = 0;
    Activity thisAct;
    //Near Field Variables
    private NfcAdapter adapter;
    private PendingIntent intent;
    TextView testText;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run);
        getSupportActionBar().setTitle("New Run");
        thisAct = this;
        BottomNavigationView navigator = findViewById(R.id.navigation2);
        navigator.setOnNavigationItemSelectedListener(this);

        rV.myInstance().currentTemp = 0;
        rV.myInstance().minNum = 0;
        rV.myInstance().maxNum = 0;

        configureSave();
        configureCancel();

        loadFragment(new CurrentRunFragment());

        //configureTimer();
        System.out.println("Timer is running");


        testText = findViewById(R.id.nfcTest);
        adapter = NfcAdapter.getDefaultAdapter(this);

        if(adapter == null){
            Toast.makeText(this, "NFC is broke", Toast.LENGTH_LONG).show();
            //This will send back to the mainActivity
            finish();
            return;
        }

        intent = PendingIntent.getActivity(
                this,
                1,
                new Intent(this, this.getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),
                0);

    }
    @Override
    public void onResume() {
        super.onResume();
        final int delay;

        if(vC.myInstance().timer < 100){
            delay = 100;
        }
        else{
            delay = vC.myInstance().timer;
        }
        System.out.println("Running Timer");
        Runnable timer = new Runnable() {

            @Override
            public void run() {
                System.out.println("Running a new temperature");
                generateRandomNum();

                if(adapter != null){
                    if(!adapter.isEnabled()){
                        showWirelessSettings();
                    }
                    adapter.enableForegroundDispatch(thisAct, intent, null, null);
                }

                handle.postDelayed(this, delay);
            }
        };
        handle.postDelayed(timer, delay * 10);
    }
    @Override
    public void onPause(){
        super.onPause();
        if(adapter != null){
            if(adapter.isEnabled())
                adapter.disableForegroundDispatch(thisAct);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        resolveIntent(intent);
    }
    private void resolveIntent(Intent i){
        String action = i.getAction();

        if(adapter.ACTION_TAG_DISCOVERED.equals(action)
                || adapter.ACTION_TECH_DISCOVERED.equals(action)
                || adapter.ACTION_NDEF_DISCOVERED.equals(action)){
            Parcelable[] rawData = i.getParcelableArrayExtra(adapter.EXTRA_NDEF_MESSAGES);
            NdefMessage[] msgs;

            if(rawData != null){
                msgs = new NdefMessage[rawData.length];
                for(int j = 0; j < rawData.length; j++){
                    msgs[j] = (NdefMessage) rawData[j];
                }
            }
            else{
                byte[] empty = new byte[0];
                byte[] id = i.getByteArrayExtra(adapter.EXTRA_ID);
                Tag tag = i.getParcelableExtra(adapter.EXTRA_TAG);
                byte[] payload = dumpTagData(tag).getBytes();
                NdefRecord record = new NdefRecord(NdefRecord.TNF_UNKNOWN,empty, id, payload);
                NdefMessage msg = new NdefMessage(new NdefRecord[]{record});
                msgs = new NdefMessage[] {msg};

            }
            displayMessages(msgs);
        }
    }private void displayMessages(NdefMessage[] msgs){
        if(msgs == null || msgs.length ==0){
            return;
        }
        StringBuilder builder = new StringBuilder();
        List<ParseNdefRecord> records = NdefMessageParser.parse(msgs[0]);
        final int size = records.size();
        for(int i = 0; i < size; i++){
            ParseNdefRecord record = records.get(i);
            String str = record.str();
            builder.append(str).append("\n");
        }

        testText.setText(builder.toString());
    }

    private void showWirelessSettings(){
        //If the wireless settings for the NFC are not enabled, it will go to settings!!!
        Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
        startActivity(intent);
    }
    private String dumpTagData(Tag tag){
        StringBuilder sb = new StringBuilder();
        byte[] id = tag.getId();

        sb.append("ID (hex): " ).append(id.toString());

        String prefix = "android.nfc.tech";
        sb.append("Technologies: ");
        for(String tech : tag.getTechList()){
            sb.append(tech.substring(prefix.length()));
            sb.append(", ");
        }

        sb.delete(sb.length() - 2 ,  sb.length());

        for(String tech : tag.getTechList()){
            if(tech.equals(MifareClassic.class.getName())){
                String type = "Unknown";
                try{

                }catch(Exception e){

                }
            }
        }
        return sb.toString();
    }
    /*
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
    }*/

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

        if(adapter != null){
            if(adapter.isEnabled()){

            }
            else {
                adapter.enableForegroundDispatch(this, intent, null, null);
            }
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
                if(adapter != null)
                    adapter.disableForegroundDispatch(thisAct);
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

        final Activity act = this;
        cancelButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                stopTimer = true;
                if(adapter != null)
                    adapter.disableForegroundDispatch(act);
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
