package lmu.de.unificiencyandroid.utils;

import android.content.Context;
import android.support.v4.content.res.ResourcesCompat;

import com.github.johnpersano.supertoasts.library.Style;
import com.github.johnpersano.supertoasts.library.SuperActivityToast;
import com.orhanobut.logger.Logger;

import lmu.de.unificiencyandroid.R;

public final class Message {
  private Message() {}

  public static void success(Context context, String message){
    try {
      SuperActivityToast.cancelAllSuperToasts();
      SuperActivityToast.create(context, new Style(), Style.TYPE_STANDARD)
          .setText(message)
          .setDuration(Style.DURATION_LONG)
          .setFrame(Style.FRAME_KITKAT)
          .setColor(ResourcesCompat.getColor(context.getResources(), R.color.green_400, null))
          .setAnimations(Style.ANIMATIONS_SCALE)
          .show();
    } catch (Exception e){
      Logger.e(e.toString());
    }
  }

  public static void fail(Context context, String message){
    try {
      SuperActivityToast.cancelAllSuperToasts();
      SuperActivityToast.create(context, new Style(), Style.TYPE_STANDARD)
          .setText(message)
          .setDuration(Style.DURATION_LONG)
          .setFrame(Style.FRAME_KITKAT)
          .setColor(ResourcesCompat.getColor(context.getResources(), R.color.red_400, null))
          .setAnimations(Style.ANIMATIONS_SCALE)
          .show();
    } catch (Exception e){
      Logger.e(e.toString());
    }

  }
}
