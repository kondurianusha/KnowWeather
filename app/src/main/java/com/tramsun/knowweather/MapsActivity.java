package com.tramsun.knowweather;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

  private GoogleMap mMap;
  private double longitude;
  private double latitude;
  private String cityName;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_maps);
    if (getIntent() != null) {
      longitude = getIntent().getDoubleExtra("Longitude", 78.47);
      latitude = getIntent().getDoubleExtra("Latitude", 17.38);
      cityName = getIntent().getStringExtra("CityName");
    }

    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

    mapFragment.getMapAsync(this);
  }

  @Override public void onMapReady(GoogleMap googleMap) {
    mMap = googleMap;

    // Add a marker in Sydney and move the camera
    LatLng cityMarker = new LatLng(latitude, longitude);
    mMap.addMarker(new MarkerOptions().position(cityMarker).title("Marker in " + cityName));
    mMap.moveCamera(CameraUpdateFactory.newLatLng(cityMarker));
    mMap.setOnMapLongClickListener(latLng -> mMap.addMarker(new MarkerOptions().position(latLng)));
  }
}
