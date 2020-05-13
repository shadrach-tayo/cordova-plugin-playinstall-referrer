package com.shadrach.cordova.plugin;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import android.os.RemoteException;

import com.android.installreferrer.api.InstallReferrerClient;
import com.android.installreferrer.api.InstallReferrerStateListener;
import com.android.installreferrer.api.ReferrerDetails;

import android.util.Log;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

import org.json.JSONArray;
import org.json.JSONException;

public class PlayInstallReferrer extends CordovaPlugin {
    private static final String LOG_TAG = "GPlayInstallReferrer";
    Context context;
    InstallReferrerClient referrerClient = null;

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        Log.d(LOG_TAG, "Executing com.shadrach.cordova.plugin");
        context = this.cordova.getContext();
        referrerClient = InstallReferrerClient.newBuilder(this.cordova.getContext()).build();
        referrerClient.startConnection(new InstallReferrerStateListener() {

            @Override
            public void onInstallReferrerSetupFinished(int responseCode) {
                switch (responseCode) {
                    case InstallReferrerClient.InstallReferrerResponse.OK:
                        // Connection established.
                        Log.d(LOG_TAG, "InstallReferrer Response.OK");
                        try {

                            ReferrerDetails response = referrerClient.getInstallReferrer();
                            String referrerUrl = response.getInstallReferrer();
                            Log.d(LOG_TAG, "InstallReferrer " + referrerUrl);

                            SharedPreferences sharedPreferences = PreferenceManager
                                    .getDefaultSharedPreferences(context);
                            Editor edit = sharedPreferences.edit();
                            edit.putString("referrer", referrerUrl);
                            edit.commit();

                            callbackContext.success(referrerUrl);
                            referrerClient.endConnection();
                        } catch (RemoteException e) {
                            e.printStackTrace();
                            callbackContext.error(e.getMessage());
                            referrerClient.endConnection();
                        }

                        break;
                    case InstallReferrerClient.InstallReferrerResponse.FEATURE_NOT_SUPPORTED:
                        // API not available on the current Play Store app.
                        Log.w(LOG_TAG, "InstallReferrer Response.FEATURE_NOT_SUPPORTED");

                        callbackContext.error("Feature not supported");
                        referrerClient.endConnection();
                        break;
                    case InstallReferrerClient.InstallReferrerResponse.SERVICE_UNAVAILABLE:
                        // Connection couldn't be established.
                        Log.w(LOG_TAG, "InstallReferrer Response.SERVICE_UNAVAILABLE");

                        callbackContext.success("connection couldn't be established");
                        referrerClient.endConnection();
                        break;
                    case InstallReferrerClient.InstallReferrerResponse.SERVICE_DISCONNECTED:
                        Log.w(LOG_TAG, "InstallReferrer Response.SERVICE_DISCONNECTED");
                        callbackContext.error("SERVICE_DISCONNECTED");
                        referrerClient.endConnection();
                        break;
                    case InstallReferrerClient.InstallReferrerResponse.DEVELOPER_ERROR:
                        Log.w(LOG_TAG, "InstallReferrer Response.DEVELOPER_ERROR");
                        callbackContext.error("DEVELOPER_ERROR");
                        referrerClient.endConnection();
                        break;
                }
            }

            @Override
            public void onInstallReferrerServiceDisconnected() {
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
            }

        });

        return true;

    }
}