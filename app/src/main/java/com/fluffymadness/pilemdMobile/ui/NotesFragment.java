package com.fluffymadness.pilemdMobile.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.FileObserver;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.fluffymadness.pilemdMobile.model.DataModel;
import com.fluffymadness.pilemdMobile.model.NotesAdapter;
import com.fluffymadness.pilemdMobile.model.NotesEditListernerInterface;
import com.fluffymadness.pilemdMobile.model.SingleNote;
import com.fluffymadness.pilemdMobile.model.SortBy;

import java.util.ArrayList;

/**
 * Created by fluffymadness on 9/28/2016.
 */

public class NotesFragment extends Fragment implements NotesEditListernerInterface {
    private DataModel dataModel;
    private String rackName;
    private String notebookName;
    private FloatingActionButton addNoteButton;
    private FileObserver directoryObserver;
    private NotesAdapter adapter;

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
        View view = inflater.inflate(R.layout.fragment_notes, container, false);
        addNoteButton = (FloatingActionButton)view.findViewById(R.id.addNoteButton);
        addNoteButton.setOnClickListener(new FloatingButtonClickListener());
        return view;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
        rackName = getArguments().getString("rackName");
        notebookName = getArguments().getString("notebookName");
        String path= PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("pref_root_directory", "");
        dataModel = new DataModel(path);
        /*directoryObserver = new FileObserver(path) {
            @Override
            public void onEvent(int i, String s) {
                Log.d("event","fileobserverevent");
                getActivity().runOnUiThread(new Runnable(){
                    @Override
                    public void run() {
                        NotesFragment.this.refreshNotes();
                    }


                });
            }
        };
        directoryObserver.startWatching();*/

    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId()==R.id.notesview) {
                menu.add(Menu.NONE, 0, 0, R.string.delete_note);
        }
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        int menuItemIndex = item.getItemId();
        Log.d("pos",String.valueOf(info.position));
        if(menuItemIndex == 0){
            String path =((SingleNote)adapter.getItem(info.position)).getPath();
            dataModel.deleteNote(path);
            adapter.remove(adapter.getItem(info.position));
            adapter.notifyDataSetChanged();
        }

        return true;
    }

    @Override
    public void onResume(){
        super.onResume();
        Log.d("gets called","gets called");
        getActivity().setTitle(this.notebookName);
        refreshNotes();
    }

    private void refreshNotes(){
        //TODO handle Exception if notelist is null
        ArrayList<SingleNote> notes = dataModel.getNotes(rackName, notebookName);
        adapter = new NotesAdapter(getActivity(),0, notes);
        adapter.sort(SortBy.DATE);
        notesList = (ListView) getView().findViewById(R.id.notesview);
        notesList.setAdapter(adapter);
        notesList.setOnItemClickListener(new NotesItemClickListener());
        registerForContextMenu(notesList);
    }

    private void addNote(){
        String folderpath = this.rackName+"/"+this.notebookName;
        Intent intent = new Intent(getActivity(), EditorActivity.class);
        intent.putExtra("folderPath",folderpath);
        startActivity(intent);
    }

    @Override
    public void editNote(String name) {
        String folderpath = this.rackName+"/"+this.notebookName;
        Intent intent = new Intent(getActivity(), EditorActivity.class);
        intent.putExtra("folderPath",folderpath);
        intent.putExtra("noteName", name);
        startActivity(intent);
    }

    private class FloatingButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            addNote();
        }
    }
    private class NotesItemClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }

    }

    private void selectItem(int position) {
        String fullPath = ((SingleNote)this.notesList.getAdapter().getItem(position)).getPath();
        Intent intent = new Intent(getActivity(), ViewNote.class);
        intent.putExtra("notePath",fullPath);
        startActivity(intent);
    }
}
