package com.example.jaiguruji.weatherlistappp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

/**
 * Created by JaiGuruji on 09/02/17.
 */

public class MapLocationsFragment extends Fragment {
    Bundle mBundle;
    ArrayList<LocationItem> arrayList;
    MapFragment mapFragment;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBundle = getArguments();
        arrayList = (ArrayList<LocationItem>) mBundle.getSerializable("locations_list");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
      View root_view = inflater.inflate(R.layout.fragment_map,container,false);
        mapFragment = (MapFragment) getActivity().getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

                for(int i=0;i<arrayList.size();i++){
                    MarkerOptions markerOptions = new MarkerOptions().
                            position(new LatLng(arrayList.get(i).getmLattitude(),arrayList.get(i).getmLongitude()))
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
                     googleMap.addMarker(markerOptions);
                }
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(
                        new LatLng(20.59,78.96),4f);
                googleMap.animateCamera(cameraUpdate);
            }
        });
        return root_view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        MapFragment mapFragment = (MapFragment)getActivity().getFragmentManager().findFragmentById(R.id.map);
        android.app.FragmentTransaction transaction = getActivity().getFragmentManager().beginTransaction();
        transaction.remove(mapFragment);
        transaction.commit();
    }
}
