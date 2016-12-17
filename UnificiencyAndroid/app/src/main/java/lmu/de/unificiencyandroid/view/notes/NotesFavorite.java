package lmu.de.unificiencyandroid.view.notes;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import lmu.de.unificiencyandroid.R;

/**
 * Created by dev on 17.12.2016.
 */

public class NotesFavorite extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.notes_favorite, null);
    }
}