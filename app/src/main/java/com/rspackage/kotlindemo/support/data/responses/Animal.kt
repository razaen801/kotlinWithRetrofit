package com.rspackage.kotlindemo.support.data.responses

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName




class Animal {

    @SerializedName("fact")
    @Expose
    public var fact: String? = null

    @SerializedName("length")
    @Expose
    private var length: Int? = null

}