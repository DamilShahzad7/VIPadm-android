package vip.com.vipmeetings.service
import android.app.Service
import android.os.*
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Binder
import android.os.IBinder
//import androidx.core.app.JobIntentService

import android.os.Handler
import android.os.HandlerThread

import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.google.gson.Gson
import com.google.gson.GsonBuilder
//import com.readystatesoftware.chuck.ChuckInterceptor//raparthy
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.greenrobot.eventbus.EventBus
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
import vip.com.vipmeetings.BuildConfig
import vip.com.vipmeetings.body.*
import vip.com.vipmeetings.envelope.*
import vip.com.vipmeetings.evacuate.IpAddress
import vip.com.vipmeetings.interfaces.LoginInterface
import vip.com.vipmeetings.models.Evacuation
import vip.com.vipmeetings.models.EvacuationResponsible
import vip.com.vipmeetings.models.Message
import vip.com.vipmeetings.request.*
import vip.com.vipmeetings.utilities.Constants
import java.security.KeyStore
import java.security.KeyStoreException
import java.security.NoSuchAlgorithmException
import java.util.*
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager
import kotlin.collections.ArrayList

class PushService : Service() {

    var service: LoginInterface? = null
    var retrofit: Retrofit? = null
    var mSharedPreferences: SharedPreferences? = null
    var client: OkHttpClient? = null
    var mGson: Gson? = null
    var eventBus: EventBus? = null

    // ────────────── New Service Boilerplate ──────────────

    // 1) a background thread & handler
    private lateinit var workerThread: HandlerThread
    private lateinit var workerHandler: Handler

    // 2) our Binder for clients
    private val binder = PushBinder()

    override fun onCreate() {
        super.onCreate()
        IpAddress.e("PUSHSERVICE", "CREATE SERVICE")
        // start the background thread
        workerThread = HandlerThread("PushServiceWorker").apply { start() }
        workerHandler = Handler(workerThread.looper)

        // init your existing singletons
        mSharedPreferences = getSharedPreferences(Constants.SHAREDPREFE, Context.MODE_PRIVATE)
        mGson = GsonBuilder().create()
        eventBus = EventBus.getDefault()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // If you ever want to auto-start something on every launch:
        // workerHandler.post { startAdminEvacuationStart() }
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder {
        return binder
    }

    override fun onDestroy() {
        IpAddress.e("PUSHSERVICE", "DESTROY SERVICE")
        workerThread.quitSafely()
        super.onDestroy()
    }

    // 3) keep your existing methods (startAdminEvacuationStart(), etc.) unchanged,
    // but when calling them from the UI, wrap them on workerHandler:
    //
    //    workerHandler.post { startAdminEvacuationStart() }




    fun getTcpIpEvacuationFalse(): String {


        return Constants.IPADDRESS2
    }

    fun setRetrofit(ip: String) {
        retrofit = Retrofit.Builder()
                .baseUrl(ip)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .client(setOKHTTPAfterLogin())
                .build()

    }

    fun getLogging(): HttpLoggingInterceptor {
        val interceptor = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger { message -> IpAddress.e("okhttp", message) })

        if (BuildConfig.DEBUG)
            interceptor.level = HttpLoggingInterceptor.Level.BODY
        else
            interceptor.level = HttpLoggingInterceptor.Level.NONE

        return interceptor
    }

    fun setOKHTTPAfterLogin(): OkHttpClient {


        try {

            if (BuildConfig.DEBUG) {
                client = addInterceptor()
                        // .addInterceptor(ChuckInterceptor(this))//raparthy
                        .addInterceptor(ChuckerInterceptor(this))
                        .sslSocketFactory(setCertificate()!!.socketFactory, getTrustManager()!!)
                        .writeTimeout(0, TimeUnit.SECONDS)
                        .addNetworkInterceptor(StethoInterceptor())
                        .readTimeout(0, TimeUnit.SECONDS)
                        .retryOnConnectionFailure(true)
                        .connectTimeout(0, TimeUnit.SECONDS)
                        .addInterceptor(getLogging())
                        .build()

            } else

                client = addInterceptor()
                        .sslSocketFactory(setCertificate()!!.socketFactory, getTrustManager()!!)
                        .writeTimeout(0, TimeUnit.SECONDS)
                        .readTimeout(0, TimeUnit.SECONDS)
                        .retryOnConnectionFailure(true)
                        .connectTimeout(0, TimeUnit.SECONDS)
                        .addInterceptor(getLogging())
                        .build()
        } catch (e: Exception) {
            IpAddress.e("errorssl", e.toString() + "")
            client = addInterceptor()
                    .writeTimeout(0, TimeUnit.SECONDS)
                    .readTimeout(0, TimeUnit.SECONDS)
                    .retryOnConnectionFailure(true)
                    .connectTimeout(0, TimeUnit.SECONDS)
                    .addInterceptor(getLogging())
                    .build()
        }

        return client!!
    }

