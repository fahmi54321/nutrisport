package com.nutrisport.home.domain

enum class CustomDrawerState {
    Opened,
    Closed
}

fun CustomDrawerState.isOpened(): Boolean{
    return this == CustomDrawerState.Opened
}

fun CustomDrawerState.oppsite(): CustomDrawerState{
    return if(this == CustomDrawerState.Opened) CustomDrawerState.Closed
    else CustomDrawerState.Opened
}