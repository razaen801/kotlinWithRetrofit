package com.rspackage.kotlindemo.support.data.api

import android.util.Log
import com.rspackage.kotlindemo.support.DemoApplication
import com.rspackage.kotlindemo.support.data.base_url
import com.rspackage.kotlindemo.support.data.endpoint
import com.rspackage.kotlindemo.support.data.responses.Animal
import com.rspackage.kotlindemo.support.network.ConnectionInterceptor
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import retrofit2.http.Headers
import java.util.concurrent.TimeUnit


interface ApiFactory {
    companion object {
        const val TAG = "ApiFactory"
        private const val cacheSize = (10 * 1024 * 1024).toLong() //10mb

        operator fun invoke(connectionInterceptor: ConnectionInterceptor): ApiFactory {
            val httpLoggingInterceptor =
                HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
                    override fun log(message: String) {
                        Log.d(TAG, message)
                    }
                }).apply {
//                level = if (debug) HttpLoggingInterceptor.Level.BODY
//                else HttpLoggingInterceptor.Level.NONE
                    HttpLoggingInterceptor.Level.BODY
                }

            val cache = Cache(DemoApplication.instance.cacheDir, cacheSize)

            val okHttpClient = OkHttpClient.Builder()
                .cache(cache)
                .addInterceptor { chain ->
                    val request = chain.request()
                    val maxAge = 60 * 5//seconds
                    val maxStale = 60 * 60 * 24 * 1 // tolerate 1 day stale
                    request.newBuilder()
                        //.cacheControl(CacheControl.Builder().maxAge(maxAge, TimeUnit.SECONDS).build())
                        .header("Cache-Control", "public, max-age=$maxAge")
                        //.header("Cache-Control", "max-stale=$maxStale")
                        .removeHeader("Pragma")
                        .build()

                    chain.proceed(request)
                }
                .addInterceptor(httpLoggingInterceptor)
                //.addInterceptor(cacheInterceptor)
                .addInterceptor(connectionInterceptor)
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build()

            return Retrofit.Builder()
                .baseUrl(base_url)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()
                .create(ApiFactory::class.java)
        }
    }

    @Headers("Content-Type:application/json")
    @GET(endpoint)
    suspend fun getData(): Response<Animal>

