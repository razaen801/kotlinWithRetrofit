package com.rspackage.kotlindemo.ui.main.viewmodel

import com.rspackage.kotlindemo.support.data.api.ApiFactory
import com.rspackage.kotlindemo.support.data.api.Resource
import com.rspackage.kotlindemo.support.data.api.handleResponse
import com.rspackage.kotlindemo.support.data.responses.Animal
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MainRepository(val apiFactory: ApiFactory/*, val appDatabase: AppDatabase*/) {

    suspend fun getData(): Resource<Animal>{
        return withContext(Dispatchers.Default){
            val response = apiFactory.getData()
            response.handleResponse()
        }
    }
}