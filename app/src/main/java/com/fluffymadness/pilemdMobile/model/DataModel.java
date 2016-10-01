package com.fluffymadness.pilemdMobile.model;

import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by fluffymadness on 9/25/2016.
 */

public class DataModel {

    private String path;

    public DataModel(String path){
        this.path = path;
    }

    public ArrayList<SingleRack> getRacks() {
        File dir = new File(path);
        if(dir.exists()) {
            ArrayList<SingleRack> racks = new ArrayList<>();

            for (File f : dir.listFiles()) {
                if (!f.getName().startsWith(".")) {
                    if (f.isDirectory()) {
                        racks.add(new SingleRack(f.getName(),f.toString()));
                    }
                }
            }
            return racks;
        }
        return null;

    }
    public ArrayList<SingleNotebook> getNotebooks(String rackname){
        File dir = new File(path+"/"+rackname);
        if(dir.exists()) {
            ArrayList<SingleNotebook> notebooks = new ArrayList<>();

            for (File f : dir.listFiles()) {
                if (!f.getName().startsWith(".")) {
                    if (f.isDirectory()) {
                        File[] files = f.listFiles(new FilenameFilter() {
                            public boolean accept(File dir, String name) {
                                if(name.toLowerCase().endsWith(".txt") || name.toLowerCase().endsWith(".md"))
                                    return true;
                                return false;
                            }
                        });
                        int numberOfNotes=files.length;
                        notebooks.add(new SingleNotebook(f.getName(),f.toString(),numberOfNotes));
                    }
                }
            }
            return notebooks;
        }
        return null;
    }
    public boolean createRack(String dir, String name){
        return true;
    }

    public ArrayList<SingleRack> loadRackContent(){
        ArrayList<SingleRack> racklist = getRacks();
        if(racklist!=null){
            Log.d("racklist", "racklist is not null");
            return racklist;
        }
        else{
            return null;
        }
    }
    public ArrayList<SingleNote> getNotes(String rackname, String foldername){
        File dir = new File(path+"/"+rackname+"/"+foldername);
        if(dir.exists()) {
            ArrayList<SingleNote> notes = new ArrayList<>();

            for (File f : dir.listFiles()) {
                if (!f.getName().startsWith(".")) {
                    if (!f.isDirectory()) {
                        String extension = getFileExtension(f);
                        Log.d("ext",extension);
                        if ((extension.equalsIgnoreCase("txt"))||(extension.equalsIgnoreCase("md"))) {
                            String notepath = dir+"/"+f.getName();
                            notes.add(new SingleNote(f.toString(),getNoteTrunc(notepath,5),f.getName(), new Date(f.lastModified())));
                        }
                    }
                }
            }
            return notes;
        }
        return null;
    }
    public String getFileExtension(File filename){
        return filename.toString().substring(filename.toString().lastIndexOf('.') + 1);
    }
    public String getPath(){
        return this.path;
    }

    public String getNote(String fullpath){
        return getNoteTrunc(fullpath, 0);
    }
    private String getNoteTrunc(String fullpath, int lines){
        int counter = 0;
        File file = new File(fullpath);
        StringBuilder text = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            finish:
            while ((line = br.readLine()) != null) {
                if(lines != 0) {
                    counter++;
                    if (counter == lines) {
                        text.append("....");
                        break finish;
                    }
                }

                text.append(line);
                text.append('\n');

            }
            br.close();
        }
        catch (IOException e) {
            //You'll need to add proper error handling here
            return null;
        }
        return text.toString();
    }
    public boolean createNote(String path, String name, String text){
        return true;
    }
}
