package com.nasahapps.mdt

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.Point
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.support.annotation.AttrRes
import android.support.annotation.ColorInt
import android.support.annotation.DimenRes
import android.util.TypedValue
import android.view.Display
import android.view.View
import android.view.WindowManager

/**
 * Created by Hakeem on 4/12/16.
 */
object Utils {

    /**
     * Convert DP units to pixels
     */
    @JvmStatic
    fun dpToPixel(c: Context, dp: Int): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(),
                c.resources.displayMetrics).toInt()
    }

    /**
     * Convert pixels to DP
     */
    @JvmStatic
    fun pixelToDp(c: Context, px: Int): Int {
        return (px / c.resources.displayMetrics.density).toInt()
    }

    private fun getScreenDimensions(c: Context): Point {
        val display: Display
        if (c is Activity) {
            display = c.windowManager.defaultDisplay
        } else {
            display = (c.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay
        }
        val size = Point()
        display.getSize(size)
        return size
    }

    @JvmStatic
    fun getScreenWidth(c: Context): Int {
        return getScreenDimensions(c).x
    }

    @JvmStatic
    fun getScreenHeight(c: Context): Int {
        return getScreenDimensions(c).y
    }

    @JvmStatic
    fun isPortrait(c: Context): Boolean {
        return c.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT
    }

    @JvmStatic
    fun isTablet(c: Context): Boolean {
        return c.resources.configuration.smallestScreenWidthDp >= 600
    }

    @JvmStatic
    fun isLargeTablet(c: Context): Boolean {
        return c.resources.configuration.smallestScreenWidthDp >= 720
    }

    /**
     * Converts the given x coordinate to what it would be if the x-origin was at the top-right of
     * the screen
     */
    @JvmStatic
    fun getRtlX(c: Context, x: Int): Int {
        val display = (c.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay
        val size = Point()
        display.getSize(size)
        val screenWidth = size.x
        return screenWidth - x
    }

    /**
     * Retrieves a color int from an attribute, e.g. R.attr.colorAccent, or black if it doesn't exist
     */
    @ColorInt
    @JvmStatic
    fun getColorFromAttribute(c: Context, @AttrRes res: Int): Int {
        val tv = TypedValue()
        val ta = c.obtainStyledAttributes(tv.data, intArrayOf(res))
        val color = ta.getColor(0, 0)
        ta.recycle()
        return color
    }

    /**
     * Retrieves a Drawable from an attribute, e.g. R.attr.selectableItemBackground, or null if
     * it doesn't exist
     */
    @JvmStatic
    fun getDrawableFromAttribute(c: Context, @AttrRes res: Int): Drawable? {
        val tv = TypedValue()
        val ta = c.obtainStyledAttributes(tv.data, intArrayOf(res))
        val drawable = ta.getDrawable(0)
        ta.recycle()
        return drawable
    }

    @JvmStatic
    fun getTintedDrawable(drawable: Drawable, @ColorInt color: Int): Drawable {
        val editedDrawable = drawable.mutate()
        editedDrawable.setColorFilter(color, PorterDuff.Mode.SRC_IN)
        return editedDrawable
    }

    @JvmStatic
    fun getFloatResource(c: Context, @DimenRes res: Int): Float {
        val tv = TypedValue()
        c.resources.getValue(res, tv, true)
        return tv.float
    }

    @JvmStatic
    fun getAbsoluteCoordinates(v: View, array: IntArray) {
        if (array.size < 2) {
            throw RuntimeException("Array must be of size 2!")
        }

        v.getLocationOnScreen(array)
        // Sometimes the coordinates are negative (they shouldn't be, make them positive
        array[0] = Math.abs(array[0])
        array[1] = Math.abs(array[1])
    }

    @JvmStatic
    fun getCenterCoordinates(v: View, array: IntArray) {
        if (array.size < 2) {
            throw RuntimeException("Array must be of size 2!")
        }

        getAbsoluteCoordinates(v, array)
        array[0] += v.width / 2
        array[1] += v.height / 2
    }

    @SuppressLint("Range")
    @JvmStatic
    fun shouldUseWhiteText(@ColorInt color: Int): Boolean {
        val calc = (Color.red(color) * 299 + Color.green(color) * 587 + Color.blue(114)) / 1000
        return calc < 128
    }

}
