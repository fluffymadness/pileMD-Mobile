package com.fluffymadness.pilemdMobile.ui.NotesFragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.fluffymadness.pilemdMobile.model.Adapters.NotesAdapter;
import com.fluffymadness.pilemdMobile.model.NotesEditListernerInterface;
import com.fluffymadness.pilemdMobile.model.Path;
import com.fluffymadness.pilemdMobile.model.DataObjects.SingleNote;
import com.fluffymadness.pilemdMobile.model.SortBy;
import com.fluffymadness.pilemdMobile.ui.EditorActivity;
import com.fluffymadness.pilemdMobile.ui.PathSupplier;
import com.fluffymadness.pilemdMobile.ui.R;

import java.util.ArrayList;

/**
 * Created by fluffymadness on 9/28/2016.
 */

public class NotesFragment extends Fragment implements NotesEditListernerInterface {
    private FloatingActionButton addNoteButton;
    private NotesAdapter adapter;
    private ListView notesList;
    private Path path;

    static final int ADD_NOTE_REQUEST = 1;

    public static NotesFragment newInstance() {
        NotesFragment f = new NotesFragment();
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notes, container, false);
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        this.path = ((PathSupplier)getActivity()).getPath();
    }

    @Override
    public void onResume(){
        super.onResume();
        getActivity().setTitle(path.getTitle());
        refreshNotes();
    }

    private void refreshNotes(){
        //TODO handle Exception if notelist is null
        ArrayList<SingleNote> notes = this.path.getNotes();
        adapter = new NotesAdapter(getActivity(),0, notes);
        adapter.sort(SortBy.DATE);
        adapter.setCallBack(this);
        addNoteButton = (FloatingActionButton)getView().findViewById(R.id.addNoteButton);
        notesList = (ListView) getView().findViewById(R.id.notesview);
        notesList.setAdapter(adapter);

        notesList.setOnItemClickListener(new NotesItemClickListener(this,notesList));
        notesList.setOnCreateContextMenuListener(new NotesItemLongClickListener(this,notesList,path));
        addNoteButton.setOnClickListener(new CreateNoteListener(this,path));
    }
    @Override
    public void editNote(String name) {
        String folderpath = path.getCurrentPath();
        Log.d("folderpath",folderpath);
        Log.d("notename",name);
        Intent intent = new Intent(getActivity(), EditorActivity.class);
        intent.putExtra("folderPath",folderpath);
        intent.putExtra("noteName", name);
        startActivity(intent);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == ADD_NOTE_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
             //  refreshNotes();
            }
        }
    }
}
