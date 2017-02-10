package com.example.jaiguruji.weatherlistappp;

import java.io.Serializable;

/**
 * Created by JaiGuruji on 09/02/17.
 */

public class LocationItem implements Serializable {
    float mLattitude;
    float mLongitude;

    public float getmLattitude() {

        return mLattitude;
    }

    public void setmLattitude(float mLattitude) {
        this.mLattitude = mLattitude;
    }

    public float getmLongitude() {
        return mLongitude;
    }

    public void setmLongitude(float mLongitude) {
        this.mLongitude = mLongitude;
    }


}
