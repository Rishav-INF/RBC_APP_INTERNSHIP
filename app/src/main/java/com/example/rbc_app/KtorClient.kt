
import android.media.Image
import android.net.Uri
import android.util.Log
import com.example.rbc_app.Employer
import com.example.rbc_app.JobFormActivities.JobFieldDefinition
import com.example.rbc_app.JobFormActivities.UserAddFormDetails
import com.example.rbc_app.LocalDateTimeSerializer
import com.example.rbc_app.OtpModel
import com.example.rbc_app.User
import io.ktor.client.*
import io.ktor.client.call.body
import io.ktor.client.engine.android.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.client.utils.EmptyContent.contentType
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDateTime

object KtorClient {
    private val client = HttpClient(Android) {
        install(ContentNegotiation) { json() }
    }

    private const val BASE_URL = "http://10.0.2.2:8080"  // Use 10.0.2.2 for Emulator
    @Serializable
    data class UserRequest(val email: String, val password: String)

    @Serializable
    data class jobforCard(
        val job_id:Int,
        val jobName : String,
        val jobDesc : String,
        val companyName : String,
        val companyLogoName : String,
        val jobType : String,
        val jobLocation : String,
        val preferredCandType : String,
        val jobStatus : String
    )

    @Serializable
    data class internshipforCard(
        val Name : String,
        val internshipDesc : String,
        val companyName : String,
        val companyLogoName : String,
        val internshipType : String,
        val internshipLocation : String,
        val preferredCandType : String,
        val internshipStatus : String
    )

    @Serializable
    data class UserReqFL(
        val freelance_id:Int,
        val user_id:Int
    )

    @Serializable
    data class Freelance(
        @Serializable(with = LocalDateTimeSerializer::class)
        val createdAt: LocalDateTime = LocalDateTime.now(),
        val freelanceDesc: String,
        val freelanceId: Int?,
        val freelanceType: String,
        val freelanceCreator: String,
        val requestsBy: String?,
        val transferredTo: String?
    )
    @Serializable
    data class FreeLanceCard(
        val created_at:String,
        val Contact:String,
        val Pay: String,
        val EstimatedDuration:String,
        val Status:String,
        val location:String,
        val companyName:String,
        val freelance_comp_logo:String,
        val freelance_type:String,
        val preferredCandType:String,
        val creator:String
    )

    @Serializable
    data class addJob(
        val job_name:String,
        val job_type:String,
        val job_desc:String,
        val job_photo:String,
        val email:String
    )

    @Serializable
    data class addInternship(
        val internship_name:String,
        val internship_type:String,
        val internship_desc:String,
        val internship_photo:String,
        val email:String
    )

    @Serializable
    data class selId_crId(
        val selectedId:String,
        val freelance_id:Int
    )

    suspend fun sendJobRowToAdd(jobRow:addJob):Boolean{
        return try{
            val response:HttpResponse=client.post("$BASE_URL/addRowJob"){
                contentType(ContentType.Application.Json)
                setBody(jobRow)
            }
            if(response.status.isSuccess()){
                Log.d("Job added", "Response Status: ${response}")
                true
            }else{
                Log.d("Job not added", "Response Status: ${response}")
                false}
        }catch(e:Exception){
            Log.e("sending failed error", "Exception: ${e.message}")
            false
        }
    }

    suspend fun sendInternRowToAdd(Intern:addInternship):Boolean{
        return try{
            val response:HttpResponse=client.post("$BASE_URL/addInternshipRow"){
                contentType(ContentType.Application.Json)
                setBody(Intern)
            }
            if(response.status.isSuccess()){
                Log.d("Internship added", "Response Status: ${response}")
                true
            }else{
                Log.d("Internship not added", "Response Status: ${response}")
                false}
        }catch(e:Exception){
            Log.e("sending failed error", "Exception: ${e.message}")
            false
        }
    }


