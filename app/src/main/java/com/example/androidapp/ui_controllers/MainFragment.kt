package com.example.androidapp.ui_controllers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import com.example.androidapp.R
import com.example.androidapp.databinding.MainFragmentBinding
import com.example.androidapp.room_db.AppDB
import com.example.androidapp.vm.BooksViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainFragment: Fragment(R.layout.main_fragment) {

    @Inject lateinit var appDB: AppDB
    private val vm: BooksViewModel by viewModels()

    var fragmentBinding : MainFragmentBinding? = null
    private val binding get() = fragmentBinding!!

    var booksList = mutableListOf("Book1", "Book2", "Book3")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentBinding = MainFragmentBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val booksAdapter : ArrayAdapter<String> = ArrayAdapter<String>(activity!!.applicationContext,
            android.R.layout.simple_list_item_1,
            booksList
            )

        with(binding){
            bookSpinner.adapter = booksAdapter

            submit.setOnClickListener {
                if(!name.text.isNullOrEmpty() && !phoneNumber.text.isNullOrEmpty()) {
                    vm.insertBookSelected(
                        name.text.toString(),
                        phoneNumber.text.toString(),
                        bookSpinner.selectedItem.toString()
                    )
                    Toast.makeText(activity, "Book saved", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(activity, "Enter all required details", Toast.LENGTH_SHORT).show()
                }
            }

            nextScreen.setOnClickListener {
                parentFragmentManager.commit {
                    add(R.id.fragment_container_view, BooksListFragment())
                        .addToBackStack(null)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentBinding = null
    }

}