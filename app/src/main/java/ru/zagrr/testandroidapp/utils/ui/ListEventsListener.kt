package ru.zagrr.testandroidapp.utils.ui

interface ListEventsListener<T>{

    fun onItemLongClick(item : T, position : Int)
    fun onItemClick(item : T, position : Int)
    fun isItemSelected(item : T) : Boolean

}