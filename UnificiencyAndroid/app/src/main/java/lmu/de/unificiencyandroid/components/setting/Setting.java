package lmu.de.unificiencyandroid.components.setting;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import lmu.de.unificiencyandroid.R;

/**
 * Created by ostdong on 10/01/2017.
 */

public class Setting extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.setting,null);

    }
}
