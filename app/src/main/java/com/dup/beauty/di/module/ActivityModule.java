package com.dup.beauty.di.module;

import android.content.Context;

import com.dup.beauty.di.scope.PerActivity;

import dagger.Module;
import dagger.Provides;

/**
 * Created by DP on 2016/12/4.
 */
@Module
public class ActivityModule {

    private Context context;

    public ActivityModule(Context context) {
        this.context = context;
    }

    @Provides
    @PerActivity
    Context provideMainActivity() {
        return context;
    }

}
