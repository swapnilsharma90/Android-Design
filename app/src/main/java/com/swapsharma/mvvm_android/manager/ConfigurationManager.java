package com.swapsharma.mvvm_android.manager;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;

/**
 * Responsible for retrieving build configuration information such
 * as endpoints and behavior flags.
 * <p/>
 * Uses: N/A
 * <p/>
 * Used by: ServiceManager, any controller or manager for
 * which broad behavior is server configurable.
 * <p/>
 * Not used by: N/A
 */
public class ConfigurationManager extends Manager {

    private static String restScheme;
    private static String restDomain;


    private String restEndpoint;

    public ConfigurationManager(Context applicationContext) {
        super(applicationContext);

        restEndpoint = restScheme + restDomain;
    }

    public static void setRestScheme(String restScheme) {
        ConfigurationManager.restScheme = restScheme;
    }

    public static void setRestDomain(String restDomain) {
        ConfigurationManager.restDomain = restDomain;
    }



    public String getRestEndpoint() {
        return restEndpoint;
    }

    public Map<String, String> getRestClientHeaders() {
        HashMap<String, String> headers = new HashMap<>();

        return headers;
    }

    public boolean isRequestLoggingEnabled() {
        // TODO: determine value from dev settings once implemented.
        return true;
    }

}
