package com.suvidhatech.application.support.data.api.responses.baseResponse

import com.google.gson.annotations.SerializedName

open class BaseErrorResponseEntity(
    /*@SerializedName("message")
    open var message: String = "",

    @SerializedName("error_code")
    var errorCode: String? = null*/

    /* @SerializedName("error")
     open var error: String = "",

     @SerializedName("error_description")
     var error_description: String = ""*/

    @SerializedName("errorCode")
    open var errorCode: String = "",

    @SerializedName("errorMessage")
    var errorMessage: String = ""
)