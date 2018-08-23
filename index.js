
import { NativeModules, DeviceEventEmitter } from 'react-native';

const { RNEnjoySdk } = NativeModules;

const EnjoySdk = {
  initWallAd: (placementId, adMark) => RNEnjoySdk.initWallAd(placementId, adMark || null),
  showWallAd: wallObj => RNEnjoySdk.showWallAd(wallObj),
  addOnWallAdCloseListener: listener => DeviceEventEmitter.addListener('enjoyOnWallAdClosed', listener),
  addOnWallAdClickedListener: listener => DeviceEventEmitter.addListener('enjoyOnWallAdClicked', listener),
 
  finishReward: rewardId => RNEnjoySdk.finishReward(rewardId),
  addOnRewardedListener: listener => DeviceEventEmitter.addListener('enjoyOnRewarded', listener)
}

export default EnjoySdk;
