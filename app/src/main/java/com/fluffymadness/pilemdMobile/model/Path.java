package com.fluffymadness.pilemdMobile.model;

import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by fluffymadness on 11/8/2016.
 */

public class Path extends DataModel{

    private File currentPath;
    private String rootPath;
    private int depth;

    public Path(String rootPath){
        this.rootPath = rootPath;
        this.currentPath = new File(rootPath);
        depth = 0;

    }
    public void getBack(){
        if(!currentPath.toString().equals(rootPath)){
            currentPath = this.currentPath.getParentFile();
            depth -= 1;
        }
    }
    public void goForward(String path){
        File newPath = new File(currentPath.toString()+"/"+path);
        if(newPath.exists() && newPath.isDirectory()) {
            depth+=1;
            this.currentPath = newPath;
        }
    }
    public void resetPath(){
        currentPath = new File(rootPath);
        depth = 0;
    }
    public String getCurrentPath(){
       return currentPath.toString();
    }
    public void setCurrentPath(String currentPath){
        this.currentPath = new File(currentPath);
    }
    public boolean isLeaf(){
        for (File f : currentPath.listFiles()) {
            if (f.isDirectory()){
                return false;
            }
        }
        return true;
    }

    public ArrayList<SingleRack> getRacks(){
        return super.getRacks(this.rootPath);
    }
    public ArrayList<SingleNotebook> getNotebooks(){
        return super.getNotebooks(this.getCurrentPath());
    }
    public ArrayList<SingleNote> getNotes(){
        return super.getNotes(this.getCurrentPath());
    }

    public void createNotebook(String name){
        super.createNotebook(getCurrentPath(),name);
    }
    public void deleteNotebook(String absoluteFolderpath){
        super.deleteNotebook(absoluteFolderpath);
    }
    public void moveFile(String absoluteFolderPathNew, String oldNoteName){
        super.moveFile(getCurrentPath(),absoluteFolderPathNew,oldNoteName);
    }
    public Date getLastModifiedDate(String noteName){
        return super.getLastModifiedDate(getCurrentPath()+"/"+noteName);
    }
    public String getNoteMarkdown(String absolutePath){
        return super.getNoteMarkdown(absolutePath);
    }
    public String getNote(String noteName){
        return super.getNote(getCurrentPath()+"/"+noteName);
    }






}
