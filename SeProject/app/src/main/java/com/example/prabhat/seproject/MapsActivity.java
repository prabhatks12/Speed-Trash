package com.example.prabhat.seproject;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.telephony.SmsManager;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener {
    public float currentspeed;
    private GoogleMap mMap;
    public float speedlimit= (float) 50.0;
    int test=1;
    int count=-1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        LocationManager lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        this.onLocationChanged(null);

    }

  @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        LatLng location = new LatLng(0.0,0.0);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(location));
        if((ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED)||
                (ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION)==PackageManager.PERMISSION_GRANTED )) {
            mMap.setMyLocationEnabled(true);
        }

  }

    @Override
    public void onLocationChanged(Location location) {
        TextView tw=(TextView)findViewById(R.id.change);
        if(location==null)
        {
            tw.setText("0.0");
        }
        else
        {
            currentspeed=location.getSpeed();
            currentspeed=currentspeed*3600/1000;
            currentspeed = (float) (Math.round(currentspeed*100)/100.0);
            tw.setText(currentspeed+"");
            check();

        }

    }

    public void check(){

        if(currentspeed>=speedlimit){
            count=-1;
            sendMessage();
            alert();
        }
        else return;
    }
    public void alert(){

        if(currentspeed>=speedlimit) {
            MediaPlayer ring = MediaPlayer.create(MapsActivity.this, R.raw.alert);
            ring.start();
        }
        else
            return;


    }

    public void sendMessage(){


        Thread thread = new Thread(){
            public void run(){
                while(test!=0){

                    try{
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                count++;
                                if(currentspeed<=speedlimit)
                                {
                                    test=0;
                                    count=-1;
                                    Toast.makeText(MapsActivity.this," Message not sent ",Toast.LENGTH_LONG).show();
                                }

                                else if(count==10) {
                                    //sendSMS("your phone number", " XYZ-123 is exceeding speed limit ");
                                    test=0;
                                    count=-1;
                                    Toast.makeText(MapsActivity.this," Message sent !! ",Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

        };
        thread.start();
    }

//    public void sendSMS(String phonenumber,String message){
  //      SmsManager sms=SmsManager.getDefault();
    //    sms.sendTextMessage(phonenumber,null,message,null,null);
    //}



    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

        Intent i=new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        Toast.makeText(MapsActivity.this," Turn on your GPS ",Toast.LENGTH_LONG).show();
        startActivity(i);
    }
}
