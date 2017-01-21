package lmu.de.unificiencyandroid.components.buildings;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;
import lmu.de.unificiencyandroid.R;

public class BuildingsAll extends BuildingsBase {

  @BindView(R.id.all_building_listview)
  GridView allBuildings;

  @OnItemClick(R.id.all_building_listview)
  public void onBuildingClick(AdapterView<?> arg0, View arg1, int position, long arg3)
  {
    Building building = (Building) allBuildings.getItemAtPosition(position);
    Intent buildungDetails = new Intent(getActivity(),BuildingDetails.class);
    buildungDetails.putExtra("address", building.getAddress());
    buildungDetails.putExtra("city", building.getCity());

    startActivity(buildungDetails);
  }

  @Nullable
  @Override
  public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    super.onCreateView(inflater, container, savedInstanceState);

    View view = inflater.inflate(R.layout.buildings_all,null);
    ButterKnife.bind(this, view);

    BuildingsAllAdapter adapter= new BuildingsAllAdapter(getContext(), buildings);

    allBuildings.setAdapter(adapter);

    return view;
  }

}