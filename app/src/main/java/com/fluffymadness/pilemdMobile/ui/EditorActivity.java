package com.fluffymadness.pilemdMobile.ui;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;

import com.fluffymadness.pilemdMobile.model.DataModel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import us.feras.mdv.MarkdownView;


/**
 * Created by fluffymadness on 9/30/2016.
 */

public class EditorActivity extends AppCompatActivity {

    private String folderpath;
    private Toolbar toolbar;
    private DataModel dataModel;
    private EditText editTextField;

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
    }
    @Override
    protected void onResume(){
        super.onResume();
        this.setTitle(R.string.title_add_note);
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
        if(note.length()!=0){
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
    protected void onStop(){
        super.onStop();
        saveNote();
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        saveNote();
    }


}
