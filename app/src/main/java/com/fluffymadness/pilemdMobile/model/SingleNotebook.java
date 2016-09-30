package com.fluffymadness.pilemdMobile.model;

/**
 * Created by fluffymadness on 9/30/2016.
 */

public class SingleNotebook {

    private String path;
    private String name;
    private int noteCount;

    public SingleNotebook(String name, String path, int noteCount){
        this.path = path;
        this.name = name;
        this.noteCount = noteCount;
    }
    public String getName() {
        return name;
    }
    public String getPath() {
        return path;
    }
    public int getNoteCount(){
        return this.noteCount;
    }
}
