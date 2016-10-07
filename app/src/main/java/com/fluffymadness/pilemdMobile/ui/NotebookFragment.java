package com.fluffymadness.pilemdMobile.ui;

import android.app.Activity;
import android.support.v4.app.DialogFragment;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.fluffymadness.pilemdMobile.model.DataModel;
import com.fluffymadness.pilemdMobile.model.NotebookAdapter;
import com.fluffymadness.pilemdMobile.model.SingleNotebook;
import com.fluffymadness.pilemdMobile.model.SortBy;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by fluffymadness on 9/28/2016.
 */

public class NotebookFragment extends Fragment implements CreateNotebookDialog.CreateNotebookDialogListener {
    private DataModel dataModel;
    private FragmentActivity myContext;
    private String rackName;
    private FloatingActionButton addNotebookButton;

    private ListView notebookList;

    public static NotebookFragment newInstance(String rackName) {
        NotebookFragment f = new NotebookFragment();
        Bundle args = new Bundle();
        args.putString("rackName", rackName);
        f.setArguments(args);
        return f;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notebook, container, false);
        addNotebookButton = (FloatingActionButton)view.findViewById(R.id.addFolderButton);
        addNotebookButton.setOnClickListener(new NotebookFragment.FloatingButtonClickListener());
        return view;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
        rackName = getArguments().getString("rackName");
        String path= PreferenceManager.getDefaultSharedPreferences(myContext).getString("pref_root_directory", "");
        dataModel = new DataModel(path);

    }
    @Override
    public void onResume(){
        super.onResume();
        getActivity().setTitle(R.string.title_fragment_notebooks);
        refreshNotebooks();
    }
    @Override
    public void onAttach(Activity activity) {
        myContext=(FragmentActivity) activity;
        super.onAttach(activity);
    }

    private void refreshNotebooks(){
        //TODO handle Exception if folder is null
        ArrayList<SingleNotebook> notebooks = dataModel.getNotebooks(this.rackName);
        NotebookAdapter adapter = new NotebookAdapter(myContext, notebooks);
        adapter.sort(SortBy.NAME);
        notebookList = (ListView) getView().findViewById(R.id.folderview);
        notebookList.setAdapter(adapter);
        notebookList.setOnItemClickListener(new NotebookItemClickListener());

    }

    @Override
    public void onDialogPositiveClick(CreateNotebookDialog dialog) {
       Log.d("bla",dialog.getNotebookName()) ;
    }

    @Override
    public void onDialogNegativeClick(CreateNotebookDialog dialog) {

    }

    private class NotebookItemClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }

    }
    private class FloatingButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            addNotebook();
        }
    }

    private void addNotebook(){
        CreateNotebookDialog dialog = new CreateNotebookDialog();
        dialog.setDialogListener(this);
        dialog.show(myContext.getSupportFragmentManager(), "CreateNotebookDialogFragment");

    }

    private void selectItem(int position) {

        String selectedNotebookName = ((SingleNotebook)this.notebookList.getAdapter().getItem(position)).getName();
        FragmentManager fm = myContext.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        NotesFragment fragment = new NotesFragment().newInstance(rackName,selectedNotebookName);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.replace(((ViewGroup)getView().getParent()).getId(), fragment);
        fragmentTransaction.commit();
    }
}
