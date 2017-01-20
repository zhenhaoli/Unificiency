package lmu.de.unificiencyandroid.components.buildings;

import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

import butterknife.BindView;
import butterknife.ButterKnife;
import lmu.de.unificiencyandroid.R;
import lmu.de.unificiencyandroid.utils.Message;

public class BuildingsMap extends BuildingsBase implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

  static final String TAG = BuildingsMap.class.getName();

  @BindView(R.id.mapView)
  MapView mMapView;

  Location mLastLocation;
  GoogleMap googleMap;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    super.onCreateView(inflater,container,savedInstanceState);

    View view = inflater.inflate(R.layout.buildings_map, container, false);

    ButterKnife.bind(this, view);

    askLocationPermission();

    mMapView.onCreate(savedInstanceState);

    mMapView.onResume(); // needed to get the map to display immediately

    try {
      MapsInitializer.initialize(getActivity().getApplicationContext());
    } catch (Exception e) {
      Log.e(TAG, e.toString());
      Message.fail(getContext(), e.toString());
    }

    mMapView.getMapAsync(new OnMapReadyCallback() {
      @Override
      public void onMapReady(GoogleMap mMap) {
        googleMap = mMap;

        // For showing a move to my location button
        try {
          googleMap.setMyLocationEnabled(true);

        } catch (SecurityException e) {
          Log.e("permission not got", e.toString());
        }

        for(Building building : buildings){
          googleMap.addMarker(new MarkerOptions().position(
              new LatLng(building.getLat(), building.getLng()))
              .title(building.getAddress())
              .snippet(building.getCity())
              .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
        }

      }
    });

    return view;
  }

  @Override
  public void onConnected(@Nullable Bundle bundle) {
    super.onConnected(bundle);
    try {
      mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
              mGoogleApiClient);

      double myLat = 48.1504788, myLng = 11.5778267;

      if(mLastLocation!=null){
        myLat = mLastLocation.getLatitude();
        myLng = mLastLocation.getLongitude();
      }

      LatLng myPos = new LatLng(myLat, myLng);

      // For zooming automatically to the location of the marker
      CameraPosition cameraPosition = new CameraPosition.Builder().target(myPos).zoom(15).build();
      googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

    } catch (SecurityException e){
      Log.e(TAG, e.toString());
      Message.fail(getContext(), e.toString());
    }
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

}