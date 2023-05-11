package com.example.jetpackcompose

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ContactViewModel(private val dao: ContactDao):ViewModel() {
    private val _sortType= MutableStateFlow(SortType.FIRST_NAME)
    private val _contact =_sortType
        .flatMapLatest { sortType -> when(sortType){
            SortType.FIRST_NAME ->dao.getContactsOderByFirstName()
            SortType.LAST_NAME->dao.getContactsOderByLastName()
            SortType.PHONE_NUMBER->dao.getContactsOderByPhoneNumber()
        }
    }
        .stateIn(viewModelScope,SharingStarted.WhileSubscribed(), emptyList())
    private val _state = MutableStateFlow(ContactState())
    val state = combine(_state,_sortType,_contact){state, sortType, contacts ->
        state.copy(
            contacts= contacts,
            sortType= sortType
        )
    }.stateIn(viewModelScope,SharingStarted.WhileSubscribed(5000), ContactState())

    fun onEvent(event:ContactEvent){
        when(event){
            is ContactEvent.DeleteContacts ->{
                viewModelScope.launch {
                    dao.deleteContact(event.contact)
                }
            }

            ContactEvent.HideDialog -> {
                _state.update { it.copy( isAddingContact = false) }
            }

            ContactEvent.SaveContact -> {
                val firstName = state.value.firstName
                val lastName = state.value.lastName
                val phoneNumber = state.value.phoneNumber

                if(firstName.isBlank() || lastName.isBlank() || phoneNumber.isBlank()){
                    return
                }
                val  contact= Contact(
                    firstName = firstName,
                    lastName = lastName,
                    phoneNumber = phoneNumber
                )
                viewModelScope.launch { dao.insertContact(contact) }
                _state.update { it.copy(
                    isAddingContact = false,
                    firstName ="",
                    lastName = "",
                    phoneNumber = ""
                ) }
            }
            is ContactEvent.SetFirstName -> {
                _state.update { it.copy(firstName = event.FirstName) }
            }
            is ContactEvent.SetLastName -> {
                _state.update { it.copy(lastName = event.LastName) }
            }
            is ContactEvent.SetPhoneNumber -> {
                _state.update { it.copy(phoneNumber = event.phoneNumber) }
            }
            ContactEvent.ShowDialog -> {
                _state.update { it.copy(isAddingContact = false) }
            }
            is ContactEvent.SortContacts -> {
                _sortType.value =event.sortType
            }
        }
    }
}