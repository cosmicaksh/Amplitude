package com.example.movies.movieshub;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import android.widget.RelativeLayout;
import android.widget.TextView;


public class MainActivity extends Activity {
    private RelativeLayout relativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button tx = (Button) findViewById(R.id.song2);
        TextView tx2 = (TextView) findViewById(R.id.textView);
        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/datalegreya-thin.ttf");
        tx2.setTypeface(custom_font);
        tx.setTypeface(custom_font);
    }

    public void onclick(View view) {
        Intent i = new Intent(MainActivity.this, SearchPage.class);
        startActivity(i);


    }
}
