package lmu.de.unificiencyandroid.components.buildings;

import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.orhanobut.logger.Logger;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import lmu.de.unificiencyandroid.R;
import lmu.de.unificiencyandroid.utils.Message;

import static android.R.attr.permission;

public abstract class BuildingsBase extends Fragment implements
    GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener  {

  GoogleApiClient mGoogleApiClient;
  Location mLastLocation;

  List<Building> buildings = new ArrayList<Building>();

  @Nullable
  @Override
  public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

    checkLocationPermission();
    setUpLocationServices();


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
      Logger.e(e, "Exception");
      Message.fail(getContext(), e.toString());
    }
    return null;
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
  public void onConnected(@Nullable Bundle bundle) {}

  @Override
  public void onConnectionSuspended(int i) {}

  @Override
  public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {}

  private static final int REQUEST_PERMISSION_LOCATION_CODE = 1;
  @TargetApi(Build.VERSION_CODES.M)
  public boolean checkLocationPermission() {

    int permission = ContextCompat.checkSelfPermission(getContext(),
        android.Manifest.permission.ACCESS_FINE_LOCATION);

    if (permission != PackageManager.PERMISSION_GRANTED) {

      ActivityCompat.requestPermissions(getActivity(),
          new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
          REQUEST_PERMISSION_LOCATION_CODE);

      return false;
    }

    return true;

  }

}

