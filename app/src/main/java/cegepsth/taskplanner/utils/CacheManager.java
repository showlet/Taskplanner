package cegepsth.taskplanner.utils;

import android.content.Context;

import android.net.http.HttpResponseCache;
import android.os.Build;

import android.util.Log;

import java.io.File;
import java.io.IOException;

/**
 * Created by Maxim on 12/17/2015.
 */
public class CacheManager {
    private static final String TAG = "CacheManager";

    private static CacheManager mInstance;
    private final Context mContext;

    public static final long HTTP_CACHE_SIZE = 10 * 1024 * 1024;

    /**
     * CTOR
     * @param context Context de l'application
     */
    private CacheManager(Context context) {
        mContext = context;

    }

    /**
     * Permet d'initialiser l'instance du singleton pour le cache manager
     * @param context
     */
    public static synchronized void initializeInstance(Context context) {
        if (mInstance == null) {
            mInstance = new CacheManager(context);
        }
    }

    /**
     * Permet d'obtenir l'instance du singleton pour le cache manager.
     * @return L'instance du singleton
     */
    public static synchronized CacheManager getInstance() {
        if (mInstance == null) {
            throw new IllegalStateException(CacheManager.class.getSimpleName() +
                    " is not initialized, call initializeInstance(Context) method first.");
        }
        return mInstance;
    }

    /**
     * Active le http caching
     */
    public void enableHttpCaching()
    {
        try {
            File httpCacheDir = new File(mContext.getCacheDir(), "http");
            if(HttpResponseCache.getInstalled() == null)
                HttpResponseCache.install(httpCacheDir, HTTP_CACHE_SIZE);
        }
        catch (IOException e) {
            Log.i(TAG, "HTTP response cache installation failed:" + e);
        }
    }

    /**
     * Flush la cache http
     */
    public void flushHttpCache()
    {
        HttpResponseCache cache = HttpResponseCache.getInstalled();
        if (cache != null) {
            cache.flush();
        }
    }
}
