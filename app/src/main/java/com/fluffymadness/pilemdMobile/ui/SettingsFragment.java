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

public class SettingsFragment extends PreferenceFragment{

    private File currentRootDirectory;
    private FragmentActivity myContext;
    private Preference rootDirPref;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        updateRootDirSummary();
    }

    void addDirectoryChooserAsFloatingFragment() {
        DialogFragment directoryChooserFragment = DirectoryChooserFragment.newInstance(currentRootDirectory);
        FragmentTransaction transaction = myContext.getSupportFragmentManager().beginTransaction();
        directoryChooserFragment.show(transaction, "RDC");
    }

    public void updateRootDir(OnDirectoryChosenEvent event) {
        currentRootDirectory = event.getFile();
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(myContext).edit();
        editor.putString("pref_root_directory", currentRootDirectory.toString());
        editor.commit();
        updateRootDirSummary();
    }
    public void updateRootDirSummary(){
        rootDirPref.setSummary(PreferenceManager.getDefaultSharedPreferences(myContext).getString("pref_root_directory", ""));
        currentRootDirectory = new File(rootDirPref.getSummary().toString());
    }

}