package com.example.androidapp.vm

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidapp.retrofit.QuotesApi
import com.example.androidapp.room_db.AppDB
import com.example.androidapp.models.Book
import com.example.androidapp.models.QuoteList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BooksViewModel @Inject constructor(private val appDB: AppDB, private val api : QuotesApi) : ViewModel() {


    var booksLiveData : LiveData<MutableList<Book>> = MutableLiveData()
    var quotesLiveData : LiveData<QuoteList> = MutableLiveData()

    private val mutableBooksLiveData : MutableLiveData<List<Book>> get() = booksLiveData as MutableLiveData<List<Book>>

    fun insertBookSelected(name: String, phoneNumber: String, booksName: String){
        viewModelScope.launch {
            appDB.booksDao().insertAll(Book(bookName = booksName, phoneNumber = phoneNumber))
        }
    }

    fun fetchSavedBooks(){
        viewModelScope.launch {
            mutableBooksLiveData.value = appDB.booksDao().getAll()
        }
    }

    fun deleteRecord(books: Book){
        viewModelScope.launch {
            appDB.booksDao().deleteBook(books)
        }
    }

    fun fetchData(){
        viewModelScope.launch {
                val result = api.getQuotes()
        }
    }

}