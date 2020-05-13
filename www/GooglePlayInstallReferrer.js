var exec = require("cordova/exec");

exports.getReferrer = function getReferrer(successCallback, errorCallback) {
  if (!successCallback) {
    return new Promise(function (resolve, reject) {
      getReferrer(resolve, reject);
    });
  }

  exec(
    function success(ref) {
      console.log("success ", ref);
      if (ref) {
        successCallback(ref);
      } else {
        errorCallback("No referrer data");
      }
    },
    errorCallback,
    "GooglePlayReferrer",
    "getReferrer"
  );
};
