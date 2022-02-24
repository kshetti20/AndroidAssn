package com.example.androidapp.ui_controllers

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.androidapp.databinding.BooksListFragmentBinding
import com.example.androidapp.retrofit.QuotesApi
import com.example.androidapp.models.Book
import com.example.androidapp.vm.BooksViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.app.Activity
import androidx.core.app.ActivityCompat
import android.graphics.*
import android.graphics.pdf.PdfDocument
import android.graphics.pdf.PdfDocument.PageInfo
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import android.content.Context

import android.content.ContextWrapper
import android.widget.Toast

import android.content.Intent

import android.net.Uri
import androidx.fragment.app.commit
import com.example.androidapp.R


@AndroidEntryPoint
class BooksListFragment : Fragment(R.layout.books_list_fragment) {

    var fragmentBinding: BooksListFragmentBinding? = null

    private val vm: BooksViewModel by viewModels()

    var selectedPosition = 0

    @Inject
    lateinit var retrofit: QuotesApi

    private val binding get() = fragmentBinding!!

    private val PERMISSION_REQUEST_CODE = 200

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentBinding = BooksListFragmentBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val booksList: MutableList<Book> = mutableListOf()

        val listEmpty: () -> Unit = {
            binding.placeHolder.visibility = View.VISIBLE
        }

        val deleteRecord: (Book) -> Unit = { it ->
            Log.e("BooksList", it.bookName!!)
            vm.deleteRecord(it)
        }

        val requestPermissionForStorage: (Int) -> Unit = { position ->
            selectedPosition = position
            requestPermission()
        }

        val booksListAdapter = BooksListAdapter(
            booksList,
            listEmpty = listEmpty,
            deleteRecord,
            requestPermissionForStorage
        )

        vm.booksLiveData.observe(this) {
            if (it.size > 0)
                booksListAdapter.updateBooksList(it)
            else
                binding.placeHolder.visibility = View.VISIBLE
        }

        //To load data from Room
        vm.fetchSavedBooks()

        //To load data from API
//        vm.fetchData()

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = booksListAdapter
        }

        binding.backButton.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    private fun requestPermission() {
        // requesting permissions if not provided.
        ActivityCompat.requestPermissions(
            activity as Activity,
            arrayOf(WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE),
            PERMISSION_REQUEST_CODE
        )
    }

    fun permissionGranted() {
        generatePDF()
    }

    private fun generatePDF() {

        try {
            val contextWrapper = ContextWrapper(activity!!.getApplicationContext())
            val directory =
                contextWrapper.getDir(activity!!.getFilesDir().getName(), Context.MODE_PRIVATE)
            val file = File(directory, "sample.pdf")
            if (file.exists())
                file.delete();
            file.createNewFile()
            val fOut = FileOutputStream(file)
            val document = PdfDocument()
            val pageInfo = PageInfo.Builder(100, 100, 1).create()
            val page = document.startPage(pageInfo)
            val canvas = page.canvas
            val paint = Paint()
            canvas.drawText("Kunal Shetti", 10f, 10f, paint)
            document.finishPage(page)
            document.writeTo(fOut)
            document.close()
            openPdf()
        } catch (e: IOException) {
            Log.i("error", e.localizedMessage)
        }
    }

    private fun openPdf() {
        val contextWrapper = ContextWrapper(activity!!.getApplicationContext())
        val directory =
            contextWrapper.getDir(activity!!.getFilesDir().getName(), Context.MODE_PRIVATE)
        val file = File(directory.toString(),"sample.pdf")
        if (file.exists()) {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.setDataAndType(Uri.fromFile(file), "application/pdf")
            val pm = activity!!.packageManager
            val sendIntent = Intent(Intent.ACTION_VIEW)
            sendIntent.type = "application/pdf"
            val openInChooser = Intent.createChooser(intent, "Choose")
            val resInfo = pm.queryIntentActivities(sendIntent, 0)
            if (resInfo.size > 0) {
                try {
                    activity!!.startActivity(openInChooser)
                } catch (throwable: Throwable) {
                    Toast.makeText(activity, "PDF apps are not installed", Toast.LENGTH_SHORT).show()
                    // PDF apps are not installed
                }
            } else {
                Toast.makeText(context, "PDF apps are not installed", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(activity, "File not exists", Toast.LENGTH_SHORT).show()
        }

    }
}

