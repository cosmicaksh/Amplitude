package com.example.movies.movieshub;
import java.io.IOException;
import java.net.HttpURLConnection;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class MovieJSONParser {


    // Receives a JSONObject and returns a list
    public List<HashMap<String, Object>> parse(JSONObject jObject) throws JSONException {




        JSONArray jMovies = null;
        try {
            // Retrieves all the elements in the 'countries' array
            jMovies = jObject.getJSONArray("results");
        } catch (JSONException e) {

        }

        // Invoking getCountries with the array of json object
        // where each json object represent a country
        return getMovies(jMovies);
    }

    String id;
    private List<HashMap<String, Object>> getMovies(JSONArray jMovies) throws NullPointerException {
        int movieCount = jMovies.length();
        List<HashMap<String, Object>> movieList = new ArrayList<HashMap<String, Object>>();
        HashMap<String, Object> movie = null;


        // Taking each movie, parses and adds to list object
        for (int i = 0; i < movieCount; i++) {

            try {
                JSONObject getid=(JSONObject) jMovies.get(i);

                movie = getMovie(getid);
                movieList.add(movie);



            } catch (JSONException e) {
                e.printStackTrace();

            }


        }

        return movieList;
    }



    public static String image = "";
    // Parsing the Country JSON object
    private HashMap<String, Object> getMovie(JSONObject jMovie) {

        HashMap<String, Object> movie = new HashMap<String, Object>();
        String title = "";

        String date = "";



        try {
                title = jMovie.getString("title");
                String path = jMovie.getString("poster_path");
                date = jMovie.getString("release_date");

                image = ("https://image.tmdb.org/t/p/w640" + path);


                URL u = new URL(image);
                HttpURLConnection huc = (HttpURLConnection) u.openConnection();
                huc.setRequestMethod("GET");  //OR  huc.setRequestMethod ("HEAD");
                huc.connect();

                int code = huc.getResponseCode();
                if (code == 404) {
                    image = "http://www.nextflowers.co.uk/assets/images/md/no-image.png";
                }
            }catch(IOException e){

            }catch(JSONException e){

            }
            movie.put("Movie", "Title : " + title);
            movie.put("poster", R.drawable.blank);
            movie.put("poster_path", image);
            movie.put("date", "Released on : " + date);



            return movie;
        }
    }

