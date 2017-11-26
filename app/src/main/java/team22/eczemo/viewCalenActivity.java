package team22.eczemo;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.res.ResourcesCompat;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Scanner;

public class viewCalenActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    Drawable image = null;
    HashMap<Date, Drawable> tempList = new HashMap<Date,Drawable>();
    Date convDate= null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_calen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        final CaldroidFragment caldroidFragment = new CaldroidFragment();
        Bundle args = new Bundle();
        Calendar cal = Calendar.getInstance();
        args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
        args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
        //args.putInt(CaldroidFragment.THEME_RESOURCE, com.caldroid.R.style.CaldroidDefaultDark);
        caldroidFragment.setArguments(args);

        FragmentTransaction calenReplace = getSupportFragmentManager().beginTransaction();
        calenReplace.replace(R.id.calendarView, caldroidFragment);
        calenReplace.commit();
        Date curDate = Calendar.getInstance().getTime();
        caldroidFragment.setMinDate(curDate);
        SimpleDateFormat simpledateformat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zz yyyy");
        ParsePosition posit = new ParsePosition(0);

        try {
            InputStream inputStream = getResources().openRawResource(R.raw.dates_data);
            String json = new Scanner(inputStream).useDelimiter("\\A").next();
            JSONArray array = new JSONArray(json);

            for (int i = 0; i < array.length(); i++) {
                JSONObject object = array.getJSONObject(i);
                String tempDate = object.getString("date");
                convDate = simpledateformat.parse(tempDate);
                int rating = object.getInt("rate");
                Log.i("View Calendar Activity","got: "+rating+" date: "+tempDate );
                if(rating == 1){
                    image = ResourcesCompat.getDrawable(getResources(), R.drawable.rate1,null);
                    Log.i("View Calendar Activity","went in: "+rating+" "+convDate );
                }else if(rating == 2){
                    image = ResourcesCompat.getDrawable(getResources(), R.drawable.rate2,null);
                    Log.i("View Calendar Activity","went in: "+rating+" "+convDate );
                }else if(rating == 3){
                    image = ResourcesCompat.getDrawable(getResources(), R.drawable.rate3,null);
                    Log.i("View Calendar Activity","went in: "+rating+" "+convDate );
                }else if(rating == 4){
                    image = ResourcesCompat.getDrawable(getResources(), R.drawable.rate4,null);
                    Log.i("View Calendar Activity","went in: "+rating+" "+convDate );
                }else if(rating == 5){
                    image = ResourcesCompat.getDrawable(getResources(), R.drawable.rate5,null);
                    Log.i("View Calendar Activity","went in: "+rating+" "+convDate );
                }

                image.setAlpha(100);
                if(convDate != null) {
                    Log.i("View Calendar Activity","went in to list");
                    tempList.put(convDate, image);
                }
                try {
                    caldroidFragment.setBackgroundDrawableForDates(tempList);
                }catch(RuntimeException ex){
                    Log.i("View Calendar Activity","error: "+ex+ " size: "+tempList.size() );
                }
            }
        } catch (JSONException e) {
            Toast.makeText(this, "Problem reading list of locations.", Toast.LENGTH_LONG).show();
        } catch (ParseException e) {
            Log.i("View Calendar Activity","error: "+e );
        }
        Log.i("View Calendar Activity","list: "+tempList );

        final CaldroidListener listener = new CaldroidListener() {

            @Override
            public void onSelectDate(Date date, View view) {
                Log.i("View Calendar Activity",date.toString());
            }

            @Override
            public void onChangeMonth(int month, int year) {

            }

            @Override
            public void onLongClickDate(Date date, View view) {

            }

            @Override
            public void onCaldroidViewCreated() {

            }

        };

        caldroidFragment.setCaldroidListener(listener);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.view_calen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            startActivity(new Intent(viewCalenActivity.this, MainActivity.class));
        } else if (id == R.id.nav_heatmap) {
            startActivity(new Intent(viewCalenActivity.this, HeatmapActivity.class));
        } else if (id == R.id.nav_calendar) {
            startActivity(new Intent(viewCalenActivity.this, viewCalenActivity.class));
        } else if (id == R.id.nav_statistics) {

        } else if (id == R.id.nav_addRec) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
