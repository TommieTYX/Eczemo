package team22.eczemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class newRecord extends AppCompatActivity {
    Button rate1,rate2,rate3,rate4,rate5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_record);
        rate1 = (Button)findViewById(R.id.button1);
        rate2 = (Button)findViewById(R.id.button2);
        rate3 = (Button)findViewById(R.id.button3);
        rate4 = (Button)findViewById(R.id.button4);
        rate5 = (Button)findViewById(R.id.button5);
        rate1.getBackground().setAlpha(90);
        rate2.getBackground().setAlpha(90);
        rate3.getBackground().setAlpha(90);
        rate4.getBackground().setAlpha(90);
        rate5.getBackground().setAlpha(90);

        if (savedInstanceState == null) {
            Log.i("newRecord","started going to fragment");
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new weatherFragment())
                    .commit();
        }

        Spinner dropdown = (Spinner)findViewById(R.id.spinner);
        String[] items = new String[]{"Atopic Dermatitis", "Contact Dermatitis", "Dyshidrotic Eczema","Hand Eczema","Neurodermatitis","Nummular Eczema","Seborrheic Dermatitis","Stasis Dermatitis"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);
    }

    public void onPressed(View curView) {
        try {
            rate1.getBackground().setAlpha(90);
            rate2.getBackground().setAlpha(90);
            rate3.getBackground().setAlpha(90);
            rate4.getBackground().setAlpha(90);
            rate5.getBackground().setAlpha(90);
        }catch(IllegalStateException ex){
            Log.i("newRecord","message: "+ex);
        }
      String idTag = curView.getTag().toString();
      Log.i("newRecord","entered: "+idTag);
        Button button;
       button = (Button) curView.findViewWithTag(idTag);
        if (idTag.equals("rate1")) {
            Log.i("newRecord","changed: "+idTag);
            button.getBackground().setAlpha(255);
        } else if (idTag.equals("rate2")) {
            Log.i("newRecord","changed: "+idTag);
            button.getBackground().setAlpha(255);
        } else if (idTag.equals("rate3")) {
            Log.i("newRecord","changed: "+idTag);
            button.getBackground().setAlpha(255);
        } else if (idTag.equals("rate4")) {
            Log.i("newRecord","changed: "+idTag);
            button.getBackground().setAlpha(255);
        } else if (idTag.equals("rate5")) {
            Log.i("newRecord","changed: "+idTag);
            button.getBackground().setAlpha(255);
        }
    }
}
