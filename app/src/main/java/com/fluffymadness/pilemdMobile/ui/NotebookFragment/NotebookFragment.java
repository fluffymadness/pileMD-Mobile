package com.fluffymadness.pilemdMobile.ui.NotebookFragment;

import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.fluffymadness.pilemdMobile.model.NotebookAdapter;
import com.fluffymadness.pilemdMobile.model.Path;
import com.fluffymadness.pilemdMobile.model.SingleNotebook;
import com.fluffymadness.pilemdMobile.model.SortBy;
import com.fluffymadness.pilemdMobile.ui.PathSupplier;
import com.fluffymadness.pilemdMobile.ui.R;

import java.util.ArrayList;

/**
 * Created by fluffymadness on 9/28/2016.
 */

public class NotebookFragment extends Fragment implements NotebookFragmentCallback  {
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

    public void refreshNotebooks(){
        //TODO handle Exception if folder is null
        ArrayList<SingleNotebook> notebooks = path.getNotebooks();
        adapter = new NotebookAdapter(getActivity(), 0, notebooks);
        adapter.sort(SortBy.NAME);

        notebookList = (ListView) getView().findViewById(R.id.notebookview);
        notebookList.setAdapter(adapter);
        addNotebookButton = (FloatingActionButton)getView().findViewById(R.id.addFolderButton);

        notebookList.setOnItemClickListener(new NotebookItemClickListener(this,path));
        notebookList.setOnCreateContextMenuListener(new NotebookItemLongClickListener(notebookList,path));
        addNotebookButton.setOnClickListener(new CreateNotebookListener(this,path));

        adapter.notifyDataSetChanged();

    }
}

interface NotebookFragmentCallback{
    void refreshNotebooks();
}