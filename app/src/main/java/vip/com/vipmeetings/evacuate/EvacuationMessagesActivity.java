package vip.com.vipmeetings.evacuate;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.TextView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import vip.com.vipmeetings.R;
import vip.com.vipmeetings.body.GetMessagesRequestBody;
import vip.com.vipmeetings.body.GetMessagestResponseBody;
import vip.com.vipmeetings.body.SendPushRequestBody;
import vip.com.vipmeetings.body.SendPushResponseBody;
import vip.com.vipmeetings.body.UpdateMessageReadStatusRequestBody;
import vip.com.vipmeetings.body.UpdateMessageReadStatusResponseBody;
import vip.com.vipmeetings.envelope.SoapGetMessagesRequestEnvelope;
import vip.com.vipmeetings.envelope.SoapGetMessagesResponseEnvelope;
import vip.com.vipmeetings.envelope.SoapSendPushRequestEnvelope;
import vip.com.vipmeetings.envelope.SoapSendPushResponseEnvelope;
import vip.com.vipmeetings.envelope.SoapUpdateMessageReadStatusRequestEnvelope;
import vip.com.vipmeetings.envelope.SoapUpdateMessageReadStatusResponseEnvelope;
import vip.com.vipmeetings.models.Message;
import vip.com.vipmeetings.models.Messages;
import vip.com.vipmeetings.request.GetMessages;
import vip.com.vipmeetings.request.SendPushMessage;
import vip.com.vipmeetings.request.UpdateMessageReadStatus;
import vip.com.vipmeetings.utilities.Constants;

/**
 * Created by Srinath on 11/02/18.
 */

public class EvacuationMessagesActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {


    @BindView(R.id.rvmessages)
    RecyclerView rvmessages;

    @BindView(R.id.etmessage)
    AppCompatEditText etmessage;

    @BindView(R.id.ivback)
    ImageView ivback;

    @BindView(R.id.tvname)
    TextView tvname;


    @BindView(R.id.tvdate)
    TextView tvdate;


    @BindView(R.id.tvsend)
    TextView tvsend;

    List<Message> messageList;
    MessageAdapter adapter;
    private String msgId = "";
    private Message message;

