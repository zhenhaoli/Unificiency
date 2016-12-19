package lmu.de.unificiencyandroid.view.buildings;

import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import lmu.de.unificiencyandroid.R;
import lmu.de.unificiencyandroid.model.Building;

public class BuildingsMap extends BuildingsFragment implements
    GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

  Location mLastLocation;
  MapView mMapView;
  GoogleApiClient mGoogleApiClient;
  private GoogleMap googleMap;

  @Override
  public void onConnected(@Nullable Bundle bundle) {
    try {
      mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
          mGoogleApiClient);
      Log.d("loc obj", mLastLocation.toString());
    } catch (SecurityException e){
    }
  }

  @Override
  public void onConnectionSuspended(int i) {

  }

  @Override
  public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    super.onCreateView(inflater,container,savedInstanceState);
    View rootView = inflater.inflate(R.layout.buildings_map, container, false);
    mMapView = (MapView) rootView.findViewById(R.id.mapView);
    mMapView.onCreate(savedInstanceState);

    if (mGoogleApiClient == null) {
      mGoogleApiClient = new GoogleApiClient.Builder(getContext())
          .addConnectionCallbacks(this)
          .addOnConnectionFailedListener(this)
          .addApi(LocationServices.API)
          .build();
    }

    mMapView.onResume(); // needed to get the map to display immediately

    try {
      MapsInitializer.initialize(getActivity().getApplicationContext());
    } catch (Exception e) {
      e.printStackTrace();
    }

    mMapView.getMapAsync(new OnMapReadyCallback() {
      @Override
      public void onMapReady(GoogleMap mMap) {
        googleMap = mMap;
        double myLat = 50, myLng = 10;

        // For showing a move to my location button
        try {
          googleMap.setMyLocationEnabled(true);

        } catch (SecurityException e) {
          Log.e("permission not got", e.toString());
        }

        if(mLastLocation!=null){
          myLat = mLastLocation.getLatitude();
          myLng = mLastLocation.getLongitude();
        }

        // For dropping a marker at a point on the Map
        LatLng myPos = new LatLng(myLat, myLng);
        googleMap.addMarker(new MarkerOptions().position(myPos).title("Marker Title").snippet("Marker Description"));

        Log.d("buildings value", buildings.toString());

        //TODO: make custom icon
        for(Building building : buildings){
          googleMap.addMarker(new MarkerOptions().position(
              new LatLng(building.getLat(), building.getLng()))
              .title(building.getAddress())
              .snippet(building.getCity())
              .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
        }

        // For zooming automatically to the location of the marker
        CameraPosition cameraPosition = new CameraPosition.Builder().target(myPos).zoom(15).build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
      }
    });

    return rootView;
  }

  @Override
  public void onResume() {
    super.onResume();
    mMapView.onResume();
  }

  @Override
  public void onPause() {
    super.onPause();
    mMapView.onPause();
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    mMapView.onDestroy();
  }

  @Override
  public void onLowMemory() {
    super.onLowMemory();
    mMapView.onLowMemory();
  }

  public void onStart() {
    mGoogleApiClient.connect();
    super.onStart();
  }

  public void onStop() {
    mGoogleApiClient.disconnect();
    super.onStop();
  }

}