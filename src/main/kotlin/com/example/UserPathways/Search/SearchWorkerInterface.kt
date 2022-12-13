package com.example.UserPathways.Search

import com.example.UserPathways.Employee.workerVisualiser.WorkerProfile
import com.example.UserPathways.Search.DataClasses.WorkerSearchQuery

interface SearchWorkerInterface {
    suspend fun  SearchForWorker(workerSearchQuery: WorkerSearchQuery):List<WorkerProfile>
}