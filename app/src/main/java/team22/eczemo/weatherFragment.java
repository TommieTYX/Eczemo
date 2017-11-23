package team22.eczemo;

import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.Date;

public class weatherFragment extends Fragment {
    TextView humid, icon;
    private Handler handler;
    Typeface weatherFont;

    public weatherFragment(){
        handler = new Handler();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.i("weatherFrag","started createView");
        View recView = inflater.inflate(R.layout.fragment_weather, container, false);
        humid = (TextView)recView.findViewById(R.id.HumidView);
        icon = (TextView)recView.findViewById(R.id.iconView);
        icon.setTypeface(weatherFont);
        return recView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.i("weatherFrag","started fragment");
        weatherFont = Typeface.createFromAsset(getActivity().getAssets(), "fonts/weather.ttf");
        retrieveWeather("Singapore, SG");
    }

    private void retrieveWeather(final String city){
        Log.i("weatherFrag","started retrieving");
        new Thread(){
            public void run(){
                final JSONObject json = openWeathAPI.getJSON(getActivity(), city);
                if(json == null){
                    handler.post(new Runnable(){
                        public void run(){
                            Toast.makeText(getActivity(),"place not found", Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    handler.post(new Runnable(){
                        public void run(){
                            displayWeather(json);
                        }
                    });
                }
            }
        }.start();
    }

    private void displayWeather(JSONObject json){
        Log.i("weatherFrag","started displaying");
        try {
            JSONObject details = json.getJSONArray("weather").getJSONObject(0);
            JSONObject main = json.getJSONObject("main");
            humid.setText(main.getString("humidity") + "%\n"+"Humidity");

            //currentTemperatureField.setText(String.format("%.2f", (main.getDouble("temp")) - 273.15)+ " ℃");


            setWeatherIcon(details.getInt("id"),
                    json.getJSONObject("sys").getLong("sunrise") * 1000,
                    json.getJSONObject("sys").getLong("sunset") * 1000);

        }catch(Exception e){
            Log.e("SimpleWeather", "One or more fields not found in the JSON data");
        }
    }

    private void setWeatherIcon(int actualId, long sunrise, long sunset){
        Log.i("weatherFrag","started setting icon");
        int id = actualId / 100;
        String iconDetail = "";
        String weather="";
        if(actualId == 800){
            long currentTime = new Date().getTime();
            if(currentTime>=sunrise && currentTime<sunset) {
               // weather = 0x7f070033;
                iconDetail = getActivity().getString(R.string.weather_sunny);
                weather = "Sunny Day";
            } else {
                //weather = 0x7f07002d;
                iconDetail =getActivity().getString(R.string.weather_clear_night);
                weather = "Clear Night";
            }
        } else {
            switch(id) {
                case 2 : //weather = 0x7f070034;
                    iconDetail = getActivity().getString(R.string.weather_thunder);
                    weather = "thunder Storm";
                    break;
                case 3 :// weather = 0x7f07002f;
                    iconDetail = getActivity().getString(R.string.weather_drizzle);
                    weather = "Drizzling Rain";
                    break;
                case 7 :// weather = 0x7f070030;
                    iconDetail = getActivity().getString(R.string.weather_foggy);
                    weather = "Foggy Day";
                    break;
                case 8 :// weather = 0x7f07002e;
                    iconDetail = getActivity().getString(R.string.weather_cloudy);
                    weather = "Cloudy Day";
                    break;
                case 6 :// weather = 0x7f070032;
                    iconDetail = getActivity().getString(R.string.weather_snowy);
                    weather = "Snowy Day";
                    break;
                case 5 : //weather = 0x7f070031;
                    iconDetail = getActivity().getString(R.string.weather_rainy);
                    weather = "Shower Rain";
                    break;
            }
        }
        icon.setText(iconDetail+"\n"+weather);
    }
}
