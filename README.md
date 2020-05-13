# cordova-plugin-playinstall-referrer

An Android Cordova plugin that allows users to capture google play install referrer

## platform

- Android

## Installation

- From cordova plugins registry: `cordova-plugin-playinstall-referrer`
- From github repository: `cordova plugin add https://github.com/shadrach-tayo/cordova-plugin-playinstall-referrer.git`

## Android Dependency

Add google play install referrer client api in your app level **build.gradle**

```Java
dependencies {
    ...
    implementation 'com.android.installreferrer:installreferrer:1.1.2'
}
```

## Usage

```javascript
this.platform.ready().then((_) => {
  const installReferrer = cordova.plugins.playInstallReferrer
    .getReferrer()
    .then((ref) => {
      // referrer string is returned - defaults to utm_source=google-play&utm_medium=origin
    })
    .catch((err) => console.log(err));
});
```

# Credits

Created and maintained by [@shadrach-tayo](https://github.com/shadrach-tayo)

Twitter [@oloyedeshadrach](https://twitter.com/oloyedeshadrach)

LinkedIn [@oloyedeshadrach](https://www.linkedin.com/in/shadrach-oloyede)
