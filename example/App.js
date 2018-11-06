import React, {Component} from 'react';
import {Button, StyleSheet, Text, View} from 'react-native';
import EnjoySdk from 'react-native-enjoy-sdk';

const WALL_AD_PLACEMENT_ID = ''  // Your Wall Ad Placement Id

export default class App extends Component {
  state = {
    rewards: []
  }

  componentDidMount () {
    this.onWallAdCloseSubscription = EnjoySdk.addOnWallAdCloseListener(adObj => {
      console.log('OnWallAdClose', adObj);
    });

    this.onWallAdClickedSubscription = EnjoySdk.addOnWallAdClickedListener(data => {
      console.log('onWallAdClicked', data);
    });

    this.onRewardedSubscription = EnjoySdk.addOnRewardedListener(data => {
      console.log('onRewarded', data);
      const { rewards } = data;
      this.setState({ rewards });
    });
  }

  componentWillUnmount () {
    this.onWallAdCloseSubscription.remove();
    this.onWallAdClickedSubscription.remove();
    this.onRewardedSubscription.remove();
  }

  _showWallAd = () => {
    EnjoySdk.initWallAd(WALL_AD_PLACEMENT_ID)
    .then(adObj => {
      console.log('adObj', adObj);
      return EnjoySdk.showWallAd(adObj);
    })
    .then(showWallObj => console.log('showWallObj', showWallObj))
    .catch(err => console.log('err', err))
  }

  _finishFirstReward = () => {
    const { rewards } = this.state
    if (rewards && rewards.length) {
      const reward = rewards[0];
      EnjoySdk.finishReward(reward.rewardId)
      .then(data => {
        console.log('finishReward', data);
        this.setState({
          rewards: JSON.parse(JSON.stringify(rewards)).filter(rew => rew.rewardId != reward.rewardId)
        });
      })
      .catch(err => console.log('finishReward err', err))
    }
  }

  render() {
    const { rewards } = this.state

    return (
      <View style={styles.container}>
        <Text style={styles.title}>Enjoy Sdk</Text>
        <View style={styles.btnsContainer}>
          <Button
            title={'Show Wall Ad'}
            onPress={this._showWallAd}
          />
          {(rewards && rewards.length) ? (
            <Button
              title={'Finish First Reward in Array'}
              onPress={this._finishFirstReward}
            />
          ): null}
          <View style={styles.rewardsContainer}>
            <Text>{JSON.stringify(rewards, null, 2)}</Text>
          </View>
        </View>
      </View>
    );
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'flex-start',
    alignItems: 'center',
    backgroundColor: '#F5FCFF'
  },
  title: {
    fontSize: 20,
    textAlign: 'center',
    margin: 10
  },
  btnsContainer: {
    flex: 1,
    justifyContent: 'space-around',
    alignItems: 'center'
  }
});
