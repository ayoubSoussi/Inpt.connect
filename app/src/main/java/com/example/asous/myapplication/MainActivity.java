package com.example.asous.myapplication;

import android.*;
import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.asous.myapplication.VolleyPackage.MySingleton;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends Activity implements LocationListener, OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    GoogleMap mGoogleMap;
    GoogleApiClient mGoogleApiClient;
    LatLng[] Lbat = {new LatLng(33.9809586,-6.868123),new LatLng(33.9806047,-6.8684366),new LatLng(33.9803336,-6.8680208),new LatLng(33.9806622,-6.8676724)} ;
    JsonObjectRequest jsonObjectRequest ;
    String url = "http://www.json-generator.com/api/json/get/ckvwCvgwpu?indent=2" ;
    int nbMarkers ;
    ArrayList<Marker> markers ;

    JSONObject bilan ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (googleServicesAvailable()) {
            setContentView(R.layout.activity_main);
            initMap();
        }
        bilan = new JSONObject() ;
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                bilan = response ;
                createMarkers();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) ;

        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);


    }
    public void createMarkers(){

        nbMarkers = bilan.names().length() ;
        markers = new ArrayList<>(nbMarkers) ;
        MarkerOptions options ;
        for (int i=0;i<nbMarkers;i++){
            //if (markers.get(i) != null) markers.get(i).remove();
            try {
                double lat = bilan.getJSONObject(bilan.names().get(i).toString()).getDouble("latitude") ;
                double lng = bilan.getJSONObject(bilan.names().get(i).toString()).getDouble("longitude") ;
                //here we are going to retrieve the values
                //             of other parameters then we will affect them to their marker


                options = new MarkerOptions().title(bilan.names().get(i).toString()).
                        position(new LatLng(lat,lng)) ;
                markers.add(i,mGoogleMap.addMarker(options)) ;
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }

    private void initMap() {
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync(this);

    }

    public boolean googleServicesAvailable() {
        GoogleApiAvailability api = GoogleApiAvailability.getInstance();
        int isAvailable = api.isGooglePlayServicesAvailable(this);
        if (isAvailable == ConnectionResult.SUCCESS) {
            return true;
        } else if (api.isUserResolvableError(isAvailable)) {
            Dialog dialog = api.getErrorDialog(this, isAvailable, 0);
            dialog.show();
        } else {
            Toast.makeText(this, "impossible", Toast.LENGTH_LONG).show();
        }
        return false;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        if (mGoogleMap!=null){
            mGoogleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                @Override
                public View getInfoWindow(Marker marker) {
                    return null;
                }

                @Override
                public View getInfoContents(Marker marker) {
                    View v = getLayoutInflater().inflate(R.layout.markerdialog,null) ;
                    TextView t = (TextView) v.findViewById(R.id.title) ;
                    TextView latt = (TextView) v.findViewById(R.id.latitude) ;
                    TextView longg = (TextView) v.findViewById(R.id.longitude) ;
                    TextView gaz = (TextView) v.findViewById(R.id.gaz) ;
                    t.setText(marker.getTitle());
                    latt.setText("la latitude est :"+marker.getPosition().latitude);
                    longg.setText("la longitude est :"+marker.getPosition().longitude);
                    gaz.setText("le pourcentage du gaz est :");

                    //here we are going to set the values of other parameters to their
                    //      textviews to be displayed when the user click on a marker


                    return v;
                }
            });
        }
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mGoogleApiClient.connect();



    }


    LocationRequest locationRequest;
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(1000);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, locationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, "impossible", Toast.LENGTH_LONG).show();

    }

    Marker marker ;
    @Override

    public void onLocationChanged(Location location) {
        if (location == null){
            Toast.makeText(this, "cant get your location", Toast.LENGTH_LONG).show();
        }
        else{
            LatLng ll  = new LatLng(location.getLatitude(),location.getLongitude()) ;
            if (marker !=null) marker.remove();
            MarkerOptions markerOptions = new MarkerOptions()
                    .title("my position")
                    .position(ll).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)) ;
            marker = mGoogleMap.addMarker(markerOptions);


        }

    }
}
