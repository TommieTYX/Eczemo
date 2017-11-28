package team22.eczemo;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
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
import android.widget.TextView;
import android.widget.Toast;

import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

public class viewCalenActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    Drawable image = null;
    Map<Date, Drawable> tempList = new TreeMap<Date,Drawable>();
    Map<Date, ArrayList<String>> jsonList = new TreeMap<>();
    Date convDate= null;
    TextView humid,humidText,icon,iconText,tempView;
    Typeface weatherFont;

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
        args.putBoolean(CaldroidFragment.ENABLE_CLICK_ON_DISABLED_DATES, true);
        //args.putInt(CaldroidFragment.THEME_RESOURCE, com.caldroid.R.style.CaldroidDefaultDark);
        caldroidFragment.setArguments(args);

        FragmentTransaction calenReplace = getSupportFragmentManager().beginTransaction();
        calenReplace.replace(R.id.calendarView, caldroidFragment);
        calenReplace.commit();
        Date curDate = Calendar.getInstance().getTime();
        caldroidFragment.setMinDate(curDate);
        SimpleDateFormat simpledateformat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zz yyyy");

        try {
            InputStream inputStream = getResources().openRawResource(R.raw.dates_data);
            String json = new Scanner(inputStream).useDelimiter("\\A").next();
            JSONArray array = new JSONArray(json);

            for (int i = 0; i < array.length(); i++) {
                JSONObject object = array.getJSONObject(i);
                String tempDate = object.getString("date");
                convDate = simpledateformat.parse(tempDate);
                ArrayList<String> tempJsList = new ArrayList<String>();
                int rating = object.getInt("rate");
                String humid = object.getString("humid");
                String tempr = object.getString("tempr");
                String weather = object.getString("weather");

                Log.i("View Calendar Activity","got: "+rating+" date: "+tempDate );
                if (rating == 1) {
                    image = ResourcesCompat.getDrawable(getResources(), R.drawable.rate1, null);
                    Log.i("View Calendar Activity", "went in: " + rating + " " + convDate);
                } else if (rating == 2) {
                    image = ResourcesCompat.getDrawable(getResources(), R.drawable.rate2, null);
                    Log.i("View Calendar Activity", "went in: " + rating + " " + convDate);
                } else if (rating == 3) {
                    image = ResourcesCompat.getDrawable(getResources(), R.drawable.rate3, null);
                    Log.i("View Calendar Activity", "went in: " + rating + " " + convDate);
                } else if (rating == 4) {
                    image = ResourcesCompat.getDrawable(getResources(), R.drawable.rate4, null);
                    Log.i("View Calendar Activity", "went in: " + rating + " " + convDate);
                } else if (rating == 5) {
                    image = ResourcesCompat.getDrawable(getResources(), R.drawable.rate5, null);
                    Log.i("View Calendar Activity", "went in: " + rating + " " + convDate);
                }

                image.setAlpha(100);
                if(convDate != null) {
                    Log.i("View Calendar Activity","went in to list");
                    tempList.put(convDate, image);
                    tempJsList.addAll(Arrays.asList(String.valueOf(rating),humid,tempr,weather));
                    jsonList.put(convDate,tempJsList);
                }
                try {
                    caldroidFragment.setBackgroundDrawableForDates(tempList);
                }catch(RuntimeException ex){
                    Log.i("View Calendar Activity","error: "+ex+ " size: "+tempList.size() );
                }
            }
        } catch (JSONException e) {
            Toast.makeText(this, "Problem retrieving dates out", Toast.LENGTH_LONG).show();
        } catch (ParseException e) {
            Log.i("View Calendar Activity","error: "+e );
        }catch(NullPointerException e){
            Log.i("View Calendar Activity","error: "+e );
        }
        Log.i("View Calendar Activity","list: "+tempList );

        final CaldroidListener listener = new CaldroidListener() {

            @Override
            public void onSelectDate(Date date, View view) {
                Log.i("View Calendar Activity",date.toString());
                for (final Map.Entry<Date, ArrayList<String>> entry : jsonList.entrySet()) {
                    Date tempDate = entry.getKey();
                    if(tempDate.getDate() == date.getDate()){
                        weatherFont = Typeface.createFromAsset(getAssets(), "fonts/weather.ttf");
                        icon = (TextView) findViewById(R.id.iconView);
                        iconText = (TextView) findViewById(R.id.iconTextView);
                        icon.setTypeface(weatherFont);
                        ArrayList<String> tempoList = entry.getValue();

                        humid = (TextView) findViewById(R.id.HumidView);
                        humid.setText(tempoList.get(1)+"%");

                        humidText =(TextView) findViewById(R.id.HumidTextView);
                        humidText.setText("Humidity");

                        tempView = (TextView) findViewById(R.id.tempView);
                        tempView.setText(tempoList.get(2)+" ℃");

                        try {
                            setWeatherIcon(Integer.parseInt(tempoList.get(3).toString()));
                            System.out.println(tempoList.get(0)+" "+tempoList.get(1)+" "+tempoList.get(2)+" "+tempoList.get(3));
                        }catch(NullPointerException e){
                            Log.i("View Calendar Activity",Integer.parseInt(tempoList.get(3))+"error: "+e );
                        }
                    }
                }
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

    private void setWeatherIcon(int actualId){
        Log.i("weatherFrag","started setting icon");
        String iconDetail = "";
        String weather="";
        if(actualId == 800){
                iconDetail = this.getString(R.string.weather_sunny);
                weather = "Sunny Day";
                icon.setTextColor(Color.parseColor("#FFA500"));
                iconText.setTextColor(Color.parseColor("#FFA500"));
        } else {
            switch(actualId) {
                case 2 :
                    iconDetail = this.getString(R.string.weather_thunder);
                    weather = "thunder Storm";
                    icon.setTextColor(Color.BLACK);
                    iconText.setTextColor(Color.BLACK);
                    break;
                case 3 :
                    iconDetail = this.getString(R.string.weather_drizzle);
                    weather = "Drizzling Rain";
                    icon.setTextColor(Color.parseColor("#d1d6ea"));
                    iconText.setTextColor(Color.parseColor("#d1d6ea"));
                    break;
                case 7 :
                    iconDetail = this.getString(R.string.weather_foggy);
                    weather = "Foggy Day";
                    icon.setTextColor(Color.parseColor("#93b0b2"));
                    iconText.setTextColor(Color.parseColor("#93b0b2"));
                    break;
                case 8 :
                    iconDetail = this.getString(R.string.weather_cloudy);
                    weather = "Cloudy Day";
                    icon.setTextColor(Color.parseColor("#87ceeb"));
                    iconText.setTextColor(Color.parseColor("#87ceeb"));
                    break;
                case 6 :
                    iconDetail = this.getString(R.string.weather_snowy);
                    weather = "Snowy Day";
                    icon.setTextColor(Color.parseColor("#fffafa"));
                    iconText.setTextColor(Color.parseColor("#fffafa"));
                    break;
                case 5 :
                    iconDetail = this.getString(R.string.weather_rainy);
                    weather = "Shower Rain";
                    icon.setTextColor(Color.parseColor("#4a6583"));
                    iconText.setTextColor(Color.parseColor("#4a6583"));
                    break;
            }
        }
        icon.setText(iconDetail);
        iconText.setText(weather);
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
        } else if (id == R.id.nav_addRec) {
            startActivity(new Intent(viewCalenActivity.this, newRecord.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
