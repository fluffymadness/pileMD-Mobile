package com.fluffymadness.pilemdMobile.ui.NotebookFragment;


import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.fluffymadness.pilemdMobile.model.Adapters.NotebookAdapter;
import com.fluffymadness.pilemdMobile.model.Path;
import com.fluffymadness.pilemdMobile.model.DataObjects.SingleNotebook;
import com.fluffymadness.pilemdMobile.ui.R;

/**
 * Created by fluffymadness on 11/11/2016.
 */

public class NotebookItemLongClickListener implements View.OnCreateContextMenuListener,CreateNotebookDialog.CreateNotebookDialogListener {

    private ListView notebookList;
    private Path path;
    private Fragment mFragmentCallback;
    private SingleNotebook selectedItem;

    public NotebookItemLongClickListener(Fragment fragment, ListView notebookList, Path path){
        this.notebookList = notebookList;
        this.path = path;
        this.mFragmentCallback = fragment;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId()== R.id.notebookview) {
            menu.add(Menu.NONE, 0, 0, R.string.rename_notebook).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    return onContextItemSelected(item);
                }
            });
            menu.add(Menu.NONE, 1, 1, R.string.delete_notebook).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    return onContextItemSelected(item);
                }
            });
        }
    }
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        int menuItemIndex = item.getItemId();
        if(menuItemIndex == 0){
            CreateNotebookDialog dialog = new CreateNotebookDialog();
            dialog.setDialogListener(this);
            this.selectedItem =((SingleNotebook)notebookList.getAdapter().getItem(info.position));
            dialog.show(mFragmentCallback.getActivity().getSupportFragmentManager(), "CreateNotebookDialogFragment");
        }
        if(menuItemIndex == 1){
            String path =((SingleNotebook)notebookList.getAdapter().getItem(info.position)).getPath();
            this.path.deleteNotebook(path);
            NotebookAdapter a1 = (NotebookAdapter)notebookList.getAdapter();
            a1.remove(a1.getItem(info.position));
            a1.notifyDataSetChanged();
        }
        return true;
    }

    @Override
    public void onDialogPositiveClick(CreateNotebookDialog dialog, String text) {
        if(this.selectedItem!=null) {
            if(!text.equals("")) {
                this.path.renameNotebook(selectedItem.getName(), text);
                ((NotebookFragmentCallback) mFragmentCallback).refreshNotebooks();
            }
        }
    }

    @Override
    public void onDialogNegativeClick(CreateNotebookDialog dialog) {

    }
}
