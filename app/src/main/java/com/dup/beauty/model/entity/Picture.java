package com.dup.beauty.model.entity;

import java.io.Serializable;

/**
 * 具体一张图片实体类
 * Created by DP on 2016/10/10.
 */
public class Picture implements Serializable {
    private long id;//图片ID
    private int gallery; //图片库ID
    private String src; //图片地址

    @Override
    public String toString() {
        return "Picture ID:"+id+" galleryID:"+gallery+" src:"+src;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getGallery() {
        return gallery;
    }

    public void setGallery(int gallery) {
        this.gallery = gallery;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }
}
