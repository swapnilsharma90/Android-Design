package com.swapsharma.mvvm_android.manager;

import android.content.Context;

/**
 * Base manager class for other managers to extend.
 */
abstract class Manager {

    @SuppressWarnings({"unused"})
    Context applicationContext;

    Manager(Context applicationContext) {
        this.applicationContext = applicationContext;
    }

}
