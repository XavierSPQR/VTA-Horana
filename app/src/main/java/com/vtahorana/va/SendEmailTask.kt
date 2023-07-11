package com.vtahorana.va

import android.os.AsyncTask
import java.util.Properties
import javax.mail.*
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

class SendEmailTask(private val senderEmail: String, private val senderPassword: String, private val recipientEmail: String, private val subject: String, private val message: String) : AsyncTask<Void, Void, Boolean>() {

    override fun doInBackground(vararg params: Void?): Boolean {
        val properties = Properties().apply {
            put("mail.smtp.host", "smtp.gmail.com")
            put("mail.smtp.port", "587")
            put("mail.smtp.auth", "true")
            put("mail.smtp.starttls.enable", "true")
        }

        val session = Session.getInstance(properties, object : Authenticator() {
            override fun getPasswordAuthentication(): PasswordAuthentication {
                return PasswordAuthentication(senderEmail, senderPassword)
            }
        })

        val emailMessage = MimeMessage(session).apply {
            setFrom(InternetAddress(senderEmail))
            setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail))
            subject = subject
            setText(message)
        }

        val transport = session.transport
        transport.connect()

        return try {
            transport.sendMessage(emailMessage, emailMessage.allRecipients)
            true
        } catch (ex: MessagingException) {
            ex.printStackTrace()
            false
        } finally {
            transport.close()
        }
    }

    override fun onPostExecute(result: Boolean) {
        // Handle the result of the email sending operation here
        if (result) {
            // Email sent successfully
        } else {
            // Failed to send email
        }
    }
}
