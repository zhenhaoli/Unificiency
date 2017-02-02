package lmu.de.unificiencyandroid.components.notes;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import lmu.de.unificiencyandroid.R;
import lmu.de.unificiencyandroid.utils.ImageUtils;
import uk.co.senab.photoview.PhotoViewAttacher;

public class NoteImage extends AppCompatActivity  {

  @BindView(R.id.imgDisplay)
  ImageView imgDisplay;

  @OnClick(R.id.btnClose)
  public void back(){
    finish();
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.note_image);
    ButterKnife.bind(this);

    Bundle bundle = getIntent().getExtras();

    if(bundle.getString("mode").equals("url")) {
      String imageUrl = bundle.getString("imageUrl");
      ImageUtils.downloadToImageView(this, imageUrl, imgDisplay);
    } else {
      Bitmap bitmap = getIntent().getParcelableExtra("image");
      imgDisplay.setImageBitmap(bitmap);
    }

    PhotoViewAttacher mAttacher = new PhotoViewAttacher(imgDisplay);

  }
}
