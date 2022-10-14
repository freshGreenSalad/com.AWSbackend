package com.example.Data.models

import kotlinx.serialization.Serializable

@Serializable
data class DriversLicence(
    val email:String,
    val typeOfLicence: TypeOfLicence,
    val licenceMap: Map<String,Boolean>,
    val highestClass: HighestClass
)


enum class TypeOfLicence {
    Learners, Restricted, Full, Empty
}

enum class HighestClass {
    Class1, Class2, Class3, Class4, Class5
}