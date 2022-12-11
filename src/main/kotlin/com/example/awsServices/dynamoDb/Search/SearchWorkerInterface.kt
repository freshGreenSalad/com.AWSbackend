package com.example.awsServices.dynamoDb.Search

import com.example.Data.models.workerVisualiser.WorkerProfile
import com.example.awsServices.dynamoDb.Search.DataClasses.WorkerSearchQuery

interface SearchWorkerInterface {
    suspend fun  SearchForWorker(workerSearchQuery: WorkerSearchQuery):List<WorkerProfile>
}