package org.wei.sptask;

import android.content.Context;
import android.support.multidex.MultiDexApplication;

/**
 * Created by admin on 3/8/2017.
 */
public class SPTaskApplication  extends MultiDexApplication {
    public static Context applicationContext;
    private static SPTaskApplication instance;

    @Override
    public void onCreate(){
        super.onCreate();

        applicationContext = this;
        instance = this;

        initSingletons();
    }

    protected void initSingletons(){

    }

    public static SPTaskApplication getInstance() {
        return instance;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

    }
}
