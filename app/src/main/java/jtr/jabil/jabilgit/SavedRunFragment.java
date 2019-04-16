package jtr.jabil.jabilgit;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.widget.CheckBox;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class SavedRunFragment extends Fragment{

    private RunCardAdapter rca;
    private RunDatabase db;

    CheckBox savedRun;
    List<RunCard> cardList;
    RunCard holder;
    public List<CheckBox> cardChecks;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.saved_run_fragment, container, false);

        cardList = new ArrayList<>();

        RecyclerView rV = view.findViewById(R.id.recyclerView);


        rV.setHasFixedSize(true);
        rV.setLayoutManager(new LinearLayoutManager(getActivity()));


        db = new RunDatabase(getActivity());
        Cursor runResult = db.getRunData();
        StringBuffer buffer = new StringBuffer();
        String runName, runDate, runData;
        int runMaxTemp, runMinTemp, runLength, runID, runInterval;
            while(runResult.moveToNext()){
                runID = runResult.getInt(0);
                runName = runResult.getString(1);
                runLength = runResult.getInt(2);
                runDate = runResult.getString(3);
                runMaxTemp = runResult.getInt(4);
                runMinTemp = runResult.getInt(5);
                runData = runResult.getString(6);
                runInterval = runResult.getInt(7);

                cardList.add(new RunCard(
                        runID,
                        runName,
                        runLength,
                        runDate,
                        runMaxTemp,
                        runMinTemp,
                        runData,
                        runInterval
                ));

            }
        //Add cards based on number of Runs

        rca = new RunCardAdapter(getActivity(), cardList);

        rV.setAdapter(rca);

        return view;
    }



}
