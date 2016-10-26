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
        import com.fluffymadness.pilemdMobile.model.RackAdapter;
        import com.fluffymadness.pilemdMobile.model.SingleRack;
        import com.fluffymadness.pilemdMobile.model.SortBy;

        import java.io.File;
        import java.util.ArrayList;

public class MainActivity extends AppCompatActivity{

    private Toolbar toolbar;                              // Declaring the Toolbar Object
    private DataModel dataModel;

    android.support.v7.app.ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkIfFirstRun();
        String path=PreferenceManager.getDefaultSharedPreferences(this).getString("pref_root_directory", "");
        dataModel = new DataModel(path);

        setupToolBar();

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        LayoutInflater inflater = getLayoutInflater();
        View listHeaderView = inflater.inflate(R.layout.header,null, false);
        mDrawerList.addHeaderView(listHeaderView);
        setupDrawerToggle();


    }
    private void refreshRackDrawer(){
        //TODO : handle exception if racklist is null, racklist is null when folder doesn't exist
        try{
            ArrayList<SingleRack> racklist = dataModel.loadRackContent();
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

    private void selectItem(int position) {

        Fragment fragment = null;
        String rackName = ((SingleRack)mDrawerList.getAdapter().getItem(position)).getName();
        fragment = NotebookFragment.newInstance(rackName);


        if (fragment != null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.content_frame, fragment, "Notebook_Fragment");
            transaction.commit();
            mDrawerList.setItemChecked(position, true);
            mDrawerList.setSelection(position);
            mDrawerLayout.closeDrawer(mDrawerList);

        } else {
            Log.e("MainActivity", "Error in creating fragment");
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
            NotebookFragment notebookFragment = (NotebookFragment)getSupportFragmentManager().findFragmentByTag("Notebook_Fragment");
            if(notebookFragment != null && notebookFragment.isVisible())
                this.mDrawerLayout.openDrawer(Gravity.LEFT);
            else
                super.onBackPressed();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
