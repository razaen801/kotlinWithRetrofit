package com.suvidhatech.application.support.data.api.responses.baseResponse

import com.google.gson.annotations.SerializedName

open class BaseResponseEntity(
    @SerializedName("message")
    open var message: String = "",
    @SerializedName("error_code")
    var errorCode: String? = null
)