    // all the functions for freelance part
    suspend fun sendFreelanceRow(freelancejoborow: Freelance): Boolean {
        return try {

            Log.d("FreelanceData", "Received Freelance: $freelancejoborow")


            val response: HttpResponse = client.post("$BASE_URL/addRowFreeLanceWorks") {
                contentType(ContentType.Application.Json)
                setBody(freelancejoborow)
            }
            if (response.status.isSuccess()) {
                Log.d("Freelance added", "Response Status: ${response.status}")
                true
            } else {
                Log.d("FreeLance_not added", "Response Status: ${response}")
                false  // Explicitly return false
            }
        } catch (e: Exception) {
            Log.e("sending failed error", "Exception: ${e.message}")
            false
        }
    }

    suspend fun loadImageFreeLance(imagename: String): ByteArray? {
        return try {
            val response: HttpResponse = client.post("$BASE_URL/serveImageFreeLance") {
                contentType(ContentType.Application.Json)
                setBody(imagename)
            }

            Log.d("ImageLoad", "Response Status: ${response.status}")

            if (response.status.isSuccess()) {
                val imageData = response.readBytes()
                Log.d("ImageLoad", "Image size: ${imageData.size} bytes")
                imageData
            } else {
                Log.e("ImageLoad", "Failed to load image: ${response.status}")
                null
            }
        } catch (e: Exception) {
            Log.e("ImageLoad", "Exception: ${e.message}")
            null
        }
    }

    suspend fun FreeLanceRequest(userId_frId: UserReqFL):Boolean{
        return try{
            val response:HttpResponse= client
                .post("$BASE_URL/UserRequestFreelance"){
                    contentType(ContentType.Application.Json)
                    setBody(userId_frId)
                }
            if (response.status.isSuccess()) {
                Log.d("Req added", "Response Status: ${response.status}")
                true
            } else {
                Log.d("Req not added", "Response Status: ${response.status}")
                false  // Explicitly return false
            }
        }catch (e:Exception){
            Log.e("sending failed error", "Exception: ${e.message}")
            false
        }
    }

    suspend fun ReqAcceptFreeLance(frId_UsId:selId_crId):Boolean{
        return try{
            val response:HttpResponse= client.post("$BASE_URL/AcceptRequestFreeLance"){
                contentType(ContentType.Application.Json)
                setBody(frId_UsId)
            }
            if (response.status.isSuccess()) {
                Log.d("Req added", "Response Status: ${response.status}")
                true
            } else {
                Log.d("Req not added", "Response Status: ${response.status}")
                false  // Explicitly return false
            }
        }catch (e:Exception){
            Log.e("sending failed error", "Exception: ${e.message}")
            false
        }

        }

    suspend fun getFreeLanceList():List<FreeLanceCard>{
        return try{
            client.get("$BASE_URL/getFreeLanceForCard").body()

        }catch (e: Exception) {
            Log.e("FreeLanceLoad", "Exception: ${e.message}")
            emptyList()
        }
    }


   // --------------------------------------

    suspend fun loadImage(imagename: String): ByteArray? {
        return try {
            val response: HttpResponse = client.post("$BASE_URL/serveimage") {
                contentType(ContentType.Application.Json)
                setBody(imagename)
            }

            Log.d("ImageLoad", "Response Status: ${response.status}")

            if (response.status.isSuccess()) {
                val imageData = response.readBytes()
                Log.d("ImageLoad", "Image size: ${imageData.size} bytes")
                imageData
            } else {
                Log.e("ImageLoad", "Failed to load image: ${response.status}")
                null
            }
        } catch (e: Exception) {
            Log.e("ImageLoad", "Exception: ${e.message}")
            null
        }
    }

    suspend fun loadImageJobs(imagename: String): ByteArray? {
        return try {
            val response: HttpResponse = client.post("$BASE_URL/serveimageJobs") {
                contentType(ContentType.Application.Json)
                setBody(imagename)
            }

            Log.d("ImageLoad", "Response Status: ${response.status}")

            if (response.status.isSuccess()) {
                val imageData = response.readBytes()
                Log.d("ImageLoad", "Image size: ${imageData.size} bytes")
                imageData
            } else {
                Log.e("ImageLoad", "Failed to load image: ${response.status}")
                null
            }
        } catch (e: Exception) {
            Log.e("ImageLoad", "Exception: ${e.message}")
            null
        }
    }

