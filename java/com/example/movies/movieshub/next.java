package com.example.movies.movieshub;

import android.app.Activity;
import android.app.ProgressDialog;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

import java.net.URL;


public class next extends Activity {
    List_View lv;
    ProgressDialog pDialog;
    String id1 = "";
    TextView tx, tx1, tx2, tx3, tx4;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.next_page);
        Bundle value1 = getIntent().getExtras();
        id1 = value1.getString("id");
        tx = (TextView) findViewById(R.id.moviename);
        tx1 = (TextView) findViewById(R.id.rating);
        tx2 = (TextView) findViewById(R.id.runtime);
        tx3 = (TextView) findViewById(R.id.genre);
        tx4 = (TextView) findViewById(R.id.summary);
        String strUrl = "https://api.themoviedb.org/3/movie/" + id1 + "?api_key=a744af2350915efbb6c162a4fb725e94";
        DownloadTask downloadTask = new DownloadTask();
        downloadTask.execute(strUrl);
    }

    public static String image = "";

    private String downloadUrl(String strUrl) throws IOException {

        String data = "";
        InputStream iStream = null;
        try {
            URL url = new URL(strUrl);
            // Creating an http connection to communicate with url
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();
            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));
            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            JSONObject jsonObject = new JSONObject(data);
            String path = jsonObject.getString("backdrop_path");
            image = ("https://image.tmdb.org/t/p/w640" + path);
            URL u = new URL(image);
            HttpURLConnection huc = (HttpURLConnection) u.openConnection();
            huc.setRequestMethod("GET");  //OR  huc.setRequestMethod ("HEAD");
            huc.connect();

            int code = huc.getResponseCode();
            if (code == 404) {
                image = "http://www.nextflowers.co.uk/assets/images/md/no-image.png";
            }


            br.close();

        } catch (Exception e) {

        } finally {
            iStream.close();
        }

        return data;
    }


    public class DownloadTask extends AsyncTask<String, Integer, String> {
        String data = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(next.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }


        @Override
        public String doInBackground(String... url) {
            try {
                data = downloadUrl(url[0]);
            } catch (Exception e) {

            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {

            Information info = new Information();
            info.execute(result);


        }
    }

    public String imdbs = null;

    public void startimdb(View view) {
        gotourl(imdbs);
    }

    private void gotourl(String url) {
        Uri uriUrl=Uri.parse(url);
        Intent launchbrowser=new Intent(Intent.ACTION_VIEW,uriUrl);
        startActivity(launchbrowser);
    }


public class Information {
    public void execute(String data) {
        String title = "";
        String runtime = "";
        String rating = "";
        String genre1 = "";
        String genre2 = "";
        String path = "";
        String summary = "";
        String imdb="";

        try {
            JSONObject jsonObject = new JSONObject(data);
            title = jsonObject.getString("original_title");
            rating = jsonObject.getString("vote_average");
            runtime = jsonObject.getString("runtime");

            summary = jsonObject.getString("overview");



            JSONArray jsonArray = jsonObject.getJSONArray("genres");
            JSONObject getgenre = jsonArray.getJSONObject(0);
            genre1 = getgenre.getString("name");
            JSONObject getgenre2 = jsonArray.getJSONObject(1);
            genre2 = getgenre2.getString("name");
            imdb=jsonObject.getString("imdb_id");
            imdbs="http://www.imdb.com/title/"+imdb;

        }
        catch (JSONException e) {
        }



            tx.setText(title);

            tx1.setText("Rating(out of 10) : " + rating );

            tx2.setText("Runtime : " + runtime + " minutes");

            tx3.setText("Genre : " + genre1 + "," + genre2);

            tx4.setText(summary);
            new DownloadImageTask((ImageView) findViewById(R.id.poster)).execute(image);


        }

        }




    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
            ImageView bmImage;

            public DownloadImageTask(ImageView bmImage) {
                this.bmImage = bmImage;
            }



            protected Bitmap doInBackground(String... urls) {
                String urldisplay = urls[0];
                Bitmap mIcon11 = null;
                Bitmap resized=null;
                try {
                    InputStream in = new java.net.URL(urldisplay).openStream();
                    mIcon11 = BitmapFactory.decodeStream(in);
                    resized=Bitmap.createScaledBitmap(mIcon11,(int)(mIcon11.getWidth()*1),(int)(mIcon11.getHeight()*1.2),true);
                } catch (Exception e) {

                }
                return resized;
            }

            protected void onPostExecute(Bitmap result) {
                bmImage.setImageBitmap(result);
                if (pDialog.isShowing()) ;
                {
                    pDialog.dismiss();
                }
            }
        }
    }

