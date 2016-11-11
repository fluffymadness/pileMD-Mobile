package com.fluffymadness.pilemdMobile.ui.NotebookFragment;

import android.support.v4.app.Fragment;
import android.view.View;

import com.fluffymadness.pilemdMobile.model.Path;
import com.fluffymadness.pilemdMobile.ui.PathSupplier;

/**
 * Created by fluffymadness on 11/11/2016.
 */

public class CreateNotebookListener implements View.OnClickListener,CreateNotebookDialog.CreateNotebookDialogListener{

    private Fragment mCallbackFragment;
    private Path path;

    public CreateNotebookListener(Fragment fragment, Path path){
        this.path = path;
        this.mCallbackFragment = fragment;
    }

    @Override
    public void onClick(View view) {
        addNotebook();
    }

    private void addNotebook(){
        CreateNotebookDialog dialog = new CreateNotebookDialog();
        dialog.setDialogListener(this);
        dialog.show(mCallbackFragment.getActivity().getSupportFragmentManager(), "CreateNotebookDialogFragment");
    }
    @Override
    public void onDialogPositiveClick(CreateNotebookDialog dialog, String notebookname) {
        path.createNotebook(notebookname);
        ((NotebookFragmentCallback)mCallbackFragment).refreshNotebooks();
    }

    @Override
    public void onDialogNegativeClick(CreateNotebookDialog dialog) {

    }
}
