package com.example.shopnest.utils

import android.content.Context
import android.content.SharedPreferences

object GDPRPrefs {
    private const val PREF_NAME = "gdpr_prefs"
    private const val KEY_AGREED = "gdpr_agreed"

    fun hasAgreed(context: Context): Boolean {
        val prefs: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean(KEY_AGREED, false)
    }

    fun setAgreed(context: Context) {
        val prefs: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putBoolean(KEY_AGREED, true).apply()
    }
}
