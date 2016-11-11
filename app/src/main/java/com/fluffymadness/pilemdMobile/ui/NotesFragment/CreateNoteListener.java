package com.fluffymadness.pilemdMobile.ui.NotesFragment;

import android.content.Intent;
import android.view.View;

import com.fluffymadness.pilemdMobile.model.Path;
import com.fluffymadness.pilemdMobile.ui.EditorActivity;

/**
 * Created by fluffymadness on 11/11/2016.
 */

public class CreateNoteListener implements View.OnClickListener {

    private NotesFragment mCallback;
    private Path path;
    static final int ADD_NOTE_REQUEST = 1;

    public CreateNoteListener(NotesFragment fragment, Path path){
        this.mCallback = fragment;
        this.path = path;
    }

    @Override
    public void onClick(View view) {
        this.addNote();
    }
    private void addNote(){
        String folderpath = path.getCurrentPath();
        Intent intent = new Intent(mCallback.getActivity(), EditorActivity.class);
        intent.putExtra("folderPath",folderpath);
        mCallback.getActivity().startActivityForResult(intent, ADD_NOTE_REQUEST);
    }
}
