package link.enjoy.rnsdk;

import android.app.Activity;

import link.enjoy.sdk.EnjoyAds;

public class EnjoyLifecycleHandler {
    private static EnjoyLifecycleHandler instance;
    private EnjoyAds enjoyAds;

    public static EnjoyLifecycleHandler getInstance() {
        if (instance == null) {
            instance = new EnjoyLifecycleHandler();
        }

        return instance;
    }

    private EnjoyLifecycleHandler() {}

    public EnjoyAds getEnjoyAds() {
        return enjoyAds;
    }

    public void onCreate(Activity activity, String appId) {
        this.enjoyAds = EnjoyAds.initialize(activity, appId);
    }

    public void onStart() {
        this.enjoyAds.onStart();
    }

    public void onResume() {
        this.enjoyAds.onResume();
    }

    public void onPause() {
        this.enjoyAds.onPause();
    }

    public void onStop() {
        this.enjoyAds.onStop();
    }

    public void onRestart() {
        this.enjoyAds.onRestart();
    }

    public void onDestroy() {
        this.enjoyAds.onDestroy();
    }
}