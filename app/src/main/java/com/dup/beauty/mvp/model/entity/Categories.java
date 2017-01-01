package com.dup.beauty.mvp.model.entity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by DP on 2016/9/18.
 */
public class Categories {
    public ArrayList<Category> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<Category> categories) {
        this.categories = categories;
    }

    @SerializedName("tngou")
    ArrayList<Category> categories;
}
