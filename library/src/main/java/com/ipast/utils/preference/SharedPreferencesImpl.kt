package com.ipast.utils.preference

import android.content.SharedPreferences
import android.content.SharedPreferences.Editor

/**
 * @author gang.cheng
 * @description :
 * @date :2021/4/19
 */
object SharedPreferencesImpl {
    @JvmStatic
    fun setValue(editor: Editor, key: String?, value: String?) {
        editor.putString(key, value)
        editor.apply()
    }
    @JvmStatic
    fun getEditor(sp: SharedPreferences): Editor? {
        return sp.edit()
    }
    @JvmStatic
    fun getValue(sp: SharedPreferences, key: String?, defValue: String?): String? {
        return sp.getString(key, defValue)
    }
    @JvmStatic
    fun setValue(editor: Editor, key: String?, value: Boolean) {
        editor.putBoolean(key, value)
        editor.apply()
    }
    @JvmStatic
    fun getValue(sp: SharedPreferences, key: String?, defValue: Boolean): Boolean {
        return sp.getBoolean(key, defValue)
    }
    @JvmStatic
    fun setValue(editor: Editor, key: String?, value: Int) {
        editor.putInt(key, value)
        editor.apply()
    }
    @JvmStatic
    fun getValue(sp: SharedPreferences, key: String?, defValue: Int): Int {
        return sp.getInt(key, defValue)
    }
    @JvmStatic
    fun setValue(editor: Editor, key: String?, value: Long) {
        editor.putLong(key, value)
        editor.apply()
    }
    @JvmStatic
    fun getValue(sp: SharedPreferences, key: String?, defValue: Long): Long {
        return sp.getLong(key, defValue)
    }
    @JvmStatic
    fun setValue(editor: Editor, key: String?, value: Float) {
        editor.putFloat(key, value)
        editor.apply()
    }
    @JvmStatic
    fun getValue(sp: SharedPreferences, key: String?, defValue: Float): Float {
        return sp.getFloat(key, defValue)
    }
    @JvmStatic
    fun setValue(editor: Editor, key: String?, value: Set<String?>?) {
        editor.putStringSet(key, value)
        editor.apply()
    }
    @JvmStatic
    fun getValue(sp: SharedPreferences, key: String?, defValue: Set<String?>?): Set<String?>? {
        return sp.getStringSet(key, defValue)
    }
}