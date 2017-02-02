package com.swapsharma.mvvm_android.manager;

import android.content.Context;

import okhttp3.OkHttpClient;

/**
 * Responsible for managing web service calls, setting appropriate endpoints for various
 * environments, adding of headers, etc. It also handles common network errors, retries, and
 * incoming object parsing.
 * <p/>
 * Uses: ConfigurationManager
 * <p/>
 * Used by: Contextual managers that need to retrieve
 * remote data.
 * <p/>
 * Not used by: Controllers (i.e. Activities or Fragments).
 */
public class ServiceManager extends Manager {

    private static final String TAG = ServiceManager.class.getSimpleName();

    private ConfigurationManager configurationManager;
    private OkHttpClient restEndpointClient;

    public ServiceManager(Context applicationContext, ConfigurationManager configurationManager) {
        super(applicationContext);
        this.configurationManager = configurationManager;
    }

}
