package com.dup.beauty.mvp.model.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.dup.beauty.mvp.model.entity.Category;
import com.dup.beauty.mvp.model.entity.CategoryDao;
import com.dup.beauty.mvp.model.entity.DaoMaster;
import com.dup.beauty.mvp.model.entity.DaoSession;
import com.dup.beauty.mvp.model.entity.User;
import com.dup.beauty.mvp.model.entity.UserDao;

import java.util.List;

/**
 * 数据库工具类
 * Created by DP on 2016/9/26.
 */
public class DBUtil {
    private static DBUtil mInstance;

    /**
     * session
     */
    private DaoSession session = null;

    public void init(Context context) {
        DaoMaster.DevOpenHelper openHelper = new DaoMaster.DevOpenHelper(context, context.getPackageName() + "_db", null);
        SQLiteDatabase db = openHelper.getWritableDatabase();
        session = new DaoMaster(db).newSession();
    }

    /**
     * 获取单例引用
     *
     * @return
     */
    public static DBUtil getInstance() {
        if (mInstance == null) {
            synchronized (DBUtil.class) {
                if (mInstance == null) {
                    mInstance = new DBUtil();
                }
            }
        }
        return mInstance;
    }


    /*********************Category***********************/

    /**
     * 插入一个分类列表
     *
     * @param categories
     */
    public void insertCategoryList(List<Category> categories) {
        CategoryDao categoryDao = session.getCategoryDao();
        categoryDao.insertInTx(categories);
    }

    /**
     * 查询分类列表
     */
    public List<Category> queryCategoryList() {
        List<Category> list = session.getCategoryDao().queryBuilder().orderAsc(CategoryDao.Properties.Seq).list();
        return list;
    }

    /****************************************************/

    /*********************User***********************/

    /**
     * 插入登陆成功或注册成功的用户数据
     *
     * @param user
     */
    public void insertUser(User user) {
        UserDao userDao = session.getUserDao();
        userDao.deleteAll();
        userDao.insert(user);
    }

    /**
     * 查询最后一次注册成功或登陆成功的用户数据
     */
    public User queryUser() {
        UserDao userDao = session.getUserDao();
        List<User> list = userDao.queryBuilder().limit(1).list();
        if (list != null && !list.isEmpty()) {
            return list.get(0);
        }
        return null;
    }

    /**
     * 退出登陆或重新登陆注册后执行
     */
    public void deleteUser() {
        UserDao userDao = session.getUserDao();
        userDao.deleteAll();
    }

    /****************************************************/
}
