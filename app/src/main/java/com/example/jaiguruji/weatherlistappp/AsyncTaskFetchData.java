package com.example.jaiguruji.weatherlistappp;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by JaiGuruji on 09/02/17.
 */

public class AsyncTaskFetchData extends AsyncTask<String,Void,String> {
    Context mContext;
    ProgressDialog progressDialog;
    ArrayList<WeatherInfoItem> mWeatherInfoList;
    OnAsynctTaskComplete mInterface;

    public AsyncTaskFetchData(Context context,OnAsynctTaskComplete asynctTaskComplete) {
        mContext = context;
        mWeatherInfoList = new ArrayList<>();
        mInterface=asynctTaskComplete;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = ProgressDialog.show(mContext,null,"Fetching data..",true);
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            URL url = new URL(params[0]);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.connect();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            StringBuilder response_string = new StringBuilder();
            String line = "";
            while ((line = bufferedReader.readLine())!=null){
                response_string.append(line);
            }
            return response_string.toString();
        }catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        //Parse json
        parseJson(s);
        progressDialog.dismiss();
        Log.v("response",s);
    }

    void parseJson(String s){
        JSONObject response_json;
        try{
            response_json = new JSONObject(s);
            JSONArray response_array = response_json.getJSONArray("list");
            for(int i=0;i<response_array.length();i++){
                JSONObject json_city_info = response_array.getJSONObject(i);
                WeatherInfoItem infoItem = new WeatherInfoItem();
                JSONObject temp_info = json_city_info.getJSONObject("main");
                infoItem.setName(json_city_info.getString("name"));
                infoItem.setTemp(Float.parseFloat(temp_info.getString("temp")));
                infoItem.setTemp_min(Float.parseFloat(temp_info.getString("temp_min")));
                infoItem.setTemp_max(Float.parseFloat(temp_info.getString("temp_max")));
                JSONObject json_weather_description = json_city_info.getJSONArray("weather").getJSONObject(0);
                infoItem.setDescription(json_weather_description.getString("description"));
                JSONObject coordinates = json_city_info.getJSONObject("coord");
                infoItem.setLongitude(Float.parseFloat(coordinates.getString("lon")));
                infoItem.setLatitude(Float.parseFloat(coordinates.getString("lat")));
                mWeatherInfoList.add(infoItem);
            }

            mInterface.OnAsyncTaskComplete(mWeatherInfoList);
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
