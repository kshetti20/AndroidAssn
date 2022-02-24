package com.example.androidapp.room_db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.androidapp.models.Book


@Database(entities = [Book::class], version = 1)
abstract class AppDB: RoomDatabase() {
    abstract fun booksDao(): BooksDao
}

