package com.example.jetpackcompose

import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "Contact")
data class Contact(
    @PrimaryKey(autoGenerate = true)
    val id:Int ?= null,
    val firstName: String,
    val lastName: String,
    val phoneNumber: String
)
