package com.example.football.utils

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.Settings
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.football.view.HomeNewsFragment
import com.example.football.view.MainActivity

class ManagePermissions(val activity: MainActivity) {

    //request permission for access storage for Offline Mode
    private fun requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            try {
                val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                intent.addCategory("android.intent.category.DEFAULT")
                intent.data = Uri.parse(String.format("package:%s", activity.applicationContext.packageName))
                activity.startActivityForResult(intent, 2296)

            } catch (e: Exception) {
                val intent = Intent()
                intent.action = Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION
                activity.startActivityForResult(intent, 2296)

            }
        } else {
            //below android 11
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ),
                123
            )
        }
    }

    //show alertbox for user to choose
    fun showAlert() {
        val builder = AlertDialog.Builder(activity)
        builder.setTitle("Yêu cầu cấp phép")
        builder.setMessage("Cho phép lưu trữ dữ liệu để có thể đọc tin tức ở chế độ Offline(Không có Internet)?")
        builder.setPositiveButton("OK") { _, _ -> requestPermission() }
        builder.setNeutralButton("Cancel"){ _, _ ->
            val fragment = HomeNewsFragment()
            fragment.getIDContent = activity
            activity.showFragment(fragment, false) }

        val dialog = builder.create()
        dialog.show()
    }

    //check permission of device
    fun checkPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            Environment.isExternalStorageManager()
        } else {
            val result =
                ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE)
            val result1 =
                ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED
        }
    }




}