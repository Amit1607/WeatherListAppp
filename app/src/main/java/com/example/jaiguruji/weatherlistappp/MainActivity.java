package com.example.jaiguruji.weatherlistappp;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements OnAsynctTaskComplete ,GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{


    Activity mContext;
    RecyclerView mRecyclerView;
    ArrayList<WeatherInfoItem> mArrayList;
    ArrayList<WeatherInfoItem> mFilteredList;
    TextView mEmpty_msg;
    double current_latitude=0;
    double current_longitude=0;
    GoogleApiClient mGoogleApiClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = MainActivity.this;


        mRecyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("WeatherInfo");
        setSupportActionBar(toolbar);
        mEmpty_msg = (TextView)findViewById(R.id.tv_empty_msg);

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mArrayList = new ArrayList<>();
        mFilteredList = new ArrayList<>();
        requestForData();
        FloatingActionButton map_btn = (FloatingActionButton) findViewById(R.id.floating);
        map_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               showLocationsInMap(mArrayList);
            }
        });
    }



    private void requestForData(){
        if(HelperFunctions.isInternetConnected(mContext)){
            StringBuilder URL = new StringBuilder();

            //open weather api with appended city ids and app Id.
            URL.append("http://api.openweathermap.org/data/2.5/group?id=1270642,1274746,1269743,1256237,1262321,1275841,1263214&units=metric&appid=e4018e2554a19c99511f7f24401f108e");

            //http://api.openweathermap.org/data/2.5/group?id=1270642,1274746,1269743,1256237,1262321,1275841,1263214,1270583,1270583,1271157&units=metric&appid=e4018e2554a19c99511f7f24401f108e
            AsyncTaskFetchData asyncTaskFetchData = new AsyncTaskFetchData(mContext,this);
            asyncTaskFetchData.execute(URL.toString());
        }
        else{
            AlertDialog alertDialog = new AlertDialog.Builder(mContext).setMessage("Internet Not Available")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create();
            alertDialog.show();
        }
    }

    @Override
    public void OnAsyncTaskComplete(ArrayList<WeatherInfoItem> arrayList) {
        WeatherInfoAdapter adapter = new WeatherInfoAdapter(mContext,arrayList,mRecyclerView);
        mArrayList=arrayList;
        mRecyclerView.setAdapter(adapter);
    }

    void showLocationsInMap(ArrayList<WeatherInfoItem> arrayList){
        ArrayList<LocationItem>locationsList = new ArrayList<>();
        for(int i=0;i<arrayList.size();i++) {
            LocationItem locationItem=new LocationItem();
            locationItem.setmLattitude(arrayList.get(i).getLatitude());
            locationItem.setmLongitude(arrayList.get(i).getLongitude());
            locationsList.add(locationItem);
        }

        Bundle args = new Bundle();
        args.putSerializable("locations_list",locationsList);
        Fragment fragment = new MapLocationsFragment();
        fragment.setArguments(args);
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.placeholder,fragment).addToBackStack(null).commit();
    }


   private void showFilterDialog(){
        final String items []= {"less than 100 km","100 to 200 km","200 to 300km ","300-500 km","500-1000km"};

        AlertDialog alertDialog = new AlertDialog.Builder(mContext).setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                filterByDistance(which);
            }
        }).setNegativeButton("Unfilter", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                WeatherInfoAdapter adapter = new WeatherInfoAdapter(mContext,mArrayList,mRecyclerView);
                mRecyclerView.setAdapter(adapter);
            }
        }).create();
        alertDialog.show();
    }

    private void filterByDistance(int position){
        mFilteredList.clear();
        if (current_latitude==0 && current_longitude==0){
            current_latitude=28.70;
            current_longitude=77.10;
        }
        switch (position){
            case 0 :
                for(int i=0;i<mArrayList.size();i++) {
                    float results[] = new float[2];
                    Location.distanceBetween(current_latitude, current_longitude, mArrayList.get(i).getLatitude(), mArrayList.get(i).getLongitude(), results);
                    if (results[0] / 1000 <100 ) {
                        mFilteredList.add(mArrayList.get(i));
                    }
                    }
                break;
            case 1 :
                for(int i=0;i<mArrayList.size();i++) {
                    float results[] = new float[2];
                    Location.distanceBetween(current_latitude, current_longitude, mArrayList.get(i).getLatitude(), mArrayList.get(i).getLongitude(), results);
                    if (results[0] / 1000 > 100 && results[0] / 1000 < 200) {
                        mFilteredList.add(mArrayList.get(i));
                    }
                }
                break;
            case 2:
                for(int i=0;i<mArrayList.size();i++) {
                    float results[] = new float[2];
                    Location.distanceBetween(current_latitude,current_longitude, mArrayList.get(i).getLatitude(), mArrayList.get(i).getLongitude(), results);
                    if (results[0] / 1000 > 200 && results[0] / 1000 < 300) {
                        mFilteredList.add(mArrayList.get(i));
                    }
                }
                break;
            case 3:
                for(int i=0;i<mArrayList.size();i++) {
                    float results[] = new float[2];
                    Location.distanceBetween(current_latitude,current_longitude, mArrayList.get(i).getLatitude(), mArrayList.get(i).getLongitude(), results);
                    if (results[0] / 1000 > 300 && results[0] / 1000 < 500) {
                        mFilteredList.add(mArrayList.get(i));
                    }
                }
                break;
            case 4:
                for(int i=0;i<mArrayList.size();i++) {
                    float results[] = new float[2];
                    Location.distanceBetween(current_latitude,current_longitude, mArrayList.get(i).getLatitude(), mArrayList.get(i).getLongitude(), results);
                    if (results[0] / 1000 > 500 && results[0] / 1000 < 1000) {
                        mFilteredList.add(mArrayList.get(i));
                    }
                }
                break;
            case 5:
                for(int i=0;i<mArrayList.size();i++) {
                    float results[] = new float[2];
                    Location.distanceBetween(current_latitude,current_longitude, mArrayList.get(i).getLatitude(), mArrayList.get(i).getLongitude(), results);
                    if (results[0] / 1000 > 1000 ) {
                        mFilteredList.add(mArrayList.get(i));
                    }
                }
                break;

        }
        if(mFilteredList.size()==0){
            mEmpty_msg.setVisibility(View.VISIBLE);
        }
        else{
            mEmpty_msg.setVisibility(View.GONE);
        }
        WeatherInfoAdapter adapter = new WeatherInfoAdapter(mContext,mFilteredList,mRecyclerView);
        mRecyclerView.setAdapter(adapter);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
       getMenuInflater().inflate(R.menu.activity_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.filter){
          showFilterDialog();
        }
        return super.onOptionsItemSelected(item);
    }

    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
            current_latitude=mLastLocation.getLatitude();
            current_longitude=mLastLocation.getLongitude();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onBackPressed() {
        try {
            super.onBackPressed();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
