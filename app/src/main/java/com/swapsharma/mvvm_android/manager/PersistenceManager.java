package com.swapsharma.mvvm_android.manager;

import android.content.Context;

/**
 * Responsible for persistent storage and removal of items like search history or other locally
 * cached data. Depending on the item it may be stored differently using a ContentProvider,
 * SharedPreferences, database, or file system.
 * <p/>
 * Uses: N/A
 * <p/>
 * Used by: Contextual managers (.) that need to persist data.
 * <p/>
 * Not used by: Controllers (i.e. Activities or Fragments).
 */
public class PersistenceManager extends Manager {

    private static final String TAG = PersistenceManager.class.getSimpleName();

    public PersistenceManager(Context applicationContext) {
        super(applicationContext);
    }


}
