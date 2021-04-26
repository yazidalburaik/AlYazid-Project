package com.example.mobileproject;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class Weather extends AppCompatActivity {
//Weather Activity


    RequestQueue RQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        RQueue = Volley.newRequestQueue(this);


        RQueue.add(Helper.weather(this));

        SharedPreferences sp= PreferenceManager.getDefaultSharedPreferences(this);

        Button set_city=findViewById(R.id.bttn_City);

        EditText city=findViewById(R.id.edit_City);






        set_city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newCityString=city.getText()+"";
                if (newCityString.isEmpty()){
                    Toast.makeText(Weather.this,"Please insert a city name.",Toast.LENGTH_SHORT).show();
                    return;
                }
                SharedPreferences.Editor e=sp.edit();
                e.putString("WeatherCity",newCityString);
                e.commit();
                RQueue.add(Helper.weather(Weather.this));
            }
        });
        RQueue.add(Helper.weather(this));

    }

}