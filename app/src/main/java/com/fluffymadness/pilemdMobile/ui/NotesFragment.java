package com.fluffymadness.pilemdMobile.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.fluffymadness.pilemdMobile.model.DataModel;
import com.fluffymadness.pilemdMobile.model.NotesAdapter;
import com.fluffymadness.pilemdMobile.model.SingleNote;

import java.util.ArrayList;

/**
 * Created by fluffymadness on 9/28/2016.
 */

public class NotesFragment extends Fragment {
    private DataModel dataModel;
    private FragmentActivity myContext;
    private String rackName;
    private String notebookName;

    private ListView notesList;

    public static NotesFragment newInstance(String rackName, String notebookName) {
        NotesFragment f = new NotesFragment();
        Bundle args = new Bundle();
        args.putString("rackName", rackName);
        args.putString("notebookName", notebookName);
        f.setArguments(args);
        return f;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notes, container, false);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
        rackName = getArguments().getString("rackName");
        notebookName = getArguments().getString("notebookName");
        String path= PreferenceManager.getDefaultSharedPreferences(myContext).getString("pref_root_directory", "");
        dataModel = new DataModel(path);
    }
    @Override
    public void onResume(){
        super.onResume();
        getActivity().setTitle(this.notebookName);
        refreshNotes();
    }
    @Override
    public void onAttach(Activity activity) {
        myContext=(FragmentActivity) activity;
        super.onAttach(activity);
    }

    private void refreshNotes(){
        //TODO handle Exception if notelist is null
        ArrayList<SingleNote> notes = dataModel.getNotes(rackName, notebookName);
        NotesAdapter adapter = new NotesAdapter(myContext, notes);
        notesList = (ListView) getView().findViewById(R.id.notebookview);
        notesList.setAdapter(adapter);
        notesList.setOnItemClickListener(new NotesItemClickListener());

    }
    private class NotesItemClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }

    }

    private void selectItem(int position) {
        String fullPath = ((SingleNote)this.notesList.getAdapter().getItem(position)).getPath();
        Intent intent = new Intent(myContext, ViewNote.class);
        intent.putExtra("notePath",fullPath);
        startActivity(intent);
    }
}
