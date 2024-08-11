package com.toufiq.testapplication


import android.content.Context
import android.content.SharedPreferences

class PrefManager {
    companion object {
        var mInstance: PrefManager? = null

        fun setInstance() {
            if (mInstance == null) {
                mInstance = PrefManager()
            }
        }

        fun getInstance(): PrefManager {
            return this.mInstance!!
        }

        fun updateValue(key: String, value: Long) {
            val sharedPref: SharedPreferences =
                BaseApplication.instance.applicationContext.getSharedPreferences(
                    Constants.SHARED_PREFERENCE, Context.MODE_PRIVATE
                )
            val editor: SharedPreferences.Editor = sharedPref.edit()
            editor.putLong(key, value)
            editor.apply()
        }

        fun removeValue(key: String) {
            val sharedPref: SharedPreferences =
                BaseApplication.instance.applicationContext.getSharedPreferences(
                    Constants.SHARED_PREFERENCE, Context.MODE_PRIVATE
                )
            val editor: SharedPreferences.Editor = sharedPref.edit()
            editor.remove(key)
            editor.apply()
        }

        fun updateValue(key: String, value: String?) {
            val sharedPref: SharedPreferences =
                BaseApplication.instance.applicationContext.getSharedPreferences(
                    Constants.SHARED_PREFERENCE, Context.MODE_PRIVATE
                )
            val editor: SharedPreferences.Editor = sharedPref.edit()
            editor.putString(key, value)
            editor.apply()
        }

        fun updateValue(key: String, value: Int) {
            val sharedPref: SharedPreferences =
                BaseApplication.instance.applicationContext.getSharedPreferences(
                    Constants.SHARED_PREFERENCE, Context.MODE_PRIVATE
                )
            val editor: SharedPreferences.Editor = sharedPref.edit()
            editor.putInt(key, value!!)
            editor.apply()
        }

        fun getBool(key: String): Boolean {
            val sharedPref: SharedPreferences =
                BaseApplication.instance.applicationContext.getSharedPreferences(
                    Constants.SHARED_PREFERENCE, Context.MODE_PRIVATE
                )
            return sharedPref.getBoolean(key, false)
        }

        fun getLong(key: String): Long {
            val sharedPref: SharedPreferences =
                BaseApplication.instance.applicationContext.getSharedPreferences(
                    Constants.SHARED_PREFERENCE, Context.MODE_PRIVATE
                )
            return sharedPref.getLong(key, 0)
        }

        fun getString(key: String): String {
            val sharedPref: SharedPreferences =
                BaseApplication.instance.applicationContext.getSharedPreferences(
                    Constants.SHARED_PREFERENCE, Context.MODE_PRIVATE
                )
            return sharedPref.getString(key, "").toString()
        }

        fun getInt(key: String): Int {
            val sharedPref: SharedPreferences =
                BaseApplication.instance.applicationContext.getSharedPreferences(
                    Constants.SHARED_PREFERENCE, Context.MODE_PRIVATE
                )
            return sharedPref.getInt(key, 0)
        }

        fun removeAllPrefData() {
            val sharedPref: SharedPreferences =
                BaseApplication.instance.applicationContext.getSharedPreferences(
                    Constants.SHARED_PREFERENCE, Context.MODE_PRIVATE
                )
            val editor: SharedPreferences.Editor = sharedPref.edit()
            editor.clear().apply()
        }


    }

}