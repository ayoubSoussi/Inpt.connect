package com.example.asous.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.asous.myapplication.VolleyPackage.GazPosition;
import com.example.asous.myapplication.VolleyPackage.JsonRequest;
import com.example.asous.myapplication.VolleyPackage.MySingleton;
import com.google.android.gms.maps.model.Marker;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Main2Activity extends Activity {



    int nbMarkers ;
    ArrayList<GazPosition> gazArray ;
    JsonRequest jsonRequest ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        jsonRequest = new JsonRequest() ;
        MySingleton.getInstance(this).addToRequestQueue(jsonRequest.getJsonObjectRequest());




    }
    public void go(View v){
        gazArray = new ArrayList<>(nbMarkers) ;
        gazArray = jsonRequest.getGazArray() ;
        if (gazArray == null){
            Toast.makeText(Main2Activity.this,"please check your connection and restart the app",Toast.LENGTH_LONG ).show();
        }else{
        Intent intent = new Intent(Main2Activity.this,MainActivity.class) ;
        intent.putExtra("gazp",gazArray) ;
        startActivity(intent);}
    }

}
