package com.dup.beauty.mvp.model.entity;


import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;


/**
 * 如果注册登陆成功后，保存用户数据，下次自动登陆
 * Created by DP on 2016/10/20.
 */
@Entity
public class User {
    private int type = 0; //0:己方 1：qq 2:weibo

    private String name; //以后默认登陆时的账号.可能为email和account
    private String email;
    private String account;
    private String pwd;
    private String qqid;
    private String weiboid;
    private String logintype;//qq/sina

    @Generated(hash = 1804162080)
    public User(int type, String name, String email, String account, String pwd,
                String qqid, String weiboid, String logintype) {
        this.type = type;
        this.name = name;
        this.email = email;
        this.account = account;
        this.pwd = pwd;
        this.qqid = qqid;
        this.weiboid = weiboid;
        this.logintype = logintype;
    }

    @Generated(hash = 586692638)
    public User() {
    }

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAccount() {
        return this.account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPwd() {
        return this.pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getQqid() {
        return this.qqid;
    }

    public void setQqid(String qqid) {
        this.qqid = qqid;
    }

    public String getWeiboid() {
        return this.weiboid;
    }

    public void setWeiboid(String weiboid) {
        this.weiboid = weiboid;
    }

    public String getLogintype() {
        return this.logintype;
    }

    public void setLogintype(String logintype) {
        this.logintype = logintype;
    }

}
