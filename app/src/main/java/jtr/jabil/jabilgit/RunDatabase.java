package jtr.jabil.jabilgit;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class RunDatabase extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "SavedRunDatabase.db";
    //First table
    /*
    _________________________________________________
    |__runID_|_runName__|_maxTemp_____|_minTemp______|
    |________|__________|_____________|______________|
     */
    public static final String RUN_TABLE = "runTable";

    public static final String COL_1 = "runID";

    public static final String COL_2 = "runName";
    public static final String COL_3 = "runLength";
    public static final String COL_4 = "runDate";
    public static final String COL_5 = "maxTemp";
    public static final String COL_6 = "minTemp";
    public static final String COL_7 = "runData";
    public static final String COL_8 = "runInterval";

    public RunDatabase(Context context) {
        super(context, DATABASE_NAME, null, 1);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + RUN_TABLE +
                " (runID INTEGER PRIMARY KEY AUTOINCREMENT, runName TEXT,  runLength INT, runDate TEXT, maxTemp INT, minTemp INT, runData TEXT, runInterval INT)");
    }
    /** Only call this if you want to delete everything in the database and start over. **/
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + RUN_TABLE);
        onCreate(db);
    }

    public boolean addRun(String runName, int runLength, String runDate, int maxTemp, int minTemp, String runData, int runInterval){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_2, runName);
        cv.put(COL_3, runLength);
        cv.put(COL_4, runDate);
        cv.put(COL_5, maxTemp);
        cv.put(COL_6, minTemp);
        cv.put(COL_7, runData);
        cv.put(COL_8, runInterval);

        long result = db.insert(RUN_TABLE, null, cv);

        return result != -1;
    }
    public Cursor getRunData(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.rawQuery("select * from " + RUN_TABLE, null);
        return result;
    }

    public Integer deleteRun(String runID){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(RUN_TABLE, "runID = " + runID, null);

    }

}
