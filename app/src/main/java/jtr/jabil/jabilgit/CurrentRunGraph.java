package jtr.jabil.jabilgit;

import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
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
    int count = 0;
    VariableController vC = new VariableController();
    RunVariables rV = new RunVariables();
    LineGraphSeries<DataPoint> series;
    LineChart graph;
    int lineCounter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        lineCounter = 0;

        fragmentView = inflater.inflate(R.layout.current_graph_fragment, container, false);
        recyclerView = fragmentView.findViewById(R.id.recyclerView);

        graph = fragmentView.findViewById(R.id.running_graph);
        setupGraph();


        miniTimer();
        return fragmentView;
    }
    void miniTimer(){
        TimerTask timer = new TimerTask() {
            @Override
            public void run() {
                if (rV.myInstance().keepRunning) {
                    LineDataSet lds = new LineDataSet(rV.myInstance().runDP, null);

                    lds.setValueTextColor(getResources().getColor(R.color.colorWhite));
                    lds.setValueTextSize(14);
                    LineDataSet minilds = new LineDataSet(rV.myInstance().filler, null);
                    minilds.setColor(getResources().getColor(R.color.colorAccent));

                    LineData lineData = new LineData(lds);
                    lineData.addDataSet(lds);
                    lineData.addDataSet(minilds);
                    graph.setData(lineData);
                    graph.notifyDataSetChanged();

                    graph.invalidate();
                    if(count == 10){
                        count = 0;
                        //setTimer();
                    }
                    else{
                        count++;
                    }
                    miniTimer();
                }
            }
        };
        long delay = vC.myInstance().timer;
        Timer time  = new Timer();
        time.schedule(timer, delay);

    }
    void setTimer(){
        if(rV.myInstance().keepRunning){
            LineDataSet lds = new LineDataSet(rV.myInstance().runDP, rV.myInstance().runName);

            lds.setValueTextColor(getResources().getColor(R.color.colorWhite));
            lds.setValueTextSize(14);
            LineData lineData = new LineData(lds);
            lineData.addDataSet(lds);
            graph.setData(lineData);
            graph.notifyDataSetChanged();

            graph.invalidate();
        }
        else{

        }
    }


    /* There is an error here */

    void setupGraph(){
        System.out.println("Setting up the Series");

        //I paint it black
        LineDataSet lds = new LineDataSet(rV.myInstance().runDP, null);
        lds.setColor(getResources().getColor(R.color.colorAccent2));
        lds.setValueTextColor(getResources().getColor(R.color.colorWhite));

        LineData lineData = new LineData(lds);

        Description desc = graph.getDescription();
        desc.setText("Current Run Graph");
        desc.setTextColor(Color.WHITE);
        desc.setPosition(1150,1647);
        desc.setTextSize(14);

        XAxis xAxis = graph.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextColor(Color.WHITE);

        YAxis yAxisL = graph.getAxisLeft();
        yAxisL.setTextColor(Color.WHITE);

        YAxis yAxisR = graph.getAxisRight();
        yAxisR.setTextColor(Color.parseColor("#444444"));

        graph.setData(lineData);
        graph.invalidate();
        graph.setDragDecelerationEnabled(true);
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setColor(Color.parseColor("#66B5DD"));
        paint.setStrokeWidth(14);
        graph.setPaint(paint, 0);
        graph.setBackgroundColor(Color.parseColor("#444444"));

        graph.getAxisLeft().mAxisMinimum = 20;
        graph.getAxisRight().mAxisMaximum = 140;
        graph.animateX(200);

        Legend leg = graph.getLegend();
        leg.setTextColor(Color.WHITE);
        leg.setTextSize(20);

        graph.setPaint(paint, 0);

    }
}
