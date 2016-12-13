package com.dup.beauty.mvp.model.api;

import com.dup.beauty.mvp.model.entity.Categories;
import com.dup.beauty.mvp.model.entity.Galleries;
import com.dup.beauty.mvp.model.entity.Gallery;
import com.dup.beauty.mvp.model.entity.Oauth;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public interface ApiService {
    /**
     * 获取图库列表数据
     * {
     * "count": 18,
     * "fcount": 0,
     * "galleryclass": 4,
     * "id": 945,
     * "img": "/ext/160918/d9d8c023ac74f8074ddff1e92e2e8000.jpg",
     * "rcount": 0,
     * "size": 7,
     * "time": 1474173212000,
     * "title": "高挑骨.."
     * }
     * {
     * }
     * ...
     *
     * @param id   图片分类 默认为首页hot，id=0；
     * @param page 第几页数据 默认第一页 page = 1。
     * @param rows 此页获取多少项。默认20 rows = 20.
     */
    @GET("/tnfs/api/list")
    Observable<Galleries> getGalleries(@Query("page") int page, @Query("rows") int rows, @Query("id") long id);

    /**
     * 获取分类数据
     * "tngou": [
     * {
     * "description": "性感美女",
     * "id": 1,
     * "keywords": "性感美女",
     * "name": "性感美女",
     * "seq": 1,
     * "title": "性感美女"
     * },
     * {
     * "description": "韩日美女",
     * "id": 2,
     * "keywords": "韩日美女",
     * "name": "韩日美女",
     * "seq": 2,
     * "title": "韩日美女"
     * }.....,
     * ]
     */
    @GET("/tnfs/api/classify")
    Observable<Categories> getCategories();

    /**
     * 根据gallery.id 获取该图库中的图片们。
     * {
     * "status":true,
     * "count":21,
     * "fcount":0,
     * "galleryclass":3,
     * "id":1,
     * "url":"http://www.tngou.net/tnfs/show/1",
     * "img":"/ext/150714/aeb85cdb34f325ccfb3ae0928f846d2d.jpg",
     * "rcount":0,
     * "size":18,
     * "time":1436874237000,
     * "title":"絕對吸引"，
     * "list":"[
     * {\"gallery\":1,\"id\":1,\"src\":\"/ext/150714/aeb85cdb34f325ccfb3ae0928f846d2d.jpg\"},
     * {……}
     * ]"
     * }
     */
    @GET("/tnfs/api/show")
    Observable<Gallery> getPictures(@Query("id") long id);

    /**
     * 己方注册
     */
    @GET("/api/oauth2/reg")
    Observable<Oauth> register(@Query("client_id") String id, @Query("client_secret") String secret, @Query("email") String email,
                               @Query("account") String account, @Query("password") String pwd);

    /**
     * 绑定qq注册
     */
    @GET("/api/oauth2/reg")
    Observable<Oauth> registerBindQQ(@Query("client_id") String id, @Query("client_secret") String secret, @Query("email") String email,
                                     @Query("account") String account, @Query("password") String pwd, @Query("qqid") String qqid);

    /**
     * 绑定微博注册
     */
    @GET("/api/oauth2/reg")
    Observable<Oauth> registerBindWeibo(@Query("client_id") String id, @Query("client_secret") String secret, @Query("email") String email,
                                        @Query("account") String account, @Query("password") String pwd, @Query("weiboid") String weiboid);

    /**
     * 己方登陆
     */
    @GET("/api/oauth2/login")
    Observable<Oauth> login(@Query("client_id") String id, @Query("client_secret") String secret, @Query("name") String name,
                            @Query("password") String pwd);

    /**
     * 第三方登陆
     *
     * @param id
     * @param secret
     * @param account 在做用户绑定注册的昵称
     * @param qqid
     * @param weiboid 微博第三方认证id
     * @param type    对应的登录类型 qq/sina (腾讯QQ或者新浪微博)
     * @return
     */
    @GET("/api/oauth2/bind")
    Observable<Oauth> loginBind(@Query("client_id") String id, @Query("client_secret") String secret, @Query("account") String account,
                                @Query("qqid") String qqid, @Query("weiboid") String weiboid, @Query("type") String type);

}
