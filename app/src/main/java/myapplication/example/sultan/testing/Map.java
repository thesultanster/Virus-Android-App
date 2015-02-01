package myapplication.example.sultan.testing;


import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;


public class Map extends ActionBarActivity {

    RelativeLayout myNotif;
    ImageView heartImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        NavigationDrawerFragment drawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);

        Firebase.setAndroidContext(this);

        RegisterDevice();
        SetListeners();

    }

    @Override
    protected void onStart() {
        super.onStart();
        View view = findViewById(R.id.zombienotification);
        myNotif = (RelativeLayout) view;
        myNotif.setVisibility(View.INVISIBLE);

        view = findViewById(R.id.heart);
        heartImage = (ImageView) view;
        // set its background to our AnimationDrawable XML resource.
        heartImage.setBackgroundResource(R.drawable.heart_animation_healthy);

        /*
         * Get the background, which has been compiled to an AnimationDrawable
         * object.
         */
        AnimationDrawable frameAnimation = (AnimationDrawable) heartImage
                .getBackground();

        // Start the animation (looped playback by default).
        frameAnimation.start();


        Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                SetListeners();
                            }
                        });
                    }
                } catch (InterruptedException e) {
                }
            }
        };

        t.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_map, menu);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void RegisterDevice() {

        WifiManager manager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = manager.getConnectionInfo();
        String address = info.getMacAddress();


        // Connect to registration list
        Firebase sendData = new Firebase("https://crackling-torch-2249.firebaseio.com/active-list");

        // Get Unique device ID
        String android_id = Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);

        // Send data to database, register device
        sendData.child(address).setValue("Healthy");

        return;
    }

    public void SetListeners() {
        // Connect to statistics
        Firebase stats = new Firebase("https://crackling-torch-2249.firebaseio.com/statistics");

        stats.child("zone_infected").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                TextView tv1 = (TextView) findViewById(R.id.zone_infected);
                tv1.setText(snapshot.getValue().toString());

            }

            @Override
            public void onCancelled(FirebaseError error) {
            }
        });

        stats.child("zone_safe").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                TextView tv1 = (TextView) findViewById(R.id.zone_safe);
                tv1.setText(snapshot.getValue().toString());
            }

            @Override
            public void onCancelled(FirebaseError error) {
            }
        });

        stats.child("zone_title").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                TextView tv1 = (TextView) findViewById(R.id.zone_title);
                tv1.setText(snapshot.getValue().toString());
            }

            @Override
            public void onCancelled(FirebaseError error) {
            }
        });

        stats.child("zone_secondary").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                TextView tv1 = (TextView) findViewById(R.id.zone_secondary);
                tv1.setText(snapshot.getValue().toString());
            }

            @Override
            public void onCancelled(FirebaseError error) {
            }
        });

        stats.child("zombie_nearby").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.getValue().toString() == "true")
                    myNotif.setVisibility(View.VISIBLE);
                else if (snapshot.getValue().toString() == "false")
                    myNotif.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onCancelled(FirebaseError error) {
            }
        });

        


    }

}
