package jtr.jabil.jabilgit;

import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
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
import com.github.mikephil.charting.formatter.IFillFormatter;
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.List;

public class GraphFragment extends Fragment {

    RecyclerView recyclerView;
    RunDatabase rd = new RunDatabase(getActivity());
    VariableController vC = new VariableController();

    int[] newData;
    int lineCounter;
    List<Entry> newPoints;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        lineCounter = 0;
        newPoints = new ArrayList<>();
        TextView graphTitle;
        View fragmentView;
        fragmentView = inflater.inflate(R.layout.create_graph_fragment, container, false);
        graphTitle = fragmentView.findViewById(R.id.graphTitle);
        graphTitle.setText(vC.myInstance().displayedName);
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

                newData[i] = (int)Float.parseFloat(temp[i]);
                newPoints.add(new Entry(i * vC.myInstance().displayedTimer, newData[i]));
            }
        }

        System.out.println("tempString = " + tempString);


    }

    void setupGraph(View fragView){
        LineChart graph;
        graph = fragView.findViewById(R.id.graph);

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

        LineDataSet lds = new LineDataSet(newPoints, tempName);
        lds.setColor(getResources().getColor(R.color.colorAccent2));
        lds.setValueTextColor(getResources().getColor(R.color.colorWhite));
        lds.setValueTextSize(14);

        lds.setFillColor(getResources().getColor(R.color.colorAccent));
        lds.setDrawFilled(true);
        LineData lineData = new LineData(lds);

        Description desc = graph.getDescription();
        desc.setText("Selected Run Graph");
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
        graph.setBackground(getResources().getDrawable(R.drawable.gradient_3));

        graph.getAxisLeft().mAxisMinimum = 20;
        graph.getAxisRight().mAxisMaximum = 140;
        graph.animateX(500);

        Legend leg = graph.getLegend();
        leg.setTextColor(Color.WHITE);
        leg.setTextSize(20);

    }


}
