package com.swapsharma.mvvm_android.di.component;

import android.app.Application;
import android.content.Context;

import com.swapsharma.mvvm_android.di.ApplicationContext;
import com.swapsharma.mvvm_android.di.module.ApplicationModule;
import com.swapsharma.mvvm_android.manager.DataManager;
import com.swapsharma.mvvm_android.network.TileService;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    //void inject(SyncService syncService);

    @ApplicationContext
    Context context();

    Application application();

    TileService tileService();

    DataManager dataManager();
//    RxEventBus eventBus();

}
