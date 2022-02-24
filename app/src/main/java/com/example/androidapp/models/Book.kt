package com.example.androidapp.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Book(
    @PrimaryKey(autoGenerate = true) val uid: Int = 0,
    @ColumnInfo(name = "bookName") val bookName: String?,
    @ColumnInfo(name = "phoneNumber") val phoneNumber: String?
)
