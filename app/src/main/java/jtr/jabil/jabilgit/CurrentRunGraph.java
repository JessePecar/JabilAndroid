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

                    LineDataSet minilds = new LineDataSet(rV.myInstance().fillerMax, null);
                    LineDataSet minlds = new LineDataSet(rV.myInstance().fillerMin, null);

                    lds.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
                    minilds.setMode(LineDataSet.Mode.LINEAR);
                    minlds.setMode(LineDataSet.Mode.LINEAR);

                    minilds.setValueTextColor(getResources().getColor(R.color.colorClear));
                    minlds.setValueTextColor(getResources().getColor(R.color.colorClear));

                    minilds.setColor(getResources().getColor(R.color.colorAccent));
                    minlds.setColor(getResources().getColor(R.color.colorAccent));

                    minilds.setFillFormatter(new MyFillFormatter(minlds));
                    minilds.setFillColor(getResources().getColor(R.color.colorAccent));
                    minilds.setDrawFilled(true);

                    LineData lineData = new LineData(lds);
                    lineData.addDataSet(minilds);
                    lineData.addDataSet(minlds);
                    graph.setRenderer(new MyLineLegendRenderer(graph, graph.getAnimator(), graph.getViewPortHandler()));

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

        minlds.setFillFormatter(new MyFillFormatter(minilds));
        minlds.setFillColor(getResources().getColor(R.color.colorAccent));
        minilds.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
        minlds.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);

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

        MyLineLegendRenderer(LineDataProvider chart, ChartAnimator animator, ViewPortHandler viewPortHandler) {
            super(chart, animator, viewPortHandler);
        }

        // This method is same as its parent implementation. (Required so our version of generateFilledPath() is called.)
        @Override
        protected void drawLinearFill(Canvas c, ILineDataSet dataSet, Transformer trans, XBounds bounds) {

            final Path filled = mGenerateFilledPathBuffer;

            final int startingIndex = bounds.min;
            final int endingIndex = bounds.range + bounds.min;
            final int indexInterval = 128;

            int currentStartIndex;
            int currentEndIndex;
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
                    }
                    else {
                        drawFilledPath(c, filled, dataSet.getFillColor(), dataSet.getFillAlpha());
                    }
                }

                iterations++;

            } while (currentStartIndex <= currentEndIndex);
        }

        // This method defines the perimeter of the area to be filled for horizontal bezier data sets.
        @Override
        protected void drawCubicFill(Canvas c, ILineDataSet dataSet, Path spline, Transformer trans, XBounds bounds) {

            final float phaseY = mAnimator.getPhaseY();

            //Call the custom method to retrieve the dataset for other line
            final List<Entry> boundaryEntries = ((MyFillFormatter)dataSet.getFillFormatter()).getFillLineBoundary();

            // We are currently at top-last point, so draw down to the last boundary point
            Entry boundaryEntry = boundaryEntries.get(bounds.min + bounds.range);
            spline.lineTo(boundaryEntry.getX(), boundaryEntry.getY() * phaseY);

            // Draw a cubic line going back through all the previous boundary points
            Entry prev = dataSet.getEntryForIndex(bounds.min + bounds.range);
            Entry cur = prev;
            for (int x = bounds.min + bounds.range; x >= bounds.min; x--) {

                prev = cur;
                cur = boundaryEntries.get(x);

                final float cpx = (prev.getX()) + (cur.getX() - prev.getX()) / 2.0f;

                spline.cubicTo(
                        cpx, prev.getY() * phaseY,
                        cpx, cur.getY() * phaseY,
                        cur.getX(), cur.getY() * phaseY);
            }

            // Join up the perimeter
            spline.close();

            trans.pathValueToPixel(spline);

            final Drawable drawable = dataSet.getFillDrawable();
            if (drawable != null) {
                drawFilledPath(c, spline, drawable);
            }
            else {
                drawFilledPath(c, spline, dataSet.getFillColor(), dataSet.getFillAlpha());
            }

        }

        // This method defines the perimeter of the area to be filled for straight-line (default) data sets.
        private void generateFilledPath(final ILineDataSet dataSet, final int startIndex, final int endIndex, final Path outputPath) {

            final float phaseY = mAnimator.getPhaseY();
            final Path filled = outputPath; // Not sure if this is required, but this is done in the original code so preserving the same technique here.
            filled.reset();

            //Call the custom method to retrieve the dataset for other line
            final List<Entry> boundaryEntries = ((MyFillFormatter)dataSet.getFillFormatter()).getFillLineBoundary();

            final Entry entry = dataSet.getEntryForIndex(startIndex);
            final Entry boundaryEntry = boundaryEntries.get(startIndex);

            // Move down to boundary of first entry
            filled.moveTo(entry.getX(), boundaryEntry.getY() * phaseY);

            // Draw line up to value of first entry
            filled.lineTo(entry.getX(), entry.getY() * phaseY);

            // Draw line across to the values of the next entries
            Entry currentEntry;
            for (int x = startIndex + 1; x <= endIndex; x++) {
                currentEntry = dataSet.getEntryForIndex(x);
                filled.lineTo(currentEntry.getX(), currentEntry.getY() * phaseY);
            }

            // Draw down to the boundary value of the last entry, then back to the first boundary value
            Entry boundaryEntry1;
            for (int x = endIndex; x > startIndex; x--) {
                boundaryEntry1 = boundaryEntries.get(x);
                filled.lineTo(boundaryEntry1.getX(), boundaryEntry1.getY() * phaseY);
            }

            // Join up the perimeter
            filled.close();

        }

    }