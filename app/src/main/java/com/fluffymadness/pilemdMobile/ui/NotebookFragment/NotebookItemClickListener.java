package com.fluffymadness.pilemdMobile.ui.NotebookFragment;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.fluffymadness.pilemdMobile.model.Path;
import com.fluffymadness.pilemdMobile.model.DataObjects.SingleNotebook;
import com.fluffymadness.pilemdMobile.ui.NotesFragment.NotesFragment;
import com.fluffymadness.pilemdMobile.ui.R;

/**
 * Created by fluffymadness on 11/11/2016.
 */

public class NotebookItemClickListener implements ListView.OnItemClickListener{

    private NotebookFragment mFragment;
    private Path path;

    public NotebookItemClickListener(NotebookFragment notebookFragment, Path path){
        mFragment = notebookFragment;
        this.path = path;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        selectItem(position, parent);
    }

    private void selectItem(int position, View view) {

        ListView notebookList = (ListView)view.findViewById(R.id.notebookview);
        String selectedNotebookName = ((SingleNotebook)notebookList.getAdapter().getItem(position)).getName();
        FragmentManager fm = mFragment.getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        NotesFragment fragment = new NotesFragment().newInstance();
        this.path.goForward(selectedNotebookName);
        fragmentTransaction.replace(((ViewGroup)mFragment.getView().getParent()).getId(), fragment, "Notes_Fragment");
        fragmentTransaction.commit();
    }
}
