package com.fluffymadness.pilemdMobile.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class SettingsActivity extends AppCompatActivity{
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);
        toolbar = (Toolbar) findViewById(R.id.toolbar); // Attaching the layout to the toolbar object
        if(toolbar!=null) {
            setSupportActionBar(toolbar);    // Setting toolbar as the ActionBar with setSupportActionBar() call
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Display the fragment as the main content.
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.settings_frame, new SettingsFragment() , "settings_frag")
                .commit();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // go to previous screen when app icon in action bar is clicked
                Intent intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//TODO: what is this?
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}