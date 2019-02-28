package jtr.jabil.jabilgit;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;


public class RunCardAdapter extends RecyclerView.Adapter<RunCardAdapter.RunCardViewHolder> {
    private Context ctx;
    private List<RunCard> runList;

    VariableController vC = new VariableController();
    final MainActivity mA = new MainActivity();
    RunCardViewHolder rcvh;
    public String runData, runDataName;
    public int runID;

    public RunCardAdapter(Context ctx, List<RunCard> runList){
        this.ctx = ctx;
        this.runList = runList;
    }

    @Override
    public RunCardViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        LayoutInflater inflater = LayoutInflater.from(ctx);
        View view = inflater.inflate(R.layout.run_card_view, null);
        return new RunCardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RunCardViewHolder holder, final int position){

        RunCard runs = runList.get(position);
        holder.runName.setText(String.valueOf(runs.getName()));
        holder.runLength.setText("Length: " + String.valueOf(runs.getRunLength()));
        holder.runDate.setText(String.valueOf(runs.getDate()));
        holder.maxTemp.setText(String.valueOf("Max Temp: " +runs.getMaxTemp()));
        holder.minTemp.setText(String.valueOf("Min Temp: " + runs.getMinTemp()));
        runData = runs.getData();
        runID = runs.getRunID();
        runDataName = runs.getName();
        holder.deleteButton.setOnClickListener(new View.OnClickListener(){
            RunDatabase db = new RunDatabase(ctx);
            @Override
            public void onClick(View view) {
                try{
                    //Setting the ID for deletin it from the database.
                    vC.myInstance().runID = runList.get(position).getRunID();
                    int run = vC.myInstance().runID;
                    //Sets the run ID to a string, just for simplicity and readablitity
                    String runS = String.valueOf(run);
                    //Deleting the run from the database, only when the blue X is hit in the run card.
                    int deYEETedRow = db.deleteRun(runS);

                    vC.myInstance().runID = -1; //This is my clean variable that I use to symbolize that it isn't set.
                    runList.remove(position);
                    notifyDataSetChanged();
                }
                catch(Exception e){
                    System.out.println("This shit empty...YEEEEEET");
                }
            }

        });

        if(runID == vC.myInstance().runID){
            //Change the color to white ===>>>
            holder.itemView.setBackgroundColor(Color.GRAY);
        }
        else{
            holder.itemView.setBackgroundColor(Color.parseColor("#424242"));
        }

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                if(runList.get(position).getRunID() == vC.myInstance().runID){
                    //Return the color to the original color...
                    vC.myInstance().displayedName = "";
                    vC.myInstance().displayedRun = "";
                    vC.myInstance().runID = -1;
                    holder.itemView.setBackgroundColor(Color.parseColor("#424242"));


                }
                else{
                    if(rcvh != null){
                        rcvh.itemView.setBackgroundColor(Color.parseColor("#424242"));
                    }
                    //Change the color, save the last value as rcvh, and change old rcvh to normal color...
                    holder.itemView.setBackgroundColor(Color.GRAY);
                    rcvh = holder;
                    vC.myInstance().displayedName = runList.get(position).getName();
                    vC.myInstance().displayedRun = runList.get(position).getData();
                    vC.myInstance().min = runList.get(position).getMinTemp();
                    vC.myInstance().max = runList.get(position).getMaxTemp();
                    vC.myInstance().runID = runList.get(position).getRunID();
                }
                System.out.println("\n+\n++\n+++ Saved The Data: " + vC.myInstance().displayedName + " \n+\n++\n+++");

            }
        });
    }

    @Override
    public int getItemCount(){  return runList.size();  }
    class RunCardViewHolder extends RecyclerView.ViewHolder{
        TextView runName, maxTemp, minTemp, runLength, runDate;
        ConstraintLayout parentLayout;
        Button deleteButton;

        public RunCardViewHolder(View itemView){
            super(itemView);

            deleteButton = itemView.findViewById(R.id.deleteButton);
            parentLayout = itemView.findViewById(R.id.constrain);
            runName = itemView.findViewById(R.id.runName);
            runLength = itemView.findViewById(R.id.runLength);
            runDate = itemView.findViewById(R.id.runDate);
            minTemp = itemView.findViewById(R.id.minTemp);
            maxTemp = itemView.findViewById(R.id.maxTemp);

        }

    }
}
