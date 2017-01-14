package lmu.de.unificiencyandroid.components.setting;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import lmu.de.unificiencyandroid.R;
import lmu.de.unificiencyandroid.components.groups.models.Group;
import lmu.de.unificiencyandroid.components.notes.Note;

/**
 * Created by ostdong on 12/01/2017.
 */

public class SettingNotesAdapter extends ArrayAdapter {

    public SettingNotesAdapter(Context context, int textViewResourceId,
                                List<Note> notes) {
        super(context, textViewResourceId, notes);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Note note = (Note) getItem(position);

        LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.settingnote_list_item, null);

        TextView note_course=(TextView) view.findViewById(R.id.settingnote_course);
        note_course.setText(note.getCourse());

        TextView note_title=(TextView) view.findViewById(R.id.settingnote_title);
        note_title.setText(note.getTitle());

        return view;
    }
}
