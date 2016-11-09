package com.fluffymadness.pilemdMobile.ui;
        import android.content.Intent;
        import android.content.SharedPreferences;
        import android.os.Bundle;
        import android.os.Environment;
        import android.preference.PreferenceManager;
        import android.support.v4.app.Fragment;
        import android.support.v4.app.FragmentManager;
        import android.support.v4.widget.DrawerLayout;
        import android.support.v7.app.AppCompatActivity;
        import android.support.v4.app.FragmentTransaction;
        import android.support.v7.widget.Toolbar;
        import android.util.Log;
        import android.view.Gravity;
        import android.view.LayoutInflater;
        import android.view.Menu;
        import android.view.MenuItem;
        import android.view.View;
        import android.widget.AdapterView;
        import android.widget.ListView;

        import com.fluffymadness.pilemdMobile.model.DataModel;
        import com.fluffymadness.pilemdMobile.model.Path;
        import com.fluffymadness.pilemdMobile.model.RackAdapter;
        import com.fluffymadness.pilemdMobile.model.SingleRack;
        import com.fluffymadness.pilemdMobile.model.SortBy;

        import java.io.File;
        import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements PathSupplier{

    private Toolbar toolbar;                              // Declaring the Toolbar Object
    android.support.v7.app.ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private Path path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkIfFirstRun();
        String path=PreferenceManager.getDefaultSharedPreferences(this).getString("pref_root_directory", "");
        this.path = new Path(path);
        setupToolBar();

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        LayoutInflater inflater = getLayoutInflater();
        View listHeaderView = inflater.inflate(R.layout.header,null, false);
        mDrawerList.addHeaderView(listHeaderView);
        setupDrawerToggle();
        loadDefaultNotebook();


    }
    private void refreshRackDrawer(){
        //TODO : handle exception if racklist is null, racklist is null when folder doesn't exist
        try{
            ArrayList<SingleRack> racklist = path.getRacks();
            RackAdapter adapter = new RackAdapter(this, 0, racklist);
            adapter.sort(SortBy.NAME);
            mDrawerList.setAdapter(adapter);
            mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        }
        catch(NullPointerException e){
        }


    }
    @Override
    protected void onResume(){
        super.onResume();
        refreshRackDrawer();
    }
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    private void setupToolBar(){
        toolbar = (Toolbar) findViewById(R.id.toolbar); // Attaching the layout to the toolbar object
        if(toolbar!=null) {
            setSupportActionBar(toolbar);    // Setting toolbar as the ActionBar with setSupportActionBar() call
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }
    }
    void setupDrawerToggle(){
        mDrawerToggle = new android.support.v7.app.ActionBarDrawerToggle(this,mDrawerLayout,toolbar,R.string.app_name, R.string.app_name);
        //This is necessary to change the icon of the Drawer Toggle upon state change.
        mDrawerToggle.syncState();
    }


    private class DrawerItemClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }

    }
    private void loadDefaultNotebook(){
        String path = PreferenceManager.getDefaultSharedPreferences(this).getString("pref_default_notebook", "");
        if(!path.equals("")){
            File f1 = new File(path);
            if(f1.exists()){
                this.path.setCurrentPath(path);
                Fragment fragment = NotesFragment.newInstance();
                if (fragment != null) {
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.content_frame, fragment, "Notebook_Fragment");
                    transaction.commit();
                }
            }
        }
    }
    private void selectItem(int position) {
        String rackName = ((SingleRack)mDrawerList.getAdapter().getItem(position)).getName();
        createNotebookFragment(rackName);
        mDrawerList.setItemChecked(position, true);
        mDrawerList.setSelection(position);
        mDrawerLayout.closeDrawer(mDrawerList);
    }
    private boolean createNotebookFragment(String path){

        Fragment fragment = NotebookFragment.newInstance();
        this.path.resetPath();
        this.path.goForward(path);

        if (fragment != null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.content_frame, fragment, "Notebook_Fragment");
            transaction.commit();
            return true;
        } else {
            return false;
        }
    }
    private void showSettings() {
        Intent settingsIntent = new Intent(this, SettingsActivity.class);
        startActivity(settingsIntent);
    }
    void checkIfFirstRun(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if (prefs.getBoolean("firstrun", true)) {
            File rootDir = Environment.getExternalStorageDirectory();
            String full = rootDir + "/pilemd";
            String fullnotebook = full+"/FirstNotebook";
            File fullpath = new File(full);
            File defaultnotebookpath = new File(fullnotebook);
            if(!(fullpath.exists() && fullpath.isDirectory())){
                fullpath.mkdirs();
                defaultnotebookpath.mkdirs();
            }

            prefs.edit().putString("pref_root_directory", full).commit();
            prefs.edit().putBoolean("firstrun", false).commit();

            prefs.edit().putString("pref_default_notebook", fullnotebook).commit();
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
    @Override
    public void onBackPressed() {

        if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
            super.onBackPressed();
        } else {
            NotebookFragment notebookFragment = (NotebookFragment) getSupportFragmentManager().findFragmentByTag("Notebook_Fragment");
            NotesFragment notesFragment = (NotesFragment) getSupportFragmentManager().findFragmentByTag("Notes_Fragment");
            if (notebookFragment != null && notebookFragment.isVisible()) {
                //path.getBack();
                this.mDrawerLayout.openDrawer(Gravity.LEFT);
            }
            else if(notesFragment !=null && notesFragment.isVisible()){
                path.getBack();
                super.onBackPressed();
            }
            else {
                super.onBackPressed();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
    public Path getPath(){
        return this.path;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("currentPath", this.path.getCurrentPath());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        this.path.setCurrentPath(savedInstanceState.getString("currentPath"));
    }
}

interface PathSupplier{
    Path getPath();
}
