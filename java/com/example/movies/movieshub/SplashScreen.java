package com.example.movies.movieshub;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import com.example.movies.movieshub.MainActivity;
import com.example.movies.movieshub.R;


/**
 * Created by Akshit on 6/20/2017.
 */

public class SplashScreen extends Activity {

    // Splash screen timer
    private static int SPLASH_TIME_OUT = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        TextView tx=(TextView)findViewById(R.id.textView2);
        Typeface custom_font1= Typeface.createFromAsset(getAssets(),"fonts/nemoylight.ttf");
        tx.setTypeface(custom_font1);


        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                Intent i = new Intent(SplashScreen.this,MainActivity.class);
                startActivity(i);


                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);
    }

}
