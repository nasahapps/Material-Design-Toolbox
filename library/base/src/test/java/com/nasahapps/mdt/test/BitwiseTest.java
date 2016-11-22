package com.nasahapps.mdt.test;

import com.nasahapps.mdt.Utils;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by hhasan on 11/22/16.
 */

public class BitwiseTest {

    @Test
    public void testReversingHexadecimal() {
        int original = 0x1f;
        // Flip it so it's 0xf1
        int output = Utils.reverseHexadecimal(original);
        Assert.assertEquals("Number did not get reversed", output, 0xf1);
    }

    @Test
    public void testConvertingAlphaToRGB() {
        int original = 0x1f000000;
        int output = Utils.moveAlphaToRGB(original);
        Assert.assertEquals("Number did not convert correctly", output, 0xf1f1f1);
    }

    @Test
    public void testIgnoringFullAlpha() {
        int original = 0xffabcdef;
        int output = Utils.moveAlphaToRGB(original);
        Assert.assertEquals("Number converted for some reason, shouldn't have", output, original);
    }

}
