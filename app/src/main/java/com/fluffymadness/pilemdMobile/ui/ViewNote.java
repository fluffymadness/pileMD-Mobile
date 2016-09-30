package com.fluffymadness.pilemdMobile.ui;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.fluffymadness.pilemdMobile.model.DataModel;

import us.feras.mdv.MarkdownView;

/**
 * Created by fluffymadness on 9/28/2016.
 */

public class ViewNote extends AppCompatActivity {

    private Toolbar toolbar;
    private DataModel dataModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_note);

        String path= PreferenceManager.getDefaultSharedPreferences(this).getString("pref_root_directory", "");
        dataModel = new DataModel(path);
        setupToolBar();

        Intent intent = getIntent();
        String notepath = intent.getStringExtra("notePath");
        MarkdownView markdownView = (MarkdownView) findViewById(R.id.markdownView);

        //TODO error handling if null
        markdownView.loadMarkdown(dataModel.getNote(notepath));

    }
    private void setupToolBar(){
        toolbar = (Toolbar) findViewById(R.id.toolbar); // Attaching the layout to the toolbar object
        if(toolbar!=null) {
            setSupportActionBar(toolbar);    // Setting toolbar as the ActionBar with setSupportActionBar() call
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }
    }
}
