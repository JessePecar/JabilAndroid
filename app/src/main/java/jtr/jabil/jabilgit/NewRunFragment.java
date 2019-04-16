package jtr.jabil.jabilgit;
import static android.app.Activity.RESULT_OK;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class NewRunFragment extends Fragment {
    RecyclerView recyclerView;
    Button sendFile;
    VariableController vC = new VariableController();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View fragmentView;


        fragmentView = inflater.inflate(R.layout.run_fragment_list, container, false);
        recyclerView = fragmentView.findViewById(R.id.recyclerView);

        sendFile = fragmentView.findViewById(R.id.sendFile);



        if (!getActivity().getPackageManager().hasSystemFeature(PackageManager.FEATURE_NFC)) {

            sendFile.setText("No NFC enabled on Device");
            sendFile.setClickable(false);

        } else {
            configSend();
        }

        return fragmentView;
    }


    void configSend() {

        sendFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Send the NFC Component a file
                Intent intent = new Intent(getActivity(), StartData.class);
                intent.putExtra("Go Gat 'em Tiger", 0);
                getActivity().startActivity(intent);
            }
        });
    }
}

