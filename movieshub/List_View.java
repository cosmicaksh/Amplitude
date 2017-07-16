package com.example.movies.movieshub;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

import java.net.URL;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import android.app.Activity;
import android.app.ProgressDialog;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;


import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;


public class List_View extends Activity implements AdapterView.OnItemClickListener {
    ProgressDialog pDialog;
    MovieJSONParser mj = new MovieJSONParser();
    ListView lv;
   TextView tx;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_view);
        tx=(TextView)findViewById(R.id.no);

        Bundle value1 = getIntent().getExtras();
        String value = value1.getString("url");




        String strUrl = ("https://api.themoviedb.org/3/search/movie?api_key=a744af2350915efbb6c162a4fb725e94&query=" + value);



        DownloadTask downloadTask = new DownloadTask();

        downloadTask.execute(strUrl);

        // Getting a reference to ListView of activity_main
        lv = (ListView) findViewById(R.id.listview);
        lv.setOnItemClickListener(this);
    }
    public void onItemClick(AdapterView<?> l, View v, int position, long id) {
        Intent intent = new Intent(this,next.class);

            String id1 = ids.get(position);
            intent.putExtra("id",id1);
            startActivity(intent);}









    ArrayList<String>ids=new ArrayList<String>();
    ArrayList<String>imdb=new ArrayList<String>();
    /** A method to download json data from url */
    private String downloadUrl(String strUrl) throws IOException{


        String data = "";
        InputStream iStream = null;
        try{
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();




            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb  = new StringBuffer();

            String line = "";
            while( ( line = br.readLine())  != null){
                sb.append(line);
            }

            data = sb.toString();

            try {
                JSONObject jsonObj = new JSONObject(data);


                int reso = Integer.parseInt(jsonObj.getString("total_results"));
                if (reso == 0) {
                    if (pDialog.isShowing());
                    {
                        pDialog.dismiss();
                    }
                    tx.setText("No Results Found!!");

                    }
                else {
                    JSONArray jsonArray=jsonObj.getJSONArray("results");
                int max=jsonArray.length();
                for(int i=0;i<max;i++) {
                    JSONObject getid = (JSONObject) jsonArray.get(i);
                    String id = getid.getString("id");
                    ids.add(id);


                }
                }
            }catch (final JSONException e) {

            }


            br.close();

        }catch(Exception e){

        }finally{
            iStream.close();
        }

        return data;
    }

    /** AsyncTask to download json data */
    public class DownloadTask extends AsyncTask<String, Integer, String>{
        String data = null;
        @Override
protected void onPreExecute() {
    super.onPreExecute();
    pDialog = new ProgressDialog(List_View.this);
    pDialog.setMessage("Please wait...");
    pDialog.setCancelable(false);
    pDialog.show();
}


    @Override
        public String doInBackground(String... url) {
            try{
                data = downloadUrl(url[0]);
            }catch(Exception e){

            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {

            // The parsing of the xml data is done in a non-ui thread
            ListViewLoaderTask listViewLoaderTask = new ListViewLoaderTask();

            // Start parsing xml data
            listViewLoaderTask.execute(result);


        }
    }

    /** AsyncTask to parse json data and load ListView */
    public class ListViewLoaderTask extends AsyncTask<String, Void, SimpleAdapter>{
int id;
        JSONObject jObject;
        // Doing the parsing of xml data in a non-ui thread
        @Override
        protected SimpleAdapter doInBackground(String... strJson) {
            try{

                jObject = new JSONObject(strJson[0]);

                MovieJSONParser movieJsonParser = new MovieJSONParser();


              movieJsonParser.parse(jObject);

            }catch(Exception e){

            }

            // Instantiating json parser class
            MovieJSONParser movieJsonParser = new MovieJSONParser();

            // A list object to store the parsed movies list
            List<HashMap<String, Object>> movies = null;

            try{
                // Getting the parsed data as a List construct
                movies = movieJsonParser.parse(jObject);

            }catch(Exception e){

            }

            // Keys used in Hashmap
            String[] from = { "Movie","poster","date"};

            // Ids of views in listview_layout/
            int[] to = { R.id.moviename,R.id.preview,R.id.year};



            // Instantiating an adapter to store each items
            // R.layout.listview_layout defines the layout of each item
            SimpleAdapter adapter = new SimpleAdapter(getBaseContext(), movies, R.layout.listview_layout, from, to);

            return adapter;
        }

        /** Invoked by the Android on "doInBackground" is executed */
        @Override
        protected void onPostExecute(SimpleAdapter adapter) {


int max;
            // Setting adapter for the listview
            lv.setAdapter(adapter);
            max=adapter.getCount();


            for(int i=0;i<max;i++){
                HashMap<String, Object> hm = (HashMap<String, Object>) adapter.getItem(i);

                String imgUrl = (String) hm.get("poster_path");
                ImageLoaderTask imageLoaderTask = new ImageLoaderTask();

                HashMap<String, Object> hmDownload = new HashMap<String, Object>();
                hm.put("poster_path",imgUrl);
                hm.put("position", i);

                // Starting ImageLoaderTask to download and populate image in the listview
                imageLoaderTask.execute(hm);
                if (pDialog.isShowing());{
                    pDialog.dismiss();}



            }
        }
    }



    /** AsyncTask to download and load an image in ListView */
    private class ImageLoaderTask extends AsyncTask<HashMap<String, Object>, Void, HashMap<String, Object>>{

        @Override
        protected HashMap<String, Object> doInBackground(HashMap<String, Object>... hm) {

            InputStream iStream=null;
            String imgUrl = (String) hm[0].get("poster_path");
            int position = (Integer) hm[0].get("position");

            URL url;
            try {
                url = new URL(imgUrl);

                // Creating an http connection to communicate with url
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                // Connecting to url
                urlConnection.connect();

                // Reading data from url
                iStream = urlConnection.getInputStream();

                // Getting Caching directory
                File cacheDirectory = getBaseContext().getCacheDir();

                // Temporary file to store the downloaded image
                File tmpFile = new File(cacheDirectory.getPath() + "/wpta_"+position+".png");

                // The FileOutputStream to the temporary file
                FileOutputStream fOutStream = new FileOutputStream(tmpFile);

                // Creating a bitmap from the downloaded inputstream
                Bitmap b = BitmapFactory.decodeStream(iStream);
                Bitmap resized=Bitmap.createScaledBitmap(b,(int)(b.getWidth()*1.2),(int)(b.getHeight()*0.8),true);

                // Writing the bitmap to the temporary file as png file
                {
                    resized.compress(Bitmap.CompressFormat.PNG,0,fOutStream);

                // Flush the FileOutputStream
                fOutStream.flush();

                //Close the FileOutputStream
                fOutStream.close();}

                // Create a hashmap object to store image path and its position in the listview
                HashMap<String, Object> hmBitmap = new HashMap<String, Object>();

                // Storing the path to the temporary image file
                hmBitmap.put("poster",tmpFile.getPath());

                // Storing the position of the image in the listview
                hmBitmap.put("position",position);

                // Returning the HashMap object containing the image path and position
                return hmBitmap;

            }catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(HashMap<String, Object> result) {
            // Getting the path to the downloaded image
            String path = (String) result.get("poster");

            // Getting the position of the downloaded image
            int position = (Integer) result.get("position");

            // Getting adapter of the listview
            SimpleAdapter adapter = (SimpleAdapter ) lv.getAdapter();

            // Getting the hashmap object at the specified position of the listview
            HashMap<String, Object> hm = (HashMap<String, Object>) adapter.getItem(position);

            // Overwriting the existing path in the adapter
            hm.put("poster",path);

            // Noticing listview about the dataset changes
            adapter.notifyDataSetChanged();
        }
    }


}