package com.exportershouse.expoapp;

/**
 * Created by Shrey on 31-01-2018.
 */
public class DataAdapter
{
    public String ImageURL;
    public String ImageTitle;

    public String getImageUrl() {

        return ImageURL;
    }

    public void setImageUrl(String imageServerUrl) {

        this.ImageURL = imageServerUrl;
    }

    public String getImageTitle() {

        return ImageTitle;
    }

    public void setImageTitle(String Imagetitlename) {

        this.ImageTitle = Imagetitlename;
    }

}