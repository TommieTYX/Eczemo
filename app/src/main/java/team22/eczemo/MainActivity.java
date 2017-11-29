//Author: Chua Xuan Long

package team22.eczemo;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Set prediction image
        ImageView preImg = (ImageView) findViewById(R.id.predictionImg);
        preImg.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.rate2));

        //button for adding new record
        FloatingActionButton newRec = (FloatingActionButton) findViewById(R.id.newRec);
        newRec.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                     .setAction("Action", null).show();*/Intent intRec = new Intent(MainActivity.this, newRecord.class);
                Log.i("Main","entering new record page");
                startActivity(intRec);

            }
        });

        //Recommendation text box
        TextView treatmentDesc = (TextView) findViewById(R.id.textview_main);
        treatmentDesc.setMovementMethod(new ScrollingMovementMethod());
        treatmentDesc.append(
                "\u2022 If you need to clean your hands, wash them with lukewarm (not hot) water and fragrance-free cleanser.\n\n" +
                "\u2022 Gently blot hands dry, and apply a moisturizer immediately after you wash your hands.\n\n" +
                "\u2022 The most effective moisturizers are the ones with a higher oil content (like ointments and creams). Keep one near every sink in your home, so you don’t forget to apply it after washing your hands\n" +
                "\u2022 Avoid waterless, antibacterial cleansers, which often contain ingredients like alcohol and solvents that are very hard on your hands (especially during flare-ups).\n\n"
        );

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
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

        if (id == R.id.nav_heatmap) {
            startActivity(new Intent(MainActivity.this, HeatmapActivity.class));
        } else if (id == R.id.nav_calendar) {
            startActivity(new Intent(MainActivity.this, viewCalenActivity.class));
        } else if (id == R.id.nav_addRec) {
            startActivity(new Intent(MainActivity.this, newRecord.class));
        }else if (id == R.id.nav_stats) {
            startActivity(new Intent(MainActivity.this, statActivity.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
