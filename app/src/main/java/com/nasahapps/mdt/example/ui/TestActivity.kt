package com.nasahapps.mdt.example.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import com.nasahapps.mdt.chips.ChipContactLoader
import com.nasahapps.mdt.chips.ChipLayout
import com.nasahapps.mdt.example.R
import kotlinx.android.synthetic.main.activity_test.*

class TestActivity : BaseActivity() {

    val REQUEST_PERMISSION_READ_CONTACTS = 1

    override fun getLayoutId(): Int {
        return R.layout.activity_test
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val contactsPermissionGranted = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED
        if (contactsPermissionGranted) {
            setupChipLayout()
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_CONTACTS), REQUEST_PERMISSION_READ_CONTACTS)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_PERMISSION_READ_CONTACTS) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setupChipLayout()
            }
        }
    }

    private fun setupChipLayout() {
        chipLayout?.setChipContactLoader(ChipContactLoader.getInstance(supportFragmentManager, ChipLayout.ContactType.EMAIL))
    }

}
