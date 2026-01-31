package com.example.weatherfirebaseapp.data.firebase

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties

 data class FavoriteDto(
     val id:String?=null,
     val cityName:String?=null,
     val note:String?=null,
     val createdAt:Long?=null,
     val createdBy:String?=null
){
     constructor():this("","","",null,"")
 }
