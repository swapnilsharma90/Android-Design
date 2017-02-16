package com.swapsharma.mvvm_android.di.component;

import com.swapsharma.mvvm_android.di.scopes.ConfigPersistent;
import com.swapsharma.mvvm_android.di.module.ActivityModule;

import dagger.Component;


/**
 * A dagger component that will live during the lifecycle of an Activity but it won't
 * be destroy during configuration changes. Check {@link com.swapsharma.mvvm_android.ui.activity.base.BaseActivity} to see how this components
 * survives configuration changes.
 * Use the {@link ConfigPersistent} scope to annotate dependencies that need to survive
 * configuration changes (for example Presenters).
 */
@ConfigPersistent
@Component(dependencies = ApplicationComponent.class)
public interface ConfigPersistentComponent {

    ActivityComponent activityComponent(ActivityModule activityModule);

}
