package lmu.de.unificiencyandroid.components.buildings;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
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

public abstract class BuildingsBase extends Fragment implements
    GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener  {

  GoogleApiClient mGoogleApiClient;
  Location mLastLocation;

  List<Building> buildings = new ArrayList<Building>();
  protected static Integer REQUEST_LOCATION = 101;
  @Nullable
  @Override
  public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

    if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
        != PackageManager.PERMISSION_GRANTED) {
      // Check Permissions Now
      this.requestPermissions(
          new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
          REQUEST_LOCATION);
    }

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


}

