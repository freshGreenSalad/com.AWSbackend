package com.example.Data.wrapperClasses

sealed class AwsResultWrapper<T>(val data:T? = null){
    class Success <T>(data:T? = null): AwsResultWrapper<T>(data)
    class Fail <T>: AwsResultWrapper<T>()
}