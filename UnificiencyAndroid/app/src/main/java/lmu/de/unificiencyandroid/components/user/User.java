package lmu.de.unificiencyandroid.components.user;

import android.provider.ContactsContract;

import java.util.ArrayList;

import lmu.de.unificiencyandroid.components.groups.models.Group;
import lmu.de.unificiencyandroid.components.notes.Note;

/**
 * Created by robertMueller on 02.01.17.
 */

public class User {
    ContactsContract.CommonDataKinds.Email email;
    String nickname;
    String major;
    ArrayList<Group> groups;
    ArrayList<Note> notes;
}