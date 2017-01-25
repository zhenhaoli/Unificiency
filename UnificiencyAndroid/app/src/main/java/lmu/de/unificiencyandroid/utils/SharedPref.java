package lmu.de.unificiencyandroid.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public final class SharedPref {
  private SharedPref() {}

  public static void setDefaults(String key, String value, Context context) {
    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
    SharedPreferences.Editor editor = prefs.edit();
    editor.putString(key, value);
    editor.commit();
  }

  public static String getDefaults(String key, Context context) {
    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
    return preferences.getString(key, null);
  }
}
