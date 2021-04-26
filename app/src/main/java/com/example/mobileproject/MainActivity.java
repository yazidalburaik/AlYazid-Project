package com.example.mobileproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class MainActivity extends AppCompatActivity {

    RequestQueue rQueue;

    SharedPreferences sPreferences;

    ImageView weatherImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rQueue = Volley.newRequestQueue(this);


        Button bttn_Firebase=findViewById(R.id.bttn_goto_firebase);

        Button bttn_Weather=findViewById(R.id.bttn_goto_weather);

        Button bttn_SQL=findViewById(R.id.bttn_goto_sqlite);


        sPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        bttn_Firebase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, FirebaseMain.class));
            }
        });




        bttn_SQL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SQL.class));
            }
        });






        bttn_Weather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Weather.class));
            }
        });




    }

    @Override
    protected void onResume() {
        super.onResume();
        rQueue.add(Helper.weather(this));
    }




}