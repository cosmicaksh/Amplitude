package com.example.movies.movieshub;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;




/**
 * Created by Akshit on 6/24/2017.
 */
public class SearchPage extends Activity {
    EditText edt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);
        TextView tx = (TextView) findViewById(R.id.searchmovie);
        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/datalegreya-thin.ttf");
        tx.setTypeface(custom_font);

    }

    public void onclick(View view)

    {
        edt = (EditText) findViewById(R.id.editText);
        String info ="";
        info = edt.getText().toString();
        if (info.equals("")) {
            Toast.makeText(SearchPage.this, "Please Enter Movie", Toast.LENGTH_LONG).show();
        }
            else{
                Intent i;
                i = new Intent(this, List_View.class);
                i.putExtra("url", info);
                startActivity(i);

            }
        }
    }








