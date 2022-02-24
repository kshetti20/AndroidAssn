package com.example.androidapp.ui_controllers

import android.content.pm.PackageManager
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import androidx.fragment.app.commit
import com.example.androidapp.R
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

@AndroidEntryPoint
class MainActivity : AppCompatActivity(R.layout.activity_main) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                add(R.id.fragment_container_view, MainFragment())
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        val writeStorage = grantResults[0] === PackageManager.PERMISSION_GRANTED
        val readStorage = grantResults[1] === PackageManager.PERMISSION_GRANTED

        if (writeStorage && readStorage) {
            val mainFragment: BooksListFragment = supportFragmentManager.fragments[1] as BooksListFragment
            mainFragment.permissionGranted()
        }
    }

    private fun generatePDF() {
        val extstoragedir = Environment.getExternalStorageDirectory().toString()
        val fol = File(extstoragedir, "pdf")
        val folder = File(fol, "pdf")
        if (!folder.exists()) {
            val bool = folder.mkdir()
        }
        try {
            val file = File(folder, "sample.pdf")
            file.createNewFile()
            val fOut = FileOutputStream(file)
            val document = PdfDocument()
            val pageInfo = PdfDocument.PageInfo.Builder(100, 100, 1).create()
            val page = document.startPage(pageInfo)
            val canvas = page.canvas
            val paint = Paint()
            canvas.drawText("Kunal Shetti", 10f, 10f, paint)
            document.finishPage(page)
            document.writeTo(fOut)
            document.close()
        } catch (e: IOException) {

        }
    }

}



//Hilt, MVVM, Licvedata, ROOM, COROUTINE, recyclerview, data binding, retrofit, Coroutine
