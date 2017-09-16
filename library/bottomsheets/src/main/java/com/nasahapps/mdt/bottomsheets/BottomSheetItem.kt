package com.nasahapps.mdt.bottomsheets

import android.graphics.drawable.Drawable

/**
 * Created by hhasan on 10/14/16.
 */

class BottomSheetItem {

    var text: CharSequence? = null
        private set
    var icon: Drawable? = null
        private set
    val menuId: Int

    @JvmOverloads
    constructor(text: CharSequence, icon: Drawable, menuId: Int = 0) {
        this.text = text
        this.icon = icon
        this.menuId = menuId
    }
}
