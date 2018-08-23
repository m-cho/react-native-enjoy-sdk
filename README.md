

# react-native-enjoy-sdk

## Table of Content
- [Install Instruction](#install-instruction)
-- [Mostly automatic installation](#mostly-automatic-installation) 
-- [Manual installation](#manual-installation) 
- [After installation instruction](#after-installation-instruction)
- [Usage](#usage)
-- [Available Methods](#available-methods)
-- [Data Types](#data-types)

## Install Instruction

### Mostly automatic installation
`$ npm install react-native-enjoy-sdk --save`

`$ react-native link react-native-enjoy-sdk`

**[Android - After installation instruction](#after-installation-instruction)**

<br/>

### Manual installation

#### Android
1. Append the following lines to `android/settings.gradle`:
```diff
+include ':react-native-enjoy-sdk'
+project(':react-native-enjoy-sdk').projectDir = new File(rootProject.projectDir, '../node_modules/react-native-enjoy-sdk/android')
 include ':app'
```
2. Insert the following lines inside the dependencies block in `android/app/build.gradle`:
```diff
dependencies {
+   compile project(':react-native-enjoy-sdk')
    compile fileTree(dir: "libs", include: ["*.jar"])
    compile "com.android.support:appcompat-v7:${rootProject.ext.supportLibVersion}"
    compile "com.facebook.react:react-native:+"  // From node_modules
}
```
3. Open up `android/app/src/main/java/[...]/MainApplication.java`
  - Add `import link.enjoy.rnsdk.RNEnjoySdkPackage;` to the imports at the top of the file
  - Add `new RNEnjoySdkPackage()` to the list returned by the `getPackages()` method
```diff
import android.app.Application;
import com.facebook.react.ReactApplication;
import com.facebook.react.ReactPackage;
import com.facebook.react.shell.MainReactPackage;
+import link.enjoy.rnsdk.RNEnjoySdkPackage;

import java.util.Arrays;
import java.util.List;

public class MainApplication extends Application implements ReactApplication {
	...
	...
    @Override
    protected List<ReactPackage> getPackages() {
      return Arrays.<ReactPackage>asList(
          new MainReactPackage(),
+            new RNEnjoySdkPackage()
      );
    }
}

```


**[Android - After installation instruction](#after-installation-instruction)**

<br/>

<!--
#### iOS

1. In XCode, in the project navigator, right click `Libraries` ➜ `Add Files to [your project's name]`
2. Go to `node_modules` ➜ `react-native-enjoy-sdk` and add `RNEnjoySdk.xcodeproj`
3. In XCode, in the project navigator, select your project. Add `libRNEnjoySdk.a` to your project's `Build Phases` ➜ `Link Binary With Libraries`
4. Run your project (`Cmd+R`)<
-->

<br/>

## After installation instruction
1. Open up **`android/build.gradle`** and add **`mvn.dreamsky.me:6080`** repository to your project.
```diff

allprojects {
    repositories {
        mavenLocal()
        jcenter()
+       maven {
+           url 'http://mvn.dreamsky.me:6080/'
+       }
        maven {
            // All of React Native (JS, Obj-C sources, Android binaries) is installed from npm
            url "$rootDir/../node_modules/react-native/android"
        }
    }
}
```
2. In **`android/app/src/main[...]/MainActivity.java`**, rewrite Activity related life-cycle method, and invoke corresponded Enjoy SDK method from it. Pass your **Enjoy App Id** to **Enjoy SDK `onCreate`** method.

Full list of corresponded life-cycle method:
|Activity Life Cycle| Enjoy SDK Corresponded method |
|--|--|
| onStart() | onStart() |
|onResume() | onResume() |
| onPause() | onPause() |
| onStop() | onStop() |
| onRestart() | onRestart() |
| onDestroy() | onDestroy() |



Example: 
```diff java
import com.facebook.react.ReactActivity;
+import link.enjoy.rnsdk.EnjoyLifecycleHandler;
+import android.os.Bundle;

public class MainActivity extends ReactActivity {
+    private  final  String  ENJOY_APP_ID  =  ""; // Fill with Enjoy App Id

+    @Override
+    protected void onCreate(Bundle savedInstanceState) {
+        super.onCreate(savedInstanceState);
+        EnjoyLifecycleHandler.getInstance().onCreate(this, ENJOY_APP_ID);
+    }

+    @Override
+    protected void onStart() {
+        super.onStart();
+        EnjoyLifecycleHandler.getInstance().onStart();
+    }

+    @Override
+    protected void onResume() {
+        super.onResume();
+        EnjoyLifecycleHandler.getInstance().onResume();
+    }

+    @Override
+    protected void onPause() {
+        super.onPause();
+        EnjoyLifecycleHandler.getInstance().onPause();
+    }

+    @Override
+    protected void onStop() {
+        super.onStop();
+        EnjoyLifecycleHandler.getInstance().onStop();
+    }

+    @Override
+    protected void onRestart() {
+        super.onRestart();
+        EnjoyLifecycleHandler.getInstance().onRestart();
+    }

+    @Override
+    protected void onDestroy() {
+        super.onDestroy();
+        EnjoyLifecycleHandler.getInstance().onDestroy();
+    }

    /**
     * Returns the name of the main component registered from JavaScript.
     * This is used to schedule rendering of the component.
     */
    @Override
    protected String getMainComponentName() {
        return "example";
    }
}
```

<br/>

## Usage
Import it in your JS:
```javascript
import EnjoySdk from 'react-native-enjoy-sdk';
```

#### Available Methods
- `initWallAd(placementId: string, adMark?: string): Promise<AdObj>`
-- Initiate Wall Ad with **`placementId`**.
-- If you want to give the current ad a unique identification, you can use **`adMark`**, which is optional. 

```javascript
EnjoySdk.initWallAd(WALL_AD_PLACEMENT_ID, userId)
    .then(adObj => console.log('adObj', adObj))
    .catch(err => console.log('err', err))
```

<br/>

- `showWallAd(adObj: AdObj): Promise<AdObj>`
-- Show Wall Ad

```javascript
EnjoySdk.showWallAd(adObj)
    .then(adObj => console.log('adObj', adObj))
    .catch(err => console.log('err', err))
```

<br/>

- `addOnWallAdCloseListener(clb: (adObj: AdObj) => void ): Subscription`
-- Callback is called when Wall Ad is closed
-- Use `remove()` method from  `Subscription` object to remove listener

```javascript
componentDidMount () {
    this.onWallAdCloseSubscription = EnjoySdk.addOnWallAdCloseListener(adObj => {
      console.log('OnWallAdClose', adObj);
    });
}

componentWillUnmount () {
	this.onWallAdCloseSubscription.remove();
}
```

<br/>

- `addOnWallAdClickedListener(clb: (adObj: AdObj) => void ): Subscription`
-- Callback is called when user click on Wall Ad
-- Use `remove()` method from  `Subscription` object to remove listener

```javascript
componentDidMount () {
    this.onWallAdClickedSubscription = EnjoySdk.addOnWallAdClickedListener(adObj => {
      console.log('OnWallAdClicked', adObj);
    });
}

componentWillUnmount () {
	this.onWallAdClickedSubscription.remove();
}
```

<br/>

- `addOnRewardedListener(clb: (data: { rewards: Array<rewardObj> }) => void): Subscription`
-- Callback is called when user needs to receive reward(s)
-- Use `remove()` method from  `Subscription` object to remove listener


```javascript
componentDidMount () {
    this.onRewardedSubscription = EnjoySdk.addOnRewardedListener(data => {
      console.log('onRewarded', data);
    });
}

componentWillUnmount () {
	this.onRewardedSubscription.remove();
}
```

<br/>

- `finishReward(rewardId: String): Promise<void>`
-- Call this method when you reward your user

```javascript
EnjoySdk.finishReward(rewardId)
    .then(() => console.log('Finished'))
    .catch(err => console.log('err', err))
```

<br/>

#### Data Types
```typescript
type AdObj = {
	adType: string;
	placementId: string;
}

type Subscription = {
	remove: () => void
}

type RewardObj = {
	rewardId: string;
	rewardNum: number;
	adMark: string;
	transactionId: string;
}
```