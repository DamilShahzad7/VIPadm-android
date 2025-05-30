package vip.com.vipmeetings.qrcode;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.MarshalDate;
import org.ksoap2.serialization.MarshalFloat;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpResponseException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import androidx.percentlayout.widget.PercentRelativeLayout;
import vip.com.vipmeetings.BaseActivity;
import vip.com.vipmeetings.R;

public class QRMainActivity extends BaseActivity {
    TextView next;
    EditText editText;
    private final String SOAP_ACTION = "http://tempuri.org/ValidateUser";
    private final String METHOD_NAME = "ValidateUser";
    private final String NAMESPACE = "http://tempuri.org/";
    // private final String URL = "http://52.74.92.30/Wsvipcloud/vipaccess.asmx?clientid=";
    String email = null;
    String xml = null;
    private TextView vipaccess, bltext, website;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    boolean test;
    ConnectionDetector cd;
    // Session Manager Class
    SessionManager session;
    String firstname = null,
            familyname = null,
            fullname = null,
            company = null,
            mobile = null,
            email1 = null,
            empcode = null,
            barcode = null, qrcode = null, securitycode = null, country = null, city = null, contactphotourl = null, companylogourl = null,checkIn=null,checkOut=null;
    private boolean isInternetPresent = false;
    Typeface custom_font1, custom_font;

    ProgressDialog loading = null;
    EditText etclient;
    String client = "";
    PercentRelativeLayout mPercentRelativeLayout;

    //client id remove url

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainone);
        etclient = (EditText) this.findViewById(R.id.etclient);
        mPercentRelativeLayout = (PercentRelativeLayout) findViewById(R.id.root);

        mPercentRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
            }
        });
        loading = new ProgressDialog(this);
        loading.setCancelable(true);
        loading.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        loading.setMessage("Please wait..");
        next = (TextView) findViewById(R.id.next);
        vipaccess = (TextView) findViewById(R.id.vipaccess);
        bltext = (TextView) findViewById(R.id.bltext);
        website = (TextView) findViewById(R.id.website);
        // Session Manager
        session = new SessionManager(getApplicationContext());
        editText = (EditText) findViewById(R.id.enteredit);
        editText.setPadding(0, 0, 0, 0);
        // custom_font = Typeface.createFromAsset(getAssets(), "fonts/Noteworthy-Bold.otf");

        vipaccess.setTypeface(custom_font);
        // custom_font1 = Typeface.createFromAsset(getAssets(), "fonts/ufonts.com_lucida-grande.ttf");