    fun getClientID(): String? {
        return mSharedPreferences?.getString(Constants.ClientID, null)
    }


    fun initializeData() {
        if (mSharedPreferences == null)
            mSharedPreferences = getSharedPreferences(Constants.SHAREDPREFE, Context.MODE_PRIVATE)
        setOKHTTPAfterLogin()
        setRetrofit(getTcpIpEvacuationFalse())
        setService()
    }


    fun addInterceptor(): OkHttpClient.Builder {

        val httpClient = OkHttpClient.Builder()

        httpClient.hostnameVerifier { _, session -> true }

        if (getClientID() != null) {
            httpClient.addInterceptor { chain ->
                val original = chain.request()

                //val originalHttpUrl = original.url()//Commented by Raparthy for Kotlin Version
                val originalHttpUrl = original.url//updated kotlin version Raparthy

                val url = originalHttpUrl
                        .newBuilder()
                        .addEncodedQueryParameter(IpAddress.CLIENTID, getClientID())
                        .build()

                // Request customization: add request headers
                val requestBuilder = original
                        .newBuilder()
                        .url(url)

                val request = requestBuilder
                        .build()
                chain.proceed(request)
            }
        }

        return httpClient
    }


    fun setCertificate(): SSLContext? {

        var sc: SSLContext? = null
        try {
            val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {

                override fun getAcceptedIssuers(): Array<java.security.cert.X509Certificate> {
                    return arrayOf()
                }

                override fun checkClientTrusted(certs: Array<java.security.cert.X509Certificate>, authType: String) {
                    //No need to implement.
                }

                override fun checkServerTrusted(certs: Array<java.security.cert.X509Certificate>, authType: String) {
                    //No need to implement.
                }
            })
            sc = SSLContext.getInstance("SSL")
            sc!!.init(null, trustAllCerts, java.security.SecureRandom())
            return sc
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null

    }

    fun getTrustManager(): X509TrustManager? {
        var trustManager: X509TrustManager? = null
        try {
            val trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
            trustManagerFactory.init(null as KeyStore?)
            val trustManagers = trustManagerFactory.trustManagers
            if (trustManagers.size != 1 || trustManagers[0] !is X509TrustManager) {
                throw IllegalStateException("Unexpected default trust managers:" + Arrays.toString(trustManagers))
            }
            trustManager = trustManagers[0] as X509TrustManager
        } catch (e: KeyStoreException) {
            e.printStackTrace()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }

        return trustManager
    }


    fun setService() {
        service = retrofit?.create(LoginInterface::class.java)
    }





    fun startPush() {

        IpAddress.e("PUSHSERVICE", "START PUSH SERVICE")
    }





    fun getAuthTokenPref_Evacuation(): String {

        return mSharedPreferences?.getString(IpAddress.AUTHTOKEN_EVACUATION, "")!!
    }

    private fun onSoapStart(soapStartEvacuationResponseEnvelope: SoapNotifyAboutEvacuationEnabledResponse): String {

        try {
            val startEvacuationResponseBody = soapStartEvacuationResponseEnvelope.body
            return startEvacuationResponseBody.notifyAboutEvacuationEnabledResponse
                    .notifyAboutEvacuationEnabledResult.status._status
        } catch (e: Exception) {
            return ""
        }

    }

    private fun onSoapStart(soapNotifyAboutEvacuationEndedResponse: SoapNotifyAboutEvacuationEndedResponse): String {

        try {
            val notifyAboutEvacuationEndedResponseBody = soapNotifyAboutEvacuationEndedResponse.body
            return notifyAboutEvacuationEndedResponseBody.notifyAboutEvacuationEndedResponse
                    .notifyAboutEvacuationEndedResult._string
        } catch (e: Exception) {
            return ""
        }

    }


