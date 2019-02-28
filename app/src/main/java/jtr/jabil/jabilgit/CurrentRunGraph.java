package jtr.jabil.jabilgit;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class CurrentRunGraph extends Fragment {

    private final Handler handle = new Handler();
    RecyclerView recyclerView;
    RunDatabase rd = new RunDatabase(getActivity());
    View fragmentView;
    Runnable graphTimer;
    VariableController vC = new VariableController();
    RunVariables rV = new RunVariables();
    LineGraphSeries<DataPoint> series;
    GraphView graph;

    int[] newData;
    int lineCounter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        lineCounter = 0;
        series = new LineGraphSeries<>(dataSetup());

        fragmentView = inflater.inflate(R.layout.current_graph_fragment, container, false);
        recyclerView = fragmentView.findViewById(R.id.recyclerView);

        graph = fragmentView.findViewById(R.id.running_graph);
        setupGraph();

        //setTimer();

        return fragmentView;
    }
    void setTimer(){
        TimerTask timer = new TimerTask() {
            @Override
            public void run() {
                //series.resetData(dataSetup());
                setTimer();
            }
        };
        long delay = vC.myInstance().timer * 10;
        Timer time  = new Timer();
        time.schedule(timer, delay);
    }

    @Override
    public void onResume(){
        super.onResume();
        graphTimer = new Runnable() {
            @Override
            public void run() {
                //series.appendData(new DataPoint(rV.myInstance().runDP.size() - 1, rV.myInstance().currentTemp), true, 150);
                series.resetData(dataSetup());

                handle.postDelayed(this, vC.myInstance().timer * 10);

            }
        };
        handle.postDelayed(graphTimer, vC.myInstance().timer * 100);
    }

    DataPoint[] dataSetup(){
        DataPoint[] newData = new DataPoint[rV.myInstance().runDP.size()];
        for(int i = 0; i < rV.myInstance().runDP.size(); i++){
            newData[i] = rV.myInstance().runDP.get(i);

        }
        return newData;
    }
    void setupGraph(){
        System.out.println("Setting up the Series");
        series.setColor(Color.parseColor("#66B5DD"));
        series.setDrawDataPoints(true);
        series.setDataPointsRadius(14);
        series.setThickness(10);
        series.setBackgroundColor(Color.parseColor("#222222"));

        System.out.println("Setting up the graph");
        graph.getGridLabelRenderer().setGridColor(Color.WHITE);
        graph.getGridLabelRenderer().setHorizontalAxisTitle("Time");
        graph.getGridLabelRenderer().setHorizontalAxisTitleColor(Color.parseColor("#66B5DD"));
        graph.getGridLabelRenderer().setHorizontalAxisTitleTextSize(48);

        System.out.println("Horizontal is done, now on to Vertical");
        graph.getGridLabelRenderer().setVerticalAxisTitle("Temperature");
        graph.getGridLabelRenderer().setVerticalAxisTitleColor(Color.parseColor("#66B5DD"));
        graph.getGridLabelRenderer().setVerticalAxisTitleTextSize(48);
        series.setAnimated(false);

        System.out.println("Setting the label colors");
        graph.getGridLabelRenderer().setHorizontalLabelsColor(Color.WHITE);
        graph.getGridLabelRenderer().setVerticalLabelsColor(Color.WHITE);

        graph.getViewport().setScrollable(true);
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(10);

        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMinY(32);
        graph.getViewport().setMaxY(120);


        System.out.println("Adding series");
        graph.addSeries(series);
    }
}
