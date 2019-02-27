package jtr.jabil.jabilgit;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import org.w3c.dom.Text;

public class SettingsFragment extends Fragment {
    TextView F, C, time;
    int timeSet;
    Switch tempSwitch;
    SeekBar timeSeek;
    VariableController vC = new VariableController();

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.settings_fragment, container, false);

        timeSet = 100;


        F = (TextView) view.findViewById(R.id.Fahrenheit);
        C = (TextView) view.findViewById(R.id.Celcius);
        time = (TextView) view.findViewById(R.id.timeText);

        tempSwitch = (Switch) view.findViewById(R.id.tempSwitch);
        timeSeek = (SeekBar) view.findViewById(R.id.timeSeek);
        timeSeek.setProgress(vC.myInstance().timer);

        if(tempSwitch.isChecked() == true){
            F.setTextColor(Color.GRAY);
            C.setTextColor(Color.WHITE);
        }
        else{
            C.setTextColor(Color.GRAY);
            F.setTextColor(Color.WHITE);
        }

        configSwitch();
        configSlide();
        return view;
    }
    public void configSwitch(){
        //Set on click here
        tempSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(tempSwitch.isChecked() == true){
                    F.setTextColor(Color.GRAY);
                    C.setTextColor(Color.WHITE);
                }
                else{
                    C.setTextColor(Color.GRAY);
                    F.setTextColor(Color.WHITE);
                }
            }
        });
    }
    public void configSlide(){
        timeSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                timeSet = progress;

                time.setText(String.valueOf(timeSet / 100));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                vC.myInstance().timer = timeSet;
            }
        });
    }
}