package com.fluffymadness.pilemdMobile.ui;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    private Toolbar toolbar;                              // Declaring the Toolbar Object
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private DataModel dataModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkIfFirstRun();
        toolbar = (Toolbar) findViewById(R.id.toolbar); // Attaching the layout to the toolbar object
        if(toolbar!=null) {
            setSupportActionBar(toolbar);    // Setting toolbar as the ActionBar with setSupportActionBar() call
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }
        Fragment frag =
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        if(frag instanceof NavigationDrawerFragment)
            mNavigationDrawerFragment = (NavigationDrawerFragment)frag;
        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));



    }
    @Override
    protected void onResume(){
        super.onResume();
        String path=PreferenceManager.getDefaultSharedPreferences(this).getString("pref_root_directory", "");
        dataModel = new DataModel(path);
        loadRackContent();



    }
    public void loadRackContent(){
        String rackDir = PreferenceManager.getDefaultSharedPreferences(this).getString("pref_root_directory","");
        File rackDirFile = new File(rackDir);
        if(rackDirFile.exists()) {
            ArrayList<File> racklist = dataModel.getRacks();
            if(racklist!=null){

            }

        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            showSettings();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showSettings() {
        Intent settingsIntent = new Intent(this, SettingsActivity.class);
        startActivity(settingsIntent);
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments

        if(position == 0){
            /*FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.container, new InternetFragment())
                    .commit();*/
        }
        if(position == 1){

        }
        if(position == 2){
            showSettings();
        }
    }
    void checkIfFirstRun(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if (prefs.getBoolean("firstrun", true)) {
            File rootDir = Environment.getExternalStorageDirectory();
            String full = rootDir + "/pilemd";
            File fullpath = new File(full);
            if(!(fullpath.exists() && fullpath.isDirectory())){
                fullpath.mkdirs();
            }

            prefs.edit().putString("pref_root_directory", rootDir.toString()+"/pilemd").commit();
            prefs.edit().putBoolean("firstrun", false).commit();
        }
    }

}
