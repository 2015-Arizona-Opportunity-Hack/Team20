package com.nagirescue;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by chandanj on 9/24/2015.
 */
public class PlacesTask extends AsyncTask<Context, Void, String>{

    Context cxt;
    @Override
    protected String doInBackground(Context... context) {

        //store mainactivity context
        cxt = context[0];

        // For storing data from web service
        String data = "";

        // Obtain browser key from https://code.google.com/apis/console
        String key = "key=AIzaSyDRmC_ch44GZvqcw7hj44L0RpyzV0z9ibg";

        String input="";

       /* try {
           *//* input = "input=" + URLEncoder.encode(place[0], "utf-8");*//*
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }*/

        // place type to be searched
        String types = "types=geocode";

        // Sensor enabled
        String sensor = "sensor=false";

        // Building the parameters to the web service
        String parameters;
        if(AnimalServicesFragment.latitude==-1f ||  AnimalServicesFragment.longitude==-1f)
        {
            parameters = "location= 33.3061600,-111.8412500&radius=10000&types=veterinary_care&"+ key;
        }
        else
        {
            parameters= "location="+ AnimalServicesFragment.latitude+","+AnimalServicesFragment.longitude+"&radius=10000&types=veterinary_care&"+ key;

        }

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/"+output+"?"+parameters;

        try{
            // Fetching the data from we service
            data = downloadUrl(url);
        }catch(Exception e){
            Log.d("Background Task", e.toString());
        }
        return data;
    }


    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        // Creating ParserTask
        /*parserTask = new ParserTask();*/

        // Starting Parsing the JSON string returned by Web Service
       /* parserTask.execute(result);*/

        //parse the string
        try {
            JSONObject obj = new JSONObject(result);
            JSONArray array = obj.getJSONArray("results");
            final List<String> placeList = new ArrayList<String>();
            //create POJOs for the object
            for(int i=0;i<array.length();i++)
            {
                JSONObject arrObj = array.getJSONObject(i);
                String name= arrObj.getString("name");
                /*String vicinity = arrObj.getString("vicinity");
                JSONObject openNowObj =  arrObj.getJSONObject("opening_hours");
                String openNow = Boolean.toString(openNowObj.getBoolean("open_now"));*/
                placeList.add(name);
            }
            //update the UI here

            if(placeList.size()==0)
                placeList.add("No results found");
            ListAdapter adapter = new ArrayAdapter<String>(cxt,android.R.layout.simple_list_item_1, placeList);
            ((ListView)AnimalServicesFragment.rootView.findViewById(R.id.placesList)).setVisibility(View.VISIBLE);
            ((ListView)AnimalServicesFragment.rootView.findViewById(R.id.placesList)).setAdapter(adapter);
            ((ListView)AnimalServicesFragment.rootView.findViewById(R.id.placesList)).setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    String clickedPlace = placeList.get(i);
                }
            });


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    /** A method to download json data from url */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();


            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while( ( line = br.readLine()) != null){
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        }catch(Exception e){
            Log.d("Exception while downloading url", e.toString());
        }finally{
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }
}