//        bltext.setTypeface(custom_font1);
//        next.setTypeface(custom_font1);
//        website.setTypeface(custom_font1);
//        editText.setTypeface(custom_font1);
        sharedpreferences = getSharedPreferences("DETAILS",
                Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = editText.getText().toString().trim();
                client = etclient.getText().toString().trim();
                cd = new ConnectionDetector(getApplicationContext());
                // get Internet status
                isInternetPresent = cd.isConnectingToInternet();
                if (!isInternetPresent) {
                    Toast.makeText(QRMainActivity.this, "Please check the network", Toast.LENGTH_SHORT).show();
                } else {

                    if (etclient.getText().toString().isEmpty()) {
                        Toast.makeText(QRMainActivity.this, "Please enter the client-ID", Toast.LENGTH_SHORT).show();
                    } else if (email.equalsIgnoreCase("")) {

                        Toast.makeText(QRMainActivity.this, "Please enter the mail", Toast.LENGTH_SHORT).show();
                    } else {


                        try {
                            next.setClickable(false);
                            //   URL u = new URL(url + host + URL);
                            myAsyncTask myRequest = new myAsyncTask();
                            myRequest.execute();

                        } catch (Exception e) {

                            // Toast.makeText(MainActivity.this, "Please enter the correct url", Toast.LENGTH_SHORT).show();
                        }

                    }
                }
            }
        });

    }


    private class myAsyncTask extends AsyncTask<Void, Void, String> {


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            loading.dismiss();
            next.setClickable(true);
            if (result == null) {
                Toast.makeText(QRMainActivity.this, "Some error occured,Please try again later", Toast.LENGTH_SHORT).show();
            } else {
                Log.d("responseinamain", "" + result);


                try {
                    // getting XML
                    Document doc = getDomElement(result);
                    // NodeList nl = doc.getElementsByTagName("Contact");

                    test = false;
                    NodeList nl = doc.getElementsByTagName("Employee");
                    Log.d("statusnl", "" + doc.getTextContent());

                    for (int i = 0; i < nl.getLength(); i++) {
                        // creating new HashMap
                        test = true;
                        Log.d("forloop", "forloop" + test);
                        Element e = (Element) nl.item(i);
                        // adding each child node to HashMap key => value
                        firstname = getValue(e, "FirstName");
                        familyname = getValue(e, "FamilyName");
                        fullname = getValue(e, "FullName");
                        company = getValue(e, "Company");
                        mobile = getValue(e, "Mobile");
                        email1 = getValue(e, "Email");
                        empcode = getValue(e, "Empcode");
                        barcode = getValue(e, "Barcode");
                        qrcode = getValue(e, "QRCode");
                        securitycode = getValue(e, "SecurityCode");
                        country = getValue(e, "Country");
                        city = getValue(e, "City");
                        contactphotourl = getValue(e, "EmployeePhotoUrl");
                        companylogourl = getValue(e, "CompanyLogoUrl");

                        checkIn = getValue(e, "INTIME");
                        checkOut = getValue(e, "OUTTIME");
                        // adding HashList to ArrayList


                        session.createLoginSession(fullname, email1);

                        Intent in = new Intent(QRMainActivity.this, QRCode_generate.class);
                        startActivity(in);
                        editor.putString("qrcode", qrcode);
                        editor.putString("contactphotourl", contactphotourl);
                        editor.putString("companylogourl", companylogourl);
                        editor.putString("fullname", fullname);
                        editor.putString("checkin", checkIn);
                        editor.putString("checkout", checkOut);
                        editor.commit();
                        finish();
                    }

                } catch (Exception e) {

                }
                if (!test) {
                    String message = null;
                    if (!result.equalsIgnoreCase("url"))
                        message = "Invalid E-mail ID";
                    else if (result.equalsIgnoreCase("url")) {
                        message = "Invalid URL";
                    }
                    final Dialog dialog = new Dialog(QRMainActivity.this);
                    // Include dialog.xml file
                    View v = getLayoutInflater().inflate(R.layout.dialog, null);
                    dialog.setContentView(v);

                    TextView tvmessage = (TextView) v.findViewById(R.id.tvmessage);

                    if (message != null)
                        tvmessage.setText(message);
                    dialog.show();

                    TextView declineButton = (TextView) dialog.findViewById(R.id.declineButton);
                    // if decline button is clicked, close the custom dialog
                    declineButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // Close dialog
                            dialog.dismiss();
                            next.setClickable(true);
                        }
                    });

                }

            }

        }

        public String getValue(Element item, String str) {
            NodeList n = item.getElementsByTagName(str);
            return this.getElementValue(n.item(0));
        }

        public final String getElementValue(Node elem) {
            Node child;
            if (elem != null) {
                if (elem.hasChildNodes()) {
                    for (child = elem.getFirstChild(); child != null; child = child.getNextSibling()) {
                        if (child.getNodeType() == Node.TEXT_NODE) {
                            // Log.d("status", "" + child.getNodeValue());
                            return child.getNodeValue();
                        }
                    }
                }
            }
            return "";
        }

        public Document getDomElement(String xml) {
            Document doc = null;
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            try {

                DocumentBuilder db = dbf.newDocumentBuilder();

                InputSource is = new InputSource();
                is.setCharacterStream(new StringReader(xml));
                doc = db.parse(is);

            } catch (ParserConfigurationException e) {
                Log.e("Error: ", e.getMessage());
                return null;
            } catch (SAXException e) {
                Log.e("Error: ", e.getMessage());
                return null;
            } catch (IOException e) {
                Log.e("Error: ", e.getMessage());
                return null;
            }
            // return DOM
            return doc;
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading.show();


        }

        @Override
        protected String doInBackground(Void... arg0) {

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.setOutputSoapObject(request);
            new MarshalDate().register(envelope);
            new MarshalFloat().register(envelope);
            envelope.setOutputSoapObject(request);
            envelope.dotNet = true;
            if (METHOD_NAME == "ValidateEmployee") {
                PropertyInfo login = new PropertyInfo();
                login.type = PropertyInfo.STRING_CLASS;
                login.setName("A_sEmailId");
                login.setValue(email);//admin@vipadm.com
                request.addProperty(login);


            }


            HttpTransportSE httpTransport = new HttpTransportSE(getApp().getTcpIp() + client);
            httpTransport.debug = true;
            try {
                httpTransport.call(SOAP_ACTION, envelope);

            } catch (HttpResponseException e) {

                return "url";
            } catch (IOException e) {

                return "url";

            } catch (XmlPullParserException e) {

                return "url";
            } //send request
            try {
                //error_code500=httpTransport.getServiceConnection().getResponseCode();
                httpTransport.call(SOAP_ACTION, envelope);
                xml = httpTransport.responseDump;
            } catch (Exception e) {
                return "url";
            }
            return xml;
        }
    }


}
