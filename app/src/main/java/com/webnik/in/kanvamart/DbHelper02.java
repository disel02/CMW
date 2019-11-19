package com.webnik.in.kanvamart;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import com.webnik.in.kanvamart.ListAdapter.subjects;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DbHelper02 extends SQLiteOpenHelper {

    private static final String DB_NAME="Notepad";
    private static final int DB_VER=1;
    public static final String DB_TABLE="Notepad";
    public static final String DB_COLUMN="rTaskName";
    public static final String DB_COLUMN2="rDate";
    public static final String DB_COLUMN3="rTime";
    public static final String DB_COLUMN4="rNote";
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    SharedPreferences.Editor editor;
    Calendar calendar,calendar2;
    Context mcontext;
    Boolean flag=false;
    Boolean Tflag;
    String fTime1="",fTime2="",fTime="";
    int fHour,fMinute,fDay,fMonth,fYear;
    public static final String[] MONTHS = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

    public DbHelper02(@Nullable Context context   ) {
        super(context, DB_NAME, null, DB_VER);
        mcontext=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query=String.format("CREATE TABLE %s (ID INTEGER PRIMARY KEY AUTOINCREMENT,%s TEXT NOT NULL,%s DATE NOT NULL,%s TIME NOT NULL,%s TEXT NOT NULL);",DB_TABLE,DB_COLUMN,DB_COLUMN2,DB_COLUMN3,DB_COLUMN4);
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String query=String.format("DELETE TABLE IF EXISTS %S",DB_TABLE);
        db.execSQL(query);
        onCreate(db);
    }

    public void insertNewTask(String Task,String Date,String Time,String Note)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(DB_COLUMN,Task);
        values.put(DB_COLUMN2,Date);
        values.put(DB_COLUMN3,Time);
        values.put(DB_COLUMN4,Note);
        db.insertWithOnConflict(DB_TABLE,null,values,SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
    }

    public void deletetask(String Task)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        db.delete(DB_TABLE,DB_COLUMN+" = ?",new String[]{Task});
        db.close();
    }

    public void updatetask(String Task,String Date,String Time,String Task2,String Date2,String Time2,String Note)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(DB_COLUMN,Task);
        values.put(DB_COLUMN2,Date);
        values.put(DB_COLUMN3,Time);
        values.put(DB_COLUMN4,Note);
        db.update(DB_TABLE,values,DB_COLUMN+" = ? AND "+DB_COLUMN2+" = ? AND "+DB_COLUMN3+" = ?",new String[]{Task2,Date2,Time2});
        db.close();
    }

    public List<subjects> getTodos(){

        List<subjects> subjectsList;
        subjects subjects = null;
        subjectsList = new ArrayList<subjects>();
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.rawQuery("SELECT rTaskName,rDate,rTime FROM Notepad ORDER BY rDATE DESC,rTIME",null);
        cursor.moveToFirst();
        flag=true;

        while (!cursor.isAfterLast()) {
            subjects = new subjects();

                String title= cursor.getString(0);
                subjects.sTitle=title;
                String tDate=cursor.getString(1);
                subjects.sDate=tDate;
                String qTime= cursor.getString(2);
                String[] separated2 = qTime.split(":");
                int selectedHour= Integer.parseInt(separated2[0]);
                int selectedMinute= Integer.parseInt(separated2[1]);
                String AM_PM = " AM";
                String mm_precede = "";
                if (selectedHour >= 12) {
                    AM_PM = " PM";
                    if (selectedHour >=13 && selectedHour < 24) {
                        selectedHour -= 12;
                    }
                    else {
                        selectedHour = 12;
                    }
                } else if (selectedHour == 0) {
                    selectedHour = 12;
                }
                if (selectedMinute < 10) {
                    mm_precede = "0";
                }
                String time2=selectedHour + ":" +mm_precede + selectedMinute+" "+AM_PM;
                subjects.sTime=time2;
                subjectsList.add(subjects);

            cursor.moveToNext();
        }

        return subjectsList;
    }

}