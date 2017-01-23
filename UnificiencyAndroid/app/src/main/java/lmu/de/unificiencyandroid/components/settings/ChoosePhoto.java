package lmu.de.unificiencyandroid.components.settings;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import lmu.de.unificiencyandroid.R;
import lmu.de.unificiencyandroid.components.notes.NoteNew;

public class ChoosePhoto extends AppCompatActivity {

  static final int REQUEST_IMAGE_CAPTURE = 1;
  @BindView(R.id.camera)
  Button camera;

  @BindView(R.id.album)
  Button album;

  @BindView(R.id.toolbar_choose_photo)
  Toolbar toolbar;

  final int RESULT_LOAD_IMAGE=123;
  final int NOTE_ADD_IMAGE=456;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_choose_photo);
    ButterKnife.bind(this);
    setupToolbar();

    camera.setOnClickListener(new View.OnClickListener() {
      public void onClick(View view) {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getApplicationContext().getPackageManager()) != null) {
          startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
      }
    });

    album.setOnClickListener(new View.OnClickListener() {
      public void onClick(View view) {

        Intent intent = new Intent(
            Intent.ACTION_PICK,
            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, RESULT_LOAD_IMAGE);

      }
    });

  }

  public void setupToolbar(){
    toolbar.setTitle(R.string.camera_album);
    setSupportActionBar(toolbar);
    ActionBar ab = getSupportActionBar();
    ab.setHomeButtonEnabled(true);
    ab.setDisplayHomeAsUpEnabled(true);
  }

  /* restore back button functionality*/
  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()){
      case android.R.id.home: {
        onBackPressed();
        return true;
      }
      default:{return super.onOptionsItemSelected(item);}

    }
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
      Bundle extras = data.getExtras();
      Intent intent = new Intent(getApplicationContext(),ProfileEdit.class);
      intent.putExtras(extras);
      startActivity(intent);
    }
    if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {

      Intent intent = new Intent(getApplicationContext(),ProfileEdit.class);

      intent.setData(data.getData());
      startActivity(intent);
    }
    if (requestCode == NOTE_ADD_IMAGE && resultCode == RESULT_OK && null != data) {

      Intent intent = new Intent(getApplicationContext(),NoteNew.class);

      intent.setData(data.getData());

      startActivity(intent);
    }
  }
}