/*

    @FormUrlEncoded
    //@Headers("Content-Type:application/json")
    @Headers("Content-Type:application/x-www-form-urlencoded")
    @POST(URL_LOGIN)
    suspend fun login(
        @Header("Authorization") authorization: String,
        @Field("grant_type") grantType: String,
        @Field("username") username: String,
        @Field("password") password: String
    ): Response<LoginApiResponse>

    @Headers("Content-Type:application/json")
    @POST(URL_REGISTER)
    suspend fun register(
        @Body params: HashMap<String, String>
    ): Response<RegisterApiResponse>

    @Headers("Content-Type:application/json")
    @GET(URL_JOB_EJEE_JOBS)
    suspend fun getJobEjeeJobs(
        @Header("Authorization") authorization: String
    ): Response<JobEjeeJobsApiResponse>

    @Headers("Content-Type:application/json")
    @GET(URL_NEWSPAPER_JOBS)
    suspend fun getNewsPaperJobs(
        @Header("Authorization") authorization: String
    ): Response<NewspaperJobsApiResponse>

    @Headers("Content-Type:application/json")
    @POST(URL_JOB_DETAIL)
    suspend fun getJobDetail(
        @Header("Authorization") authorization: String,
        @Body params: HashMap<String, String>
    ): Response<JobEjeeJobDetailApiResponse>

    @Headers("Content-Type:application/json")
    @POST(URL_NEWSPAPER_JOB_DETAIL)
    suspend fun getNewspaperJobDetail(
        @Body params: HashMap<String, String>
    ): Response<NewspaperJobDetailApiResponse>

    @Headers("Content-Type:application/json")
    @POST(URL_JOB_SEARCH)
    suspend fun jobSearch(
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Body body: JobSearchModel
    ): Response<JobSearchApiResponse>

    @Headers("Content-Type:application/json")
    @POST(URL_GET_LOCATION)
    suspend fun getLocation(
        @Body params: HashMap<String, String>
    ): Response<List<Locations>>

    @Headers("Content-Type:application/json")
    @GET(URL_INDUSTRY)
    suspend fun getIndustries(): Response<IndustriesApiResponse>

    @Headers("Content-Type:application/json")
    @GET(URL_FUNCTIONAL_AREA)
    suspend fun getFunctionalAreas(): Response<FunctionalAreaApiResponse>

    @Headers("Content-Type:application/json")
    @GET(URL_DESIGNATION)
    suspend fun getDesignations(): Response<DesignationApiResponse>

    @Headers("Content-Type:application/json")
    @GET(URL_LANGUAGE_LIST)
    suspend fun getLanguageList(): Response<LanguageListApiResponse>

    @Headers("Content-Type:application/json")
    @DELETE(URL_DELETE_LANGUAGE)
    suspend fun deleteLanguage(
        @Header("Authorization") authorization: String,
        @Path("id") id: Int
    ): Response<DeleteLanguageApiResponse>

    @Headers("Content-Type:application/json")
    @GET(URL_USER_PROFILE)
    suspend fun getUserProfile(
        @Header("Authorization") authorization: String
    ): Response<ProfileApiResponse>


    @Headers("Content-Type:application/json")
    @POST(URL_USER_PROFILE)
    suspend fun updateUserProfile(
        @Header("Authorization") authorization: String,
        @Body profilePic: HashMap<String, String>
    ): Response<ProfileApiResponse>

    @Multipart
    @POST(URL_UPLOAD_PROFILE_PHOTO)
    suspend fun uploadProfilePhoto(
        @Header("Authorization") authorization: String,
        @Part file: MultipartBody.Part
    ): Response<UploadProfilePhotoApiResponse>


    @Headers("Content-Type:application/json")
    @GET(URL_USER_INFO)
    suspend fun getUserInfo(
        @Header("Authorization") authorization: String
    ): Response<UserInfoApiResponse>

    @Headers("Content-Type:application/json")
    @GET(URL_GET_STAT)
    suspend fun getUserStat(
        @Header("Authorization") authorization: String
    ): Response<StatApiResponse>

    @Headers("Content-Type:application/json")
    @GET(URL_GET_PROFILE_COMPLETE)
    suspend fun getUserProfileComplete(
        @Header("Authorization") authorization: String
    ): Response<UserProfileCompleteApiResponse>

    @Headers("Content-Type:application/json")
    @GET(URL_PROFILE_STATUS)
    suspend fun getProfileStatus(
        @Header("Authorization") authorization: String
    ): Response<ProfileStatusApiResponse>


    @Headers("Content-Type:application/json")
    @GET(URL_GET_PROFILE_OVERVIEW)
    suspend fun getUserProfileOverview(
        @Header("Authorization") authorization: String
    ): Response<ProfileOverviewApiResponse>

    @Headers("Content-Type:application/json")
    @POST(URL_UPDATE_PERSONAL_DETAIL)
    suspend fun updatePersonalDetail(
        @Header("Authorization") authorization: String,
        @Body params: HashMap<String, String>
    ): Response<UpdatePersonalDetailApiResponse>

    @Headers("Content-Type:application/json")
    @POST(URL_UPDATE_PERSONAL_DETAIL)
    suspend fun updateResume(
        @Header("Authorization") authorization: String,
        @Body params: ResumePost
    ): Response<UpdateResumeApiResponse>

    @Multipart
    @POST(URL_UPLOAD_RESUME)
    suspend fun uploadResume(
        @Header("Authorization") authorization: String,
        @Part file: MultipartBody.Part
    ): Response<UploadResumeApiResponse>

    @Multipart
    @POST(URL_UPLOAD_COVER_LETTER)
    suspend fun uploadCoverLetter(
        @Header("Authorization") authorization: String,
        @Part("jobCreateId") jobCreateId: RequestBody,
        @Part file: MultipartBody.Part
    ): Response<UploadCoverLetterApiResponse>

    @Headers("Content-Type:application/json")
    @POST(URL_ADD_EDIT_EMPLOYMENT)
    suspend fun addEmploymentDetail(
        @Header("Authorization") authorization: String,
        @Body params: EmploymentDetailPostObj
    ): Response<AddEmploymentDetailApiResponse>

    @Headers("Content-Type:application/json")
    @GET(URL_GET_EMPLOYMENT)
    suspend fun getEmployment(
        @Header("Authorization") authorization: String
    ): Response<List<EmploymentApiResponse>>

    @Headers("Content-Type:application/json")
    @DELETE(URL_DELETE_EMPLOYMENT)
    suspend fun deleteEmployment(
        @Header("Authorization") authorization: String,
        @Path("id") id: Int
    ): Response<DeleteEmploymentApiResponse>


    @Headers("Content-Type:application/json")
    @GET(URL_GET_EDUCATION_DETAILS)
    suspend fun getEducationDetails(
        @Header("Authorization") authorization: String
    ): Response<List<EducationDetailApiResponse>>

    @Headers("Content-Type:application/json")
    @POST(URL_ADD_EDUCATION_DETAILS)
    suspend fun addEducationDetails(
        @Header("Authorization") authorization: String,
        @Body educationPojo: EducationDetailPojo
    ): Response<AddEducationApiResponse>

    @Headers("Content-Type:application/json")
    @DELETE(URL_DELETE_EDUCATION_DETAILS)
    suspend fun deleteEducationDetail(
        @Header("Authorization") authorization: String,
        @Path("id") id: String
    ): Response<DeleteEducationApiResponse>

    @Multipart
    @POST(URL_UPLOAD_TRANSCRIPT)
    suspend fun uploadCertificate(
        @Header("Authorization") authorization: String,
        @Part file: MultipartBody.Part
    ): Response<UploadCertificateApiResponse>

    @Headers("Content-Type:application/json")
    @GET(URL_PREFERRED_LOCATION)
    suspend fun getPreferredLocations(
        @Header("Authorization") authorization: String
    ): Response<List<PreferredLocation>>

    @Headers("Content-Type:application/json")
    @POST(URL_ADD_JOB_PREFERENCE)
    suspend fun addJobPreference(
        @Header("Authorization") authorization: String,
        @Body preference: JobPreferencePojo
    ): Response<AddJobPreferenceApiResponse>

    @Headers("Content-Type:application/json")
    @GET(URL_GET_LANGUAGE)
    suspend fun getLanguage(
        @Header("Authorization") authorization: String
    ): Response<List<LanguageApiResponse>>

    @Headers("Content-Type:application/json")
    @POST(URL_ADD_LANGUAGE)
    suspend fun addLanguage(
        @Header("Authorization") authorization: String,
        @Body languagePojo: LanguagePojo
    ): Response<LanguageAddApiResponse>


    @Headers("Content-Type:application/json")
    @GET(URL_GET_CERTIFICATION)
    suspend fun getCertification(
        @Header("Authorization") authorization: String
    ): Response<List<CertificationApiResponse>>

    @Headers("Content-Type:application/json")
    @DELETE(URL_DELETE_CERTIFICATION)
    suspend fun deleteCertification(
        @Header("Authorization") authorization: String,
        @Path("id") id: Int
    ): Response<DeleteCertificationApiResponse>

    @Headers("Content-Type:application/json")
    @POST(URL_ADD_CERTIFICATION)
    suspend fun addCertification(
        @Header("Authorization") authorization: String,
        @Body params: HashMap<String, String>
    ): Response<AddCertificateApiResponse>

    @Headers("Content-Type:application/json")
    @GET(URL_GET_REFERENCE)
    suspend fun getReference(
        @Header("Authorization") authorization: String
    ): Response<List<ReferenceApiResponse>>

    @Headers("Content-Type:application/json")
    @POST(URL_ADD_REFERENCE)
    suspend fun addReference(
        @Header("Authorization") authorization: String,
        @Body params: HashMap<String, String>
    ): Response<AddReferenceApiResponse>

    @Headers("Content-Type:application/json")
    @DELETE(URL_DELETE_REFERENCE)
    suspend fun deleteReference(
        @Header("Authorization") authorization: String,
        @Path("id") id: Int
    ): Response<DeleteReferenceApiResponse>

    @Headers("Content-Type:application/json")
    @GET(URL_GET_DOCS)
    suspend fun getDocs(
        @Header("Authorization") authorization: String
    ): Response<List<DocumentApiResponse>>

    @Multipart
    @POST(URL_UPLOAD_DOCS)
    suspend fun uploadDocument(
        @Header("Authorization") authorization: String,
        @Part file: MultipartBody.Part
    ): Response<UploadDocumentApiResponse>

    @Headers("Content-Type:application/json")
    @GET(URL_DELETE_DOCS) //DELETE doesnot work
    suspend fun deleteDocument(
        @Header("Authorization") authorization: String,
        @Path("id") id: String
    ): Response<DeleteDocumentApiResponse>

    @Headers("Content-Type:application/json")
    @POST(URL_ADD_DOCS)
    suspend fun addDocuments(
        @Header("Authorization") authorization: String,
        @Body body: HashMap<String, String>
    ): Response<AddDocumentApiResponse>


    @Headers("Content-Type:application/json")
    @POST(URL_CHANGE_PASSWORD)
    suspend fun changePassword(
        @Header("Authorization") authorization: String,
        @Body body: HashMap<String, String>
    ): Response<ChangePasswordResponse>

    @Headers("Content-Type:application/json")
    @POST(URL_CHANGE_EMAIL)
    suspend fun changeEmail(
        @Header("Authorization") authorization: String,
        @Body body: HashMap<String, String>
    ): Response<ChangeEmailApiResponse>

    */
