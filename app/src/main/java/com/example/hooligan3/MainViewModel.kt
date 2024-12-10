package com.example.hooligan3

import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData

class MainViewModel : ViewModel() {
    private val teamList = listOf(
        "Team A",
        "Team B",
        "Team C"
    )

    val teams = MutableLiveData<List<String>>().apply { postValue(teamList) }
}