package com.example.awsServices.dynamoDb.hire.interfaces

import com.example.Data.models.workerVisualiser.WorkerProfile
import com.example.Data.wrapperClasses.AwsResultWrapper
import com.example.awsServices.dynamoDb.hire.HireWorker

interface HireWorkerDataSourceInterface {
    suspend fun addWorkerToSupervisorTeam(hireWorker: HireWorker)
    suspend fun getListOfSavedWorkers(email: String): AwsResultWrapper<List<WorkerProfile>>
}