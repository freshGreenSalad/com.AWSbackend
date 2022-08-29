package com.example.Data.models

import kotlinx.serialization.Serializable

@Serializable
data class WorkerPrimaryInfo(
    val key: Int,
    val name: String,
    val age: Int,
    val hourlyRate: Int,
    val imageURL: String
)

val WorkerOne = WorkerPrimaryInfo(1,"John", 25, 40,"1")
val WorkerTwo = WorkerPrimaryInfo(2,"Sam", 32, 45,"2")
val WorkerThree = WorkerPrimaryInfo(3,"Bob", 41, 35,"3")
val WorkerFour = WorkerPrimaryInfo(4,"Rick", 28, 50,"4")
val WorkerFive = WorkerPrimaryInfo(5,"Dave", 19, 30,"5")


val WorkerList = listOf(WorkerOne, WorkerTwo, WorkerThree, WorkerFour, WorkerFive)
