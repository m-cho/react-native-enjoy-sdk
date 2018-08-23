
import { NativeModules, DeviceEventEmitter, Platform } from 'react-native';

const { RNEnjoySdk } = NativeModules;

const EnjoySdk = Platform.OS === 'android' ? ({
  initWallAd: (placementId, adMark) => RNEnjoySdk.initWallAd(placementId, adMark || null),
  showWallAd: wallObj => RNEnjoySdk.showWallAd(wallObj),
  addOnWallAdCloseListener: listener => DeviceEventEmitter.addListener('enjoyOnWallAdClosed', listener),
  addOnWallAdClickedListener: listener => DeviceEventEmitter.addListener('enjoyOnWallAdClicked', listener),
 
  finishReward: rewardId => RNEnjoySdk.finishReward(rewardId),
  addOnRewardedListener: listener => DeviceEventEmitter.addListener('enjoyOnRewarded', listener)
}) : ({
  initWallAd: () => Promise.resolve(),
  showWallAd: () => Promise.resolve(),
  addOnWallAdCloseListener: () => ({ remove: () => null }),
  addOnWallAdClickedListener: () => ({ remove: () => null }),
  
  finishReward: () => Promise.resolve(),
  addOnRewardedListener: () => ({ remove: () => null })
});

export default EnjoySdk
