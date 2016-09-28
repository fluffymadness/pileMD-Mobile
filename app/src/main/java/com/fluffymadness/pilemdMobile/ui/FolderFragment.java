package com.fluffymadness.pilemdMobile.ui;

import android.app.Activity;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by fluffymadness on 9/28/2016.
 */

public class FolderFragment extends Fragment {
    private DataModel dataModel;
    private FragmentActivity myContext;
    private String folders;

    private ListView mFolderList;

    public static FolderFragment newInstance(String folder) {
        FolderFragment f = new FolderFragment();
        Bundle args = new Bundle();
        args.putString("folder", folder);
        f.setArguments(args);
        return f;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_folders, container, false);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
        folders = getArguments().getString("folder");
        String path= PreferenceManager.getDefaultSharedPreferences(myContext).getString("pref_root_directory", "");
        dataModel = new DataModel(path);
    }
    @Override
    public void onResume(){
        super.onResume();
        refreshFolders();
    }
    @Override
    public void onAttach(Activity activity) {
        myContext=(FragmentActivity) activity;
        super.onAttach(activity);
    }

    private void refreshFolders(){
        //TODO handle Exception if folder is null
        ArrayList<File> folders = dataModel.getRackFolders(this.folders);
        FolderAdapter adapter = new FolderAdapter(myContext, folders);
        mFolderList = (ListView) getView().findViewById(R.id.folderview);
        mFolderList.setAdapter(adapter);
        mFolderList.setOnItemClickListener(new FolderItemClickListener());

    }
    private class FolderItemClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }

    }

    private void selectItem(int position) {

    }
}
