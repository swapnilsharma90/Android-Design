package com.swapsharma.mvvm_android.di.component;

import com.swapsharma.mvvm_android.di.scopes.PerActivity;
import com.swapsharma.mvvm_android.di.module.ActivityModule;
import com.swapsharma.mvvm_android.ui.activity.MainActivity;
import com.swapsharma.mvvm_android.ui.activity.MosaicActivity;

import dagger.Subcomponent;


/**
 * This component inject dependencies to all Activities across the application
 */
@PerActivity
@Subcomponent(modules = ActivityModule.class)
public interface ActivityComponent {

    void inject(MainActivity mainActivity);
    void inject(MosaicActivity mosaicActivity);

}
