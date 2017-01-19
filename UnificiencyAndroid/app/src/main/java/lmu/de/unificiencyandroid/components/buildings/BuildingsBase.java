package lmu.de.unificiencyandroid.components.buildings;

import android.Manifest;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import lmu.de.unificiencyandroid.R;

public abstract class BuildingsBase extends Fragment implements
    GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener  {

  GoogleApiClient mGoogleApiClient;
  Location mLastLocation;

  List<Building> buildings = new ArrayList<Building>();


  public void askLocationPermission(){
    int MyVersion = Build.VERSION.SDK_INT;
    if (MyVersion > Build.VERSION_CODES.LOLLIPOP_MR1) {
      if (!checkIfAlreadyhavePermission()) {
        requestForSpecificPermission();
      }
    }

  }

  private boolean checkIfAlreadyhavePermission() {
    int result = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.GET_ACCOUNTS);
    if (result == PackageManager.PERMISSION_GRANTED) {
      return true;
    } else {
      return false;
    }
  }

  private void requestForSpecificPermission() {
    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 101);
  }

  public void setUpLocationServices(){
    if (mGoogleApiClient == null) {
      mGoogleApiClient = new GoogleApiClient.Builder(getContext())
          .addConnectionCallbacks(this)
          .addOnConnectionFailedListener(this)
          .addApi(LocationServices.API)
          .build();
    }
  }

  public void onStart() {
    mGoogleApiClient.connect();
    super.onStart();
  }

  public void onStop() {
    mGoogleApiClient.disconnect();
    super.onStop();
  }

  @Override
  public void onConnected(@Nullable Bundle bundle) {

  }

  @Override
  public void onConnectionSuspended(int i) {

  }

  @Override
  public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

  }


  @Nullable
  @Override
  public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

    setUpLocationServices();
    //just to fake buildings, it useless
    Resources res = this.getResources();
    Bitmap imga17str = ((BitmapDrawable) res.getDrawable(R.drawable.a17str)).getBitmap();
    Bitmap imga7astr = ((BitmapDrawable) res.getDrawable(R.drawable.a7astr)).getBitmap();
    Bitmap imgo67str = ((BitmapDrawable) res.getDrawable(R.drawable.o67str)).getBitmap();
    Bitmap imgg1str = ((BitmapDrawable) res.getDrawable(R.drawable.g1str)).getBitmap();

    InputStream is = getResources().openRawResource(R.raw.buildings);

    Reader in = new InputStreamReader(is);
    try {
      Iterable<CSVRecord> records = CSVFormat.newFormat(';').withQuote('"').withHeader().parse(in);

      for (CSVRecord record : records) {
        String address = record.get("address");
        Double lat = Double.parseDouble(record.get("lat"));
        Double lng = Double.parseDouble(record.get("lng"));
        String city = record.get("city");
        buildings.add(new Building(address, city, lat, lng));
      }
    } catch (Exception e){
      Log.e("failed to parse csv", e.toString());
    }
    return null;
  }
}

