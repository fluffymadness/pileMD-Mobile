package com.fluffymadness.pilemdMobile.ui.NotebookFragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.fluffymadness.pilemdMobile.ui.R;

/**
 * Created by fluffymadness on 10/7/2016.
 */

public class CreateNotebookDialog extends DialogFragment {

    public interface CreateNotebookDialogListener {
        void onDialogPositiveClick(CreateNotebookDialog dialog, String text);
        void onDialogNegativeClick(CreateNotebookDialog dialog);
    }

    // Use this instance of the interface to deliver action events
    CreateNotebookDialogListener mListener;

    public void setDialogListener(CreateNotebookDialogListener mListener){
        this.mListener = mListener;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        final View view = inflater.inflate(R.layout.create_notebook_dialog, null);
        builder.setView(view);
        builder.setPositiveButton(R.string.create_notebook, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                if(mListener!=null) {
                    EditText notebookName = (EditText) view.findViewById(R.id.folder_name);
                    mListener.onDialogPositiveClick(CreateNotebookDialog.this, notebookName.getText().toString());
                }
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if(mListener!=null)
                    mListener.onDialogNegativeClick(CreateNotebookDialog.this);
            }
        });
        return builder.create();
    }
}
