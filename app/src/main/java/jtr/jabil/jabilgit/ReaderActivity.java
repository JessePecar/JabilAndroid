package jtr.jabil.jabilgit;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.os.Parcelable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class ReaderActivity extends AppCompatActivity {
    NFCManager nfcMger;
    private View v;
    TextView nfcConnect;
    Button saveFile, cancelFile;
    VariableController vC = new VariableController();
    RunDatabase rDb = new RunDatabase(this);
    String classText;
    String date, tempUnit;
    EditText run;
    String timer = "";
    Context ctx;
    int timeNum = -1;
    List<Float> dataList = new ArrayList<Float>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reader);
        nfcMger = new NFCManager(this);
        nfcConnect = findViewById(R.id.nfcConnect);
        NfcAdapter nfcAdpt = NfcAdapter.getDefaultAdapter(this);
        if(nfcAdpt.isEnabled()) {
            //parseText("NFC is Enable \n Tap your NFC device");
            Toast.makeText(this, "NFC Adapter is enabled", Toast.LENGTH_SHORT).show();
        }
        run = findViewById(R.id.saveName);
        saveFile = findViewById(R.id.saveFile);
        cancelFile = findViewById(R.id.cancelFile);
        ctx = this;
        configSave();
        configCancel();

    }

    void configSave(){
        saveFile.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                //Do the parse here -> then take the name from the editText -> save into database
                dataList.clear();
                date = "";
                timer = "";
                for(int charCount = 0; charCount < classText.length(); charCount++){
                    if(classText.charAt(charCount) == '['){
                        // TODO: Make this work
                        if(date != "" && timeNum < 0){
                            String tempTimer = String.valueOf(date.charAt(12)) + String.valueOf(date.charAt(13));
                            timer = String.valueOf(classText.charAt(charCount + 13)) + String.valueOf(classText.charAt(charCount + 14));
                            timeNum = Integer.parseInt(tempTimer) - Integer.parseInt(timer);
                            /** Special case if the interval is an hour **/
                            if(timeNum == 0){
                                timeNum = 60;
                            }
                            Toast.makeText(ctx, String.valueOf(timeNum), Toast.LENGTH_LONG).show();
                        }
                        /** if date isnt set up -> will get the date here and then add the new run -> also gets the temp unit**/

                        if(date == null || date.equals(null) || date.equals("")){
                             date = String.valueOf(classText.charAt(charCount + 1)) + String.valueOf(classText.charAt(charCount + 2)) + "/" +
                                     String.valueOf(classText.charAt(charCount + 4)) + String.valueOf(classText.charAt(charCount + 5)) + "/" +
                                     String.valueOf(classText.charAt(charCount + 7)) + String.valueOf(classText.charAt(charCount + 8)) + " " +
                                     String.valueOf(classText.charAt(charCount + 10)) + String.valueOf(classText.charAt(charCount + 11)) + ":" +
                                     String.valueOf(classText.charAt(charCount + 13)) + String.valueOf(classText.charAt(charCount + 14)) ;
                             tempUnit = String.valueOf(classText.charAt(charCount + 22));

                         }

                         String tempString = String.valueOf(classText.charAt(charCount + 17)) + String.valueOf(classText.charAt(charCount + 18))
                                     + String.valueOf(classText.charAt(charCount + 19) + String.valueOf(classText.charAt(charCount + 20)));
                         dataList.add(Float.parseFloat(tempString));

                         //toastMyBuns();
                         charCount = charCount + 20;
                    }
                    /** [04/16/19 08:51] 43.0 C
                     *   12 45 78 10,11 13,14 17,18,19,20, 22**/
                }
                float max = 0, min = 0;
                if(dataList != null && !dataList.isEmpty()){
                    for(int i = 0; i < dataList.size(); i++){
                        if(max == 0 && min == 0){
                            max = dataList.get(i);
                            min = max;
                        }
                        else if(dataList.get(i) > max){
                            max = dataList.get(i);
                        }
                        else if(dataList.get(i) < min){
                            min = dataList.get(i);
                        }
                    }
                }
                String runName = run.getText().toString();
                try{
                    if(vC.myInstance().timer < 1 || vC.myInstance().timer == 0){
                        vC.myInstance().timer = 1;
                    }
                    rDb.addRun(runName, dataList.size(), date,(int)max, (int)min, dataList.toString(), timeNum, tempUnit);

                    changeAct();
                }
                catch(Exception e){

                }
            }
        });
    }
    void toastMyBuns(){
        Toast.makeText(this, dataList.toString(), Toast.LENGTH_LONG).show();
    }
    void changeAct(){
        finish();
    }
    void configCancel(){
        cancelFile.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                /** Throwing away everything here**/
                finish();
            }
        });
    }

    @Override
    protected void onResume(){
        super.onResume();
        try {
            nfcMger.verifyNFC();
            Intent nfcIntent = new Intent(this, getClass());
            nfcIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, nfcIntent, 0);
            IntentFilter[] intentFiltersArray = new IntentFilter[] {};
            String[][] techList = new String[][] { { android.nfc.tech.Ndef.class.getName() }, { android.nfc.tech.NdefFormatable.class.getName() } };
            NfcAdapter nfcAdpt = NfcAdapter.getDefaultAdapter(this);
            nfcAdpt.enableForegroundDispatch(this, pendingIntent, intentFiltersArray, techList);

        }
        catch(NFCManager.NFCNotSupported nfcnsup) {
            Snackbar.make(v, "NFC not supported", Snackbar.LENGTH_LONG).show();
        }
        catch(NFCManager.NFCNotEnabled nfcnEn) {
            Snackbar.make(v, "NFC Not enabled", Snackbar.LENGTH_LONG).show();
        }

        Intent intent = getIntent();
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())
                || NfcAdapter.ACTION_TECH_DISCOVERED.equals(intent.getAction())
                || NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {

            Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            NdefMessage[] msgs = null;

            if (rawMsgs != null) {
                msgs = new NdefMessage[rawMsgs.length];
                for (int i = 0; i < rawMsgs.length; i++) {
                    msgs[i] = (NdefMessage) rawMsgs[i];
                }
                parseText("Recieved Message");
            }
            else{
                //parseText("Rubba Dub Dub");
                Toast.makeText(this, "no raw message available", Toast.LENGTH_LONG).show();
            }
            vC.myInstance().showText = false;
            buildTagViews(msgs);
        }
        else{
            if(vC.myInstance().showText) {
                //parseText("WHERE'S YOUR SHIT NIBBA");
            }
        }


    }
    @Override
    protected void onPause(){
        super.onPause();
        nfcMger.disableDispatch();

    }

    private void readFromIntent(Intent intent) {
        String action = intent.getAction();

    }
    private void buildTagViews(NdefMessage[] msgs) {
        if (msgs == null || msgs.length == 0) return;

        String text = "";
//        String tagId = new String(msgs[0].getRecords()[0].getType());
        byte[] payload = msgs[0].getRecords()[0].getPayload();
        String textEncoding = ((payload[0] & 128) == 0) ? "UTF-8" : "UTF-16"; // Get the Text Encoding
        int languageCodeLength = payload[0] & 0063; // Get the Language Code, e.g. "en"
        // String languageCode = new String(payload, 1, languageCodeLength, "US-ASCII");

        try {
            // Get the Text
            text = new String(payload, languageCodeLength + 1, payload.length - languageCodeLength - 1, textEncoding);

            /** This is for debugging purposes **/
            //Toast.makeText(this, text, Toast.LENGTH_LONG).show();

            /** Set text to a class variable to use in the configSave() **/
            classText = text;
            parseText(text);
        } catch (UnsupportedEncodingException e) {
            Log.e("UnsupportedEncoding", e.toString());
        }


    }

    void parseText(String text){

        nfcConnect.setText(text);

        //Toast.makeText(this, "this is parseText()", Toast.LENGTH_LONG).show();
    }

}
