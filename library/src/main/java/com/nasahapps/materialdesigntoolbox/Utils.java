package com.nasahapps.materialdesigntoolbox;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.support.annotation.AttrRes;
import android.support.annotation.ColorInt;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.TypedValue;
import android.view.Display;
import android.view.WindowManager;

/**
 * Created by Hakeem on 4/12/16.
 */
public class Utils {

    /**
     * Convert DP units to pixels
     */
    public static int dpToPixel(Context c, int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                c.getResources().getDisplayMetrics());
    }

    /**
     * Converts the given x coordinate to what it would be if the x-origin was at the top-right of
     * the screen
     */
    public static int getRtlX(Context c, int x) {
        Display display = ((WindowManager) c.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int screenWidth = size.x;
        return screenWidth - x;
    }

    /**
     * Retrieves a color int from an attribute, e.g. R.attr.colorAccent, or black if it doesn't exist
     */
    @ColorInt
    public static int getColorFromAttribute(Context c, @AttrRes int res) {
        try {
            TypedValue tv = new TypedValue();
            TypedArray ta = c.obtainStyledAttributes(tv.data, new int[]{res});
            int color = ta.getColor(0, 0);
            ta.recycle();
            return color;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static Drawable getTintedDrawable(Drawable drawable, @ColorInt int color) {
        drawable = DrawableCompat.wrap(drawable.mutate());
        DrawableCompat.setTint(drawable, color);
        return drawable;
    }

}
