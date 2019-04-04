package jtr.jabil.jabilgit;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.animation.ChartAnimator;
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
import com.github.mikephil.charting.renderer.LineChartRenderer;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.utils.ViewPortHandler;
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

                    ILineDataSet minilds = new LineDataSet(rV.myInstance().fillerMax, null);
                    ILineDataSet minlds = new LineDataSet(rV.myInstance().fillerMin, null);

                    ((LineDataSet) minilds).setValueTextColor(getResources().getColor(R.color.colorClear));
                    ((LineDataSet) minlds).setValueTextColor(getResources().getColor(R.color.colorClear));

                    ((LineDataSet) minilds).setColor(getResources().getColor(R.color.colorAccent));
                    ((LineDataSet) minlds).setColor(getResources().getColor(R.color.colorAccent));

                    ((LineDataSet) minilds).setFillFormatter(new MyFillFormatter(minlds));
                    ((LineDataSet)minilds).setFillColor(getResources().getColor(R.color.colorAccent));

                    LineData lineData = new LineData(lds);
                    graph.setRenderer(new MyLineLegendRenderer(graph, graph.getAnimator(), graph.getViewPortHandler()));
                    lineData.addDataSet(minilds);
                    lineData.addDataSet(minlds);

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

        LineDataSet minilds = new LineDataSet(rV.myInstance().fillerMax, null);

        LineDataSet minlds = new LineDataSet(rV.myInstance().fillerMin, null);

        minilds.setColor(getResources().getColor(R.color.colorAccent));
        minlds.setColor(getResources().getColor(R.color.colorAccent));

        minilds.setFillFormatter(new MyFillFormatter(minlds));
        minilds.setFillColor(getResources().getColor(R.color.colorAccent));

        LineData lineData = new LineData(lds);

        lineData.addDataSet(minilds);
        lineData.addDataSet(minlds);

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

        graph.setRenderer(new MyLineLegendRenderer(graph, graph.getAnimator(), graph.getViewPortHandler()));

        Legend leg = graph.getLegend();
        leg.setTextColor(Color.WHITE);
        leg.setTextSize(20);

        graph.setPaint(paint, 0);

    }
}
/** This is for the fill **/

class MyFillFormatter implements IFillFormatter {
    private ILineDataSet boundaryDataSet;

    public MyFillFormatter() {
        this(null);
    }
    //Pass the dataset of other line in the Constructor
    public MyFillFormatter(ILineDataSet boundaryDataSet) {
        this.boundaryDataSet = boundaryDataSet;
    }

    @Override
    public float getFillLinePosition(ILineDataSet dataSet, LineDataProvider dataProvider) {
        return 0;
    }

    //Define a new method which is used in the LineChartRenderer
    public List<Entry> getFillLineBoundary() {
        if(boundaryDataSet != null) {
            return ((LineDataSet) boundaryDataSet).getValues();
        }
        return null;
    }}

    class MyLineLegendRenderer extends LineChartRenderer {

    public MyLineLegendRenderer(LineDataProvider chart, ChartAnimator animator, ViewPortHandler viewPortHandler) {
        super(chart, animator, viewPortHandler);
    }

    //This method is same as it's parent implemntation
    @Override
    protected void drawLinearFill(Canvas c, ILineDataSet dataSet, Transformer trans, XBounds bounds) {
        final Path filled = mGenerateFilledPathBuffer;

        final int startingIndex = bounds.min;
        final int endingIndex = bounds.range + bounds.min;
        final int indexInterval = 128;

        int currentStartIndex = 0;
        int currentEndIndex = indexInterval;
        int iterations = 0;

        // Doing this iteratively in order to avoid OutOfMemory errors that can happen on large bounds sets.
        do {
            currentStartIndex = startingIndex + (iterations * indexInterval);
            currentEndIndex = currentStartIndex + indexInterval;
            currentEndIndex = currentEndIndex > endingIndex ? endingIndex : currentEndIndex;

            if (currentStartIndex <= currentEndIndex) {
                generateFilledPath(dataSet, currentStartIndex, currentEndIndex, filled);

                trans.pathValueToPixel(filled);

                final Drawable drawable = dataSet.getFillDrawable();
                if (drawable != null) {

                    drawFilledPath(c, filled, drawable);
                } else {

                    drawFilledPath(c, filled, dataSet.getFillColor(), dataSet.getFillAlpha());
                }
            }

            iterations++;

        } while (currentStartIndex <= currentEndIndex);
    }

    //This is where we define the area to be filled.
    private void generateFilledPath(final ILineDataSet dataSet, final int startIndex, final int endIndex, final Path outputPath) {

        //Call the custom method to retrieve the dataset for other line
        final List<Entry> boundaryEntry = ((MyFillFormatter) dataSet.getFillFormatter()).getFillLineBoundary();

        final float phaseY = mAnimator.getPhaseY();
        final Path filled = outputPath;
        filled.reset();

        final Entry entry = dataSet.getEntryForIndex(startIndex);

        filled.moveTo(entry.getX(), boundaryEntry.get(0).getY());
        filled.lineTo(entry.getX(), entry.getY() * phaseY);

        // create a new path
        Entry currentEntry = null;
        Entry previousEntry = null;
        for (int x = startIndex + 1; x <= endIndex; x++) {

            currentEntry = dataSet.getEntryForIndex(x);
            filled.lineTo(currentEntry.getX(), currentEntry.getY() * phaseY);

        }

        // close up
        if (currentEntry != null && previousEntry != null) {
            filled.lineTo(currentEntry.getX(), previousEntry.getY());
        }

        //Draw the path towards the other line
        for (int x = endIndex; x > startIndex; x--) {
            previousEntry = boundaryEntry.get(x);
            filled.lineTo(previousEntry.getX(), previousEntry.getY() * phaseY);
        }

        filled.close();
    }}