package com.railway.mmfile;

public class myFile {
    private String name;
    private String url;
    private String time;
    private int format;
    private int imageId;

    public myFile(String name,String url,String time,int format)
    {
        this.name=name;
        this.format=format;
        this.time=time;
        this.url=url;
        if(format==0)
        {
            imageId=R.mipmap.file;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getFormat() {
        return format;
    }

    public void setFormat(int format) {
        this.format = format;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTime() {
        return time;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public int getImageId() {
        return imageId;
    }
}
