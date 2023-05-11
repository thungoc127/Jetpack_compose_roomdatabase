package com.example.jetpackcompose

sealed interface ContactEvent{
    object SaveContact: ContactEvent
    data class SetFirstName(val FirstName: String):ContactEvent
    data class SetLastName(val LastName:String):ContactEvent
    data class SetPhoneNumber(val phoneNumber: String):ContactEvent
    object ShowDialog: ContactEvent
    object HideDialog: ContactEvent
    data class SortContacts(val sortType:SortType):ContactEvent
    data class DeleteContacts(val contact: Contact):ContactEvent

}