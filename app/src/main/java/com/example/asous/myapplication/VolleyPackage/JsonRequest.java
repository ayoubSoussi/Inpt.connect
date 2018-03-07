package com.example.asous.myapplication.VolleyPackage;


import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.asous.myapplication.Main2Activity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static java.security.AccessController.getContext;

public class JsonRequest {
    JsonObjectRequest jsonObjectRequest ;
    String url = "http://www.json-generator.com/api/json/get/bRysHyczhe?indent=2" ;
    int nbMarkers ;
    ArrayList<GazPosition> gazArray ;

    JSONObject bilan ;


    public JsonRequest() {
        sendRequest();
    }

    private void sendRequest() {
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
    }

    public void createMarkers(){

        nbMarkers = bilan.names().length() ;
        gazArray = new ArrayList<>(nbMarkers) ;

        for (int i=0;i<nbMarkers;i++){
            try {
                double lat = bilan.getJSONObject(bilan.names().get(i).toString()).getDouble("latitude") ;
                double lng = bilan.getJSONObject(bilan.names().get(i).toString()).getDouble("longitude") ;
                String title = bilan.names().get(i).toString() ;
                String Extra = bilan.getJSONObject(bilan.names().get(i).toString()).getString("extra") ;
                double coo = bilan.getJSONObject(bilan.names().get(i).toString()).getDouble("coo") ;
                gazArray.add(i,new GazPosition(title,lat,lng,coo,Extra ))  ;

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }

    public JsonObjectRequest getJsonObjectRequest() {
        return jsonObjectRequest;
    }

    public ArrayList<GazPosition> getGazArray() {
        return gazArray;
    }
}
