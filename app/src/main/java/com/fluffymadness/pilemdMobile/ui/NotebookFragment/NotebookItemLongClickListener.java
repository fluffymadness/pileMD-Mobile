package com.fluffymadness.pilemdMobile.ui.NotebookFragment;


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

public class NotebookItemLongClickListener implements View.OnCreateContextMenuListener {

    private ListView notebookList;
    private Path path;

    public NotebookItemLongClickListener(ListView notebookList, Path path){
        this.notebookList = notebookList;
        this.path = path;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId()== R.id.notebookview) {
            menu.add(Menu.NONE, 0, 0, R.string.delete_notebook).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
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
            String path =((SingleNotebook)notebookList.getAdapter().getItem(info.position)).getPath();
            this.path.deleteNotebook(path);
            NotebookAdapter a1 = (NotebookAdapter)notebookList.getAdapter();
            a1.remove(a1.getItem(info.position));
            a1.notifyDataSetChanged();
        }
        return true;
    }
}
