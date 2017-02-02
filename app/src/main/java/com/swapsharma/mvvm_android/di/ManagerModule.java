package com.swapsharma.mvvm_android.di;

import android.app.Application;

import com.swapsharma.mvvm_android.manager.ConfigurationManager;
import com.swapsharma.mvvm_android.manager.PersistenceManager;
import com.swapsharma.mvvm_android.manager.ServiceManager;

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
public class ManagerModule {

    public ManagerModule() {
    }

    @Provides
    @Singleton
    ConfigurationManager provideConfigurationManager(Application application) {
        return new ConfigurationManager(application.getApplicationContext());
    }

    @Provides
    @Singleton
    PersistenceManager providePersistenceManager(Application application) {
        return new PersistenceManager(application.getApplicationContext());
    }

    @Provides
    @Singleton
    ServiceManager provideServiceManager(Application application,
                                         ConfigurationManager configurationManager) {
        return new ServiceManager(application.getApplicationContext(), configurationManager);
    }




}
