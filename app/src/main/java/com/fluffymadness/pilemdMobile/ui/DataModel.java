package com.fluffymadness.pilemdMobile.ui;

import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by fluffymadness on 9/25/2016.
 */

public class DataModel {

    private String path;

    public DataModel(String path){
        this.path = path;
    }

    public ArrayList<File> getRacks() {
        File dir = new File(path);
        if(dir.exists()) {
            ArrayList<File> racks = new ArrayList<>();

            for (File f : dir.listFiles()) {
                if (!f.getName().startsWith(".")) {
                    if (f.isDirectory()) {
                        racks.add(f);
                    }
                }
            }
            return racks;
        }
        return null;

    }
    public ArrayList<File> getRackFolders(String rackname){
        File dir = new File(path+"/"+rackname);
        if(dir.exists()) {
            ArrayList<File> racks = new ArrayList<>();

            for (File f : dir.listFiles()) {
                if (!f.getName().startsWith(".")) {
                    if (f.isDirectory()) {
                        racks.add(f);
                    }
                }
            }
            return racks;
        }
        return null;
    }
    public boolean createRack(String dir, String name){
        return true;
    }

    public ArrayList<File> loadRackContent(){
        ArrayList<File> racklist = getRacks();
        if(racklist!=null){
            Log.d("racklist", "racklist is not null");
            return racklist;
        }
        else{
            return null;
        }
    }
    public ArrayList<File> getNotes(String rackname, String foldername){
        File dir = new File(path+"/"+rackname+"/"+foldername);
        if(dir.exists()) {
            ArrayList<File> notes = new ArrayList<>();

            for (File f : dir.listFiles()) {
                if (!f.getName().startsWith(".") ) {
                    if (!f.isDirectory()) {
                        notes.add(f);
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
        File file = new File(fullpath);
        StringBuilder text = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            while ((line = br.readLine()) != null) {
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
}
