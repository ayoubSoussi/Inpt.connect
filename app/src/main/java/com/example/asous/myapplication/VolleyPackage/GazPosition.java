package com.example.asous.myapplication.VolleyPackage;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;



public class GazPosition implements Serializable {
    private static final long serialVersionUID = 1L;
    String title,Extra ;
    double coo ;
    double lat,lng  ;


    public GazPosition(String title, double lat, double lng,double coo,String extra ) {
        Extra = extra;
        this.coo = coo;
        this.title = title;
        this.lat = lat;
        this.lng = lng;
    }

    public LatLng getLl() {
        return new LatLng(lat,lng);
    }

    public double getCoo() {
        return coo;
    }

    public String getTitle() {
        return title;
    }

    public String getExtra() {
        return Extra;
    }
}
