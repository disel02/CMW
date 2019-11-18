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
import android.widget.Toast;

import com.webnik.in.kanvamart.ListAdapter.subjects;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DbHelper extends SQLiteOpenHelper {

    private static final String DB_NAME="TodoList";
    private static final int DB_VER=1;
    public static final String DB_TABLE="Task";
    public static final String DB_COLUMN="rTaskName";
    public static final String DB_COLUMN2="rDate";
    public static final String DB_COLUMN3="rTime";
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    SharedPreferences.Editor editor;
    Calendar calendar,calendar2;
    Context mcontext;
    Boolean flag=false;
    Boolean Tflag;
    String fTime1="",fTime2="",fTime="";
    int fHour,fMinute,fDay,fMonth,fYear;
    public static final String[] MONTHS = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

    public DbHelper(@Nullable Context context   ) {
        super(context, DB_NAME, null, DB_VER);
        mcontext=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query=String.format("CREATE TABLE %s (ID INTEGER PRIMARY KEY AUTOINCREMENT,%s TEXT NOT NULL,%s DATE NOT NULL,%s TIME NOT NULL);",DB_TABLE,DB_COLUMN,DB_COLUMN2,DB_COLUMN3);
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String query=String.format("DELETE TABLE IF EXISTS %S",DB_TABLE);
        db.execSQL(query);
        onCreate(db);
    }

    public void insertNewTask(String Task,String Date,String Time)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(DB_COLUMN,Task);
        values.put(DB_COLUMN2,Date);
        values.put(DB_COLUMN3,Time);
        db.insertWithOnConflict(DB_TABLE,null,values,SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
    }

    public void deletetask(String Task)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        db.delete(DB_TABLE,DB_COLUMN+" = ?",new String[]{Task});
        db.close();
    }

    public void updatetask(String Task,String Date,String Time,String Task2,String Date2,String Time2)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(DB_COLUMN,Task);
        values.put(DB_COLUMN2,Date);
        values.put(DB_COLUMN3,Time);
        db.update(DB_TABLE,values,DB_COLUMN+" = ? AND "+DB_COLUMN2+" = ? AND "+DB_COLUMN3+" = ?",new String[]{Task2,Date2,Time2});
        db.close();
    }

    public List<subjects> getTodos(){

        SharedPreferences prefs = mcontext.getSharedPreferences(MY_PREFS_NAME, mcontext.MODE_PRIVATE);
        editor = mcontext.getSharedPreferences(MY_PREFS_NAME, mcontext.MODE_PRIVATE).edit();

        calendar = Calendar.getInstance();
        calendar2 = Calendar.getInstance();
        int cYear=calendar.get(Calendar.YEAR);
        int cMonth=calendar.get(Calendar.MONTH);
        int cDay=calendar.get(Calendar.DAY_OF_MONTH);
        int cHour=calendar.get(Calendar.HOUR_OF_DAY);
        int cMinute=calendar.get(Calendar.MINUTE);

        List<subjects> subjectsList;
        subjects subjects = null;
        subjectsList = new ArrayList<subjects>();
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.rawQuery("SELECT rTaskName,rDate,rTime FROM Task ORDER BY rDATE DESC,rTIME",null);
        cursor.moveToFirst();
        flag=true;

        while (!cursor.isAfterLast()) {
            subjects = new subjects();
            String tDate=cursor.getString(1);
            String[] separated = tDate.split("-");
            int qYear= Integer.parseInt(separated[0]);
            int qMonth= Integer.parseInt(separated[1]);
            int qDay= Integer.parseInt(separated[2]);
            if (cYear<=qYear && cMonth<=qMonth && cDay<=qDay)
            {
                String title= cursor.getString(0);
                subjects.sTitle=title;
                String time1= qDay+" "+MONTHS[qMonth]+" "+qYear;
                subjects.sDate=time1;
                String qTime= cursor.getString(2);
                String[] separated2 = qTime.split(":");
                int selectedHour= Integer.parseInt(separated2[0]);
                int qHour=selectedHour;
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
                fTime1=selectedHour+"|"+selectedMinute+"|"+qDay+"|"+qMonth+"|"+qYear;
                fTime = prefs.getString("fTime", "abc");

                fHour=prefs.getInt("fHour", cHour);
                fMinute=prefs.getInt("fMinute", cMinute);
                fDay=prefs.getInt("fDay", cDay);
                fMonth=prefs.getInt("fMonth", cMonth);
                fYear=prefs.getInt("fYear", cYear);

                if (flag && cHour<=qHour && cMinute<=selectedMinute )
                {
                    // Toast.makeText(mcontext, fHour+":"+fMinute, Toast.LENGTH_SHORT).show();

                    if (fHour>=qHour && fMinute>selectedMinute && fDay>=qDay && fMonth>=qMonth && fYear>=qYear)
                    {
                        //   Toast.makeText(mcontext, "set time = " + selectedHour + ":" + selectedMinute, Toast.LENGTH_SHORT).show();
                        calendar2.set(qYear, qMonth, qDay, qHour, selectedMinute, 0);
                        setAlarm(calendar2.getTimeInMillis());
                        editor.putString("title", title);
                        editor.putString("time", time2 + " " + time1);
                        fTime2 = selectedHour + "|" + selectedMinute + "|" + qDay + "|" + qMonth + "|" + qYear;

                        editor.putInt("fHour", qHour);
                        editor.putInt("fMinute", selectedMinute);
                        editor.putInt("fDay", qDay);
                        editor.putInt("fMonth", qMonth);
                        editor.putInt("fYear", qYear);

                        editor.apply();
                        flag = false;
                    }

                    Tflag=prefs.getBoolean("Tflag", true);

                    if (Tflag)
                    {
                        //   Toast.makeText(mcontext, "set time = " + selectedHour + ":" + selectedMinute, Toast.LENGTH_SHORT).show();
                        calendar2.set(qYear, qMonth, qDay, qHour, selectedMinute, 0);
                        setAlarm(calendar2.getTimeInMillis());
                        editor.putString("title", title);
                        editor.putString("time", time2);
                        fTime2 = selectedHour + "|" + selectedMinute + "|" + qDay + "|" + qMonth + "|" + qYear;

                        editor.putInt("fHour", qHour);
                        editor.putInt("fMinute", selectedMinute);
                        editor.putInt("fDay", qDay);
                        editor.putInt("fMonth", qMonth);
                        editor.putInt("fYear", qYear);

                        editor.putBoolean("Tflag", false);

                        editor.apply();
                        flag = false;
                    }
                }
            }
            else {

            }

            cursor.moveToNext();
        }

        return subjectsList;
    }

    private void setAlarm(long timeInMillis)
    {
        AlarmManager alarmManager=(AlarmManager)mcontext.getSystemService(Context.ALARM_SERVICE);
        Intent intent=new Intent(mcontext,MyAlarm.class);
        PendingIntent pendingIntent=PendingIntent.getBroadcast(mcontext,0,intent,0);
        alarmManager.set(AlarmManager.RTC,timeInMillis,pendingIntent);
    }
}