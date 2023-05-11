package com.example.jetpackcompose

import androidx.lifecycle.LiveData
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import java.util.concurrent.Flow

interface ContactDao {
    @Insert
    suspend fun insertContact(contact: Contact)

    @Delete
    suspend fun deleteContact(contact: Contact)

    @Query("SELECT * FROM Contact order by firstName ASC")
    fun getContactsOderByFirstName(): kotlinx.coroutines.flow.Flow<List<Contact>>

    @Query("SELECT * FROM Contact order by lastName ASC")
    fun getContactsOderByLastName(): kotlinx.coroutines.flow.Flow<List<Contact>>

    @Query("SELECT * FROM Contact order by phoneNumber ASC")
    fun getContactsOderByPhoneNumber(): kotlinx.coroutines.flow.Flow<List<Contact>>

}