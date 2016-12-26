package lmu.de.unificiencyandroid.components.buildings;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import lmu.de.unificiencyandroid.R;

public abstract class BuildingsFragment extends Fragment {

  List<Building> buildings = new ArrayList<Building>();
  @Nullable
  @Override
  public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

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
        buildings.add(new Building(address, city, imga7astr, lat, lng, null));
      }
    } catch (Exception e){
      Log.e("failed to parse csv", e.toString());
    }
    return null;
  }
}

