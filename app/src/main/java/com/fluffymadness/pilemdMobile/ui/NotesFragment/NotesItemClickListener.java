package com.fluffymadness.pilemdMobile.ui.NotesFragment;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.fluffymadness.pilemdMobile.model.DataObjects.SingleNote;
import com.fluffymadness.pilemdMobile.ui.ViewNote;

/**
 * Created by fluffymadness on 11/11/2016.
 */

public class NotesItemClickListener implements ListView.OnItemClickListener {

    private NotesFragment mCallback;
    private ListView notesList;

    public NotesItemClickListener(NotesFragment fragment, ListView notesList){
        this.mCallback = fragment;
        this.notesList = notesList;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id){
        String path = ((SingleNote)notesList.getAdapter().getItem(position)).getFullPath();
        Intent intent = new Intent(mCallback.getActivity(), ViewNote.class);
        intent.putExtra("notePath",path);
        mCallback.getActivity().startActivity(intent);
    }
}
