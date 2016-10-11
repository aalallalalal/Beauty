package com.dup.beauty.model.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 图片库（一组图片）
 * Created by DP on 2016/9/18.
 */
public class Gallery implements Serializable {
    private long id;
    private int galleryclass;//          图片分类
    private String title;//          标题
    private String img;//          图库封面
    private int count;//          访问数
    private int rcount;//           回复数
    private int fcount;//          收藏数
    private int size;//      图片多少张
    private List<Picture> list; //图片们

    @Override
    public String toString() {
        return "Gallery id:" + id + " galleryclass:" + galleryclass + " title:" + title
                + " img:" + img + " count:" + count + " rcount:" + rcount + " fcount:" + fcount
                + " size:" + size + " list:" + list.toString();
    }

    public List<Picture> getList() {
        return list;
    }

    public void setList(List<Picture> list) {
        this.list = list;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getGalleryclass() {
        return galleryclass;
    }

    public void setGalleryclass(int galleryclass) {
        this.galleryclass = galleryclass;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getRcount() {
        return rcount;
    }

    public void setRcount(int rcount) {
        this.rcount = rcount;
    }

    public int getFcount() {
        return fcount;
    }

    public void setFcount(int fcount) {
        this.fcount = fcount;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
