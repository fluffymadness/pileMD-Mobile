package com.fluffymadness.pilemdMobile.ui;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.turhanoz.android.reactivedirectorychooser.event.OnDirectoryCancelEvent;
import com.turhanoz.android.reactivedirectorychooser.event.OnDirectoryChosenEvent;
import com.turhanoz.android.reactivedirectorychooser.ui.DirectoryChooserFragment;
import com.turhanoz.android.reactivedirectorychooser.ui.OnDirectoryChooserFragmentInteraction;

import java.io.File;

import de.greenrobot.event.EventBus;

public class SettingsFragment extends PreferenceFragment implements OnDirectoryChooserFragmentInteraction {

    private File currentRootDirectory = Environment.getExternalStorageDirectory();
    private FragmentActivity myContext;
    private Preference rootDirPref;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);
        this.setupRootDirPref();
    }
    @Override
    public void onAttach(Activity activity) {
        myContext=(FragmentActivity) activity;
        super.onAttach(activity);
    }

    void setupRootDirPref(){
        this.rootDirPref = (Preference) findPreference("pref_root_directory");
        rootDirPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                addDirectoryChooserAsFloatingFragment();
                return true;
            }
        });
    }

    void addDirectoryChooserAsFloatingFragment() {
        DialogFragment directoryChooserFragment = DirectoryChooserFragment.newInstance(currentRootDirectory);
        FragmentTransaction transaction = myContext.getSupportFragmentManager().beginTransaction();
        directoryChooserFragment.show(transaction, "RDC");
    }

    @Override
    public void onEvent(OnDirectoryChosenEvent event) {
        currentRootDirectory = event.getFile();
        Log.d("test",currentRootDirectory.toString());
        PreferenceManager.getDefaultSharedPreferences(myContext).edit().putString("pref_root_directory", event.getFile().toString()).commit();
        rootDirPref.setSummary(PreferenceManager.getDefaultSharedPreferences(myContext).getString("pref_root_directory", ""));
    }

    @Override
    public void onEvent(OnDirectoryCancelEvent event) {
    }
}