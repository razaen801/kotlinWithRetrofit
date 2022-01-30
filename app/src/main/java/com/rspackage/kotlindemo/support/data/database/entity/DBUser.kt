package com.rspackage.kotlindemo.support.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.rspackage.kotlindemo.support.data.database.configuration.DatabaseConfigs
import org.jetbrains.annotations.NotNull

@Entity(tableName = DatabaseConfigs.tbl_user)
class DBUser {
    @SerializedName("id")
    @Expose
    @PrimaryKey
    @NotNull
    var id = 0

    @SerializedName("name")
    @Expose
    var name = 0
}