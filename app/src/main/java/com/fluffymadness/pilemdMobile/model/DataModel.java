package com.fluffymadness.pilemdMobile.model;

import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
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
                        if ((extension.equalsIgnoreCase("txt"))||(extension.equalsIgnoreCase("md"))) {
                            String notepath = dir+"/"+f.getName();
                            notes.add(new SingleNote(dir.getPath(),getNoteTrunc(notepath,1,5),f.getName(), new Date(f.lastModified())));
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
        return getNoteTrunc(fullpath, 0,0);
    }

    public String getNoteMarkdown (String fullpath){
        File file = new File(fullpath);
        StringBuilder text = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            int counter=0;

            while ((line = br.readLine()) != null) {
                if(counter==0) {
                    text.append("##");
                    counter++;
                }
                text.append(line);
                text.append("  \n");
            }
            br.close();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return text.toString();
    }
    private String getNoteTrunc(String fullpath, int fromLine, int toLine ){
        int counter = 0;
        File file = new File(fullpath);
        StringBuilder text = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            finish:
            while ((line = br.readLine()) != null) {
                if(counter >= fromLine) {
                    text.append(line);
                    text.append("\n");
                }
                if(toLine !=0) {
                    counter++;
                    if (counter == toLine) {
                        text.append("....");
                        break finish;
                    }
                }
            }
            br.close();
        }
        catch (IOException e) {
            //You'll need to add proper error handling here
            return null;
        }
        return text.toString();
    }
    public boolean createNote(String path, String name, String text) {

        String filename = path + "/" + name + ".md";
        String tempfile = path + "/" + name + ".md.temp";

        File temppath = new File(tempfile);
        File filepath = new File(filename);

        FileWriter writer = null;
        try {
            writer = new FileWriter(temppath);
            writer.append(text);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (filepath.exists()) {
            int counter = 1;
            while (filepath.exists()) {
                String tempFile = path + "/" + name + "##" + counter + "##" + ".md";
                filepath = new File(tempFile);
                counter++;
            }
            temppath.renameTo(filepath);
        }
        else {
            temppath.renameTo(filepath);
        }
        return true;
    }
    public Date getLastModifiedDate(String filename){
        File filepath = new File(filename);
        return new Date(filepath.lastModified());
    }

    /**
     * Note : this is not robust, since I don't know what syncthing does
     * Does the oldFile still exist, that means it didn't get deleted by Syncthing
     * - Check if it was modified by syncthing in the meantime by checking date
     *      - Yes
     *          Create Note should save it as a duplicate with higher number
     *      - No
     *          Delete Old File, then Create Note
     */
    public void modifyNote(String path, String oldname, String newName, String text, Date lastModified){
        File oldFile = new File(path+"/"+oldname+".md");

        if(oldFile.exists()){
            long lastModifiedDate = new Date(oldFile.lastModified()).getTime();
            Log.d("lastModDate",String.valueOf(lastModifiedDate));
            Log.d("lastModDate2",String.valueOf(lastModified.getTime()));
            if(lastModifiedDate == lastModified.getTime()){
                oldFile.delete();
                Log.d("del","delete old file");
                createNote(path,newName,text);
            }
            else{
                createNote(path,newName,text);
            }
        }
        else{
            createNote(path,newName,text);
        }
    }
    public void deleteNote(String path){
        File toDelete = new File(path);
        toDelete.delete();
    }
    public void createNotebook(String rackname,String notebookname){
        File dir = new File(this.path+"/"+rackname+"/"+notebookname);
        dir.mkdir();
    }
    public void deleteNotebook(String path){
        File dir = new File(path);
        deleteRecursive(dir);
    }
    private void deleteRecursive(File fileOrDirectory) {
        if (fileOrDirectory.isDirectory())
            for (File child : fileOrDirectory.listFiles())
                deleteRecursive(child);

        fileOrDirectory.delete();
    }

    public void moveFile(String oldpath, String newpath, String notename){
        String note = getNote(oldpath);
        createNote(newpath,notename,note);
        deleteNote(oldpath);
    }
}
