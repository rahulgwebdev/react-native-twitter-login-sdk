package com.reactnativetwitterloginsdk

import android.app.Activity
import android.content.Intent
import android.util.Log
import com.facebook.react.bridge.*
import com.twitter.sdk.android.core.*
import com.twitter.sdk.android.core.Callback
import com.twitter.sdk.android.core.TwitterCore
import com.twitter.sdk.android.core.identity.TwitterAuthClient


class TwitterLoginSdkModule(reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext), ActivityEventListener {

  var twitterAuthClient: TwitterAuthClient? = null

  init {
      reactApplicationContext.addActivityEventListener((this))
  }

    override fun getName(): String {
        return "TwitterLoginSdk"
    }

    @ReactMethod
    fun init(consumerKey: String, consumerSecret: String, promise: Promise){
      val config = TwitterConfig.Builder(reactApplicationContext)
        .logger(DefaultLogger(Log.DEBUG))
        .twitterAuthConfig(TwitterAuthConfig(consumerKey, consumerSecret))
        .debug(true)
        .build()
      Twitter.initialize(config)
      val map: WritableMap = Arguments.createMap()
      promise.resolve(map)
    }

  @ReactMethod
  fun logIn(promise: Promise) {
    twitterAuthClient = TwitterAuthClient()
    twitterAuthClient?.authorize(currentActivity, object : Callback<TwitterSession?>() {
      override fun success(result: Result<TwitterSession?>?) {
        val session = result?.data
        val twitterAuthToken = session?.authToken
        val map = Arguments.createMap()
        map.putString("authToken", twitterAuthToken?.token)
        map.putString("authTokenSecret", twitterAuthToken?.secret)
        map.putString("name", session?.userName)
        map.putString("userID", session?.userId.toString())
        map.putString("userName", session?.userName)
        twitterAuthClient?.requestEmail(session, object : Callback<String?>() {
          override fun success(result: Result<String?>?) {
            map.putString("email", result?.data)
            promise.resolve(map)
          }

          override fun failure(exception: TwitterException) {
            map.putString("email", "COULD_NOT_FETCH")
            promise.reject(
              "COULD_NOT_FETCH",
              map.toString(),
              Exception("Failed to obtain email", exception))
          }
        })
      }

      override fun failure(exception: TwitterException) {
        promise.reject("USER_CANCELLED", exception.message, exception)
      }
    })
  }

  @ReactMethod
  fun logOut() {
    val instance = TwitterCore.getInstance()
    val sessionManager = instance.sessionManager
    val sessions = sessionManager.sessionMap
    println("TWITTER SEESIONS " + +sessions.size)
    val sessids: Set<Long> = sessions.keys
    for (sessid in sessids) {
      println("TWITTER SESSION CLEARING $sessid")
      instance.sessionManager.clearSession(sessid)
    }
    sessionManager
      .clearActiveSession()
  }

    override fun onNewIntent(intent: Intent?) {
      TODO("Not yet implemented")
    }

    override fun onActivityResult(activity: Activity?, requestCode: Int, resultCode: Int, data: Intent?) {
      if (twitterAuthClient != null && twitterAuthClient!!.requestCode == requestCode) {
        twitterAuthClient!!.onActivityResult(requestCode, resultCode, data);
      }
    }

}
