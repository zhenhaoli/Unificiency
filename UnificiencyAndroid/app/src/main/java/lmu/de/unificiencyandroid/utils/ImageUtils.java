package lmu.de.unificiencyandroid.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.orhanobut.logger.Logger;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import lmu.de.unificiencyandroid.R;

public final class ImageUtils {

  private ImageUtils() {

  }

  private static final String baseUrl = "https://romue404.pythonanywhere.com/api/";

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

  public static void downloadToImageView(Context context, String imageUrl, ImageView imageView){
    Picasso.with(context)
        .load(baseUrl + imageUrl)
        .networkPolicy(NetworkPolicy.NO_CACHE)
        .placeholder(R.drawable.placeholder)
        .error(R.drawable.error_loading)
        .into(imageView);
  }
}
