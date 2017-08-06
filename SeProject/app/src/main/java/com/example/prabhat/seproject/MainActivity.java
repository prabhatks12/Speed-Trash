package com.example.prabhat.seproject;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements LocationListener {

   public float currentspeed;
   public float speedlimit= (float) 50.0;
   int test=1;
   int count=-1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LocationManager lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        this.onLocationChanged(null);

    }

    public void onLocationChanged(Location location) {
        TextView tw=(TextView)findViewById(R.id.speed);
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
            alert();
            check();
        }
    }

  public void onMap(View view)
  {
      Button btn=(Button)findViewById(R.id.Map);
      Intent i=new Intent(MainActivity.this,MapsActivity.class);
      startActivity(i);

  }

  public void onProject(View view){
      Button btn=(Button)findViewById(R.id.Project);
              Intent i=new Intent(MainActivity.this,Project.class);
              startActivity(i);
     }

    public void onAbout(View view){
        Button btn=(Button)findViewById(R.id.Aboutus);
                Intent i=new Intent(MainActivity.this,Aboutus.class);
                startActivity(i);

    }

    public void check(){

        if(currentspeed>=speedlimit){
            count=-1;
            sendMessage();

        }
        else return;
    }
    public void alert(){

      if(currentspeed>=speedlimit) {
          MediaPlayer ring = MediaPlayer.create(MainActivity.this, R.raw.alert);
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
                                    count=-1;
                                    Toast.makeText(MainActivity.this," Message not sent ",Toast.LENGTH_LONG).show();
                                    test=0;
                                }

                                else if(count==9) {
                                   // sendSMS(" phone number ", "XYZ 123 is exceeding speed limit");
                                    count=-1;
                                    Toast.makeText(MainActivity.this," Message sent !! ",Toast.LENGTH_LONG).show();
                                    test=0;
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

  //  public void sendSMS(String phonenumber,String message){
    //    SmsManager sms=SmsManager.getDefault();
      //  sms.sendTextMessage(phonenumber,null,message,null,null);
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
        Toast.makeText(MainActivity.this," Turn on your GPS ",Toast.LENGTH_LONG).show();
        startActivity(i);

    }
}
