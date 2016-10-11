package com.dup.beauty.model.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by DP on 2016/9/18.
 */
public class Galleries implements Serializable {
    public ArrayList<Gallery> getGalleries() {
        return galleries;
    }

    public void setGalleries(ArrayList<Gallery> galleries) {
        this.galleries = galleries;
    }

    @SerializedName("tngou")
    ArrayList<Gallery> galleries;
}