    fun startAdminEvacuationStart() {

        initializeData()
        val soapNotifyAboutEvacuationEnabledRequest = SoapNotifyAboutEvacuationEnabledRequest()
        val startEvacuationRequestBody = SoapNotifyAboutEvacuationEnabledRequestBody()

        val notifyAboutEvacuationEnabled = NotifyAboutEvacuationEnabled()
        notifyAboutEvacuationEnabled.a_iPlaceId = mSharedPreferences
                ?.getString(IpAddress.PLACEID_NEW, "")
        notifyAboutEvacuationEnabled.authToken = getAuthTokenPref_Evacuation()
        startEvacuationRequestBody.notifyAboutEvacuationEnabled = notifyAboutEvacuationEnabled


        soapNotifyAboutEvacuationEnabledRequest.body = startEvacuationRequestBody

        val lClientDetailsObservable = service?.notifyAboutEvacuationEnabled(soapNotifyAboutEvacuationEnabledRequest)
        lClientDetailsObservable?.subscribeOn(Schedulers.io())
                ?.map(this::onSoapStart)
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe(this::onPushSuccess, this::onError)

    }

    fun onError(throwable: Throwable) {


        IpAddress.e("Error", throwable.toString())
    }

    private fun onPushSuccess(status: String) {


    }

    fun endEvacuationStart() {


        initializeData()
        val soapNotifyAboutEvacuationEndedRequest = SoapNotifyAboutEvacuationEndedRequest()
        val soapNotifyAboutEvacuationEndedBody = SoapNotifyAboutEvacuationEndedBody()
        val notifyAboutEvacuationEnded = NotifyAboutEvacuationEnded()


        notifyAboutEvacuationEnded.a_iPlaceId = mSharedPreferences
                ?.getString(IpAddress.PLACEID_NEW, "")
        notifyAboutEvacuationEnded.authToken = getAuthTokenPref_Evacuation()

        soapNotifyAboutEvacuationEndedBody.notifyAboutEvacuationEnded = notifyAboutEvacuationEnded

        soapNotifyAboutEvacuationEndedRequest.body = soapNotifyAboutEvacuationEndedBody


        val lClientDetailsObservable = service?.notifyAboutEvacuationEnded(soapNotifyAboutEvacuationEndedRequest)
        lClientDetailsObservable
                ?.subscribeOn(Schedulers.io())
                ?.map(this::onSoapStart)
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe(this::onPushSuccess, this::onError)
    }

    private fun onSoapUpdate(soapUpdateResponsibleResponseEnvelope: SoapUpdateResponsibleResponseEnvelope): String {


        val updateResponsibleAvailabilityResponseBody = soapUpdateResponsibleResponseEnvelope.body
        return updateResponsibleAvailabilityResponseBody.updateResponsibleAvailabilityResponse.upDateResponsible
    }

