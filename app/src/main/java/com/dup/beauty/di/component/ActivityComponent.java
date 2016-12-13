package com.dup.beauty.di.component;

import com.dup.beauty.di.module.ActivityModule;
import com.dup.beauty.di.scope.PerActivity;
import com.dup.beauty.mvp.ui.activity.CategoryActivity;
import com.dup.beauty.mvp.ui.activity.DownLoadActivity;
import com.dup.beauty.mvp.ui.activity.GalleryActivity;
import com.dup.beauty.mvp.ui.activity.LoginRegisterActivity;
import com.dup.beauty.mvp.ui.activity.MainActivity;
import com.dup.beauty.mvp.ui.activity.PictureActivity;
import com.dup.beauty.mvp.ui.activity.SettingActivity;
import com.dup.beauty.mvp.ui.activity.SplashActivity;

import javax.inject.Scope;

import dagger.Component;

/**
 * Created by DP on 2016/12/4.
 * 参考：http://www.jianshu.com/p/65737ac39c44
 */
@PerActivity
@Component(modules = ActivityModule.class)
public interface ActivityComponent {
    void inject(MainActivity activity);

    void inject(SplashActivity activity);

    void inject(SettingActivity activity);

    void inject(PictureActivity activity);

    void inject(LoginRegisterActivity activity);

    void inject(GalleryActivity activity);

    void inject(DownLoadActivity activity);

    void inject(CategoryActivity activity);
}
