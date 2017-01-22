package lmu.de.unificiencyandroid.components.settings;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import lmu.de.unificiencyandroid.R;

import static android.app.Activity.RESULT_OK;

public class ActivityThatStartsCamera extends Fragment {

  static final int REQUEST_IMAGE_CAPTURE = 1;

  @BindView(R.id.imageView)
  ImageView mImageView;

  @BindView(R.id.button)
  Button mButton;

  @OnClick(R.id.button)
  public void dispatchTakePictureIntent() {
    if(!alreadyhavePermission()){
      askCameraPermission();
    }

    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
      startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
    }
  }

  public void askCameraPermission(){
    int MyVersion = Build.VERSION.SDK_INT;
    if (MyVersion > Build.VERSION_CODES.LOLLIPOP_MR1) {
      if (!alreadyhavePermission()) {
        requestForSpecificPermission();
      }
    }
  }

  private boolean alreadyhavePermission() {
    int result = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA);
    return(result == PackageManager.PERMISSION_GRANTED);
  }

  private void requestForSpecificPermission() {
    requestPermissions(new String[]{Manifest.permission.CAMERA}, 0);
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
      Bundle extras = data.getExtras();
      Bitmap imageBitmap = (Bitmap) extras.get("data");
      mImageView.setImageBitmap(imageBitmap);
    }
  }


  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

    View view =  inflater.inflate(R.layout.activity_picture,null);

    ButterKnife.bind(this, view);
    return view;
  }
}
