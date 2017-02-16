package com.swapsharma.mvvm_android.di.component;

import android.app.Application;
import android.content.Context;

import com.swapsharma.mvvm_android.di.qualifier.ApplicationContext;
import com.swapsharma.mvvm_android.di.module.ApplicationModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {
    @ApplicationContext
    Context context();

    Application application();
}
