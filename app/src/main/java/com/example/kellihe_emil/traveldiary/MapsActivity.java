package com.example.kellihe_emil.traveldiary;

import android.app.Dialog;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    private Button btnSearch;
    private TextView address;
    private Geocoder gc;
    private double lat;
    private double lon;
    private LatLng position = null;

    private int locations = 5;  //number of locations to find


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Toast.makeText(this, "Hope we found it!", Toast.LENGTH_LONG).show();
        address = (TextView) findViewById(R.id.myAddress);
        btnSearch = (Button) findViewById(R.id.myBtnSearch);

        gc = new Geocoder(this);  //create Geocoder object

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        //set marker listener
        mMap.setOnMarkerClickListener(
                new GoogleMap.OnMarkerClickListener() {

                    public boolean onMarkerClick(Marker m) {
                        String title = m.getTitle();
                        String snip = m.getSnippet();
                        Toast.makeText(getApplicationContext(), title + "\n" + snip, Toast.LENGTH_LONG).show();
                        return true;
                    }
                }
        );

        btnSearch.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String addressInput = address.getText().toString();
                //clear map
                mMap.clear();

                try {
                    //get latitude/longitude pairs from location name
                    List<Address> FoundAddresses = gc.getFromLocationName(
                            addressInput, locations);
                    Toast.makeText(getApplicationContext(), addressInput, Toast.LENGTH_LONG).show();

                    if (FoundAddresses.size() == 0)
                        showInvalidAddressMsg();
                    else {

                        //loop over the locations found
                        for (int i = 0; i < FoundAddresses.size(); i++) {

                            String msg = "";
                            // show results as address, Longitude and Latitude
                            Address a = FoundAddresses.get(i);
                            lat = a.getLatitude();
                            lon = a.getLongitude();
                            String adr = "\n" + a.getAddressLine(0)
                                    + "\n" + a.getAddressLine(1);
                            msg = lat + " " + lon + adr;
                            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();

                            //place marker at address
                            position = new LatLng(lat, lon);
                            mMap.addMarker(new MarkerOptions()
                                    .title(adr)
                                    .snippet("")
                                    .position(position)
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)));

                        }//end loop

                        //move camera to last location found
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(position, 4.0f));
                    }
                } catch (Exception e) {
                    Toast.makeText(MapsActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }// onClick
        });

    }

    //alert dialog if invalid address
    private void showInvalidAddressMsg() {
        Dialog locationError = new AlertDialog.Builder(
                MapsActivity.this)
                .setIcon(0)
                .setTitle("Error")
                .setPositiveButton("OK", null)
                .setMessage("Sorry, your address doesn't exist.")
                .create();
        locationError.show();
    }// showInvalidAddressMsg

}

