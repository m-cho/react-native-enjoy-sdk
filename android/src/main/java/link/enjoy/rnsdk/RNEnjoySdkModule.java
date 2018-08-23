
package link.enjoy.rnsdk;

import android.util.Log;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Nullable;

import link.enjoy.sdk.AdError;
import link.enjoy.sdk.EnjoyAds;
import link.enjoy.sdk.RewardListener;
import link.enjoy.sdk.RewardLog;
import link.enjoy.sdk.WallAd;
import link.enjoy.sdk.WallAdListener;


public class RNEnjoySdkModule extends ReactContextBaseJavaModule implements RewardListener, WallAdListener {
    private final String WALL_ADS = "WALL_ADS";

    private final String E_WALL_AD_WITH_THIS_PLACEMENT_ID_NOT_INIT = "E_WALL_AD_WITH_THIS_PLACEMENT_ID_NOT_INIT";
    private final String E_WALL_AD_NOT_LOADED = "E_WALL_AD_NOT_LOADED";
    private final String E_REWARD_ID_NOT_VALID = "E_REWARD_ID_NOT_VALID";

    private final String EV_ON_WALL_AD_CLOSED = "enjoyOnWallAdClosed";
    private final String EV_ON_WALL_AD_CLICKED = "enjoyOnWallAdClicked";
    private final String EV_ON_REWARDED = "enjoyOnRewarded";

    private final ReactApplicationContext mReactContext;
    private EnjoyAds enjoyAds;
    private Promise adLoadedPromise;
    private Promise onShowWallAdPromise;

    private HashMap<String, WallAd> wallAds = new HashMap<>();
    private HashMap<String, RewardLog.RewardLogBean> allRewards = new HashMap<>();

    public RNEnjoySdkModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.mReactContext = reactContext;

        enjoyAds = EnjoyLifecycleHandler.getInstance().getEnjoyAds();

        enjoyAds.setRewardListener(this);
    }

    @Override
    public String getName() {
        return "RNEnjoySdk";
    }

    private void sendEvent (String eventName, @Nullable WritableMap params) {
        mReactContext
            .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
            .emit(eventName, params);
    }

    private WritableMap generateAdMap (String adType, String placementId) {
        WritableMap map = Arguments.createMap();
        map.putString("adType", adType);
        map.putString("placementId", placementId);
        return map;
    }

    private WritableMap generateRewardMap (RewardLog.RewardLogBean rewardLogBean) {
        WritableMap reward = Arguments.createMap();

        reward.putString("rewardId", rewardLogBean.getRewardId());
        reward.putInt("rewardNum", rewardLogBean.getRewardNum());
        reward.putString("adMark", rewardLogBean.getAdMark());
        reward.putString("transactionId", rewardLogBean.getTransactionId());

        return reward;
    }

    @Override
    public void onRewarded(RewardLog rewardLog) {
        List<RewardLog.RewardLogBean> rewardLogList = rewardLog.getRewardLogList();

        WritableMap eventParams = Arguments.createMap();

        WritableArray rewards = Arguments.createArray();

        for (int i = 0; i < rewardLogList.size(); i++) {
            RewardLog.RewardLogBean rewardLogBean = rewardLogList.get(i);

            allRewards.put(rewardLogBean.getRewardId(), rewardLogBean);

            WritableMap reward = generateRewardMap(rewardLogBean);

            rewards.pushMap(reward);
        }

        eventParams.putArray("rewards", rewards);

        sendEvent(EV_ON_REWARDED, eventParams);
    }

    @Override
    public void onWallShowed(String placementId) {
        if (onShowWallAdPromise != null) {
            onShowWallAdPromise.resolve(generateAdMap(WALL_ADS, placementId));
            onShowWallAdPromise = null;
        }
    }

    @Override
    public void onWallClosed(String placementId) {
        sendEvent(EV_ON_WALL_AD_CLOSED, generateAdMap(WALL_ADS, placementId));
    }

    @Override
    public void onAdClicked(String placementId) {
        sendEvent(EV_ON_WALL_AD_CLICKED, generateAdMap(WALL_ADS, placementId));
    }

    @Override
    public void onAdLoaded(String placementId) {
        if (adLoadedPromise != null) {
            adLoadedPromise.resolve(generateAdMap(WALL_ADS, placementId));
            adLoadedPromise = null;
        }
    }

    @Override
    public void onError(String placementId, AdError adError) {
        Log.d("ENJOYSDK errCode", String.valueOf(adError.getErrorCode()));
        Log.d("ENJOYSDK errMsg", adError.getMessage());

        if (adLoadedPromise != null) {
            adLoadedPromise.reject(String.valueOf(adError.getErrorCode()), adError.getMessage());
            adLoadedPromise = null;
        }
        if (onShowWallAdPromise != null) {
            onShowWallAdPromise.reject(String.valueOf(adError.getErrorCode()), adError.getMessage());
            onShowWallAdPromise = null;
        }
    }

    @ReactMethod
    public void initWallAd (String placementId, @Nullable String adMark, Promise promise) {
        WallAd wallAd = wallAds.get(placementId);

        if (wallAd == null) {
            adLoadedPromise = promise;
            wallAd = new WallAd(mReactContext.getCurrentActivity(), placementId);
            wallAd.setWallAdListener(this);

            if (adMark != null && !adMark.isEmpty()) { wallAd.setAdMark(adMark); }

            wallAds.put(placementId, wallAd);
        } else {
            if (adMark != null && !adMark.isEmpty()) { wallAd.setAdMark(adMark); }
            adLoadedPromise = null;
            promise.resolve(generateAdMap(WALL_ADS, placementId));
        }

    }

    @ReactMethod
    public void showWallAd (ReadableMap wallObj, Promise promise) {
        WallAd wallAd = wallAds.get(wallObj.getString("placementId"));

        if (wallAd == null) {
            promise.reject(E_WALL_AD_WITH_THIS_PLACEMENT_ID_NOT_INIT, "WallAd with this placement id is not initialized.");
            onShowWallAdPromise = null;
            return;
        }
        if (!wallAd.isLoaded()) {
            promise.reject(E_WALL_AD_NOT_LOADED, "WallAd not loaded.");
            onShowWallAdPromise = null;
            return;
        }
        onShowWallAdPromise = promise;
        wallAd.show();
    }

    @ReactMethod
    public void finishReward (String rewardId, Promise promise) {
        RewardLog.RewardLogBean rewardLogBean = allRewards.get(rewardId);

        if (rewardLogBean == null) {
            promise.reject(E_REWARD_ID_NOT_VALID, "Reward Id not valid or reward not found.");
            return;
        }

        enjoyAds.finishReward(rewardLogBean);
        promise.resolve(null);
    }
}