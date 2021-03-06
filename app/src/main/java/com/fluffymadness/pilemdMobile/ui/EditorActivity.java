package com.fluffymadness.pilemdMobile.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;

import com.fluffymadness.pilemdMobile.model.Path;

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
    private Path path;
    private EditText editTextField;
    private Date fileLastModifiedDate;
    private String previousNoteTitle;
    private boolean onBackPressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_note);
        setupToolBar();
        Intent intent = getIntent();
        folderpath = intent.getStringExtra("folderPath");
        path = new Path(folderpath);
        editTextField = (EditText)findViewById(R.id.edit_text);

        noteToEdit = intent.getStringExtra("noteName");
        onBackPressed = false;
        loadNote();
    }
    @Override
    protected void onResume(){
        super.onResume();

    }
    private void loadNote(){
        if(noteToEdit != null){
            this.setTitle(R.string.title_edit_note);
            String noteText = path.getNote(noteToEdit);
            this.fileLastModifiedDate = path.getLastModifiedDate(noteToEdit);
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
            path.modifyNote(folderpath,previousNoteTitle,getNoteTitle(note),note,this.fileLastModifiedDate);
        }
        else if(note.length()!=0){
            path.createNote(folderpath,getNoteTitle(note),note);
            Intent result = getIntent();
            result.putExtra("RESULT_STRING", folderpath+"/"+getNoteTitle(note)+".md");
            setResult(Activity.RESULT_OK, result);
            finish();
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
        if(!onBackPressed)
            saveNote();
    }

    /*Fixes result canceled on back button press*/
    @Override
    public void onBackPressed() {
        saveNote();
        onBackPressed = true;
        super.onBackPressed();
    }


}
