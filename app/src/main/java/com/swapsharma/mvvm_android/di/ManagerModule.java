package com.swapsharma.mvvm_android.di;

import dagger.Module;

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
}
