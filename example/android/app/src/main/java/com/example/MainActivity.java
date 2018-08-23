package com.example;

import android.os.Bundle;

import com.facebook.react.ReactActivity;

import link.enjoy.rnsdk.EnjoyLifecycleHandler;

public class MainActivity extends ReactActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EnjoyLifecycleHandler.getInstance().onCreate(this, getString(R.string.enjoy_app_id));
    }

    @Override
    protected void onStart() {
        super.onStart();
        EnjoyLifecycleHandler.getInstance().onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        EnjoyLifecycleHandler.getInstance().onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        EnjoyLifecycleHandler.getInstance().onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        EnjoyLifecycleHandler.getInstance().onStop();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        EnjoyLifecycleHandler.getInstance().onRestart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EnjoyLifecycleHandler.getInstance().onDestroy();
    }

    /**
     * Returns the name of the main component registered from JavaScript.
     * This is used to schedule rendering of the component.
     */
    @Override
    protected String getMainComponentName() {
        return "example";
    }
}
