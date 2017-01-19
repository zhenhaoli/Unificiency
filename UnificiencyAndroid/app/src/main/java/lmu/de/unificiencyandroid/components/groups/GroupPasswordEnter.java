package lmu.de.unificiencyandroid.components.groups;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import lmu.de.unificiencyandroid.R;

public class GroupPasswordEnter extends DialogFragment {

    GroupPasswordEnterListener epwListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.group_password_enter, null);
        builder.setView(view);
        builder.setMessage(R.string.groups_details_password_title)
                .setTitle(R.string.groups_details_password_message);
        builder.setPositiveButton(R.string.groups_details_password_send, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                EditText pw = (EditText) view.findViewById(R.id.groups_details_password);
                epwListener.onPwEntered(pw.getText().toString());
            }
        });
        builder.setNegativeButton(R.string.groups_details_password_cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        AlertDialog dialog = builder.create();
        return dialog;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            epwListener = (GroupPasswordEnterListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(context.toString()
                    + " must implement NoticeDialogListener");
        }

    }
}