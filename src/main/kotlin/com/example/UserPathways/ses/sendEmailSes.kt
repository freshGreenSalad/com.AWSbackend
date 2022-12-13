package com.example.UserPathways.ses

import aws.sdk.kotlin.services.ses.SesClient
import aws.sdk.kotlin.services.ses.model.Body
import aws.sdk.kotlin.services.ses.model.Content
import aws.sdk.kotlin.services.ses.model.Destination
import aws.sdk.kotlin.services.ses.model.Message
import aws.sdk.kotlin.services.ses.model.SendEmailRequest

suspend fun testSendEmail(sender:String,recipient:String,subjectVal:String) {

    val bodyHTML = (
            "<html>" + "<head></head>" + "<body>" + "<h1>Hello!</h1>" +
                    "<p> See the list of customers.</p>" + "</body>" + "</html>"
            )

    val destinationOb = Destination {
        toAddresses = listOf("csma174@aucklanduni.ac.nz")
    }

    val contentOb = Content { data = bodyHTML }

    val subOb = Content { data = subjectVal }

    val bodyOb = Body { html = contentOb }

    val msgOb = Message { subject = subOb; body = bodyOb }

    val emailRequest = SendEmailRequest {
        destination = destinationOb
        message = msgOb
        source = "tamakilabour.small@gmail.com"
    }

    SesClient { region = "ap-southeast-2" }.use { sesClient ->
        println("Attempting to send an email through Amazon SES using the AWS SDK for Kotlin...")
        sesClient.sendEmail(emailRequest)
    }
}