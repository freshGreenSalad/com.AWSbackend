package com.example.UserPathways.hire

import com.example.UserPathways.Employee.workerVisualiser.WorkerProfile
import com.example.utilitys.wrapperClasses.AwsResultWrapper
import com.example.UserPathways.Employee.WorkerProfileDynamoDBDataSource
import com.example.UserPathways.hire.adapters.HireObjectAdapters
import com.example.UserPathways.hire.interfaces.HireWorkerDataSourceInterface
import com.example.UserPathways.notification.OneSignalServiceImplemention
import com.example.awsServices.UserPathways.hire.HireWorker
import com.example.utilitys.AWSHelperFunctions
import com.tamaki.workerapp.userPathways.notification.notificationDataClasses.NotificationToIndividualUser

class HireWorkerDataSource(): HireWorkerDataSourceInterface {

    override suspend fun addWorkerToSupervisorTeam(hireWorker: HireWorker){
        val hiredWorkerMap = HiredWorkerList().addWorkerToHiredWorkerList(
            supervisoremail = hireWorker.supervisorEmail,
            workerEmail = hireWorker.WorkerEmail
        ) ?: return
        val itemValues = HireObjectAdapters().HireWorkerToItemValues(hireWorker, hiredWorkerMap)
        val request = AWSHelperFunctions().buildPutItemRequest(itemValues)
        AWSHelperFunctions().PutObject(request)
    }

    override suspend fun getListOfSavedWorkers(email: String): AwsResultWrapper<List<WorkerProfile>> {
        val keyToGet = AWSHelperFunctions().KeyToGet(email, "HiredWorkers")
        val request = AWSHelperFunctions().BuildGetItemRequest(keyToGet)
        val listOfSavedWorker = hireWorkerObjectConverters().dynamoResultToListOfEmails(request)
        println(listOfSavedWorker)
        val listOfWorkerProfiles = mutableListOf<WorkerProfile>()
        for (workerEmail in listOfSavedWorker){
            try {
                val workerprofile = WorkerProfileDynamoDBDataSource().getWorkerPersonalData(workerEmail).data!!
                listOfWorkerProfiles.add(workerprofile)
            }catch(e:Exception){ }
        }
        return if(listOfWorkerProfiles!= emptyList<WorkerProfile>())
        {
            AwsResultWrapper.Success(listOfWorkerProfiles)}
        else
        {
            AwsResultWrapper.Fail()}
    }

    override suspend fun pushNotificationToHiredWorker(hireWorker: HireWorker, oneSignalServiceImplemention: OneSignalServiceImplemention) {
        //need to get sign in
        try {
            val workerloginDetails = WorkerProfileDynamoDBDataSource().getWorkerSignupAuth(hireWorker.WorkerEmail).data!!
            val notification = NotificationToIndividualUser(
                includePlayerIds = workerloginDetails.,
                contents = ,
                headings = ,
                appId = ,
            )
            oneSignalServiceImplemention.sendNotification()
        } catch (e:Exception){}
    }
}

