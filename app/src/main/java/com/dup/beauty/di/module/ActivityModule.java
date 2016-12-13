package com.dup.beauty.di.module;

import android.app.Activity;

import com.dup.beauty.di.scope.PerActivity;
import com.dup.beauty.mvp.presenter.contract.IMainContentPresenter;
import com.dup.beauty.mvp.presenter.contract.IMainMenuPresenter;
import com.dup.beauty.mvp.presenter.impl.MainContentPresenter;
import com.dup.beauty.mvp.presenter.impl.MainMenuPresenter;
import com.dup.beauty.mvp.view.IBaseView;
import com.dup.beauty.mvp.view.IMainContentView;
import com.dup.beauty.mvp.view.IMainMenuView;

import dagger.Module;
import dagger.Provides;

/**
 * Created by DP on 2016/12/4.
 */
@Module
public class ActivityModule {

    private Activity activity;

    public ActivityModule(Activity activity) {
        this.activity = activity;
    }

    @Provides
    @PerActivity
    Activity provideMainActivity() {
        return activity;
    }

}
