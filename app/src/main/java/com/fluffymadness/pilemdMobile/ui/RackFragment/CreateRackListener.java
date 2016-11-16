package com.fluffymadness.pilemdMobile.ui.RackFragment;


import android.support.v4.app.FragmentActivity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;

import com.fluffymadness.pilemdMobile.model.Path;

/**
 * Created by fluffymadness on 11/16/2016.
 */

public class CreateRackListener implements AdapterView.OnClickListener, CreateRackDialog.CreateRackDialogListener {

    private AppCompatActivity activity;
    private Path path;

    public CreateRackListener(AppCompatActivity activity, Path path){
        this.activity = activity;
        this.path = path;
    }

    @Override
    public void onClick(View view) {
        addRack();
    }

    public void addRack(){
        CreateRackDialog dialog = new CreateRackDialog();
        dialog.setDialogListener(this);
        dialog.show(activity.getSupportFragmentManager(), "CreateNotebookDialogFragment");
    }

    @Override
    public void onDialogPositiveClick(CreateRackDialog dialog, String text) {
        path.createRack(text);
        ((RackFragmentCallback)activity).refreshRackDrawer();
    }

    @Override
    public void onDialogNegativeClick(CreateRackDialog dialog) {

    }
}
