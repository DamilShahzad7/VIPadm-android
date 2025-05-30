package vip.com.vipmeetings.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import android.telephony.SmsMessage;
import android.util.Log;

import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.Status;

import org.greenrobot.eventbus.EventBus;

import vip.com.vipmeetings.evacuate.IpAddress;

public class SMSBroadCastReceiver extends BroadcastReceiver {


    public static final String SMS_BUNDLE = "pdus";
    SmsMessage smsMessage;
    Context context;

    public SMSBroadCastReceiver() {

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (SmsRetriever.SMS_RETRIEVED_ACTION.equals(intent.getAction())) {
            Bundle extras = intent.getExtras();
            Status status = (Status) extras.get(SmsRetriever.EXTRA_STATUS);

            switch(status.getStatusCode()) {
                case CommonStatusCodes.SUCCESS:
                    // Get SMS message contents
                    String message = (String) extras.get(SmsRetriever.EXTRA_SMS_MESSAGE);
                    // Extract one-time code from the message and complete verification
                    // by sending the code back to your server.

                    IpAddress.e("message ",message);
                    EventBus.getDefault().postSticky(message);

                    break;
                case CommonStatusCodes.TIMEOUT:
                    // Waiting for SMS timed out (5 minutes)
                    // Handle the error ...
                    break;
            }
        }
    }



//    public void onReceive(Context context, Intent intent) {
//        IpAddress.e("SMS", "SMS SUCCESS");
//        this.context = context;
//        Bundle intentExtras = intent.getExtras();
//        if (intentExtras != null) {
//            Object[] sms = (Object[]) intentExtras.get(SMS_BUNDLE);
//            String smsMessageStr = "";
//            for (int i = 0; i < sms.length; ++i) {
//                smsMessage = SmsMessage.createFromPdu((byte[]) sms[i]);
//
//                String smsBody = smsMessage.getMessageBody();
//                Log.e("smsbody", smsBody);
//                String address = smsMessage.getOriginatingAddress();
//                smsMessageStr += "SMS From: " + address + "\n";
//                smsMessageStr += smsBody + "\n";
//            }
//            Log.e("getDisplayMessageBody", smsMessage.getDisplayMessageBody());//actual sms
//            Log.e("DisplayOriginating", smsMessage.getDisplayOriginatingAddress());
//            Log.e("getEmailBody", smsMessage.getEmailBody() + "");
//            Log.e("s", smsMessage.getEmailFrom() + "");
//            Log.e("getEmailFrom", smsMessage.getMessageClass().name() + "");
//            Log.e("getOriginatingAddress", smsMessage.getOriginatingAddress() + "");
//            Log.e("getPseudoSubject", smsMessage.getPseudoSubject() + "");
//            Log.e("getServiceCenterAddress", smsMessage.getServiceCenterAddress() + "");
//            Log.e("sms", smsMessageStr + "");
//
//            EventBus.getDefault().postSticky(smsMessage.getDisplayMessageBody());
//        } else {
//            IpAddress.e("SMS", "SMS null");
//        }
//    }
//

}