package jtr.jabil.jabilgit;

import android.graphics.Color;
import android.os.Bundle;
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

public class GraphFragment extends Fragment {

    RecyclerView recyclerView;
    RunDatabase rd = new RunDatabase(getActivity());

    int[] newData;
    int lineCounter;
    List<DataPoint> newPoints;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        lineCounter = 0;
        newPoints = new ArrayList<>();

        View fragmentView;
        fragmentView = inflater.inflate(R.layout.create_graph_fragment, container, false);
        recyclerView = fragmentView.findViewById(R.id.recyclerView);

        setupGraph(fragmentView);

        return fragmentView;
    }
    void setupData(String runData){

        String tempString = "80, 75, 78, 102, 96, 120, 32";
        tempString = runData;
        if(tempString.equalsIgnoreCase("")){
            newData = new int[] {80, 75, 78, 102, 96, 120, 32};
        }
        else{
            String[] temp = tempString.replaceAll("\\[", "").replaceAll("\\]", "").replaceAll("\\s", "").split(",");
            newData = new int[temp.length];

            for(int i = 0; i < temp.length; i++){
                newData[i] = Integer.parseInt(temp[i]);
                newPoints.add(new DataPoint((double)i, (double)newData[i]));
            }
        }

        System.out.println("tempString = " + tempString);


    }
    DataPoint createDataPoint(){
        DataPoint dataPoint;
        if(lineCounter < newData.length){
            dataPoint = new DataPoint(lineCounter, newData[lineCounter]);
            lineCounter++;
            return dataPoint;
        }
        else{
            return null;
        }
    }
    private DataPoint[] popData(){
        DataPoint[] newPopData = new DataPoint[newPoints.size()];

        for(int i = 0; i < newPoints.size(); i++){
            newPopData[i] = newPoints.get(i);
        }
        return newPopData;
    }
    void setupGraph(View fragView){
        GraphView graph;
        graph = fragView.findViewById(R.id.graph);
        VariableController vC = new VariableController();
        LineGraphSeries<DataPoint> series;
        String tempString;
        String tempName;
        if(vC.myInstance().displayedName == null){
            tempString = "";
            tempName = "";
        }else{
            tempString = vC.myInstance().displayedRun;
            tempName= vC.myInstance().displayedName;
        }
        setupData(tempString);
        System.out.println(tempName + ": " + tempString);
        if (tempString != null){
            series = new LineGraphSeries<>(popData());
            series.setTitle(tempName);
            TextView tV = fragView.findViewById(R.id.graphTitle);
            tV.setText(tempName);
        }
        else{
            series = new LineGraphSeries<>(new DataPoint[]{
                    new DataPoint(0,0),
                    new DataPoint(1,1),
                    new DataPoint(2,2)
            });
            series.setTitle("Actual Cannibal Shia LeBouf");
            TextView tV = fragView.findViewById(R.id.graphTitle);
            tV.setText(series.getTitle());
        }

        series.setColor(getResources().getColor(R.color.colorAccent2));
        series.setDrawDataPoints(true);
        series.setDataPointsRadius(14);
        series.setThickness(10);
        series.setBackgroundColor(getResources().getColor(R.color.colorFragment));
        graph.getGridLabelRenderer().setGridColor(getResources().getColor(R.color.colorWhite));
        graph.getGridLabelRenderer().setHorizontalAxisTitle("Minutes");
        graph.getGridLabelRenderer().setHorizontalAxisTitleColor(getResources().getColor(R.color.colorAccent2));
        graph.getGridLabelRenderer().setHorizontalAxisTitleTextSize(48);
        graph.getGridLabelRenderer().setVerticalAxisTitle("Temperature");
        graph.getGridLabelRenderer().setVerticalAxisTitleColor(getResources().getColor(R.color.colorAccent2));
        graph.getGridLabelRenderer().setVerticalAxisTitleTextSize(48);
        series.setAnimated(true);

        graph.getGridLabelRenderer().setHorizontalLabelsColor(getResources().getColor(R.color.colorWhite));
        graph.getGridLabelRenderer().setVerticalLabelsColor(getResources().getColor(R.color.colorWhite));
        graph.addSeries(series);
    }


}
