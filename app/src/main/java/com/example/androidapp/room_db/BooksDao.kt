package com.example.androidapp.room_db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

import androidx.room.Delete
import com.example.androidapp.models.Book

@Dao
interface BooksDao {
    @Insert
    suspend fun insertAll(vararg users: Book)


    @Query("SELECT * FROM book")
    suspend fun getAll(): List<Book>

    @Delete
    suspend fun deleteBook(books: Book)

}