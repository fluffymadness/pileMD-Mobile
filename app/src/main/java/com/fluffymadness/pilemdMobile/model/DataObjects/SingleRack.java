package com.fluffymadness.pilemdMobile.model.DataObjects;

/**
 * Created by fluffymadness on 9/30/2016.
 */

public class SingleRack {
    private String path;
    private String name;

    public SingleRack(String name, String path){
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
