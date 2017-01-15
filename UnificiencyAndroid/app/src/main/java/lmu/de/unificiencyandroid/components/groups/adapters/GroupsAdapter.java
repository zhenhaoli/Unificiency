package lmu.de.unificiencyandroid.components.groups.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

import lmu.de.unificiencyandroid.R;
import lmu.de.unificiencyandroid.components.groups.GroupDetails;
import lmu.de.unificiencyandroid.components.groups.models.Group;

public class GroupsAdapter extends RecyclerView.Adapter<GroupsAdapter.ViewHolderGroups> {
  /*extras : groups_details_groupname_extra*/
  private LayoutInflater layoutInflater;
  private ArrayList<String> data;

  public GroupsAdapter(Context context) {
    this.layoutInflater = LayoutInflater.from(context);
    this.data = new ArrayList<String>();
    try {
      //TODO: this is blocking UI thread, but how to make result to that callback??
      Log.d("start", "before http");
      HttpRequestTask h = new HttpRequestTask();
      h.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

      List<Group> groups = h.get();

      Log.d("end", "after http");
      for(Group group : groups){
        this.data.add(group.toString());
      }
    }catch (ExecutionException | InterruptedException ei) {
      ei.printStackTrace();
    }
  }

  private class HttpRequestTask extends AsyncTask<Void, Void, ArrayList<Group>> {

    public HttpRequestTask() {

    }

    @Override
    protected void onPreExecute() {
      super.onPreExecute();
      Log.d("in pre group task", "yes");

    }

    protected ArrayList<Group> doInBackground(Void... params) {
      try {
        final String url = "http://li.mz-host.de:5048/groups";
        Log.d("url", url);
        RestTemplate restTemplate = new RestTemplate(true);
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

        ResponseEntity<Group[]> responseEntity = restTemplate.getForEntity(url, Group[].class);

        Group[] groups = responseEntity.getBody();

for(int i = 0; i<groups.length; i++){
  Log.d("groups", groups[i].toString());
}

        return new ArrayList<>(Arrays.asList(groups));
      } catch (Exception e) {
        Log.e("MainActivity", e.getMessage(), e);
      }

      return null;
    }

    protected void onPostExecute( ArrayList<Group> groups) {

    }

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

  }}


