package com.swapsharma.mvvm_android.di.module;

import android.app.Application;
import android.content.Context;

import com.swapsharma.mvvm_android.di.qualifier.ApplicationContext;

import dagger.Module;
import dagger.Provides;

/**
 * Provide application-level dependencies.
 */
@Module
public class ApplicationModule {
    protected final Application mApplication;

    public ApplicationModule(Application application) {
        mApplication = application;
    }

    @Provides
    Application provideApplication() {
        return mApplication;
    }

    @Provides
    @ApplicationContext
    Context provideContext() {
        return mApplication;
    }

//    @Provides
//    @Singleton
//    TileService provideTileService() {
//        return TileService.Creator.newTilesService();
//    }

}
