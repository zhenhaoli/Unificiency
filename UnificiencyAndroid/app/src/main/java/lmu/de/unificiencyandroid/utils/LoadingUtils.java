package lmu.de.unificiencyandroid.utils;


import android.view.View;
import android.view.ViewGroup;

public final class LoadingUtils {
  private LoadingUtils() {}

  public static void enableView(View view, boolean enabled) {
    view.setEnabled(enabled);
    if (view instanceof ViewGroup) {
      ViewGroup viewGroup = (ViewGroup) view;
      for (int i = 0; i < viewGroup.getChildCount(); i++) {
        View child = viewGroup.getChildAt(i);
        enableView(child, enabled);
      }
    }
  }

  public static void showView(View view, boolean enabled) {
    view.setVisibility(enabled ? View.VISIBLE : View.INVISIBLE);
    if (view instanceof ViewGroup) {
      ViewGroup viewGroup = (ViewGroup) view;
      for (int i = 0; i < viewGroup.getChildCount(); i++) {
        View child = viewGroup.getChildAt(i);
        showView(child, enabled);
      }
    }
  }

}
