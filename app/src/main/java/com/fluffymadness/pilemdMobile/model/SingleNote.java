package com.fluffymadness.pilemdMobile.model;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by fluffymadness on 9/30/2016.
 */

public class SingleNote {

    private String path;
    private String summary;
    private String name;
    private Date lastModifiedDate;

    public SingleNote(String path, String summary, String name, Date date){
        this.path=path;
        this.summary=summary;
        this.name=name;
        this.lastModifiedDate = date;
    }
    public String getPath(){
        return path;
    }
    public String getSummary() {
        return summary;
    }
    public String getLastModifiedString(){
        return new SimpleDateFormat("MM/dd/yyyy").format(lastModifiedDate);
    }
    public Date getLastModifiedDate(){
        return lastModifiedDate;
    }
    public String getName(){
        return this.name.substring(0,this.name.lastIndexOf('.'));
    }
}
