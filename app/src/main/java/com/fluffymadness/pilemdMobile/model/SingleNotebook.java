package com.fluffymadness.pilemdMobile.model;

/**
 * Created by fluffymadness on 9/30/2016.
 */

public class SingleNotebook {

    private String path;
    private String name;

    public SingleNotebook(String name, String path){
        this.path = path;
        this.name = name;
    }
    public String getName() {
        return name;
    }
    public String getPath() {
        return path;
    }
}
