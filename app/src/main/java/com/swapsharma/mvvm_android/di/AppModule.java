package com.swapsharma.mvvm_android.di;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Dagger module acts as an instance provider during dependency injection, when all modules
 * are searched for methods providing matching instance type.
 * <p>
 * Note: method parameters are fulfilled by other @Providers.
 */
@Module
public class AppModule {

    Application mApplication;

    public AppModule() {
        super();
    }

    public AppModule(Application application) {
        mApplication = application;
    }

    @Provides
    @Singleton
    Application providesApplication() {
        return mApplication;
    }

}
