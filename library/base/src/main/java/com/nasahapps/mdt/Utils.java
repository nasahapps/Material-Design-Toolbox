package com.nasahapps.mdt;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.AttrRes;
import android.support.annotation.ColorInt;
import android.support.annotation.DimenRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

import java.util.Random;

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
     * Convert pixels to DP
     */
    public static int pixelToDp(Context c, int px) {
        return (int) (px / c.getResources().getDisplayMetrics().density);
    }

    private static Point getScreenDimensions(Context c) {
        Display display;
        if (c instanceof Activity) {
            display = ((Activity) c).getWindowManager().getDefaultDisplay();
        } else {
            display = ((WindowManager) c.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        }
        Point size = new Point();
        display.getSize(size);
        return size;
    }

    public static int getScreenWidth(Context c) {
        return getScreenDimensions(c).x;
    }

    public static int getScreenHeight(Context c) {
        return getScreenDimensions(c).y;
    }

    public static boolean isPortrait(Context c) {
        return c.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
    }

    public static boolean isTablet(Context c) {
        return c.getResources().getConfiguration().smallestScreenWidthDp >= 600;
    }

    public static boolean isLargeTablet(Context c) {
        return c.getResources().getConfiguration().smallestScreenWidthDp >= 720;
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

    /**
     * Retrieves a Drawable from an attribute, e.g. R.attr.selectableItemBackground, or null if
     * it doesn't exist
     */
    public static Drawable getDrawableFromAttribute(Context c, @AttrRes int res) {
        try {
            TypedValue tv = new TypedValue();
            TypedArray ta = c.obtainStyledAttributes(tv.data, new int[]{res});
            Drawable drawable = ta.getDrawable(0);
            ta.recycle();
            return drawable;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Drawable getTintedDrawable(Drawable drawable, @ColorInt int color) {
        drawable = DrawableCompat.wrap(drawable.mutate());
        DrawableCompat.setTint(drawable, color);
        return drawable;
    }

    // This method tends to work better pre-Lollipop
    public static Drawable getTintedDrawableCompat(Drawable drawable, @ColorInt int color) {
        drawable = drawable.mutate();
        drawable.setColorFilter(color, PorterDuff.Mode.SRC_IN);
        return drawable;
    }

    public static float getFloatResource(Context c, @DimenRes int res) {
        TypedValue tv = new TypedValue();
        c.getResources().getValue(res, tv, true);
        return tv.getFloat();
    }

    public static void getAbsoluteCoordinates(View v, int[] array) {
        if (array.length < 2) {
            throw new RuntimeException("Array must be of size 2!");
        }

        v.getLocationOnScreen(array);
        // Sometimes the coordinates are negative (they shouldn't be, make them positive
        array[0] = Math.abs(array[0]);
        array[1] = Math.abs(array[1]);
    }

    public static void getCenterCoordinates(View v, int[] array) {
        if (array.length < 2) {
            throw new RuntimeException("Array must be of size 2!");
        }

        getAbsoluteCoordinates(v, array);
        array[0] += v.getWidth() / 2;
        array[1] += v.getHeight() / 2;
    }

    public static boolean shouldUseWhiteText(@ColorInt int color) {
        // Use a color with the alpha value already mixed in
        int actualColor = moveAlphaToRGB(color);
        int calc = ((Color.red(actualColor) * 299) + (Color.green(actualColor) * 587) + (Color.blue(actualColor) * 114)) / 1000;
        return calc < 128;
    }

    @Nullable
    public static Bitmap convertDrawableToBitmap(@Nullable Drawable drawable) {
        if (drawable == null) return null;

        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        } else {
            Bitmap bitmap;
            if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
                bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
            } else {
                bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(),
                        Bitmap.Config.ARGB_8888);
            }

            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
            return bitmap;
        }
    }

    @Nullable
    public static RoundedBitmapDrawable getRoundedDrawable(Context c, @DrawableRes int res) {
        return getRoundedDrawable(c, ContextCompat.getDrawable(c, res));
    }

    @Nullable
    public static RoundedBitmapDrawable getRoundedDrawable(Context c, @Nullable Drawable drawable) {
        if (drawable != null) {
            Bitmap bitmap = convertDrawableToBitmap(drawable);
            if (bitmap != null) {
                RoundedBitmapDrawable roundedDrawable = RoundedBitmapDrawableFactory.create(c.getResources(), bitmap);
                roundedDrawable.setCircular(true);
                return roundedDrawable;
            }
        }

        return null;
    }

    /**
     * Given a hexadecimal number, reverse it, i.e. 0x1f -> 0xf1
     */
    public static int reverseHexadecimal(int hex) {
        // For now, just restrict this to 8-bit ints
        if (hex > 0xff) {
            return hex;
        }

        // Get the last 4 bits
        int lastFour = hex >>> 4;
        // Then the first 4 bits
        int firstFour = (hex & 0xf) << 4;
        // Then return the combination
        return lastFour | firstFour;
    }

    /**
     * For a color hexadecimal, take its alpha value and transfer it to the RGB values, i.e. 0x1f000000 -> 0xfff1f1f1
     */
    public static int moveAlphaToRGB(int color) {
        // if alpha == 0xff, no need to do anything here
        int alpha = color >>> 24;
        if (alpha == 0xff) {
            return color;
        }

        // Flip the alpha
        int flippedAlpha = reverseHexadecimal(alpha);
        int newRed = ((color >> 16) & 0xff) | flippedAlpha;
        int newGreen = ((color >> 8) & 0xff) | flippedAlpha;
        int newBlue = (color & 0xff) | flippedAlpha;

        return (newRed << 16) | (newGreen << 8) | newBlue;
    }

    @ColorInt
    public static int getRandomMaterialColor(Context c) {
        int[] colors = new int[]{
                R.color.mdt_red_500,
                R.color.mdt_pink_500,
                R.color.mdt_purple_500,
                R.color.mdt_deep_purple_500,
                R.color.mdt_indigo_500,
                R.color.mdt_blue_500,
                R.color.mdt_light_blue_500,
                R.color.mdt_cyan_500,
                R.color.mdt_teal_500,
                R.color.mdt_green_500,
                R.color.mdt_light_green_500,
                R.color.mdt_lime_500,
                R.color.mdt_yellow_500,
                R.color.mdt_amber_500,
                R.color.mdt_orange_500,
                R.color.mdt_deep_orange_500,
                R.color.mdt_brown_500,
                R.color.mdt_grey_500,
                R.color.mdt_blue_grey_500
        };
        return ContextCompat.getColor(c, colors[new Random().nextInt(colors.length)]);
    }

    public static boolean isInNightMode(Context c) {
        return (c.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK)
                == Configuration.UI_MODE_NIGHT_YES;
    }

}
