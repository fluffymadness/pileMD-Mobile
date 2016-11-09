package com.fluffymadness.pilemdMobile.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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

import com.fluffymadness.pilemdMobile.model.NotesAdapter;
import com.fluffymadness.pilemdMobile.model.NotesEditListernerInterface;
import com.fluffymadness.pilemdMobile.model.Path;
import com.fluffymadness.pilemdMobile.model.SingleNote;
import com.fluffymadness.pilemdMobile.model.SortBy;

import java.io.File;
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
        addNoteButton = (FloatingActionButton)view.findViewById(R.id.addNoteButton);
        addNoteButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                addNote();
            }
        });
        return view;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        this.path = ((PathSupplier)getActivity()).getPath();
        super.onCreate(savedInstanceState);

    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId()==R.id.notesview) {
            menu.add(Menu.NONE, 0, 0, R.string.move_note);
            menu.add(Menu.NONE, 1, 1, R.string.delete_note);
        }
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        int menuItemIndex = item.getItemId();
        Log.d("pos",String.valueOf(info.position));
        if(menuItemIndex == 0){
            final String oldNoteName=((SingleNote)adapter.getItem(info.position)).getName();
            final String fullpathold = ((SingleNote)adapter.getItem(info.position)).getFullPath();
            final String oldPath = ((SingleNote)adapter.getItem(info.position)).getPath();

            File mPath = new File(oldPath);
            FileDialog moveNoteDialog = new FileDialog(this.getActivity(), mPath);
            moveNoteDialog.setSelectDirectoryOption(true);
            moveNoteDialog.addDirectoryListener(new FileDialog.DirectorySelectedListener() {
                public void directorySelected(File directory) {
                    path.moveFile(directory.getPath(),oldNoteName);
                }
            });
            moveNoteDialog.showDialog();
            adapter.remove(adapter.getItem(info.position));
            adapter.notifyDataSetChanged();

        }
        if(menuItemIndex == 1){
            String path =((SingleNote)adapter.getItem(info.position)).getFullPath();
            Log.d("fulldeletepath",path);
            this.path.deleteNote(path);
            adapter.remove(adapter.getItem(info.position));
            adapter.notifyDataSetChanged();
        }
        return true;
    }

    @Override
    public void onResume(){
        super.onResume();
        //getActivity().setTitle(this.notebookName);
        refreshNotes();
    }

    private void refreshNotes(){
        //TODO handle Exception if notelist is null
        ArrayList<SingleNote> notes = ((PathSupplier)getActivity()).getPath().getNotes();
        adapter = new NotesAdapter(getActivity(),0, notes);
        adapter.sort(SortBy.DATE);
        adapter.setCallBack(this);
        notesList = (ListView) getView().findViewById(R.id.notesview);
        notesList.setAdapter(adapter);
        notesList.setOnItemClickListener(new NotesItemClickListener());
        registerForContextMenu(notesList);
    }

    private void addNote(){
        String folderpath = path.getCurrentPath();
        Intent intent = new Intent(getActivity(), EditorActivity.class);
        intent.putExtra("folderPath",folderpath);
        startActivityForResult(intent, ADD_NOTE_REQUEST);
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
    private class NotesItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String path = ((SingleNote)NotesFragment.this.notesList.getAdapter().getItem(position)).getFullPath();
            Intent intent = new Intent(getActivity(), ViewNote.class);
            intent.putExtra("notePath",path);
            startActivity(intent);
        }

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