    suspend fun loadImageInternships(imagename: String): ByteArray? {
        return try {
            val response: HttpResponse = client.post("$BASE_URL/serveimageInternships") {
                contentType(ContentType.Application.Json)
                setBody(imagename)
            }

            Log.d("ImageLoad", "Response Status: ${response.status}")

            if (response.status.isSuccess()) {
                val imageData = response.readBytes()
                Log.d("ImageLoad", "Image size: ${imageData.size} bytes")
                imageData
            } else {
                Log.e("ImageLoad", "Failed to load image: ${response.status}")
                null
            }
        } catch (e: Exception) {
            Log.e("ImageLoad", "Exception: ${e.message}")
            null
        }
    }

    suspend fun getJobList():List<jobforCard>{
        return try{
            client.get("$BASE_URL/getJobsForCard").body()
        }catch (e: Exception) {
            Log.e("JobLoad", "Exception: ${e.message}")
            emptyList()
        }
    }

    suspend fun getInternshipList():List<internshipforCard>{
        return try{
            client.get("$BASE_URL/getInternshipsForCard").body()
        }catch (e: Exception) {
            Log.e("InternLoad", "Exception: ${e.message}")
            emptyList()
        }
    }



    suspend fun loginseeker(email: String,password: String): String {
        return try {
            val response: HttpResponse = client.post("$BASE_URL/loginseeker") {
                contentType(ContentType.Application.Json)
                setBody(UserRequest(email, password))
            }
            if (response.status.isSuccess()) {
                "Login successful"
            } else {
                "Error: ${response.bodyAsText()}"
            }
              // Returns "Login Successful" or "Invalid Credentials"
        } catch (e: Exception) {
            e.printStackTrace()
            "Error: ${e.localizedMessage}"
        }
    }


    suspend fun loginemployer(email: String,password: String): String {
        return try {
            val response: HttpResponse = client.post("$BASE_URL/loginemployer") {
                contentType(ContentType.Application.Json)
                setBody(UserRequest(email, password))
            }
            if (response.status.isSuccess()) {
                "Login successful"
            } else {
                "Error: ${response.bodyAsText()}"
            }  // Returns "Login Successful" or "Invalid Credentials"
        } catch (e: Exception) {
            e.printStackTrace()
            "Error: ${e.localizedMessage}"
        }
    }


    suspend fun signUpseeker(user: User): String {
        return try {
            // Prepare request data including createdAt
            val requestData = Json.encodeToString(
                mapOf(
                    "firstName" to user.firstName,
                    "lastName" to user.lastName,
                    "email" to user.email,
                    "password" to user.password,
                    "phone" to user.phone,
                    "gender" to user.gender,
                    "createdAt" to user.createdAt.toString(),
                )
            )

            println("Sending request: $requestData") // Debug log

            val response = client.post("$BASE_URL/signupseeker") {
                contentType(ContentType.Application.Json)
                setBody(requestData)
            }

            when {
                response.status.isSuccess() -> "OTP sent successfully"
                else -> "Error: ${response.bodyAsText()}"
            }
        } catch (e: Exception) {
            e.printStackTrace()
            "Failed: ${e.message}"
        }
    }

    suspend fun signUpemployer(employer: Employer): String {
        return try {
            // Combine employer data with OTP
            val requestData = mapOf(
                "firstName" to employer.firstName,
                "lastName" to employer.lastName,
                "email" to employer.email,
                "password" to employer.password,
                "phone" to employer.phone,
                "gender" to employer.gender,
                "companyName" to employer.companyName,
                "createdAt" to employer.createdAt.toString()
            )

            // Make the API call
            val response = client.post("$BASE_URL/signupemployer") {
                contentType(ContentType.Application.Json)
                setBody(requestData)
            }

            // Return response
            if (response.status.isSuccess()) {
                "OTP sent successfully"
            } else {
                "Error: ${response.bodyAsText()}"
            }
        } catch (e: Exception) {
            "Failed: ${e.message}"
        }
    }


