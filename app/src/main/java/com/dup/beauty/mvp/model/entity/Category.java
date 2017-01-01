package com.dup.beauty.mvp.model.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

import java.io.Serializable;

/**
 * 分类实体类
 * Created by DP on 2016/9/26.
 */
@Entity
public class Category implements Serializable {
    private static final long serialVersionUID = -511117304508211164L;
    @Id
    private Long id;
    private String name;
    private String title;
    private String keywords;
    private String description;
    private int seq;//排序，从0--10

    @Generated(hash = 63384930)
    public Category(Long id, String name, String title, String keywords,
                    String description, int seq) {
        this.id = id;
        this.name = name;
        this.title = title;
        this.keywords = keywords;
        this.description = description;
        this.seq = seq;
    }

    @Generated(hash = 1150634039)
    public Category() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }
}
