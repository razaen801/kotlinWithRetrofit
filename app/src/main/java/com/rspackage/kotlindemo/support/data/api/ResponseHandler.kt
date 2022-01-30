package com.rspackage.kotlindemo.support.data.api

import android.util.Log
import com.google.gson.Gson
import com.suvidhatech.application.support.data.api.responses.baseResponse.BaseErrorResponseEntity
import retrofit2.Response


suspend fun <T> Response<T>.handleResponse(doActionOnSuccess: suspend (body: T) -> Unit = {}): Resource<T> {
    return if (isSuccessful) {
        if (body() != null) {
            doActionOnSuccess.invoke(body()!!)
            Resource.Success(body()!!)
        } else {
            Resource.Error(message())
        }
    } else if (code() in listOf(401, 403)) {
        //App.instance.forceLogout(true)
        Log.e("ResponseHandler", "Response code: " + code())
        //AppController.instance.forceLogout()
        Resource.Error(message(), code())
    } else {
        val genericErrorMessage = "Error encountered. Please try again later."
        val errorBody = errorBody()
        return try {
            /* val errorModel =
                 Gson().fromJson(errorBody?.charStream(), BaseResponseEntity::class.java)
             Resource.Error(errorModel.message)*/

            val errorModel = Gson().fromJson(errorBody?.charStream(), BaseErrorResponseEntity::class.java)

            Resource.Error(errorModel.errorMessage)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(genericErrorMessage)
        }
    }
}
