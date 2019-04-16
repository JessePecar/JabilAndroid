package jtr.jabil.jabilgit;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class StartData extends AppCompatActivity {
    private NFCManager nfcMgr;
    VariableController vC = new VariableController();
    private View v;
    private NdefMessage message = null;
    private ProgressDialog dialog;
    Tag currentTag;
    TextView F, C, time;
    int timeSet;
    Switch tempSwitch;
    SeekBar timeSeek;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_data);
        getSupportActionBar().setTitle("Setup NFC");


        F = findViewById(R.id.Fahrenheit);
        C = findViewById(R.id.Celcius);
        time = findViewById(R.id.timeText);

        tempSwitch = findViewById(R.id.tempSwitch);
        timeSeek = findViewById(R.id.timeSeek);
        timeSeek.setProgress(vC.myInstance().timer);
        time.setText(String.valueOf(vC.myInstance().timer));

        nfcMgr = new NFCManager(this);
        configSlide();
        configSwitch();

        final EditText et = (EditText) findViewById(R.id.content);
        Button btn = findViewById(R.id.sendFile);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                message = nfcMgr.createTextMessage("ST");
                if (message != null) {

                    dialog = new ProgressDialog(StartData.this);
                    dialog.setMessage("Tag NFC to Start");
                    dialog.show();
                }
            }

        });
        Button setDate = findViewById(R.id.setDate);
        setDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                message = nfcMgr.createTextMessage("DA " + vC.myInstance().Month + " " + vC.myInstance().Day + " "+ vC.myInstance().Year);
                if (message != null) {

                    dialog = new ProgressDialog(StartData.this);
                    dialog.setMessage("Tag NFC to Set Date");
                    dialog.show();
                }
            }

        });
        Button setTime = findViewById(R.id.setTime);
        setTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pageCount = 0;

                Date date = new Date();
                DateFormat newFormat;


                String hour = "hh";
                newFormat = new SimpleDateFormat(hour);
                final String formattedhour = newFormat.format(date);

                String minute = "mm";
                newFormat = new SimpleDateFormat(minute);
                final String formattedminute = newFormat.format(date);

                String second = "ss";
                newFormat = new SimpleDateFormat(second);
                final String formattedsecond = newFormat.format(date);

                message = nfcMgr.createTextMessage("TI " + formattedhour + " " + formattedminute + " " + formattedsecond);
                if (message != null) {

                    dialog = new ProgressDialog(StartData.this);
                    dialog.setMessage("Tag NFC to Set Time");
                    dialog.show();
                }
            }

        });
        Button clearData = findViewById(R.id.clearData);
        clearData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                message = nfcMgr.createTextMessage("CD");
                if (message != null) {

                    dialog = new ProgressDialog(StartData.this);
                    dialog.setMessage("Tag NFC to Clear");
                    dialog.show();
                }
            }

        });
    }


    public void configSwitch() {
        //Set on click here
        if (vC.myInstance().tempUnit.equals("C")) {

            tempSwitch.toggle();
        }

        if (tempSwitch.isChecked() == true) {
            F.setTextColor(Color.GRAY);
            C.setTextColor(Color.WHITE);
        } else {
            C.setTextColor(Color.GRAY);
            F.setTextColor(Color.WHITE);
        }

        tempSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (tempSwitch.isChecked() == true) {
                    vC.myInstance().tempUnit = "C";
                    F.setTextColor(Color.GRAY);
                    C.setTextColor(Color.WHITE);
                } else {
                    vC.myInstance().tempUnit = "F";
                    C.setTextColor(Color.GRAY);
                    F.setTextColor(Color.WHITE);
                }
                message = nfcMgr.createTextMessage("TM " + vC.myInstance().tempUnit);
                if (message != null) {

                    dialog = new ProgressDialog(StartData.this);
                    dialog.setMessage("Tag NFC Tag please");
                    dialog.show();
                }
            }
        });
    }

    public void configSlide() {
        timeSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                timeSet = progress;

                time.setText(String.valueOf(timeSet));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                vC.myInstance().timer = timeSet;
                message = nfcMgr.createTextMessage("PI " + vC.myInstance().timer);
                if (message != null) {
                    dialog = new ProgressDialog(StartData.this);
                    dialog.setMessage("Tag NFC Tag please");
                    dialog.show();
                }
            }
        });
    }



    @Override
    protected void onResume() {
        super.onResume();

        try {
            nfcMgr.verifyNFC();
            //nfcMger.enableDispatch();

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

    }


    @Override
    protected void onPause() {
        super.onPause();
        nfcMgr.disableDispatch();
    }

    @Override
    public void onNewIntent(Intent intent) {
        Log.d("Nfc", "New intent");
        // It is the time to write the tag
        currentTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        if (message != null) {
            nfcMgr.writeTag(currentTag, message);
            dialog.dismiss();
//            Snackbar.make(v, "Tag written", Snackbar.LENGTH_LONG).show();

        }
        else {
            // Handle intent
        }
    }

}
/**
 * Temp Units get sent as F or C,
 *
 * RE -> Reset
 * ST -> Start
 * CD -> Clear Data
 * SP -> Stop
 * MO -> (Shouldn't really be used too much, but we will use it for safety) Always uses Temp only -> 0 (Example MO 0)
 * TM -> Temp Mode -> sets F or C (Example: TM F)
 * TI -> Sets time (Example: TI hh mm ss)
 * DA -> Sets the date (Example: DA MM dd yy
 * PI -> polling interval (Example: PI vC.myInstance().timer) delay will need to be an int
 *
 * Will need to send files for ST, MO, TM, TI, DA, and PI every time
 *
 * make sure you send 6 files every time, in this order -> CD -> MO -> TM -> TI -> DA -> PI -> ST
 *
 * When receiving you need to send two files when done with the NFC, one for CD and one for RE...
 * **/
