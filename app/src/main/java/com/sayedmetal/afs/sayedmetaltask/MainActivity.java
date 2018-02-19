package com.sayedmetal.afs.sayedmetaltask;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.maps.android.clustering.ClusterManager;
import org.json.JSONException;
import java.io.InputStream;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private ClusterManager<MyItem> mClusterManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUpMap();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMap();
    }

    @Override
    public void onMapReady(GoogleMap map) {
        //if the mMap is already initialized return
        if (mMap != null) {
            return;
        }
        //otherwise assign the new maps reference to our member variable
        mMap = map;

        //Move the view to the given location , "SayedMetals company location"
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(25.2877396,55.401065), 10));

        //Construct a new ClusterManager
        mClusterManager = new ClusterManager<MyItem>(this, mMap);

        //sets the on camera idle listener to the one prvided by the cluster manager
        mMap.setOnCameraIdleListener(mClusterManager);

        //Add an onClick listener to handle Marker click
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                marker.showInfoWindow(); // Shows Marker name
                return true;
            }
        });

        try {
            readItems(); // Loads locations into the cluster manager
        } catch (JSONException e) {
            Toast.makeText(this, "Problem reading list of markers.", Toast.LENGTH_LONG).show();
        }
    }

    private void readItems() throws JSONException {
        //obtain an input stream from the Json data file
        InputStream inputStream = getResources().openRawResource(R.raw.locations_data);
        //Converts Json Location data into a suitable data structure for the cluster manager
        List<MyItem> items = new LocationsReader().read(inputStream);
        //Add Marker data
        mClusterManager.addItems(items);
    }

    //Initialize the map , and calls onMapReady
    private void setUpMap() {
        ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMapAsync(this);
    }

}
