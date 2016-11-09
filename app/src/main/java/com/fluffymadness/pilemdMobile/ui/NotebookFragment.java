package com.fluffymadness.pilemdMobile.ui;

import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.fluffymadness.pilemdMobile.model.DataModel;
import com.fluffymadness.pilemdMobile.model.NotebookAdapter;
import com.fluffymadness.pilemdMobile.model.Path;
import com.fluffymadness.pilemdMobile.model.SingleNotebook;
import com.fluffymadness.pilemdMobile.model.SortBy;

import java.util.ArrayList;

/**
 * Created by fluffymadness on 9/28/2016.
 */

public class NotebookFragment extends Fragment implements CreateNotebookDialog.CreateNotebookDialogListener {
    private FloatingActionButton addNotebookButton;
    private NotebookAdapter adapter;
    private ListView notebookList;
    private Path path;

    public static NotebookFragment newInstance() {
        NotebookFragment f = new NotebookFragment();
        Bundle args = new Bundle();
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
        path = ((PathSupplier)getActivity()).getPath();
    }
    @Override
    public void onResume(){
        super.onResume();
        getActivity().setTitle(R.string.title_fragment_notebooks);
        path = ((PathSupplier)getActivity()).getPath();
        refreshNotebooks();
    }

    private void refreshNotebooks(){
        //TODO handle Exception if folder is null
        ArrayList<SingleNotebook> notebooks = path.getNotebooks();
        adapter = new NotebookAdapter(getActivity(), 0, notebooks);
        adapter.sort(SortBy.NAME);
        notebookList = (ListView) getView().findViewById(R.id.notebookview);
        notebookList.setAdapter(adapter);
        notebookList.setOnItemClickListener(new NotebookItemClickListener());
        registerForContextMenu(notebookList);
        adapter.notifyDataSetChanged();

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
        dialog.show(getActivity().getSupportFragmentManager(), "CreateNotebookDialogFragment");
    }
    @Override
    public void onDialogPositiveClick(CreateNotebookDialog dialog, String notebookname) {
        path.createNotebook(notebookname);
        this.refreshNotebooks();
    }

    @Override
    public void onDialogNegativeClick(CreateNotebookDialog dialog) {

    }

    private void selectItem(int position) {

        String selectedNotebookName = ((SingleNotebook)this.notebookList.getAdapter().getItem(position)).getName();
        FragmentManager fm = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        NotesFragment fragment = new NotesFragment().newInstance();
        path.goForward(selectedNotebookName);
        fragmentTransaction.replace(((ViewGroup)getView().getParent()).getId(), fragment, "Notes_Fragment");
        fragmentTransaction.commit();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId()==R.id.notebookview) {
            menu.add(Menu.NONE, 0, 0, R.string.delete_notebook);
        }
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        int menuItemIndex = item.getItemId();
        if(menuItemIndex == 0){
            String path =((SingleNotebook)adapter.getItem(info.position)).getPath();
            this.path.deleteNotebook(path);
            adapter.remove(adapter.getItem(info.position));
            adapter.notifyDataSetChanged();
        }

        return true;
    }
}
