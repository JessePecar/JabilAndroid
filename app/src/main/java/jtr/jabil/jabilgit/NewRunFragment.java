package jtr.jabil.jabilgit;
import static android.app.Activity.RESULT_OK;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class NewRunFragment extends Fragment{
    RecyclerView recyclerView;
    RunDatabase rd = new RunDatabase(getActivity());
    static final int START_RUN_REQUEST = 1;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        View fragmentView;

        fragmentView = inflater.inflate(R.layout.run_fragment_list, container, false);
        recyclerView = fragmentView.findViewById(R.id.recyclerView);

        return fragmentView;
    }
    /*
    private void configureSave(View view){
        Button saveRunButton = view.findViewById(R.id.saveButton);
        saveRunButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String runName = "";
                int maxTemp = 0, minTemp = 0;

                TextView tV = fragmentView.findViewById(R.id.runName);
                TextView MT = fragmentView.findViewById(R.id.maxTempNum);
                TextView mT = fragmentView.findViewById(R.id.minTempNum);

                runName = tV.getText().toString();
                maxTemp = Integer.parseInt(MT.getText().toString());
                minTemp = Integer.parseInt(mT.getText().toString());
                //This should be adding to the new database
                rd.addRun(runName, maxTemp, minTemp);

            }
        });
    }*/

    void configStart(){
        Button startRunButton = getActivity().findViewById(R.id.startButton);
        startRunButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Change to the new Activity
                Intent intent = new Intent(getActivity(), NewRunActivity.class);
                startActivityForResult(intent, START_RUN_REQUEST);
            }
        });
    }
    // This isn't needed unless we go to a new activity
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        configStart();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == START_RUN_REQUEST){
            if(resultCode == RESULT_OK){
                System.out.println();
            }

        }
    }


}

