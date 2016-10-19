package com.nasahapps.mdt.example.ui.style

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.view.View
import com.nasahapps.mdt.Utils
import com.nasahapps.mdt.example.R
import com.nasahapps.mdt.example.ui.BaseFragment
import com.nasahapps.mdt.example.ui.main.MainActivity
import kotlinx.android.synthetic.main.fragment_color_view_pager.*

/**
 * Created by hhasan on 10/19/16.
 */
class ColorViewPagerFragment : BaseFragment() {

    private val TOOLBAR_COLORS = intArrayOf(
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
    )
    private val STATUS_BAR_COLORS = intArrayOf(
            R.color.mdt_red_700,
            R.color.mdt_pink_700,
            R.color.mdt_purple_700,
            R.color.mdt_deep_purple_700,
            R.color.mdt_indigo_700,
            R.color.mdt_blue_700,
            R.color.mdt_light_blue_700,
            R.color.mdt_cyan_700,
            R.color.mdt_teal_700,
            R.color.mdt_green_700,
            R.color.mdt_light_green_700,
            R.color.mdt_lime_700,
            R.color.mdt_yellow_700,
            R.color.mdt_amber_700,
            R.color.mdt_orange_700,
            R.color.mdt_deep_orange_700,
            R.color.mdt_brown_700,
            R.color.mdt_grey_700,
            R.color.mdt_blue_grey_700
    )

