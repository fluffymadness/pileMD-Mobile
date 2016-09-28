package com.fluffymadness.pilemdMobile.ui;

import android.util.Log;

import java.io.File;
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
}
