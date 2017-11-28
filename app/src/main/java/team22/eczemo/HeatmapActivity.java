package team22.eczemo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.maps.android.heatmaps.Gradient;
import com.google.maps.android.heatmaps.HeatmapTileProvider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class HeatmapActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback {

    private static final String TAG = HeatmapActivity.class.getSimpleName();
    private GoogleMap mMap;
    private CameraPosition mCameraPosition;
    // The entry points to the Places API.
    private GeoDataClient mGeoDataClient;
    private PlaceDetectionClient mPlaceDetectionClient;

    // The entry point to the Fused Location Provider.
    private FusedLocationProviderClient mFusedLocationProviderClient;

    private final LatLng mDefaultLocation = new LatLng(1.3521, 103.8198);
    private static final int DEFAULT_ZOOM = 15;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean mLocationPermissionGranted;
    private Location mLastKnownLocation;
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";

    private HeatmapTileProvider mProvider;
    private TileOverlay mOverlay;
    private TileOverlay mOverlay_s1;
    private TileOverlay mOverlay_s2;
    private TileOverlay mOverlay_s3;
    private TileOverlay mOverlay_s4;
    private TileOverlay mOverlay_s5;
    private TileOverlay mOverlay_s6;
    private TileOverlay mOverlay_s7;
    private TileOverlay mOverlay_s8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            mLastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            mCameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
        }

        setContentView(R.layout.activity_heatmap);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Construct a GeoDataClient.
        mGeoDataClient = Places.getGeoDataClient(this, null);

        // Construct a PlaceDetectionClient.
        mPlaceDetectionClient = Places.getPlaceDetectionClient(this, null);

        // Construct a FusedLocationProviderClient.
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);


        final Switch s0 = (Switch) findViewById(R.id.switch0);
        final Switch s1 = (Switch) findViewById(R.id.switch1);
        final Switch s2= (Switch) findViewById(R.id.switch2);
        final Switch s3 = (Switch) findViewById(R.id.switch3);
        final Switch s4 = (Switch) findViewById(R.id.switch4);
        final Switch s5 = (Switch) findViewById(R.id.switch5);
        final Switch s6 = (Switch) findViewById(R.id.switch6);
        final Switch s7 = (Switch) findViewById(R.id.switch7);
        final Switch s8 = (Switch) findViewById(R.id.switch8);

        s0.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Create the gradient.
                int[] colors = {
                        Color.rgb(102, 225, 0), // green
                        Color.rgb(250, 220, 0), // yellow
                        Color.rgb(255, 0, 0)    // red
                };

                float[] startPoints = {
                        0.1f, 0.5f, 1f
                };

                Gradient gradient = new Gradient(colors, startPoints);

                if (isChecked) {
                    mOverlay = addHeatMap(R.raw.dataset, gradient);
                    s1.setChecked(false);
                    s2.setChecked(false);
                    s3.setChecked(false);
                    s4.setChecked(false);
                    s5.setChecked(false);
                    s6.setChecked(false);
                    s7.setChecked(false);
                    s8.setChecked(false);
                } else {
                    mOverlay.remove();
                }
            }
        });

        s1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Create the gradient.
                int[] colors = {
                        Color.rgb(96, 187, 255),
                        Color.rgb(0, 115, 255)
                };

                float[] startPoints = {
                        0.3f, 1f
                };

                Gradient gradient = new Gradient(colors, startPoints);

                if (isChecked) {
                    mOverlay_s1 = addHeatMap(R.raw.s1, gradient);
                    s0.setChecked(false);
                } else {
                    mOverlay_s1.remove();
                }
            }
        });

        s2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Create the gradient.
                int[] colors = {
                        Color.rgb(102, 225, 0), // green
                        Color.rgb(255, 0, 0)    // red
                };

                float[] startPoints = {
                        0.2f, 1f
                };

                Gradient gradient = new Gradient(colors, startPoints);

                if (isChecked) {
                    mOverlay_s2 = addHeatMap(R.raw.s2, gradient);
                    s0.setChecked(false);
                } else {
                    mOverlay_s2.remove();
                }
            }
        });

        s3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Create the gradient.
                int[] colors = {
                        Color.rgb(102, 225, 0), // green
                        Color.rgb(255, 0, 0)    // red
                };

                float[] startPoints = {
                        0.2f, 1f
                };

                Gradient gradient = new Gradient(colors, startPoints);

                if (isChecked) {
                    mOverlay_s3 = addHeatMap(R.raw.s3, gradient);
                    s0.setChecked(false);
                } else {
                    mOverlay_s3.remove();
                }
            }
        });

        s4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Create the gradient.
                int[] colors = {
                        Color.rgb(102, 225, 0), // green
                        Color.rgb(255, 0, 0)    // red
                };

                float[] startPoints = {
                        0.2f, 1f
                };

                Gradient gradient = new Gradient(colors, startPoints);

                if (isChecked) {
                    mOverlay_s4 = addHeatMap(R.raw.s4, gradient);
                    s0.setChecked(false);
                } else {
                    mOverlay_s4.remove();
                }
            }
        });

        s5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Create the gradient.
                int[] colors = {
                        Color.rgb(102, 225, 0), // green
                        Color.rgb(255, 0, 0)    // red
                };

                float[] startPoints = {
                        0.2f, 1f
                };

                Gradient gradient = new Gradient(colors, startPoints);

                if (isChecked) {
                    mOverlay_s5 = addHeatMap(R.raw.s5, gradient);
                    s0.setChecked(false);
                } else {
                    mOverlay_s5.remove();
                }
            }
        });

        s6.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Create the gradient.
                int[] colors = {
                        Color.rgb(102, 225, 0), // green
                        Color.rgb(255, 0, 0)    // red
                };

                float[] startPoints = {
                        0.2f, 1f
                };

                Gradient gradient = new Gradient(colors, startPoints);

                if (isChecked) {
                    mOverlay_s6 = addHeatMap(R.raw.s6, gradient);
                    s0.setChecked(false);
                } else {
                    mOverlay_s6.remove();
                }
            }
        });

        s7.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Create the gradient.
                int[] colors = {
                        Color.rgb(102, 225, 0), // green
                        Color.rgb(255, 0, 0)    // red
                };

                float[] startPoints = {
                        0.2f, 1f
                };

                Gradient gradient = new Gradient(colors, startPoints);

                if (isChecked) {
                    mOverlay_s7 = addHeatMap(R.raw.s7, gradient);
                    s0.setChecked(false);
                } else {
                    mOverlay_s7.remove();
                }
            }
        });

        s8.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Create the gradient.
                int[] colors = {
                        Color.rgb(102, 225, 0), // green
                        Color.rgb(255, 0, 0)    // red
                };

                float[] startPoints = {
                        0.2f, 1f
                };

                Gradient gradient = new Gradient(colors, startPoints);

                if (isChecked) {
                    mOverlay_s8 = addHeatMap(R.raw.s8, gradient);
                    s0.setChecked(false);
                } else {
                    mOverlay_s8.remove();
                }
            }
        });
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
            startActivity(new Intent(HeatmapActivity.this, MainActivity.class));
        } else if (id == R.id.nav_heatmap) {
            //startActivity(new Intent(MainActivity.this, HeatmapActivity.class));
        } else if (id == R.id.nav_calendar) {
            startActivity(new Intent(HeatmapActivity.this, viewCalenActivity.class));
        } else if (id == R.id.nav_addRec) {
            startActivity(new Intent(HeatmapActivity.this, newRecord.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // Add a marker in Sydney, Australia,
        // and move the map's camera to the same location.
        /*LatLng sydney = new LatLng(-33.852, 151.211);
        googleMap.addMarker(new MarkerOptions().position(sydney)
                .title("Marker in Sydney"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));*/
        mMap = googleMap;

        mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener(){
            @Override
            public boolean onMyLocationButtonClick() {


                return false;
            }
        });

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {

                //{"lat" : 1.3576457690427843, "lng" : 103.75096892472357 } ,
                //System.out.println("{\"lat\" : " + latLng.latitude + ", \"lng\" : " + latLng.longitude + " } ,");
            }
        });



        // Do other setup activities here too, as described elsewhere in this tutorial.

        // Turn on the My Location layer and the related control on the map.
        updateLocationUI();

        // Get the current location of the device and set the position of the map.
        getDeviceLocation();

        Switch defaultSwitch = (Switch) findViewById(R.id.switch0);
        defaultSwitch.setChecked(true);
    }

    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            if (mLocationPermissionGranted) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                mLastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
        updateLocationUI();
    }

    /**
     * Handles the result of the request for location permissions.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
        updateLocationUI();
    }

    private void getDeviceLocation() {
    /*
     * Get the best and most recent location of the device, which may be null in rare
     * cases when a location is not available.
     */
        try {
            if (mLocationPermissionGranted) {
                Task locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            mLastKnownLocation = (Location) task.getResult();
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                    new LatLng(mLastKnownLocation.getLatitude(),
                                            mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                        } else {
                            Log.d(TAG, "Current location is null. Using defaults.");
                            Log.e(TAG, "Exception: %s", task.getException());
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
                            mMap.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
            }
        } catch(SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    private TileOverlay addHeatMap(int dataset, Gradient gradient) {
        List<LatLng> list = null;

        // Get the data: latitude/longitude positions of police stations.
        try {
            list = readItems(dataset);
        } catch (JSONException e) {
            Toast.makeText(this, "Problem reading list of locations.", Toast.LENGTH_LONG).show();
        }

        // Create a heat map tile provider, passing it the latlngs of the police stations.
        mProvider = new HeatmapTileProvider.Builder()
                .data(list)
                .gradient(gradient)
                .opacity(0.7)
                .build();
        // Add a tile overlay to the map, using the heat map tile provider.
        return mMap.addTileOverlay(new TileOverlayOptions().tileProvider(mProvider));
    }

    private ArrayList<LatLng> readItems(int resource) throws JSONException {
        ArrayList<LatLng> list = new ArrayList<LatLng>();
        InputStream inputStream = getResources().openRawResource(resource);
        String json = new Scanner(inputStream).useDelimiter("\\A").next();
        JSONArray array = new JSONArray(json);
        for (int i = 0; i < array.length(); i++) {
            JSONObject object = array.getJSONObject(i);
            double lat = object.getDouble("lat");
            double lng = object.getDouble("lng");
            list.add(new LatLng(lat, lng));
        }
        return list;
    }
}
