package com.swapsharma.mvvm_android;

import android.app.Application;
import android.content.Context;

import com.swapsharma.mvvm_android.di.component.ApplicationComponent;
import com.swapsharma.mvvm_android.di.component.DaggerApplicationComponent;
import com.swapsharma.mvvm_android.di.module.ApplicationModule;

/**
 * Created by swapsharma on 1/31/17.
 */

public class DesignApplication extends Application {

    ApplicationComponent mApplicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public static DesignApplication get(Context context) {
        return (DesignApplication) context.getApplicationContext();
    }

    public ApplicationComponent getComponent() {
        if (mApplicationComponent == null) {
            mApplicationComponent = DaggerApplicationComponent.builder()
                    .applicationModule(new ApplicationModule(this))
                    .build();
        }
        return mApplicationComponent;
    }

    // Needed to replace the component with a test specific one
    public void setComponent(ApplicationComponent applicationComponent) {
        mApplicationComponent = applicationComponent;
    }
}
