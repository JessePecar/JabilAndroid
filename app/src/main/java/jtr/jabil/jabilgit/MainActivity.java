package jtr.jabil.jabilgit;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.support.v4.app.Fragment;
import android.widget.Button;

import java.util.List;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    public Context ctx;
    private VariableController vC = new VariableController();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("Jabil");

        ctx = this;
        BottomNavigationView navigator = findViewById(R.id.navigation);
        navigator.setOnNavigationItemSelectedListener(this);
        loadFragment(new NewRunFragment());

        RunDatabase rd = new RunDatabase(this);
        //rd.onUpgrade(rd.getWritableDatabase(), 0, 1);
        System.out.println(vC.myInstance().timer);
        configSavedRuns();
        configGraphRuns();
        configSettings();

    }
    public void configSavedRuns(){
        Button savedRuns;
    }
    public void configGraphRuns(){
        Button graphRuns;
    }
    public void configSettings(){
        Button settings;
    }

    public boolean loadFragment(Fragment fragment){
        if(fragment != null){
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item){
        Fragment fragment = null;
        switch(item.getItemId()){
            case R.id.navigator_new_run:
                fragment = new NewRunFragment();
                break;
            case R.id.navigator_saved_runs:
                fragment = new SavedRunFragment();
                break;
            case R.id.navigator_graphed_run:
                fragment = new GraphFragment();
                break;
            case R.id.navigator_settings:
                fragment = new SettingsFragment();
                break;
        }
        return loadFragment(fragment);
    }

}
