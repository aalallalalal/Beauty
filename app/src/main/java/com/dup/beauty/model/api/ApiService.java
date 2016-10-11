package com.dup.beauty.model.api;

import com.dup.beauty.model.entity.Categories;
import com.dup.beauty.model.entity.Galleries;
import com.dup.beauty.model.entity.Gallery;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
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
     "status":true,
     "count":21,
     "fcount":0,
     "galleryclass":3,
     "id":1,
     "url":"http://www.tngou.net/tnfs/show/1",
     "img":"/ext/150714/aeb85cdb34f325ccfb3ae0928f846d2d.jpg",
     "rcount":0,
     "size":18,
     "time":1436874237000,
     "title":"絕對吸引"，
     "list":"[
     {\"gallery\":1,\"id\":1,\"src\":\"/ext/150714/aeb85cdb34f325ccfb3ae0928f846d2d.jpg\"},
     {……}
     ]"
     }
     */
    @GET("/tnfs/api/show")
    Observable<Gallery> getPictures(@Query("id") long id);

}
