package com.railway.mmfile.file;

import org.litepal.crud.DataSupport;

public class localFile extends DataSupport {

    private String fileid;
    private String url;
    private String name;
    private String locate;
    private int fileType;
    private long time;
    private String hash;

    public int getStates() {
        return states;
    }

    public void setStates(int states) {
        this.states = states;
    }

    private int states;

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }
    public static class state{
          public static int online=1,offline=2,waitupload=3;
    }
    public static class type{
       public static int audio=1;
       public static int imge=2,movie=3,documents=4,link=5,others=6;
    }

    public String getId() {
        return fileid;
    }

    public void setId(String id) {
        this.fileid = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocate() {
        return locate;
    }

    public void setLocate(String locate) {
        this.locate = locate;
    }

    public int getFileType() {
        return fileType;
    }

    public void setFileType(int fileType) {
        this.fileType = fileType;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
