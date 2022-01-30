package com.rspackage.kotlindemo.ui.main.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rspackage.kotlindemo.support.data.api.Resource
import com.rspackage.kotlindemo.support.data.responses.Animal
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import java.lang.Exception

class MainViewModel(val repository: MainRepository) : ViewModel() {
    val animalData = MutableLiveData<Resource<Animal>>()
    val animalData2 = MutableLiveData<Resource<Animal>>()

    fun getData() {
        viewModelScope.launch {
            try {
                animalData.value = Resource.Loading()
                //animalData.value = repository.getDataFromDB()
                animalData.value = repository.getData()
            } catch (ex: Exception) {
                animalData.value = Resource.Error("some thing went wrong")
            }

        }
    }

    fun getData2() {
        viewModelScope.launch {
            try {
                animalData2.value = Resource.Loading()
                //animalData.value = repository.getDataFromDB()
                animalData2.value = repository.getData()
            } catch (ex: Exception) {
                animalData2.value = Resource.Error("some thing went wrong")
            }

        }
    }
}