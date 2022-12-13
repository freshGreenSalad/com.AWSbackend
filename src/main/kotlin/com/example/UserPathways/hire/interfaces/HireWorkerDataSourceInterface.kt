package com.example.UserPathways.hire.interfaces

import com.example.UserPathways.Employee.workerVisualiser.WorkerProfile
import com.example.utilitys.wrapperClasses.AwsResultWrapper
import com.example.awsServices.UserPathways.hire.HireWorker

interface HireWorkerDataSourceInterface {
    suspend fun addWorkerToSupervisorTeam(hireWorker: HireWorker)
    suspend fun getListOfSavedWorkers(email: String): AwsResultWrapper<List<WorkerProfile>>
}