package vip.com.vipmeetings;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.provider.ContactsContract;


//import com.readystatesoftware.chuck.ChuckInterceptor;//raparthy

import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;
import vip.com.vipmeetings.body.GetClientRequestBody;
import vip.com.vipmeetings.body.GetContactsRequestBody;
import vip.com.vipmeetings.body.GetFormerVisitorsRequestBody;
import vip.com.vipmeetings.envelope.SoapGetClient;
import vip.com.vipmeetings.envelope.SoapGetClientResponseEnvelope;
import vip.com.vipmeetings.envelope.SoapGetContactsRequestEnvelope;
import vip.com.vipmeetings.envelope.SoapGetContactsResponseEnvelope;
import vip.com.vipmeetings.envelope.SoapGetFormerVisitorsRequestEnvelope;
import vip.com.vipmeetings.envelope.SoapGetFormerVisitorsResponseEnvelope;
import vip.com.vipmeetings.evacuate.IpAddress;
import vip.com.vipmeetings.interfaces.ContactsApi;
import vip.com.vipmeetings.interfaces.MainApi;
import vip.com.vipmeetings.models.Client;
import vip.com.vipmeetings.models.Contact;
import vip.com.vipmeetings.models.Contacts;
import vip.com.vipmeetings.models.Visitor;
import vip.com.vipmeetings.models.Visitors;
import vip.com.vipmeetings.request.GetClient;
import vip.com.vipmeetings.request.GetContacts;
import vip.com.vipmeetings.request.GetFormerVisitors;
import vip.com.vipmeetings.utilities.Constants;

import static vip.com.vipmeetings.utilities.Constants.PHONEURI;
import static vip.com.vipmeetings.utilities.Constants.allContactList;
import static vip.com.vipmeetings.utilities.Constants.companyContactList1;
import static vip.com.vipmeetings.utilities.Constants.phoneContactList1;
import static vip.com.vipmeetings.utilities.Constants.visContactList1;

import com.chuckerteam.chucker.api.ChuckerInterceptor;

/**
 * Created by Srinath on 11/02/18.
 */

public class ContactService extends JobIntentService {


    private SharedPreferences mSharedPreferences;
    private Retrofit retrofit;
    private OkHttpClient client;
    private ContactsApi contactsApi;
    private boolean isPatient;
    private Intent intent;


    public ContactService() {

    }