    override fun getLayoutId() = R.layout.fragment_color_view_pager

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewPager?.adapter = ColorPagerAdapter(childFragmentManager)
        tabs?.setupWithViewPager(viewPager)
        viewPager?.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                tintToolbarAndTabs(position)
            }
        })

        tintToolbarAndTabs(viewPager.currentItem)
    }

    override fun onResume() {
        super.onResume()
        innerToolbar?.title = "Color"
        (activity as? MainActivity)?.let {
            it.setToolbarVisible(false)
        }
    }

    fun tintToolbarAndTabs(position: Int) {
        val currentToolbarColor = (innerToolbar?.background as? ColorDrawable)?.color ?: 0
        val currentStatusBarColor = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.window.statusBarColor
        } else 0
        val newToolbarColor = ContextCompat.getColor(activity, TOOLBAR_COLORS[position])
        val newStatusBarColor = ContextCompat.getColor(activity, STATUS_BAR_COLORS[position])
        val textColor = if (Utils.shouldUseWhiteText(newToolbarColor)) Color.WHITE else Color.BLACK
        val inactiveTextColor = if (textColor == Color.WHITE) ContextCompat.getColor(activity, R.color.mdt_light_secondary_text_color)
        else ContextCompat.getColor(activity, R.color.mdt_dark_secondary_text_color)

        val toolbarAnim = ValueAnimator.ofObject(ArgbEvaluator(), currentToolbarColor, newToolbarColor)
        val statusBarAnim = ValueAnimator.ofObject(ArgbEvaluator(), currentStatusBarColor, newStatusBarColor)
        toolbarAnim.addUpdateListener {
            innerToolbar?.setBackgroundColor(it.animatedValue as Int)
            tabs?.setBackgroundColor(it.animatedValue as Int)

        }
        statusBarAnim.addUpdateListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                activity.window.statusBarColor = it.animatedValue as Int
            }
        }
        toolbarAnim.start()
        statusBarAnim.start()

        innerToolbar?.setTitleTextColor(textColor)
        tabs?.setTabTextColors(inactiveTextColor, textColor)
        tabs?.setSelectedTabIndicatorColor(textColor)
    }

    class ColorPagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

        private val RED_ARRAY = intArrayOf(
                R.color.mdt_red_50,
                R.color.mdt_red_100,
                R.color.mdt_red_200,
                R.color.mdt_red_300,
                R.color.mdt_red_400,
                R.color.mdt_red_500,
                R.color.mdt_red_600,
                R.color.mdt_red_700,
                R.color.mdt_red_800,
                R.color.mdt_red_900,
                R.color.mdt_red_a100,
                R.color.mdt_red_a200,
                R.color.mdt_red_a400,
                R.color.mdt_red_a700
        )
        private val PINK_ARRAY = intArrayOf(
                R.color.mdt_pink_50,
                R.color.mdt_pink_100,
                R.color.mdt_pink_200,
                R.color.mdt_pink_300,
                R.color.mdt_pink_400,
                R.color.mdt_pink_500,
                R.color.mdt_pink_600,
                R.color.mdt_pink_700,
                R.color.mdt_pink_800,
                R.color.mdt_pink_900,
                R.color.mdt_pink_a100,
                R.color.mdt_pink_a200,
                R.color.mdt_pink_a400,
                R.color.mdt_pink_a700
        )
        private val PURPLE_ARRAY = intArrayOf(
                R.color.mdt_purple_50,
                R.color.mdt_purple_100,
                R.color.mdt_purple_200,
                R.color.mdt_purple_300,
                R.color.mdt_purple_400,
                R.color.mdt_purple_500,
                R.color.mdt_purple_600,
                R.color.mdt_purple_700,
                R.color.mdt_purple_800,
                R.color.mdt_purple_900,
                R.color.mdt_purple_a100,
                R.color.mdt_purple_a200,
                R.color.mdt_purple_a400,
                R.color.mdt_purple_a700
        )
        private val DEEP_PURPLE_ARRAY = intArrayOf(
                R.color.mdt_deep_purple_50,
                R.color.mdt_deep_purple_100,
                R.color.mdt_deep_purple_200,
                R.color.mdt_deep_purple_300,
                R.color.mdt_deep_purple_400,
                R.color.mdt_deep_purple_500,
                R.color.mdt_deep_purple_600,
                R.color.mdt_deep_purple_700,
                R.color.mdt_deep_purple_800,
                R.color.mdt_deep_purple_900,
                R.color.mdt_deep_purple_a100,
                R.color.mdt_deep_purple_a200,
                R.color.mdt_deep_purple_a400,
                R.color.mdt_deep_purple_a700
        )
        private val INDIGO_ARRAY = intArrayOf(
                R.color.mdt_indigo_50,
                R.color.mdt_indigo_100,
                R.color.mdt_indigo_200,
                R.color.mdt_indigo_300,
                R.color.mdt_indigo_400,
                R.color.mdt_indigo_500,
                R.color.mdt_indigo_600,
                R.color.mdt_indigo_700,
                R.color.mdt_indigo_800,
                R.color.mdt_indigo_900,
                R.color.mdt_indigo_a100,
                R.color.mdt_indigo_a200,
                R.color.mdt_indigo_a400,
                R.color.mdt_indigo_a700
        )
        private val BLUE_ARRAY = intArrayOf(
                R.color.mdt_blue_50,
                R.color.mdt_blue_100,
                R.color.mdt_blue_200,
                R.color.mdt_blue_300,
                R.color.mdt_blue_400,
                R.color.mdt_blue_500,
                R.color.mdt_blue_600,
                R.color.mdt_blue_700,
                R.color.mdt_blue_800,
                R.color.mdt_blue_900,
                R.color.mdt_blue_a100,
                R.color.mdt_blue_a200,
                R.color.mdt_blue_a400,
                R.color.mdt_blue_a700
        )
        private val LIGHT_BLUE_ARRAY = intArrayOf(
                R.color.mdt_light_blue_50,
                R.color.mdt_light_blue_100,
                R.color.mdt_light_blue_200,
                R.color.mdt_light_blue_300,
                R.color.mdt_light_blue_400,
                R.color.mdt_light_blue_500,
                R.color.mdt_light_blue_600,
                R.color.mdt_light_blue_700,
                R.color.mdt_light_blue_800,
                R.color.mdt_light_blue_900,
                R.color.mdt_light_blue_a100,
                R.color.mdt_light_blue_a200,
                R.color.mdt_light_blue_a400,
                R.color.mdt_light_blue_a700
        )
        private val CYAN_ARRAY = intArrayOf(
                R.color.mdt_cyan_50,
                R.color.mdt_cyan_100,
                R.color.mdt_cyan_200,
                R.color.mdt_cyan_300,
                R.color.mdt_cyan_400,
                R.color.mdt_cyan_500,
                R.color.mdt_cyan_600,
                R.color.mdt_cyan_700,
                R.color.mdt_cyan_800,
                R.color.mdt_cyan_900,
                R.color.mdt_cyan_a100,
                R.color.mdt_cyan_a200,
                R.color.mdt_cyan_a400,
                R.color.mdt_cyan_a700
        )
        private val TEAL_ARRAY = intArrayOf(
                R.color.mdt_teal_50,
                R.color.mdt_teal_100,
                R.color.mdt_teal_200,
                R.color.mdt_teal_300,
                R.color.mdt_teal_400,
                R.color.mdt_teal_500,
                R.color.mdt_teal_600,
                R.color.mdt_teal_700,
                R.color.mdt_teal_800,
                R.color.mdt_teal_900,
                R.color.mdt_teal_a100,
                R.color.mdt_teal_a200,
                R.color.mdt_teal_a400,
                R.color.mdt_teal_a700
        )
        private val GREEN_ARRAY = intArrayOf(
                R.color.mdt_green_50,
                R.color.mdt_green_100,
                R.color.mdt_green_200,
                R.color.mdt_green_300,
                R.color.mdt_green_400,
                R.color.mdt_green_500,
                R.color.mdt_green_600,
                R.color.mdt_green_700,
                R.color.mdt_green_800,
                R.color.mdt_green_900,
                R.color.mdt_green_a100,
                R.color.mdt_green_a200,
                R.color.mdt_green_a400,
                R.color.mdt_green_a700
        )
        private val LIGHT_GREEN_ARRAY = intArrayOf(
                R.color.mdt_light_green_50,
                R.color.mdt_light_green_100,
                R.color.mdt_light_green_200,
                R.color.mdt_light_green_300,
                R.color.mdt_light_green_400,
                R.color.mdt_light_green_500,
                R.color.mdt_light_green_600,
                R.color.mdt_light_green_700,
                R.color.mdt_light_green_800,
                R.color.mdt_light_green_900,
                R.color.mdt_light_green_a100,
                R.color.mdt_light_green_a200,
                R.color.mdt_light_green_a400,
                R.color.mdt_light_green_a700
        )
        private val LIME_ARRAY = intArrayOf(
                R.color.mdt_lime_50,
                R.color.mdt_lime_100,
                R.color.mdt_lime_200,
                R.color.mdt_lime_300,
                R.color.mdt_lime_400,
                R.color.mdt_lime_500,
                R.color.mdt_lime_600,
                R.color.mdt_lime_700,
                R.color.mdt_lime_800,
                R.color.mdt_lime_900,
                R.color.mdt_lime_a100,
                R.color.mdt_lime_a200,
                R.color.mdt_lime_a400,
                R.color.mdt_lime_a700
        )
        private val YELLOW_ARRAY = intArrayOf(
                R.color.mdt_yellow_50,
                R.color.mdt_yellow_100,
                R.color.mdt_yellow_200,
                R.color.mdt_yellow_300,
                R.color.mdt_yellow_400,
                R.color.mdt_yellow_500,
                R.color.mdt_yellow_600,
                R.color.mdt_yellow_700,
                R.color.mdt_yellow_800,
                R.color.mdt_yellow_900,
                R.color.mdt_yellow_a100,
                R.color.mdt_yellow_a200,
                R.color.mdt_yellow_a400,
                R.color.mdt_yellow_a700
        )
        private val AMBER_ARRAY = intArrayOf(
                R.color.mdt_amber_50,
                R.color.mdt_amber_100,
                R.color.mdt_amber_200,
                R.color.mdt_amber_300,
                R.color.mdt_amber_400,
                R.color.mdt_amber_500,
                R.color.mdt_amber_600,
                R.color.mdt_amber_700,
                R.color.mdt_amber_800,
                R.color.mdt_amber_900,
                R.color.mdt_amber_a100,
                R.color.mdt_amber_a200,
                R.color.mdt_amber_a400,
                R.color.mdt_amber_a700
        )
        private val ORANGE_ARRAY = intArrayOf(
                R.color.mdt_orange_50,
                R.color.mdt_orange_100,
                R.color.mdt_orange_200,
                R.color.mdt_orange_300,
                R.color.mdt_orange_400,
                R.color.mdt_orange_500,
                R.color.mdt_orange_600,
                R.color.mdt_orange_700,
                R.color.mdt_orange_800,
                R.color.mdt_orange_900,
                R.color.mdt_orange_a100,
                R.color.mdt_orange_a200,
                R.color.mdt_orange_a400,
                R.color.mdt_orange_a700
        )
        private val DEEP_ORANGE_ARRAY = intArrayOf(
                R.color.mdt_deep_orange_50,
                R.color.mdt_deep_orange_100,
                R.color.mdt_deep_orange_200,
                R.color.mdt_deep_orange_300,
                R.color.mdt_deep_orange_400,
                R.color.mdt_deep_orange_500,
                R.color.mdt_deep_orange_600,
                R.color.mdt_deep_orange_700,
                R.color.mdt_deep_orange_800,
                R.color.mdt_deep_orange_900,
                R.color.mdt_deep_orange_a100,
                R.color.mdt_deep_orange_a200,
                R.color.mdt_deep_orange_a400,
                R.color.mdt_deep_orange_a700
        )
        private val BROWN_ARRAY = intArrayOf(
                R.color.mdt_brown_50,
                R.color.mdt_brown_100,
                R.color.mdt_brown_200,
                R.color.mdt_brown_300,
                R.color.mdt_brown_400,
                R.color.mdt_brown_500,
                R.color.mdt_brown_600,
                R.color.mdt_brown_700,
                R.color.mdt_brown_800,
                R.color.mdt_brown_900
        )
        private val GREY_ARRAY = intArrayOf(
                R.color.mdt_grey_50,
                R.color.mdt_grey_100,
                R.color.mdt_grey_200,
                R.color.mdt_grey_300,
                R.color.mdt_grey_400,
                R.color.mdt_grey_500,
                R.color.mdt_grey_600,
                R.color.mdt_grey_700,
                R.color.mdt_grey_800,
                R.color.mdt_grey_900
        )
        private val BLUE_GREY_ARRAY = intArrayOf(
                R.color.mdt_blue_grey_50,
                R.color.mdt_blue_grey_100,
                R.color.mdt_blue_grey_200,
                R.color.mdt_blue_grey_300,
                R.color.mdt_blue_grey_400,
                R.color.mdt_blue_grey_500,
                R.color.mdt_blue_grey_600,
                R.color.mdt_blue_grey_700,
                R.color.mdt_blue_grey_800,
                R.color.mdt_blue_grey_900
        )
        private val ALL_ARRAYS = arrayOf(
                RED_ARRAY,
                PINK_ARRAY,
                PURPLE_ARRAY,
                DEEP_PURPLE_ARRAY,
                INDIGO_ARRAY,
                BLUE_ARRAY,
                LIGHT_BLUE_ARRAY,
                CYAN_ARRAY,
                TEAL_ARRAY,
                GREEN_ARRAY,
                LIGHT_GREEN_ARRAY,
                LIME_ARRAY,
                YELLOW_ARRAY,
                AMBER_ARRAY,
                ORANGE_ARRAY,
                DEEP_ORANGE_ARRAY,
                BROWN_ARRAY,
                GREY_ARRAY,
                BLUE_GREY_ARRAY
        )

        override fun getItem(position: Int) = ColorFragment.newInstance(ALL_ARRAYS[position], getPageTitle(position))

        override fun getCount() = ALL_ARRAYS.size

        override fun getPageTitle(position: Int) = when (position) {
            1 -> "Pink"
            2 -> "Purple"
            3 -> "Deep Purple"
            4 -> "Indigo"
            5 -> "Blue"
            6 -> "Light Blue"
            7 -> "Cyan"
            8 -> "Teal"
            9 -> "Green"
            10 -> "Light Green"
            11 -> "Lime"
            12 -> "Yellow"
            13 -> "Amber"
            14 -> "Orange"
            15 -> "Deep Orange"
            16 -> "Brown"
            17 -> "Grey"
            18 -> "Blue Grey"
            else -> "Red"
        }
    }

}