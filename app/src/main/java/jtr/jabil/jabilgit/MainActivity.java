package jtr.jabil.jabilgit;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    public Context ctx;
    private VariableController vC = new VariableController();
    BottomNavigationView navigator;
    private View v;

    private NFCManager nfcMger;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("Jabil");
        getSupportActionBar().setBackgroundDrawable(getDrawable(R.drawable.gradient_3));
        nfcMger = new NFCManager(this);
        ctx = this;
        navigator = findViewById(R.id.navigation);
        navigator.setOnNavigationItemSelectedListener(this);
        navigator.setBackground(getDrawable(R.drawable.gradient_5));
        loadFragment(new NewRunFragment(), 0);

        Date date = new Date();
        String day;
        day = "dd";
        DateFormat newFormat = new SimpleDateFormat(day);
        vC.myInstance().Day = newFormat.format(date);
        vC.myInstance().showText = true;
        String month;
        month = "MM";
        newFormat = new SimpleDateFormat(month);
        vC.myInstance().Month = newFormat.format(date);

        String year;
        year = "yy";
        newFormat = new SimpleDateFormat(year);
        vC.myInstance().Year = newFormat.format(date);


        RunDatabase rd = new RunDatabase(this);
        //rd.onUpgrade(rd.getWritableDatabase(), 1, 2);
        System.out.println(vC.myInstance().timer);
        if(vC.myInstance().timer < 1){
            vC.myInstance().timer = 1;
        }
        if(vC.myInstance().tempUnit == null){
            vC.myInstance().tempUnit = "F";
        }

        /** Under here I will have the data read functionality set up so the application can take in the data, this will happen last in coding, I want to
         * set up the sending of files functionality **/
    }

    @Override
    protected void onResume(){
        super.onResume();

    }
    @Override
    protected void onPause(){
        super.onPause();
        //nfcMger.disableDispatch();

    }
    public boolean loadFragment(Fragment fragment, int test){
        if(fragment != null){
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            if(test == 1){
                navigator.setSelectedItemId(R.id.navigator_graphed_run);
            }

            return true;
        }
        return false;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item){
        Fragment fragment = null;
        switch(item.getItemId()){
            case R.id.navigator_new_run:
                fragment = new NewRunFragment();
                break;
            case R.id.navigator_saved_runs:
                fragment = new SavedRunFragment();
                break;
            case R.id.navigator_graphed_run:
                fragment = new GraphFragment();
                break;
        }
        return loadFragment(fragment, 0);
    }

}
