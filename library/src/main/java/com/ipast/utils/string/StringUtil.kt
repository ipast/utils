package com.ipast.utils.string

import android.graphics.Paint
import android.graphics.Rect
import android.text.TextUtils
import java.math.BigDecimal
import java.util.regex.Pattern

/**
 * @author gang.cheng
 * @description :
 * @date :2021/4/16
 */
object StringUtil {
    /**
     * 判断字符串是否有值，如果为null或者是空字符串或者只有空格或者为"null"字符串，则返回true，否则则返回false
     */
    @JvmStatic
    fun isEmpty(value: String): Boolean {
        if (value != null && !"".equals(value.trim(), true)
            && !"null".equals(value.trim(), true)
        ) {
            return false
        }
        return true
    }


    /**
     * 判断邮箱格式是否正确
     * @param email
     * @return
     */
    @JvmStatic
    fun isEmail(email: String): Boolean {
        val str =
            "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$"
        val p = Pattern.compile(str)
        val m = p.matcher(email)
        return m.matches()
    }

    /**
     * 判断手机号码是否正确
     * @param phoneNumber
     * @return
     */
    @JvmStatic
    fun isPhoneNumberValid(phoneNumber: String): Boolean {
        var isVaild = false
        val expression = "^1[3|4|5|6|7|8|9][0-9]\\\\d{8}\$"
        val pattern = Pattern.compile(expression)
        val matcher = pattern.matcher(phoneNumber)
        if (matcher.matches()) {
            isVaild = true
        }
        return isVaild
    }

    /**
     * 判断电话号码是否正确
     * @param areaCode String
     * @param phoneNumber String
     * @return Boolean
     */
    @JvmStatic
    fun isPhoneNumberValid(areaCode: String, phoneNumber: String): Boolean {
        if (TextUtils.isEmpty(phoneNumber)) {
            return false
        }
        if (phoneNumber.length < 5) {
            return false
        }
        if (TextUtils.equals(areaCode, "+86") || TextUtils.equals(areaCode, "86")) {
            return isPhoneNumberValid(phoneNumber)
        }
        var isValid = false;
        val expression = "^[0-9]*$"
        val pattern = Pattern.compile(expression)
        val matcher = pattern.matcher(phoneNumber)
        if (matcher.matches()) {
            isValid = true
        }
        return isValid
    }


    /**
     * 验证是否是数字
     * @param number String
     * @return Boolean
     */
    @JvmStatic
    fun isNumber(number: String): Boolean {
        return try {
            val p = Pattern.compile("\\d+")
            val m = p.matcher(number)
            m.matches()
        } catch (e: Exception) {
            false
        }

    }

    /**
     * 获取字符串长度（中文2个）
     * @param str String
     * @return Int
     */
    @JvmStatic
    fun getStringLength(str: String?): Int {
        if (TextUtils.isEmpty(str)) {
            return 0
        }
        var count = 0
        val strArray = str!!.toCharArray()
        for (item in strArray) {
            count += if (item.toInt() < 128) {
                1
            } else {
                2
            }
        }
        return count
    }

    /**
     * 一个em单位的宽度等于行高的高度
     * @param str
     * @return 字符串ems
     */
    @JvmStatic
    fun getEms(str: String?): Int {
        if (TextUtils.isEmpty(str)) {
            return 0
        }
        val paint = Paint()
        val rect = Rect()
        paint.getTextBounds(str, 0, str!!.length, rect)
        return rect.width() / rect.height()

    }

    /**
     * 百分比
     * @param num Long
     * @param total Long
     * @return String
     */
    @JvmStatic
    fun getPercent(num: Long, total: Long): String {
        return getPercent(num, total, 2)
    }

    /**
     *  百分比
     * @param num Long
     * @param total Long
     * @param precision Int
     * @return String
     */
    @JvmStatic
    fun getPercent(num: Long, total: Long, precision: Int): String {
        if (num == 0.toLong() || total == 0.toLong()) {
            return "0%"
        }
        val result = (num * 100 / total).toBigDecimal()
        return result.setScale(precision, BigDecimal.ROUND_HALF_UP)
            .toPlainString() + "%"
    }

}