    suspend fun otpverificationSeeekerSignUp(
        employer: Employer,
        otp: String
    ): String {
        val requestData = mapOf(
            "firstName" to employer.firstName,
            "lastName" to employer.lastName,
            "email" to employer.email,
            "password" to employer.password,
            "phone" to employer.phone,
            "gender" to employer.gender,
            "otp" to otp,
            "createdAt" to employer.createdAt.toString(),
        )

        val json = Json.encodeToString(requestData)
        println("Sending JSON otpveri: $json") // Debugging

        return try {
            val response = client.post("$BASE_URL/otpverificationseekerssignup") {
                contentType(ContentType.Application.Json)
                setBody(requestData)
            }
            if (response.status.isSuccess()) {
                "Sign up successful"
            } else {
                "Error: ${response.bodyAsText()}"
            }
        } catch (e: Exception) {
            e.printStackTrace()
            "Error: ${e.message}"
        }
    }

    suspend fun otpverificationEmployerSignUp(
        employer: Employer,
        otp: String
    ): String {
        val requestData = mapOf(
            "firstName" to employer.firstName,
            "lastName" to employer.lastName,
            "email" to employer.email,
            "password" to employer.password,
            "phone" to employer.phone,
            "gender" to employer.gender,
            "otp" to otp,
            "companyName" to employer.companyName,
            "createdAt" to employer.createdAt.toString(),

            )

        val json = Json.encodeToString(requestData)
        println("Sending JSON otpveri: $json") // Debugging

        return try {
            val response = client.post("$BASE_URL/otpverificationemployersignUp") {
                contentType(ContentType.Application.Json)
                setBody(requestData)
            }
            if (response.status.isSuccess()) {
                "Sign up successful"
            } else {
                "Error: ${response.bodyAsText()}"
            }
        } catch (e: Exception) {
            e.printStackTrace()
            "Error: ${e.message}"
        }
    }

    suspend fun getUserId(email: String) : Int {
        val encodedEmail = Uri.encode(email)
        return try{
            client.get("$BASE_URL/getUidForRoom/$encodedEmail").body()
        }catch(e:Exception){
            -1
        }
    }

    suspend fun getEmpIdFromJobId(job_id:Int):Int{
        return try{
            client.get("$BASE_URL/getUidEmployerFromMail/$job_id").body()
        }catch (e:Exception){
            -1
        }
    }

//    suspend fun getJobFormTemplate(jobId: Int):List<JobFieldDefinition>{
//        return try{ val template :String= client.get("$BASE_URL/getJobFormTemplate/$jobId")
//            Log.d("RAW DATA TEMPLATE","$template")
//                    val p=Json.decodeFromString<List<JobFieldDefinition>>(template.toString())
//            Log.d("Deserialized job fields: ","$p")
//        return p}
//        catch(e:Exception){
//            emptyList<JobFieldDefinition>()
//        }
//    }

    suspend fun getJobFormTemplate(jobId: Int): List<JobFieldDefinition> {
        return try {
            val response: String = client.get("$BASE_URL/getJobFormTemplate/$jobId").bodyAsText()
            Log.d("RAW DATA TEMPLATE", response)

            val parsed = Json.decodeFromString<List<JobFieldDefinition>>(response)
            Log.d("Deserialized job fields:", parsed.toString())

            parsed
        } catch (e: Exception) {
            Log.e("TEMPLATE ERROR", "Failed to fetch or parse template", e)
            emptyList()
        }
    }



    suspend fun detailAddJobForm(jobFormDetails: UserAddFormDetails): String {
        return try{
            val response:String =client.post("$BASE_URL/addDataForForm"){
                contentType(ContentType.Application.Json)
                    setBody(jobFormDetails)
            }.bodyAsText()
            response
        }catch (e:Exception){
            Log.e("FormSubmit", "Error: ${e.message}")
            "Error: ${e.message}"
        }
    }
}

