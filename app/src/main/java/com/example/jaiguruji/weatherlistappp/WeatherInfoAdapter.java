package com.example.jaiguruji.weatherlistappp;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by JaiGuruji on 09/02/17.
 */

public class WeatherInfoAdapter extends RecyclerView.Adapter<WeatherInfoAdapter.MyViewHolder> {
    Activity mActivity;
    ArrayList<WeatherInfoItem> mWeatherInfoList;
    LayoutInflater inflater;
    Typeface typeface1,typeface2;
    RecyclerView mRecyclerView;


    public WeatherInfoAdapter(Activity mActivity, ArrayList<WeatherInfoItem> mWeatherInfoList, RecyclerView recyclerView) {
        this.mActivity = mActivity;
        this.mWeatherInfoList = mWeatherInfoList;
        inflater = (LayoutInflater)mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
       // typeface1 = Typeface.createFromAsset(mActivity.getAssets(),"robotoMedium.ttf");
        //typeface2= Typeface.createFromAsset(mActivity.getAssets(),"RobotoRegular.ttf");
        mRecyclerView = recyclerView;
    }

    @Override
    public int getItemCount() {
        return mWeatherInfoList.size();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       View v = inflater.inflate(R.layout.item_city_weather_info,parent,false);
       v.setOnClickListener(new MyOnClickListener());
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.mName.setText(mWeatherInfoList.get(position).getName());
        holder.mTempMin.setText(String.valueOf(mWeatherInfoList.get(position).getTemp_min()) +"˚C");
        holder.mTempMax.setText(String.valueOf(mWeatherInfoList.get(position).getTemp_max()) +"˚C");
        holder.mTemp.setText(String.valueOf(mWeatherInfoList.get(position).getTemp())+"˚C");
        holder.mDescription.setText(String.valueOf(mWeatherInfoList.get(position).getDescription()));
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView mName,mTemp,mTempMin,mTempMax,mDescription;
        public MyViewHolder(View itemView) {
            super(itemView);
            mName = (TextView)itemView.findViewById(R.id.tv_name);
            mTemp = (TextView)itemView.findViewById(R.id.tv_temp_main);
            mTempMax = (TextView)itemView.findViewById(R.id.tv_temp_max);
            mTempMin = (TextView)itemView.findViewById(R.id.tv_temp_min);
            mDescription = (TextView)itemView.findViewById(R.id.tv_description);
        }
    }

    class MyOnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            int pos = mRecyclerView.getChildLayoutPosition(v);

        }
    }

    String toFloat(Float f){
        return String.valueOf(f);
    }
}