    public static void enqueWork(Context context, Intent intent) {
        enqueueWork(context, ContactService.class, IpAddress.JOBID_CONTACTSERVICE, intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mSharedPreferences = getSharedPreferences(Constants.SHAREDPREFE, MODE_PRIVATE);

    }

    private VIPmeetings getApp() {

        return (VIPmeetings) getApplicationContext();
    }


    public void setRetrofit(String ip) {


        try {
            retrofit = new Retrofit.Builder()
                    .baseUrl(ip)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(SimpleXmlConverterFactory.create())
                    .client(setOKHTTPAfterLogin())
                    .build();
        } catch (Exception e) {
            Constants.e("URL SET RETROFIT", e.toString());
        }

    }

    public OkHttpClient.Builder addInterceptor() {

        OkHttpClient.Builder httpClient =
                new OkHttpClient.Builder();

        if (getClientID() != null && getClientID().trim().length() > 0)
            httpClient.addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request original = chain.request();
                    HttpUrl originalHttpUrl = original.url();
                    HttpUrl url = originalHttpUrl.newBuilder()
                            .addEncodedQueryParameter
                                    (IpAddress.CLIENTID, getClientID())
                            .build();

                    // Request customization: add request headers
                    Request.Builder requestBuilder = original.newBuilder()
                            .url(url);

                    Request request = requestBuilder.build();
                    return chain.proceed(request);
                }
            });

        return httpClient;
    }

    public void logSend(String key, String attribute1, String attribute2) {

    }


    private OkHttpClient setOKHTTPAfterLogin() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {


                IpAddress.e("okhttp", message);
            }
        });
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        try {

            client = addInterceptor()
                    // .addInterceptor(new ChuckInterceptor(this))//raparthy
                    .addInterceptor(new ChuckerInterceptor(this))//raparthy

                    .sslSocketFactory(setCertificate().getSocketFactory(), getTrustManager())
                    .writeTimeout(0, TimeUnit.SECONDS)
                    .readTimeout(0, TimeUnit.SECONDS)
                    .retryOnConnectionFailure(true)
                    .connectTimeout(0, TimeUnit.SECONDS)
                    .addInterceptor(interceptor)
                    .build();
        } catch (Exception e) {
            IpAddress.e("errorssl", e.toString() + "");
            client = addInterceptor()
                    .writeTimeout(0, TimeUnit.SECONDS)
                    .retryOnConnectionFailure(true)
                    .readTimeout(0, TimeUnit.SECONDS)
                    .connectTimeout(0, TimeUnit.SECONDS)
                    .addInterceptor(interceptor)
                    .build();
        }

        return client;
    }


    public SSLContext setCertificate() {

        SSLContext sc = null;
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {

                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }

                        public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {
                            //No need to implement.
                        }

                        public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {
                            //No need to implement.
                        }
                    }
            };
            sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            return sc;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;

    }

    public X509TrustManager getTrustManager() {
        X509TrustManager trustManager = null;
        try {
            TrustManagerFactory trustManagerFactory =
                    TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init((KeyStore) null);
            TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
            if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
                throw new IllegalStateException("Unexpected default trust managers:" +
                        Arrays.toString(trustManagers));
            }
            trustManager = (X509TrustManager) trustManagers[0];
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return trustManager;
    }


    public String getEmail() {

        return mSharedPreferences.getString(Constants.EMAIL, null);

    }


    public String getClientID() {
        return mSharedPreferences.getString(Constants.ClientID, "");
    }

    public String getAuthTokenPref_Mobile() {

        return mSharedPreferences.getString(IpAddress.AUTHTOKEN_MOBILE, "CC484588-C0B4-4777-B6C6-3F149728FC49");
    }

    public boolean isPatient() {


        return mSharedPreferences.getBoolean(Constants.ISPATIENT, false);
    }

    private void getFormerVisitor() {


        if (allContactList == null || allContactList.size() == 0) {
            allContactList = new ArrayList<>();
            allContactList.clear();
            setRetrofit(getApp().getTcpIp());
            contactsApi = retrofit.create(ContactsApi.class);

            SoapGetContactsRequestEnvelope soapGetContactsRequestEnvelope = new SoapGetContactsRequestEnvelope();

            GetContactsRequestBody getContactsRequestBody = new GetContactsRequestBody();

            GetContacts getContacts = new GetContacts();
            getContacts.setAuthToken(getAuthTokenPref_Mobile());
            getContacts.setA_sUserEmail(getEmail());
            getContactsRequestBody.setGetContacts(getContacts);
            soapGetContactsRequestEnvelope.setBody(getContactsRequestBody);


            SoapGetFormerVisitorsRequestEnvelope soapGetFormerVisitorsRequestEnvelope = new SoapGetFormerVisitorsRequestEnvelope();

            GetFormerVisitorsRequestBody getFormerVisitorsRequestBody = new GetFormerVisitorsRequestBody();

            GetFormerVisitors getFormerVisitors = new GetFormerVisitors();
            getFormerVisitors.setAuthToken(getAuthTokenPref_Mobile());
            getFormerVisitors.setA_sHostMail(getEmail());
            getFormerVisitorsRequestBody.setGetFormerVisitors(getFormerVisitors);
            soapGetFormerVisitorsRequestEnvelope.setBody(getFormerVisitorsRequestBody);

            Observable<SoapGetFormerVisitorsResponseEnvelope> contactsObservable =
                    contactsApi.getFormerVisitors(soapGetFormerVisitorsRequestEnvelope);


            contactsObservable.
                    subscribeOn(Schedulers.newThread())
                    .map(this::soapVisitors)
                    .subscribe(this::addVisitors, this::onError);
        }
    }

    private void getClient() {

        setRetrofit(getApp().getTcpIp());
        MainApi mainApi = retrofit.create(MainApi.class);
        SoapGetClient soapGetClient = new SoapGetClient();

        GetClientRequestBody getClientRequestBody = new GetClientRequestBody();

        GetClient getClient = new GetClient();

        getClient.setAuthToken(getAuthTokenPref_Mobile());
        getClient.setsClientID(getClientID());

        getClientRequestBody.setGetClient(getClient);

        soapGetClient.setBody(getClientRequestBody);


        Observable<SoapGetClientResponseEnvelope> lClientDetailsObservable =
                mainApi.getClient(soapGetClient);
        lClientDetailsObservable.
                subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onGetClient, this::onError);
    }

    public void storeIsPatient(boolean isPatient) {


        mSharedPreferences.edit().putBoolean(Constants.ISPATIENT, isPatient).apply();
    }

    private void onGetClient(SoapGetClientResponseEnvelope soapGetClientResponseEnvelope) {


        try {

            Client client = soapGetClientResponseEnvelope.getBody().getGetClientResponse().getGetClientResult().getClient();
            if (client != null) {

                isPatient = client.isPatientSystem();
                storeIsPatient(isPatient);

                if (intent != null && intent.hasExtra(IpAddress.SKIPCONTACT)) {
                    loadContacts(intent.hasExtra(IpAddress.SKIPCONTACT));
                } else {
                    loadContacts(false);
                }


            }
        } catch (Exception e) {

        }

    }


    private void loadContacts(boolean skipcontact) {


        if (isPatient || isPatient()) {
            getFormerVisitor();

        } else {

            if (allContactList == null || allContactList.size() == 0) {
                allContactList = new ArrayList<>();
                allContactList.clear();

                SoapGetContactsRequestEnvelope soapGetContactsRequestEnvelope = new SoapGetContactsRequestEnvelope();

                GetContactsRequestBody getContactsRequestBody = new GetContactsRequestBody();

                GetContacts getContacts = new GetContacts();
                getContacts.setAuthToken(getAuthTokenPref_Mobile());
                getContacts.setA_sUserEmail(getEmail());
                getContactsRequestBody.setGetContacts(getContacts);
                soapGetContactsRequestEnvelope.setBody(getContactsRequestBody);


                SoapGetFormerVisitorsRequestEnvelope soapGetFormerVisitorsRequestEnvelope = new SoapGetFormerVisitorsRequestEnvelope();

                GetFormerVisitorsRequestBody getFormerVisitorsRequestBody = new GetFormerVisitorsRequestBody();

                GetFormerVisitors getFormerVisitors = new GetFormerVisitors();
                getFormerVisitors.setAuthToken(getAuthTokenPref_Mobile());
                getFormerVisitors.setA_sHostMail(getEmail());
                getFormerVisitorsRequestBody.setGetFormerVisitors(getFormerVisitors);
                soapGetFormerVisitorsRequestEnvelope.setBody(getFormerVisitorsRequestBody);

                contactsApi = retrofit.create(ContactsApi.class);
                Observable<SoapGetContactsResponseEnvelope> lClientDetailsObservable =
                        contactsApi.getContacts2(soapGetContactsRequestEnvelope);


                Observable<SoapGetFormerVisitorsResponseEnvelope> contactsObservable =
                        contactsApi.getFormerVisitors(soapGetFormerVisitorsRequestEnvelope);

                try {

                    if (skipcontact) {
                        lClientDetailsObservable.
                                subscribeOn(Schedulers.io())
                                .map(this::soapContacts)
                                .map(this::addContacts)
                                .flatMap(contacts -> contactsObservable
                                        .observeOn(Schedulers.io()))
                                .map(this::soapVisitors)
                                .subscribe(this::addVisitors, this::onError);

                    } else
                        lClientDetailsObservable.
                                subscribeOn(Schedulers.io())
                                .map(this::soapContacts)
                                .map(this::addContacts)
                                .map(this::getPhoneContacts)
                                .flatMap(contacts -> contactsObservable
                                        .observeOn(Schedulers.io()))
                                .map(this::soapVisitors)
                                .subscribe(this::addVisitors, this::onError);
                } catch (Exception e) {
                    IpAddress.e(IpAddress.SERVICE, e.toString());
                }
            } else {
                IpAddress.e(IpAddress.SERVICE, "size" + allContactList.size());
            }
        }
    }

    private Contacts soapContacts(SoapGetContactsResponseEnvelope soapGetContactsResponseEnvelope) {


        Contacts contacts = soapGetContactsResponseEnvelope.getBody().getGetContactsResponse()
                .getGetContactsResult().getContacts();
        if (contacts == null)
            contacts = new Contacts();
        return contacts;
    }

    private Visitors soapVisitors(SoapGetFormerVisitorsResponseEnvelope soapGetFormerVisitorsResponseEnvelope) {


        Visitors contacts = soapGetFormerVisitorsResponseEnvelope.getBody().getGetFormerVisitorsResponse()
                .getGetFormerVisitorsResult().getVisitors();

        if (contacts == null) {
            contacts = new Visitors();
        }


        return contacts;
    }

    private void onError(Throwable throwable) {

        IpAddress.e(IpAddress.SERVICE, throwable.toString());
        stopSelf();

    }

