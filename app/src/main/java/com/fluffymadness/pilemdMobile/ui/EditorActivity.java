package com.fluffymadness.pilemdMobile.ui;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.EditText;

import com.fluffymadness.pilemdMobile.model.DataModel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.Date;


/**
 * Created by fluffymadness on 9/30/2016.
 */

public class EditorActivity extends AppCompatActivity {

    private String folderpath;
    private String noteToEdit;
    private Toolbar toolbar;
    private DataModel dataModel;
    private EditText editTextField;
    private Date fileLastModifiedDate;
    private String previousNoteTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_note);

        String path= PreferenceManager.getDefaultSharedPreferences(this).getString("pref_root_directory", "");
        dataModel = new DataModel(path);
        setupToolBar();

        Intent intent = getIntent();
        folderpath = path + "/" + intent.getStringExtra("folderPath");
        editTextField = (EditText)findViewById(R.id.edit_text);

        noteToEdit = intent.getStringExtra("noteName");
        loadNote();
    }
    @Override
    protected void onResume(){
        super.onResume();

    }
    private void loadNote(){
        if(noteToEdit != null){
            this.setTitle(R.string.title_edit_note);
            String noteText = dataModel.getNote(folderpath+"/"+noteToEdit);
            this.fileLastModifiedDate = dataModel.getLastModifiedDate(folderpath+"/"+noteToEdit);
            editTextField.setText(noteText);
            this.previousNoteTitle = getNoteTitle(noteText);
        }
        else{
            this.setTitle(R.string.title_add_note);
        }

    }
    private void setupToolBar(){
        toolbar = (Toolbar) findViewById(R.id.toolbar); // Attaching the layout to the toolbar object
        if(toolbar!=null) {
            setSupportActionBar(toolbar);    // Setting toolbar as the ActionBar with setSupportActionBar() call
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }
    }
    private void saveNote(){
        String note = editTextField.getText().toString();
        if(this.noteToEdit != null){
            Log.d("bla",previousNoteTitle);
            Log.d("blo",getNoteTitle(note));
            dataModel.modifyNote(folderpath,previousNoteTitle,getNoteTitle(note),note,this.fileLastModifiedDate);
        }
        else if(note.length()!=0){
            Log.d("create","createnote");
            dataModel.createNote(folderpath,getNoteTitle(note),note);
        }

        //TODO, only overwrite when date is newer then the already synced version, else make a copy with _1
        //also some exception handling would be nice here
    }
    private String getNoteTitle(String note){
        BufferedReader reader = new BufferedReader(new StringReader(note));
        try {
            String title = reader.readLine();
            return title;
        } catch (IOException e) {
            e.printStackTrace();
            return "No Title";
        }
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        saveNote();
    }


}
