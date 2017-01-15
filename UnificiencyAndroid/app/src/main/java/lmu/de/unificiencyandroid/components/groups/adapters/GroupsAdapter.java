package lmu.de.unificiencyandroid.components.groups.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;

import lmu.de.unificiencyandroid.R;
import lmu.de.unificiencyandroid.components.buildings.Building;
import lmu.de.unificiencyandroid.components.buildings.BuildingDetails;
import lmu.de.unificiencyandroid.components.buildings.BuildingsAdapter;
import lmu.de.unificiencyandroid.components.groups.GroupDetails;

import static android.R.attr.x;
import static lmu.de.unificiencyandroid.R.id.all_building_listview;

public class GroupsAdapter extends RecyclerView.Adapter<GroupsAdapter.ViewHolderGroups> {
    /*extras : groups_details_groupname_extra*/
    private LayoutInflater layoutInflater;
    private ArrayList<String> data;

    public GroupsAdapter(Context context) {
        this.layoutInflater = LayoutInflater.from(context);
        this.data = new ArrayList<String>();
        this.data.addAll(Arrays.asList("The Nonames", "The Pirates", "The Langstrumpfs", "The Arnolds", "Chinese Democracy", "Colony", "Fear is the Weakness", "Dijkstra", "Analysis", "Tim und Struppi "));
        this.data.addAll(Arrays.asList("The Nonames", "The Pirates", "The Langstrumpfs", "The Arnolds", "Chinese Democracy", "Colony", "Fear is the Weakness", "Dijkstra", "Analysis", "Tim und Struppi "));
        this.data
    }

    @Override
    public ViewHolderGroups onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.group_list_item, parent, false);
        ViewHolderGroups viewHolderGroups = new ViewHolderGroups(view);
        return viewHolderGroups;
    }

    @Override
    public void onBindViewHolder(ViewHolderGroups holder, int position) {
        //filling data in
        String name = this.data.get(position);
        holder.groupNameTextView.setText(name);
        //make colored circle with text
        ColorGenerator generator = ColorGenerator.MATERIAL;
        int color = generator.getColor(name);
        TextDrawable drawable = TextDrawable.builder()
            .buildRoundRect(name.substring(0,2), color, 100);
        holder.groupFirtCharsImageView.setImageDrawable(drawable);
    }

    @Override
    public int getItemCount() {
        return this.data.size();
    }

    static class ViewHolderGroups extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView groupNameTextView;
        public ImageView groupFirtCharsImageView;
        public ViewHolderGroups(View itemView) {
            super(itemView);
            this.groupNameTextView = (TextView) itemView.findViewById(R.id.group_name);
            this.groupFirtCharsImageView = (ImageView) itemView.findViewById(R.id.group_name_first_chars);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            String groupName = String.valueOf(groupNameTextView.getText());
            Context context = view.getContext();
            Intent intent = new Intent(context, GroupDetails.class);
            intent.putExtra(context.getString(R.string.groups_details_groupname_extra), groupName);
            context.startActivity(intent);
        }

        private class HttpRequestTask extends AsyncTask<Void, Void, ArrayList<Building>> {
            protected ArrayList<Building> doInBackground(Void... params) {
                try {
                    final String url = "http://li.mz-host.de:5048/buildings";
                    RestTemplate restTemplate = new RestTemplate(true);
                    restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

                    ResponseEntity<Building[]> responseEntity = restTemplate.getForEntity(url, Building[].class);

                    Building[] buildings = responseEntity.getBody();

                    return new ArrayList<>(Arrays.asList(buildings));
                } catch (Exception e) {
                    Log.e("MainActivity", e.getMessage(), e);
                }

                return null;
            }

            protected void onPostExecute( ArrayList<Building> buildings) {
                all_building_listview = (ListView) x.findViewById(all_building_listview);

                BuildingsAdapter adapter= new BuildingsAdapter(getContext(), android.R.layout.simple_list_item_1, buildings);
                all_building_listview.setAdapter(adapter);

                all_building_listview.setOnItemClickListener(new AdapterView.OnItemClickListener()
                {
                    @Override
                    public void onItemClick(AdapterView<?> arg0, View arg1,int position, long arg3)
                    {

                        Building building=(Building) all_building_listview.getItemAtPosition(position);
                        Intent buildungDetails=new Intent(getActivity(),BuildingDetails.class);
                        buildungDetails.putExtra("address", building.getAddress());
                        buildungDetails.putExtra("city", building.getCity());
                        startActivity(buildungDetails);
                    }
                });

            }

        }
    }}