//    private void setContactList(List<Contact> allContactList1) {
//
//        Collections.sort(allContactList1);
//        for (Contact contact : allContactList1) {
//
//            Contact contact1 = new Contact();
//            if (Character.isLetter(contact.getName().trim().charAt(0))) {
//                contact1.setName(contact.getName().trim().substring(0, 1));
//                if (contactsList.contains(contact1)) {
//
//                } else
//                    contactsList.add(contact1);
//            }
//            contactsList.add(contact);
//        }
//    }


    private void addVisitors(Visitors visitors) {

        Constants.e("size", "3");
        if (visitors != null && visitors.getVisitor() != null &&
                visitors.getVisitor().size() > 0) {

            if (isPatient()) {
                if (allContactList != null)
                    allContactList.clear();
            }

            visContactList1 = new ArrayList<>();
            for (Visitor visitor : visitors.getVisitor()) {
                Contact contacts = new Contact();

                contacts.setMobile(visitor.getMobile());
                contacts.setCompany(visitor.getCompanyName());
                contacts.setName(visitor.getName());
                contacts.setEmail(visitor.getEMail());
                contacts.setType(visitor.getType());

                if (visContactList1 != null) {
                    if (visContactList1.contains(contacts)) {

                    } else
                        visContactList1.add(contacts);
                }

                if (allContactList.contains(contacts)) {

                } else {
                    allContactList.add(contacts);
                }

            }

        }
        stopSelf();


    }


    @Override
    protected void onHandleWork(@NonNull Intent intent) {


        if (getApp().isReached()) {
            setRetrofit(getApp().getTcpIp());

            this.intent = intent;

            getClient();
        }
    }

    public Contacts getPhoneContacts(Contacts contacts) {
        try {
            Constants.e("size", "2");
            Cursor phones = getContentResolver().query(PHONEURI,
                    null, null, null, null);

            if (phones != null) {

                phoneContactList1 = new ArrayList<>();
                while (phones.moveToNext()) {

                    Contact contact = new Contact();

                    contact.setType("My Contact");
                    contact.setName(phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds
                            .Phone.DISPLAY_NAME)));
                    contact.setMobile(phones.getString(
                            phones.getColumnIndex(
                                    ContactsContract.CommonDataKinds.Phone.NUMBER)));

                    if (phoneContactList1.contains(contact)) {

                    } else
                        phoneContactList1.add(contact);
                    if (allContactList.contains(contact)) {

                    } else
                        allContactList.add(contact);

                }
                phones.close();
            }
        } catch (Exception e) {

            Constants.e("err", e.toString());
        }

        return contacts;
    }

    private Contacts addContacts(Contacts contacts) {

        Constants.e("size", "1");

        if (contacts != null && contacts.getContact() != null && contacts.getContact().size() > 0) {


            companyContactList1 = new ArrayList<>();
            for (Contact c : contacts.getContact()) {
                c.setType("Company Staff");
                if (companyContactList1.contains(c)) {

                } else
                    companyContactList1.add(c);

                if (allContactList.contains(c)) {

                } else
                    allContactList.add(c);
            }
        }


        return contacts;
    }
}
