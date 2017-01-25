package lmu.de.unificiencyandroid.utils;

import android.content.Context;
import android.graphics.Bitmap;

import com.orhanobut.logger.Logger;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

public final class ImageUtils {

  private ImageUtils() {}

  public static File bitmapToFile(Context context, Bitmap bitmap){
    try {
      File f = new File(context.getCacheDir(), "upload.png");
      f.createNewFile();

      ByteArrayOutputStream bos = new ByteArrayOutputStream();
      bitmap.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
      byte[] bitmapdata = bos.toByteArray();

      FileOutputStream fos = new FileOutputStream(f);
      fos.write(bitmapdata);
      fos.flush();
      fos.close();
      return f;
    } catch (Exception e){
      Logger.e(e, "Exception during Bitmap to File: ");
      return null;
    }
  }
}