/* @Headers("Content-Type:application/json")
     @GET(URL_REQUEST_OTP)
     suspend fun requestOtpVerification(
         @Header("Authorization") authorization: String,
         @Path("mobile") mobile: String
     ): Response<OtpRequestApiResponse>*//*


    @Headers("Content-Type:application/json")
    @GET(URL_REQUEST_OTP)
    suspend fun requestOtpVerification(
        @Header("Authorization") authorization: String,
        @Path("mobile") mobile: String
    ): Response<OtpRequestApiResponse>

    @Headers("Content-Type:application/json")
    @PUT(URL_VERIFY_OTP)
    suspend fun verifyOtpVerification(
        @Header("Authorization") authorization: String,
        @Path("otp") otp: String
    ): Response<OtpVerificationApiResponse>

    @Headers("Content-Type:application/json")
    @GET(URL_CHANGE_MOBILE)
    suspend fun changeMobileNumber(
        @Header("Authorization") authorization: String,
        @Path("mobile") mobile: String
    ): Response<ChangeMobileApiResponse>

    @Headers("Content-Type:application/json")
    @POST(URL_EDIT_OTHER_DETAILS)
    suspend fun editOtherDetails(
        @Header("Authorization") authorization: String,
        @Body params: HashMap<String, String>
    ): Response<EditOtherDetailApiResponse>


    @Headers("Content-Type:application/json")
    @GET(URL_APPLIED_JOBS)
    suspend fun getAppliedJobs(
        @Header("Authorization") authorization: String,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): Response<AppliedJobsApiResponse>

    @Headers("Content-Type:application/json")
    @GET(URL_RECOMMENDED_JOBS)
    suspend fun getRecommendedJobs(
        @Header("Authorization") authorization: String,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): Response<RecommendedJobsApiResponse>

    @Headers("Content-Type:application/json")
    @POST(URL_APPLY_FOR_JOB)
    suspend fun applyForJob(
        @Header("Authorization") authorization: String,
        @Body params: HashMap<String, String>
    ): Response<ApplyForJobApiResponse>

    @Headers("Content-Type:application/json")
    @GET(URL_FORGOT_PASSWORD)
    suspend fun forgotPassword(
        @Query("email") email: String
    ): Response<ForgotPasswordApiResponse>

    @Headers("Content-Type:application/json")
    @GET(URL_SAVED_JOBS)
    suspend fun getSavedJobs(
        @Header("Authorization") authorization: String,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): Response<SavedJobsApiResponse>

    @Headers("Content-Type:application/json")
    @PUT(URL_BOOKMARK_JOB)
    suspend fun bookmarkJob(
        @Header("Authorization") authorization: String,
        @Path("jobId") jobId: String
    ): Response<BookmarkJobApiResponse>

    @Headers("Content-Type:application/json")
    @POST(URL_CONTACT_US)
    suspend fun contactUs(
        @Header("Authorization") authorization: String,
        @Body params: HashMap<String, String>
    ): Response<ContactUsApiResponse>


    @Headers("Content-Type:application/json")
    @POST(URL_COMPANY)
    suspend fun getCompanyDetail(
        @Header("Authorization") authorization: String,
        @Body params: HashMap<String, String>
    ): Response<CompanyApiResponse>


    @Headers("Content-Type:application/json")
    @POST(URL_VERIFY_EMAIL_FROM_CODE)
    suspend fun verifyEmailFromCode(
        @Header("Authorization") authorization: String,
        @Path("code") code: String
    ): Response<VerifyEmailApiResponse>

    @Headers("Content-Type:application/json")
    @GET(URL_VERIFY_EMAIL)
    suspend fun verifyEmail(
        @Header("Authorization") authorization: String
    ): Response<VerifyEmailApiResponse>

*/

}
