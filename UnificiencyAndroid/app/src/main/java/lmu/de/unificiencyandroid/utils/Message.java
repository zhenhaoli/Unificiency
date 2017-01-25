package lmu.de.unificiencyandroid.utils;

import android.content.Context;
import android.support.v4.content.res.ResourcesCompat;

import com.github.johnpersano.supertoasts.library.Style;
import com.github.johnpersano.supertoasts.library.SuperActivityToast;

import lmu.de.unificiencyandroid.R;

public final class Message {
  private Message() {}

  public static void success(Context context, String message){
    SuperActivityToast.cancelAllSuperToasts();
    SuperActivityToast.create(context, new Style(), Style.TYPE_STANDARD)
        .setText(message)
        .setDuration(Style.DURATION_LONG)
        .setFrame(Style.FRAME_KITKAT)
        .setColor(ResourcesCompat.getColor(context.getResources(), R.color.green_400, null))
        .setAnimations(Style.ANIMATIONS_SCALE)
        .show();
  }

  public static void fail(Context context, String message){
    SuperActivityToast.cancelAllSuperToasts();
    SuperActivityToast.create(context, new Style(), Style.TYPE_STANDARD)
        .setText(message)
        .setDuration(Style.DURATION_LONG)
        .setFrame(Style.FRAME_KITKAT)
        .setColor(ResourcesCompat.getColor(context.getResources(), R.color.red_400, null))
        .setAnimations(Style.ANIMATIONS_SCALE)
        .show();
  }
}
