package com.fluffymadness.pilemdMobile.model;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.fluffymadness.pilemdMobile.ui.NotesFragment;
import com.fluffymadness.pilemdMobile.ui.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by fluffymadness on 9/25/2016.
 */

public class NotesAdapter extends ArrayAdapter{

    private Context context;
    private ArrayList<SingleNote> data;

    public NotesAdapter(Context context, int resource, ArrayList<SingleNote> content) {
        super(context, resource, content);
        this.context = context;
        this.data = content;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View row = inflater.inflate(R.layout.notes_row, viewGroup, false);
        TextView noteTitle = (TextView) row.findViewById(R.id.note_title);
        noteTitle.setText(data.get(i).getName());
        TextView noteText = (TextView) row.findViewById(R.id.note_text);
        noteText.setText(data.get(i).getSummary());
        TextView noteDate  = (TextView) row.findViewById(R.id.note_date);
        noteDate.setText(data.get(i).getLastModifiedString());
        ImageView imageView = (ImageView) row.findViewById(R.id.editIcon);
        imageView.setImageResource(R.drawable.ic_mode_edit_black_24dp);
        imageView.setOnClickListener(new NotesEditClickListener(data.get(i).getName()+"."+data.get(i).getExtension()){
            @Override
            public void onClick(View view) {
                ((NotesEditListernerInterface)context).editNote(this.getName());
            }
        });
        return row;
    }
    public void sort(SortBy sortBy){

        if(sortBy == SortBy.NAME) {
            Collections.sort(data, new Comparator<SingleNote>() {

                @Override
                public int compare(SingleNote singleNote, SingleNote t1) {
                    return singleNote.getName().compareTo(t1.getName());
                }


            });
        }
        if(sortBy == SortBy.DATE) {
            Collections.sort(data, new Comparator<SingleNote>() {

                @Override
                public int compare(SingleNote singleNote, SingleNote t1) {
                    return t1.getLastModifiedDate().compareTo(singleNote.getLastModifiedDate());
                }


            });
        }
        this.notifyDataSetChanged();
    }

}
class NotesEditClickListener implements ImageView.OnClickListener {

    private String name;
    public NotesEditClickListener(String name) {
        this.name = name;
    }

    @Override
    public void onClick(View v)
    {
        //read your lovely variable
    }

    public String getName(){
        return this.name;
    }
}
