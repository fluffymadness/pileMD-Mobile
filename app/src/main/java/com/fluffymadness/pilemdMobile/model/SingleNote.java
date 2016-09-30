package com.fluffymadness.pilemdMobile.model;

import java.io.File;

/**
 * Created by fluffymadness on 9/30/2016.
 */

public class SingleNote {

    private String path;
    private String summary;
    private String name;
    private String lastModified;

    public SingleNote(String path, String summary, String name, String lastModified){
        this.path=path;
        this.summary=summary;
        this.name=name;
        this.lastModified=lastModified;
    }
    public String getPath(){
        return path;
    }
    public String getSummary() {
        return summary;
    }
    public String getLastModified(){
        return this.lastModified;
    }
    public String getName(){
        return this.name.substring(0,this.name.lastIndexOf('.'));
    }
}
