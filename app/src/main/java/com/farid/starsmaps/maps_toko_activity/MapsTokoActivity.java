package com.farid.starsmaps.maps_toko_activity;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.farid.starsmaps.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsTokoActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    TextView TextLatitude, TextLongitude, TextNamaToko;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_toko);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        TextLatitude = findViewById(R.id.TxtLatitude);
        TextLongitude = findViewById(R.id.TxtLongitude);
        TextNamaToko = findViewById(R.id.TxtNamaToko);

        //menangkap bundle yang telah di-parsed dari MainActivity
        Bundle b = getIntent().getExtras();
        String latitude = b.getString("latitude");
        String longitude= b.getString("longitude");
        String nama_toko= b.getString("nama_toko");

        TextLatitude.setText(latitude);
        TextLongitude.setText(longitude);
        TextNamaToko.setText(nama_toko);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        double lat = Double.parseDouble((String) TextLatitude.getText().toString().trim());
        double lon = Double.parseDouble((String) TextLongitude.getText().toString().trim());
        String nama = (String) TextNamaToko.getText();
        // Add a marker in Sydney and move the camera
        LatLng toko = new LatLng(lat, lon);
        mMap.addMarker(new MarkerOptions().position(toko).title(nama));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(toko, 15));
    }
}
