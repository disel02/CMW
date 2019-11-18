package com.webnik.in.kanvamart;

import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences.Editor editor;
    Fragment fragment=null;
    FragmentManager fragmentManager;
    FragmentTransaction ft;
    Boolean exitkey,notification,adminKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SharedPreferences prefs = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        editor=getSharedPreferences(MyPREFERENCES,MODE_PRIVATE).edit();
        String username = prefs.getString("userkey", "name");
        notification = prefs.getBoolean("notification", false);
        adminKey = prefs.getBoolean("admin", false);

        fragmentManager=getSupportFragmentManager();
        ft=fragmentManager.beginTransaction();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View header = navigationView.getHeaderView(0);
        TextView tvname=(TextView)header.findViewById(R.id.tvname);
        final ImageView ivdp=(ImageView)header.findViewById(R.id.ivdp);
        tvname.setText(username);

        if (notification)
        {
            navigationView.getMenu().getItem(4).setChecked(true);
            editor.putBoolean("notification", false);
            editor.apply();
            fragment= new ListFragment();
            ft.add(R.id.screen_area,fragment,"Fragment One");
            ft.addToBackStack(null);
            ft.commit();
        }
        else
        {
            navigationView.getMenu().getItem(0).setChecked(true);
            fragment= new ShowRecordFragment();
            ft.add(R.id.screen_area,fragment,"Fragment One");
            ft.addToBackStack(null);
            ft.commit();
        }

        if (adminKey)
        {
            Menu menu =navigationView.getMenu();
            MenuItem target = menu.findItem(R.id.nav_viewuserlist);
            target.setVisible(true);
        }

        Picasso.get()
                .load("http://theextrastep.in/kanvamart/img/"+username+".jpg")
                .resize(200, 200)
                .centerCrop()
                .into(ivdp, new Callback() {
                    @Override
                    public void onSuccess() {
                        Bitmap imageBitmap = ((BitmapDrawable) ivdp.getDrawable()).getBitmap();
                        RoundedBitmapDrawable imageDrawable = RoundedBitmapDrawableFactory.create(getResources(), imageBitmap);
                        imageDrawable.setCircular(true);
                        imageDrawable.setCornerRadius(Math.max(imageBitmap.getWidth(), imageBitmap.getHeight()) / 2.0f);
                        ivdp.setImageDrawable(imageDrawable);
                    }

                    @Override
                    public void onError(Exception e) {
                        ivdp.setImageResource(R.mipmap.ic_launcher_round);
                    }
                });

    }

    private Boolean exit = false;
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
        }

        SharedPreferences prefs = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        exitkey = prefs.getBoolean("exit", false);

        int count = getSupportFragmentManager().getBackStackEntryCount();
      //  Toast.makeText(this, ""+count, Toast.LENGTH_SHORT).show();

        if (count == 1 ) {
          //  getFragmentManager().popBackStack();
            if (exit) {
                finish(); // finish activity
            } else {
                Toast.makeText(this, "Press Back again to Exit.", Toast.LENGTH_SHORT).show();
                exit = true;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        exit = false;
                    }
                }, 3 * 1000);
            }
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_logout) {
            SharedPreferences prefs = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("loginkey", "0");
            editor.putBoolean("admin", false);
            editor.commit();
            Intent intent=new Intent(MainActivity.this,LoginActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        fragmentManager=getSupportFragmentManager();
        ft=fragmentManager.beginTransaction();

        if (id==R.id.nav_home)
        {
            fragment= new ShowRecordFragment();
            ft.replace(R.id.screen_area,fragment,"Fragment One");
            ft.addToBackStack(null);
            ft.commit();
        }
        else if (id==R.id.nav_mon_record)
        {
            fragment= new MonthRecordFragment();
            ft.replace(R.id.screen_area,fragment,"Fragment One");
            ft.addToBackStack(null);
            ft.commit();
        }
        else if (id==R.id.nav_viewprofile)
        {
            fragment= new ViewProfileFragment();
            ft.replace(R.id.screen_area,fragment,"Fragment One");
            ft.addToBackStack(null);
            ft.commit();
        }
        else if (id==R.id.nav_year_record)
        {
            fragment= new YearRecordFragment();
            ft.replace(R.id.screen_area,fragment,"Fragment One");
            ft.addToBackStack(null);
            ft.commit();
        }
        else if (id == R.id.nav_calculator) {
            final ArrayList<HashMap<String,Object>> items =new ArrayList<HashMap<String,Object>>();

            final PackageManager pm = getPackageManager();
            List<PackageInfo> packs = pm.getInstalledPackages(0);
            for (PackageInfo pi : packs) {
                if( pi.packageName.toString().toLowerCase().contains("calcul")){
                    HashMap<String, Object> map = new HashMap<String, Object>();
                    map.put("appName", pi.applicationInfo.loadLabel(pm));
                    map.put("packageName", pi.packageName);
                    items.add(map);
                }
            }
            if(items.size()>=1){
                String packageName = (String) items.get(0).get("packageName");
                Intent i = pm.getLaunchIntentForPackage(packageName);
                if (i != null)
                    startActivity(i);
            }
            else{
                Toast.makeText(MainActivity.this, "app not found", Toast.LENGTH_SHORT).show();
            }
        } else if (id == R.id.nav_reminder) {
            fragment= new ListFragment();
            ft.replace(R.id.screen_area,fragment,"Fragment One");
            ft.addToBackStack(null);
            ft.commit();
        }
        else if (id == R.id.nav_notepad) {
            fragment= new ListFragment02();
            ft.replace(R.id.screen_area,fragment,"Fragment One");
            ft.addToBackStack(null);
            ft.commit();
        }
        else if (id == R.id.nav_viewuserlist) {
            fragment= new UserListFragment();
            ft.replace(R.id.screen_area,fragment,"Fragment One");
            ft.addToBackStack(null);
            ft.commit();
        }else if (id == R.id.nav_chpassword) {
            Intent intent=new Intent(MainActivity.this,ChpasswordActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