    @BindView(R.id.swipe)
    SwipeRefreshLayout swipeRefreshLayout;
    private String empty;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evacuatemessage);
        ButterKnife.bind(this);


        tvname.setText(getIntent().getStringExtra(IpAddress.EVACUATIONMESSAGE));
        messageList = new ArrayList<>();
        adapter = new MessageAdapter();
        rvmessages.setLayoutManager(new LinearLayoutManager(this));
        rvmessages.setAdapter(adapter);

        etmessage.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (adapter != null && messageList.size() > 1) {
                    rvmessages.smoothScrollToPosition(adapter.getItemCount() - 1);
                }
            }
        });


        etmessage.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {


                if (i == EditorInfo.IME_ACTION_DONE) {
                    if (!etmessage.getText().toString().trim().isEmpty())
                        sendReplyMessage(etmessage.getText().toString().trim());
                }
                return false;
            }
        });


        swipeRefreshLayout.setOnRefreshListener(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        regis(this);
    }

    @Override
    protected void onStop() {
        unregis(this);
        super.onStop();
    }


    @OnClick(R.id.clhide)
    public void onHide(View v) {


        hideKeyboard(getCurrentFocus());
    }


    @OnClick(R.id.tvsend)
    public void onSend(View v) {


        if (!etmessage.getText().toString().trim().isEmpty()) {
            hideKeyboard(etmessage);
            sendReplyMessage(etmessage.getText().toString().trim());
        }

    }

    public void refreshScreen() {

        deleteStiky(this);
        readMessage();
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onEva(String update) {

        refreshScreen();

    }


    private void sendReplyMessage(String trim) {

        if (evacuationResponsible.getUserType() != null) {

            String title = "";
            String msgid = "";
            String appName1 = null;
            String appname2 = null;
            showPD(this);
            SoapSendPushRequestEnvelope soapSendPushRequestEnvelope = new SoapSendPushRequestEnvelope();
            SendPushRequestBody sendPushRequestBody = new SendPushRequestBody();
            SendPushMessage sendPushMessage = new SendPushMessage();
            if (evacuationResponsible.getUserType().equalsIgnoreCase(IpAddress.ADMINUSER)) {

                title = "You have a message from reception";
                appName1 = IpAddress.VIPRECEPTION.toUpperCase();
                appname2 = IpAddress.VIPadm.toUpperCase();
                msgid = msgId;
                sendPushMessage.setA_sEvacuationId(getIntent().getStringExtra(IpAddress.EVACUATIONID));
            } else {

                if (evacuationResponsible.getCompany() != null
                        && !evacuationResponsible.getCompany().isEmpty()
                        && evacuationResponsible.getAreaName() != null
                        && evacuationResponsible.getFloor() != null) {
                    title = "Message from:" + evacuationResponsible.getCompany() + "," +
                            evacuationResponsible.getAreaName() + "," +
                            evacuationResponsible.getFloor();
                } else if (evacuationResponsible.getAreaName() != null
                        && evacuationResponsible.getFloor() != null) {
                    title = "Message from:" +
                            evacuationResponsible.getAreaName() + "," +
                            evacuationResponsible.getFloor();
                }

                appName1 = IpAddress.VIPadm.toUpperCase();
                appname2 = IpAddress.VIPRECEPTION.toUpperCase();
                msgid = "";
                sendPushMessage.setA_sEvacuationId(getIntent().getStringExtra(IpAddress.EVACUATIONID));
                // sendPushMessage.setA_sEvacuationId("");
            }


            if (title != null
                    && appName1 != null
                    && appname2 != null
                    && msgid != null) {


                sendPushMessage.setA_sClientId(getClientID());

                try {
                    // sendPushMessage.setA_sPlaceId(getStoreCitySelect().getPlaceID());
                    sendPushMessage.setA_sPlaceId(evacuationResponsible.getA_sPlaceId());
                } catch (Exception e) {

                    sendPushMessage.setA_sPlaceId(evacuationResponsible.getA_sPlaceId());
                }

                sendPushMessage.setA_sMessage(trim);
                sendPushMessage.setA_sTitle(title);
                if (msgid.isEmpty()) {
                    sendPushMessage.setA_sBarcode(getBarcode());
                } else
                    sendPushMessage.setA_sBarcode(message.getBarcode());

                sendPushMessage.setA_sReplyToMessageId(msgid);

                sendPushMessage.setA_sSendByApp(appName1);
                sendPushMessage.setA_sReceivedByApp(appname2);

                sendPushMessage.setA_sSenderId(getBarcode());

                sendPushMessage.setAuthToken(getAuthTokenPref_Evacuation());

                sendPushRequestBody.setSendPushMessage(sendPushMessage);

                soapSendPushRequestEnvelope.setBody(sendPushRequestBody);

                Observable<SoapSendPushResponseEnvelope> lClientDetailsObservable =
                        service.saveReceptionEvacuationMessage(soapSendPushRequestEnvelope);

                lClientDetailsObservable.
                        subscribeOn(Schedulers.io())
                        .map(this::onSoapPush)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(this::onSendMessage, this::onError);
            }
        }

    }

    private String onSoapPush(SoapSendPushResponseEnvelope soapSendPushResponseEnvelope) {


        SendPushResponseBody sendPushResponseBody = soapSendPushResponseEnvelope.getBody();

        return sendPushResponseBody.getSendPushMessageResponse().getSendPushMessage();
    }

    private void onSendMessage(String status) {

        if (status != null) {
            if (status.equalsIgnoreCase(IpAddress.SUCCESS)) {

                etmessage.getText().clear();
                readMessage();
            } else {
                hidePD();
            }

        } else {
            hidePD();
            showToast(getString(R.string.someerror));
        }

    }


    @OnClick(R.id.ivback)
    public void onBack(View v) {

        setResult(RESULT_OK);
        finish();
    }

    private void readMessage() {

        if (evacuationResponsible != null) {

            if (evacuationResponsible.getUserType() != null) {
                if ((evacuationResponsible.getUserType().equalsIgnoreCase(IpAddress.ADMINUSER))) {

                    getMessages(getClientID(),
                            evacuationResponsible.getA_sPlaceId(),
                            getIntent().getStringExtra(IpAddress.EVACUATIONID), true);

                } else {

                    getMessages(getClientID(),
                            evacuationResponsible.getA_sPlaceId(),
                            getIntent().getStringExtra(IpAddress.EVACUATIONID), false);

                }
            } else {
                IpAddress.e("error", mGson.toJson(evacuationResponsible));
            }
        }


    }

    private void getMessages(String clientid, String placeID, String evaid, boolean isUSer) {
        if (getIntent().hasExtra(IpAddress.EVACUATIONID) && !isUSer) {
            evaid = getIntent().getStringExtra(IpAddress.EVACUATIONID);
        } else {

        }
        if (pushService != null) {
            pushService.getMessages(clientid, placeID, evaid, isUSer);
            hidePD();
        } else {

            showPD(this);
            SoapGetMessagesRequestEnvelope
                    soapGetMessagesRequestEnvelope = new SoapGetMessagesRequestEnvelope();
            GetMessagesRequestBody getMessagesRequestBody = new GetMessagesRequestBody();
            GetMessages getMessages = new GetMessages();

            getMessages.setA_sClientId(clientid);
            getMessages.setA_sEvacuationId(evaid);
            getMessages.setA_sPlaceId(placeID);
            getMessages.setAuthToken(getAuthTokenPref_Evacuation());

            getMessagesRequestBody.setGetMessages(getMessages);
            soapGetMessagesRequestEnvelope.setBody(getMessagesRequestBody);

            Observable<SoapGetMessagesResponseEnvelope> lClientDetailsObservable =
                    service.getResponsibleEvacuationMessages(soapGetMessagesRequestEnvelope);

            lClientDetailsObservable.
                    subscribeOn(Schedulers.io())
                    .map(this::onSoapGetMessages)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::onMessages, this::onMessageError);
        }
    }


    private List<Message> onSoapGetMessages(SoapGetMessagesResponseEnvelope soapGetMessagesResponseEnvelope) {

        messageList.clear();
        GetMessagestResponseBody getMessagestResponseBody = soapGetMessagesResponseEnvelope.getBody();
        if (getMessagestResponseBody.getGetMessagesResponse().getGetMessagesResult().
                getEmpty() == null) {

            Messages messages = getMessagestResponseBody
                    .getGetMessagesResponse().getGetMessagesResult().getMessages();

            if (messages != null && messages.getMessage() != null) {
                Message message1 = null;
                for (Message message : messages.getMessage()) {

                    if (message1 == null) {
                        message1 = new Message();
                        message1.setDate(message.getDate());
                        message1.setDate(true);
                        messageList.add(message1);
                    } else if (!Constants.formatDateFromMessage(message.getDate().trim()).equalsIgnoreCase(
                            Constants.formatDateFromMessage(message1.getDate().trim()))) {

                        message1 = new Message();
                        message1.setDate(message.getDate());
                        message1.setDate(true);
                        messageList.add(message1);
                    }
                    message.setDate(false);
                    messageList.add(message);
                }

                // messageList.addAll(messages.getMessage());
            }
            empty = null;
            return messageList;
        } else {
            empty = getMessagestResponseBody.getGetMessagesResponse().
                    getGetMessagesResult().getEmpty().get_empty();

        }


        return messageList;
    }

    private void onMessageError(Throwable throwable) {

        hidePD();
        IpAddress.e("error", throwable.toString());
        empty = null;
        if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN, sticky = false)
    public void onMessages(List<Message> messages) {

        messageList = messages;
        if (messages != null && messageList.size() > 0) {
            msgId = messageList.get(messageList.size() - 1).getMessageId();
        }
        if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }

        if (messages != null && messages.size() > 0) {

            updateMessage(messages.get(messages.size() - 1).getEvacuationId());
            tvdate.setText(Constants.formatDateFromMessage2(messages.get(0).getDate().trim()));
        } else {
            tvdate.setText("");
        }
        adapter.notifyDataSetChanged();
        rvmessages.smoothScrollToPosition(adapter.getItemCount() - 1);
        hidePD();
    }

    @Override
    public void onBackPressed() {


        setResult(RESULT_OK);
        finish();
        // super.onBackPressed();
    }

    @Override
    public void onRefresh() {

        readMessage();
    }


    public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.VH> {


        @Override
        public int getItemViewType(int position) {


//            if (messageList.get(position).isDate()) {
//
//                return 0;
//            } else if (messageList.get(position).getSendByApp().equalsIgnoreCase(IpAddress.VIPadm)) {
//                return 1;
//            } else if (messageList.get(position).getSendByApp().equalsIgnoreCase(IpAddress.VIPRECEPTION)) {
//                return 2;
//            }


            if (messageList.get(position).isDate()) {

                return 0;
            } else if (messageList.get(position).getSendByApp().equalsIgnoreCase(IpAddress.VIPRECEPTION)) {
                return 2;
            } else if (messageList.get(position).getBarcode().equalsIgnoreCase(getBarcode())) {
                return 1;
            } else if (messageList.get(position).getSendByApp().equalsIgnoreCase(IpAddress.VIPadm)) {
                return 1;
            }

            return -1;

        }

        @Override
        public VH onCreateViewHolder(ViewGroup parent, int viewType) {

            if (viewType == 1) {
                return new VH(getLayoutInflater().inflate(R.layout.inflate_messages, parent, false));

            } else if (viewType == 2) {

                return new VH(getLayoutInflater().inflate(R.layout.inflate_messagesright, parent, false));
            } else
                return new VH(getLayoutInflater().inflate(R.layout.inflate_messagedate, parent, false));

        }

        @Override
        public void onBindViewHolder(VH holder, int position) {


            message = messageList.get(position);

            if (holder.getItemViewType() == 0) {

                try {
                    holder.tvdate.setText(Constants.formatDateFromMessage2(message.getDate().trim()));
                } catch (Exception e) {

                }
            } else {
                msgId = message.getMessageId();
                if (message.getSendByApp().equalsIgnoreCase(IpAddress.VIPadm)) {
                    holder.tvdate.setText("By " + message.getSenderName() + ", " +
                            Constants.formatDateFromMessage2Time(message.getDate()));
                } else {
                    holder.tvdate.setText("By " + message.getSenderName()
                            + ", " + Constants.formatDateFromMessage2Time(message.getDate()));
                }

                if (message.getMessage() != null)
                    holder.tvmessage.setText(message.getMessage().trim());
                else

                    holder.tvmessage.setText("");


                if (message.getSendByApp().trim().equalsIgnoreCase("VIPADM")) {


                    holder.tvmessage.setGravity(Gravity.START);
                    holder.tvdate.setGravity(Gravity.START);

                    holder.tvmessage.setBackgroundResource(R.drawable.rect_message_grey);

                } else if (message.getSendByApp().equalsIgnoreCase("VIPRECEPTION")) {

                    holder.tvmessage.setGravity(Gravity.START);
                    holder.tvdate.setGravity(Gravity.END);
                    holder.tvmessage.setBackgroundResource(R.drawable.rect_message_admin);

                }
                try {
                    tvdate.setText(Constants.formatDateFromMessage2(message.getDate().trim()));
                } catch (Exception e) {

                }
            }

        }

        @Override
        public int getItemCount() {
            return messageList.size();
        }

        public class VH extends RecyclerView.ViewHolder {


            @BindView(R.id.tvdate)
            TextView tvdate;
            @BindView(R.id.tvmessage)
            TextView tvmessage;

            @BindView(R.id.clrow)
            ConstraintLayout clrow;

            public VH(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }

    }

    private void updateMessage(String evaId) {

        String A_sAppname = null;
        if (evacuationResponsible != null) {

            if (evacuationResponsible.getUserType() != null) {
                if (evacuationResponsible.getUserType().equalsIgnoreCase(IpAddress.ADMINUSER)) {

                    A_sAppname = IpAddress.VIPRECEPTION;
                } else {

                    A_sAppname = IpAddress.VIPadm;

                }
            } else {
                A_sAppname = IpAddress.VIPadm;
            }
        }

        if (A_sAppname != null) {

            // showPD(this);
            SoapUpdateMessageReadStatusRequestEnvelope soapUpdateMessageReadStatusRequestEnvelope = new
                    SoapUpdateMessageReadStatusRequestEnvelope();

            UpdateMessageReadStatusRequestBody updateMessageReadStatusRequestBody = new UpdateMessageReadStatusRequestBody();

            UpdateMessageReadStatus updateMessageReadStatus = new UpdateMessageReadStatus();
            updateMessageReadStatus.setA_sAppName(A_sAppname);

            updateMessageReadStatus.setAuthToken(getAuthTokenPref_Evacuation());

            updateMessageReadStatus.setA_sBarcode(getBarcode());
            updateMessageReadStatus.setA_sClientId(getClientID());

            updateMessageReadStatus.setA_sEvacuationId(evaId);

            updateMessageReadStatusRequestBody.setUpdateMessageReadStatus(updateMessageReadStatus);
            soapUpdateMessageReadStatusRequestEnvelope.setBody(updateMessageReadStatusRequestBody);


            Observable<SoapUpdateMessageReadStatusResponseEnvelope> lClientDetailsObservable =
                    service.updateMessageReadStatus(soapUpdateMessageReadStatusRequestEnvelope);

            lClientDetailsObservable.
                    subscribeOn(Schedulers.io())
                    .map(this::onSoapUpdateMessageReadStatus)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::onUpdate, this::onUpdateError);
        }

    }

    private String onSoapUpdateMessageReadStatus(SoapUpdateMessageReadStatusResponseEnvelope
                                                         soapUpdateMessageReadStatusResponseEnvelope) {

        UpdateMessageReadStatusResponseBody updateMessageReadStatusResponseBody =
                soapUpdateMessageReadStatusResponseEnvelope.getBody();

        return updateMessageReadStatusResponseBody.getUpdateMessageReadStatusResponse().getUpdateMessageReadStatusResult();
    }

    private void onUpdateError(Throwable throwable) {

        hidePD();

    }

    private void onUpdate(String status) {

        hidePD();
    }
}