    fun onYesNoPush(isYesNo: String, barcode: String, clientID: String) {


        initializeData()
        val soapUpdateResponsibleRequestEnvelope = SoapUpdateResponsibleRequestEnvelope()

        val updateResponsibleRequestBody = UpdateResponsibleRequestBody()

        val updateResponsibleAvailability = UpdateResponsibleAvailability()

        updateResponsibleAvailability.a_sAvailableStatus = isYesNo
        updateResponsibleAvailability.authToken = getAuthTokenPref_Evacuation()
        updateResponsibleAvailability.a_sBarcode = barcode
        updateResponsibleAvailability.a_sClientId = clientID
        updateResponsibleRequestBody.updateResponsibleAvailability = updateResponsibleAvailability
        soapUpdateResponsibleRequestEnvelope.body = updateResponsibleRequestBody

        val lClientDetailsObservable = service!!.updateResponsibleAvailability(soapUpdateResponsibleRequestEnvelope)

        lClientDetailsObservable.subscribeOn(Schedulers.io())
                .map(this::onSoapUpdate)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onSuccessRadioButtonClicked, this::onError)
    }

    private fun onSuccessRadioButtonClicked(status_evacuate: String?) {


    }

    fun sendPushMessage(placeid: String, barcode: String) {


        initializeData()
        val soapNotifyAboutResponsibleAvailability = SoapNotifyAboutResponsibleAvailability()

        val notifyAboutResponsibleAvailabilityBody = NotifyAboutResponsibleAvailabilityBody()


        val notifyAboutResponsibleAvailability = NotifyAboutResponsibleAvailability()

        notifyAboutResponsibleAvailability.a_iPlaceId = placeid
        notifyAboutResponsibleAvailability.a_sBarcode = barcode
        notifyAboutResponsibleAvailability.authToken = getAuthTokenPref_Evacuation()

        notifyAboutResponsibleAvailabilityBody.notifyAboutResponsibleAvailability = notifyAboutResponsibleAvailability

        soapNotifyAboutResponsibleAvailability.body = notifyAboutResponsibleAvailabilityBody

        val lClientDetailsObservable = service!!.notifyAboutResponsibleAvailability(soapNotifyAboutResponsibleAvailability)

        lClientDetailsObservable.subscribeOn(Schedulers.io())
                .map(this::onSoapEvacuationPush)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onPush, this::onError)
    }

    private fun onSoapEvacuationPush(soapSendEvacuationPushResponseEnvelope: SoapNotifyAboutResponsibleAvailabilityResponse): String {


        try {
            val soapNotifyAboutAreaClearenceResponse = soapSendEvacuationPushResponseEnvelope.body

            return soapNotifyAboutAreaClearenceResponse.notifyAboutResponsibleAvailabilityResponse
                    .notifyAboutResponsibleAvailabilityResult._string
        } catch (e: Exception) {
            return ""
        }

    }

    private fun onPush(status: String) {


        IpAddress.e("push", status + "")
    }

    inner class PushBinder : Binder() {

        fun getPushService(): PushService {
            return this@PushService
        }
    }


    fun notifyaboutAreaClearence(placeid: String, evacuationID: String) {


        initializeData()
        val soapNotifyAboutAreaClearence = SoapNotifyAboutAreaClearence()

        val notifyAboutAreaClearenceRequestBody = SoapNotifyAboutAreaClearenceRequestBody()


        val notifyAboutAreaClearence = NotifyAboutAreaClearence()
        notifyAboutAreaClearence.a_iPlaceId = placeid
        notifyAboutAreaClearence.authToken = getAuthTokenPref_Evacuation()

        notifyAboutAreaClearence.a_sEvacuationIds = evacuationID;

        notifyAboutAreaClearenceRequestBody.notifyAboutAreaClearence = notifyAboutAreaClearence

        soapNotifyAboutAreaClearence.body = notifyAboutAreaClearenceRequestBody

        val lClientDetailsObservable = service!!.notifyAboutAreaClearence(soapNotifyAboutAreaClearence)

        lClientDetailsObservable.subscribeOn(Schedulers.io())
                .map(this::onSoapEvacuationPush)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onPush, this::onError)
    }

    private fun onSoapEvacuationPush(soapNotifyAboutAreaClearenceResponse: SoapNotifyAboutAreaClearenceResponse): String {


        try {

            return soapNotifyAboutAreaClearenceResponse.body
                    .notifyAboutAreaClearenceResponse.notifyAboutAreaClearenceResult._string
        } catch (e: Exception) {
            return ""
        }

    }

    fun getBarcode(): String? {


        return mSharedPreferences?.getString(Constants.BARCODE, null)
    }

    fun getEvacuationResponsiblePlaced(): String {


        var evacuationResponsible = mGson?.fromJson(mSharedPreferences?.getString(Constants.EVACUATION, null),
                EvacuationResponsible::class.java)
        if (getClientID() != null)
            evacuationResponsible?.setClientId(getClientID())
        if (getBarcode() != null)
            evacuationResponsible?.setBarcode(getBarcode())

        if (evacuationResponsible?.getAdminID() == null ||
                evacuationResponsible?.getAdminID()!!.trim().length == 0) {
            evacuationResponsible?.setUserType("N")
            IpAddress.e("LOGIN", "NORMAL")
        } else {
            IpAddress.e("LOGIN", "ADMIN")
            evacuationResponsible?.setUserType("A")
        }

        IpAddress.e("placeid2", evacuationResponsible?.a_sPlaceId!!)
        return evacuationResponsible?.a_sPlaceId!!

    }

    fun refreshEvacuationList(placeid: String) {


        IpAddress.e("placeid1", placeid + "")
        initializeData()
        val soapGetEvacuationsRequestEnvelope = SoapGetEvacuationsRequestEnvelope()
        val getEvacuationsRequestBody = GetEvacuationsRequestBody()

        val getEvacuations = GetEvacuations()
        getEvacuations.a_sBarcode = ""
        getEvacuations.a_sClientId = getClientID()
        getEvacuations.a_sPlaceId = getEvacuationResponsiblePlaced()
        getEvacuations.authToken = getAuthTokenPref_Evacuation()
        getEvacuationsRequestBody.getEvacuations = getEvacuations
        soapGetEvacuationsRequestEnvelope.body = getEvacuationsRequestBody

        val lClientDetailsObservable = service?.getEvacuations(soapGetEvacuationsRequestEnvelope)
        lClientDetailsObservable!!.subscribeOn(Schedulers.io())
                .map<Evacuation>(this::onSoapMap)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onEvacuations, this::onError)

    }


    private fun onSoapMap(soapGetEvacuationsResponseEnvelope: SoapGetEvacuationsResponseEnvelope): Evacuation? {

        val getEvacuationsResponseBody = soapGetEvacuationsResponseEnvelope.body
        val evacuations = getEvacuationsResponseBody.getEvacuationsResponse
                .getEvacuationsResult.evacuations

        var evacuation1: Evacuation? = Evacuation()
        if (evacuations != null && evacuations.evacuation != null) {

            evacuation1 = evacuations.evacuation.get(0)
            evacuation1?.setEvacuationListOLD(evacuations.evacuation)
            evacuation1?.setEvacuationList(evacuations.evacuation)
        }


        return evacuation1

    }


    private fun onEvacuations(evacuation1: Evacuation) {


        eventBus?.post(evacuation1);


    }

    fun getMessages(clientid: String, placeID: String, evaid: String, isUSer: Boolean) {


        initializeData()
        val soapGetMessagesRequestEnvelope = SoapGetMessagesRequestEnvelope()
        val getMessagesRequestBody = GetMessagesRequestBody()
        val getMessages = GetMessages()

        getMessages.a_sClientId = clientid
        getMessages.a_sEvacuationId = evaid
        getMessages.a_sPlaceId = placeID
        getMessages.authToken = getAuthTokenPref_Evacuation()

        getMessagesRequestBody.getMessages = getMessages
        soapGetMessagesRequestEnvelope.body = getMessagesRequestBody

        val lClientDetailsObservable = service?.getResponsibleEvacuationMessages(soapGetMessagesRequestEnvelope)

        lClientDetailsObservable!!.subscribeOn(Schedulers.io())
                .map(this::onSoapGetMessages)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onMessages, this::onMessageError);

    }

    private fun onMessageError(throwable: Throwable) {

        IpAddress.e("msg", throwable.toString())
    }


    private fun onSoapGetMessages(soapGetMessagesResponseEnvelope: SoapGetMessagesResponseEnvelope): List<Message>? {

        IpAddress.e("msg", "Push")
        var messageList: ArrayList<Message>
        messageList = ArrayList()
        val getMessagestResponseBody = soapGetMessagesResponseEnvelope.body
        if (getMessagestResponseBody.getMessagesResponse.getMessagesResult.empty == null) {

            val messages = getMessagestResponseBody
                    .getMessagesResponse.getMessagesResult.messages

            if (messages != null && messages.message != null) {
                var message1: Message? = null
                for (message in messages.message) {

                    if (message1 == null) {
                        message1 = Message()
                        message1.date = message.date
                        message1.isDate = true
                        messageList.add(message1)
                    } else if (!Constants.formatDateFromMessage(message.date.trim()).equals(
                                    Constants.formatDateFromMessage(message1.date.trim()), ignoreCase = true)) {

                        message1 = Message()
                        message1.date = message.date
                        message1.isDate = true
                        messageList.add(message1)
                    }
                    message.isDate = false
                    messageList.add(message)
                }

                // messageList.addAll(messages.getMessage());
            }

            return messageList
        }

//        if (messageList.size > 0) {
//            msgId = messageList.get(messageList.size - 1).getMessageId()
//        }
        return messageList

        // return null
    }

    private fun onMessages(messages: List<Message>?) {


        eventBus?.post(messages)

    }


}