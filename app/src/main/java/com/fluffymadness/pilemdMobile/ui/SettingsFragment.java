package com.fluffymadness.pilemdMobile.ui;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceManager;

import java.io.File;

public class  SettingsFragment extends PreferenceFragmentCompat {

    private File currentRootDirectory;
    private File currentNotebookDirectory;
    private Preference rootDirPref;
    private Preference currentNotebookPref;
    private FileDialog rootFolderDialog;
    private FileDialog defaultNotebookDialog;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.preferences);
        this.setupRootDirPref();
        this.setupDefaultNotebookPref();
        restorePref();

        File mPath = currentRootDirectory;
        rootFolderDialog = new FileDialog(this.getActivity(), mPath);
        rootFolderDialog.setSelectDirectoryOption(true);
        rootFolderDialog.addDirectoryListener(new FileDialog.DirectorySelectedListener() {
            public void directorySelected(File directory) {
                updateRootDir(directory);
                updateRootDirSummary();
            }
        });


        File mPath2 = currentNotebookDirectory;
        defaultNotebookDialog = new FileDialog(this.getActivity(), mPath2);
        defaultNotebookDialog.setSelectDirectoryOption(true);
        defaultNotebookDialog.addDirectoryListener(new FileDialog.DirectorySelectedListener() {
            public void directorySelected(File directory) {
                updateDefaultNotebook(directory);
                updateDefaultNotebookSummary();
            }
        });


    }
    void restorePref(){
        updateRootDirSummary();
        updateDefaultNotebookSummary();
    }

    void setupRootDirPref(){
        this.rootDirPref = findPreference("pref_root_directory");
        rootDirPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                rootFolderDialog.showDialog();
                return true;
            }
        });
    }


    public void updateDefaultNotebook(File directory) {
        this.currentNotebookDirectory= directory;
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getActivity()).edit();
        editor.putString("pref_default_notebook", currentNotebookDirectory.toString());
        editor.commit();
    }
    void setupDefaultNotebookPref(){
        this.currentNotebookPref= findPreference("pref_default_notebook");
        currentNotebookPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                defaultNotebookDialog.showDialog();
                return true;
            }
        });
    }
    public void updateDefaultNotebookSummary(){
        currentNotebookPref.setSummary(PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("pref_default_notebook", ""));
        currentNotebookDirectory = new File(currentNotebookPref.getSummary().toString());
    }
    public void updateRootDir(File directory) {
        currentRootDirectory = directory;
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getActivity()).edit();
        editor.putString("pref_root_directory", currentRootDirectory.toString());
        editor.commit();
    }
    public void updateRootDirSummary(){
        rootDirPref.setSummary(PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("pref_root_directory", ""));
        currentRootDirectory = new File(rootDirPref.getSummary().toString());
    }



}