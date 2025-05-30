package vip.com.vipmeetings.evacuate;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import vip.com.vipmeetings.R;
import vip.com.vipmeetings.body.GetMessagesRequestBody;
import vip.com.vipmeetings.body.GetMessagestResponseBody;
import vip.com.vipmeetings.body.UpdateMessageReadStatusRequestBody;
import vip.com.vipmeetings.body.UpdateMessageReadStatusResponseBody;
import vip.com.vipmeetings.envelope.SoapGetMessagesRequestEnvelope;
import vip.com.vipmeetings.envelope.SoapGetMessagesResponseEnvelope;
import vip.com.vipmeetings.envelope.SoapUpdateMessageReadStatusRequestEnvelope;
import vip.com.vipmeetings.envelope.SoapUpdateMessageReadStatusResponseEnvelope;
import vip.com.vipmeetings.models.Evacuation;
import vip.com.vipmeetings.models.EvacuationResponsible;
import vip.com.vipmeetings.models.Message;
import vip.com.vipmeetings.models.Messages;
import vip.com.vipmeetings.request.GetMessages;
import vip.com.vipmeetings.request.UpdateMessageReadStatus;
import vip.com.vipmeetings.utilities.Constants;

/**
 * Created by Srinath on 07/03/18.
 */

public class MessageSingleActivity extends BaseActivity {


    @BindView(R.id.ivplus)
    ImageView ivplus;

    @BindView(R.id.etsearch)
    EditText etsearch;

    List<Message> messageList, messageListOLD;
    @BindView(R.id.rvmessages)
    RecyclerView rvmessages;
    @BindView(R.id.tvmessagehistory)
    TextView tvmessagehistory;
    @BindView(R.id.ivback)
    ImageView ivback;
    MessageSingleAdapter messageSingleAdapter;
    SimpleDateFormat simpleDateFormat;
    PopupMenu popupMenu;
    HashMap<String, Evacuation>
            hashmap;


    HashMap<String, EvacuationResponsible>
            stringEvacuationResponsibleHashMap;
    private Message message_selected;
    Context wrapper;
    Type type, typeEva;
    private String empty;
    List<Message> messageList2;
    HashMap<String, Message> mapMessage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singlemessage);
        ButterKnife.bind(this);
        try {
            wrapper = new ContextThemeWrapper(getApplicationContext(), R.style.MyPopupMenu);
            popupMenu = new PopupMenu(wrapper, ivplus, Gravity.BOTTOM);
        } catch (Exception e) {

        }


        mapMessage = new HashMap<>();
        simpleDateFormat = new SimpleDateFormat(IpAddress.DATEMESSAGE);
        messageList = new ArrayList<>();
        messageList2 = new ArrayList<>();
        messageListOLD = new ArrayList<>();
        messageSingleAdapter = new MessageSingleAdapter();
        rvmessages.setAdapter(messageSingleAdapter);


        rvmessages.addItemDecoration(new SeparatorDecoration(this,
                ContextCompat.getColor(this, R.color.md_grey_300), 1f, 55, 50));


        etsearch.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (hasFocus) {
                    // rvmessages.scrollToPosition(messageList.size()-1);
                }
            }
        });
        etsearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {


            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (editable.length() == 0) {

                    if (messageListOLD.size() > 0) {
                        messageList.clear();
                        messageList.addAll(messageListOLD);
                        messageSingleAdapter.notifyDataSetChanged();

                    }
                    hideKeyboard(etsearch);
                }
            }
        });


        etsearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE) {
                    //do something
                    searchList(etsearch.getText().toString().trim());
                }

                return false;
            }
        });


        if (getIntent().hasExtra(IpAddress.AREALISTNORMAL)) {

            ivplus.setVisibility(View.VISIBLE);
            typeEva = new TypeToken<HashMap<String,
                    EvacuationResponsible>>() {
            }.getType();

            if (popupMenu != null) {

                stringEvacuationResponsibleHashMap = mGson.fromJson(
                        getIntent().getStringExtra(IpAddress.AREALISTNORMAL), typeEva);

                for (Object o : stringEvacuationResponsibleHashMap.entrySet()) {
                    Map.Entry pair = (Map.Entry) o;

                    if (pair != null && pair.getKey() != null)

                        popupMenu.getMenu().add(pair.getKey() + "," +
                                ((EvacuationResponsible) pair.getValue()).getCompany());
                }

                popupMenu.setOnMenuItemClickListener(item -> {


                    getMessagesByArea(item.getTitle().toString());
                    return false;
                });
            }

        } else if (getIntent().hasExtra(IpAddress.AREALIST)) {
            ivplus.setVisibility(View.VISIBLE);
            type = new TypeToken<HashMap<String,
                    Evacuation>>() {
            }.getType();

            if (popupMenu != null) {

                hashmap = mGson.fromJson(
                        getIntent().getStringExtra(IpAddress.AREALIST), type);

                for (Object o : hashmap.entrySet()) {
                    Map.Entry pair = (Map.Entry) o;
                    IpAddress.e((String) pair.getKey(), mGson.toJson((Evacuation) pair.getValue()) + "");

                    if (pair != null && pair.getKey() != null)
                        popupMenu.getMenu().add(pair.getKey() + "," +
                                ((Evacuation) pair.getValue()).getCompany());
                }

                popupMenu.setOnMenuItemClickListener(item -> {


                    getMessagesByArea(item.getTitle().toString());
                    return false;
                });
            }

        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onEva(String update) {

        onRefreshScreen();

    }


    private void onRefreshScreen() {


        deleteStiky(this);
        readMessages();
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

    private void searchList(String trim) {


        if (!trim.isEmpty()) {

            messageSingleAdapter.getFilter().filter(trim.trim());
        } else {


        }
    }

    private void readMessages() {


        getMessages(getClientID(),
                evacuationResponsible.getA_sPlaceId(),
                evacuationResponsible.getEvacuationId());
    }

    private void getMessagesByArea(String s) {


        hideSoft();
        IpAddress.e("area", s);
        String[] split = s.split(",");

        if (stringEvacuationResponsibleHashMap != null) {
            EvacuationResponsible evacuation = stringEvacuationResponsibleHashMap.get(split[0]);
            Intent intent = new Intent(MessageSingleActivity.this,
                    EvacuationMessagesActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra(IpAddress.EVACUATIONID, evacuation.getEvacuationId());
            intent.putExtra(IpAddress.EVACUATIONMESSAGE, evacuation.getAreaName() + "-" +
                    evacuation.getFloor());
            startActivityForResult(intent, Constants.EVACUATIONREFRESH);

        } else if (hashmap != null) {

            Evacuation evacuation = hashmap.get(split[0]);
            Intent intent = new Intent(MessageSingleActivity.this,
                    EvacuationMessagesActivity.class);
            intent.putExtra(IpAddress.BARCODE, evacuation.getBarcode());
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra(IpAddress.EVACUATIONID, evacuation.getEvacuationId());
            intent.putExtra(IpAddress.EVACUATIONMESSAGE, evacuation.getAreaName() + "-" +
                    evacuation.getFloor());
            startActivityForResult(intent, Constants.EVACUATIONREFRESH);

        }

    }


    @OnClick(R.id.ivplus)
    public void onPlus(View v) {

        if (popupMenu != null) {
            popupMenu.show();
        }
    }

    @OnClick(R.id.ivback)
    public void onBack(View v) {
        //onBackPressed();

        setResult(RESULT_OK);
        finish();
    }

    private void getMessages(String clientid, String placeID, String evaid) {


        showPD(this);
        if (evacuationResponsible.getUserType().equalsIgnoreCase("N")) {

            evaid = "";
        } else {
            evaid = "";
        }

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
                .map(this::onSingleMessage)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onMessages, this::onMessageError);
    }

    private void onMessageError(Throwable throwable) {

        hidePD();
        empty = null;
    }

    private Messages onSoapGetMessages(SoapGetMessagesResponseEnvelope soapGetMessagesResponseEnvelope) {


        GetMessagestResponseBody getMessagestResponseBody = soapGetMessagesResponseEnvelope.getBody();

        if (getMessagestResponseBody.getGetMessagesResponse().getGetMessagesResult().getEmpty() == null) {
            empty = null;
            return getMessagestResponseBody.getGetMessagesResponse().getGetMessagesResult().getMessages();
        } else {

            empty = getMessagestResponseBody.getGetMessagesResponse().getGetMessagesResult().getEmpty().get_empty();
            return new Messages();
        }

    }

    private List<Message> onSingleMessage(Messages messages) {


        messageList.clear();
        messageList2.clear();
        messageListOLD.clear();
        message_selected = null;
        mapMessage.clear();
        if (empty == null) {
            if (messages != null && messages.getMessage() != null) {

                List<Message> messageList3 = messages.getMessage();

                if (messageList3 != null && messageList3.size() > 0) {
                    Collections.sort(messageList3, (m1, m2) ->
                            m1.getDateFormat().compareTo(m2.getDateFormat()));


                    for (Message message : messageList3) {
                        mapMessage.put(message.getEvacuationId(), message);
                    }
                    messageList2.addAll(mapMessage.values());
                }


            }


            if (stringEvacuationResponsibleHashMap != null && messageList2.size() > 0) {


                for (Object o : stringEvacuationResponsibleHashMap.entrySet()) {
                    Map.Entry pair = (Map.Entry) o;
                    for (Message message : messageList2) {

                        if (pair.getKey() != null &&
                                message.getAreaName() != null &&
                                ((String) pair.getKey()).equalsIgnoreCase(message.getAreaName())) {

                            messageList.add(message);
                        }

                    }

                }

            } else if (hashmap != null && messageList2.size() > 0) {

                for (Object o : hashmap.entrySet()) {
                    Map.Entry pair = (Map.Entry) o;
                    for (Message message : messageList2) {

                        if (pair.getKey() != null &&
                                message.getAreaName() != null &&
                                ((String) pair.getKey()).equalsIgnoreCase(message.getAreaName())) {

                            messageList.add(message);
                        }

                    }

                }
            }

            if (messageList != null && messageList.size() > 0) {


                messageListOLD.addAll(messageList);
            }
        }

        empty = null;
        return messageList;
    }


    private void onMessages(List<Message> messages) {


        messageSingleAdapter.notifyDataSetChanged();
        hidePD();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case Constants.EVACUATIONREFRESH:

                    //  readMessages();
                    break;
            }
        }
    }

    public class MessageSingleAdapter extends RecyclerView.Adapter<MessageSingleAdapter.MessageViewHolder>
            implements Filterable {

        List<Message> filteredList;

        public MessageSingleAdapter() {


            filteredList = new ArrayList();
        }


        @NonNull
        @Override
        public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new MessageViewHolder(getLayoutInflater().inflate(R.layout.inflate_singlemessage,
                    parent, false));
        }


        @Override
        public Filter getFilter() {


            return new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence charSequence) {

                    Filter.FilterResults filterResults = new Filter.FilterResults();
                    filteredList.clear();
                    String charString = charSequence.toString().trim();
                    if (charString.isEmpty()) {
                        filteredList.addAll(messageListOLD);
                    } else {

                        for (Message row : messageListOLD) {

                            // name match condition. this might differ depending on your requirement
                            // here we are looking for name or phone number match

                            if (row.getAreaName() != null && charString != null &&
                                    row.getAreaName().toLowerCase().contains(charString.toLowerCase())) {

                                filteredList.add(row);

                            }


                        }
                    }
                    filterResults.values = filteredList;
                    return filterResults;

                }

                @Override
                protected void publishResults(CharSequence charSequence, FilterResults filterResults) {


                    messageList.clear();
                    messageList.addAll((Collection<? extends Message>) filterResults.values);
                    notifyDataSetChanged();
                }
            };
        }


        @Override
        public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {


            Message message = messageList.get(position);
            holder.ivimage.setImageResource(R.drawable.profile);
            if (message.getAreaName() != null
                    && !message.getAreaName().isEmpty())
                holder.tvareaname.setText(message.getAreaName());
            else
                holder.tvareaname.setText("");

            if (message.getMessage() != null
                    && message.getMessage().trim().length() > 0)
                holder.tvlastmessage.setText(message.getMessage());
            else
                holder.tvlastmessage.setText("");

            if (message.getDate() != null) {
                holder.tvtime.setText(message.getDate());
            } else {
                holder.tvtime.setText("");
            }


            if (evacuationResponsible.getUserType() != null) {
                if (evacuationResponsible.getUserType().equalsIgnoreCase(IpAddress.NORMALUSER)
                        && message.getSendByApp().equalsIgnoreCase(IpAddress.VIPRECEPTION)
                        && !message.getIsRead()) {

                    holder.ivcount_image.setImageResource(R.drawable.circlered);

                } else if (evacuationResponsible.getUserType().equalsIgnoreCase(IpAddress.ADMINUSER)
                        && message.getSendByApp().equalsIgnoreCase(IpAddress.VIPadm)
                        && !message.getIsRead()) {
                    holder.ivcount_image.setImageResource(R.drawable.circlered);

                } else {
                    holder.ivcount_image.setImageResource(0);
                }
            } else {
                holder.ivcount_image.setImageResource(0);
            }


            holder.llclick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (messageList != null) {
                        message_selected = messageList.get(holder.getAdapterPosition());
                        if (hasNetwork()
                                && !message_selected.isRead()) {
                            updateMessage(message_selected.getEvacuationId());
                        } else {
                            onUpdate(null);
                        }
                    }

                }
            });

        }

        @Override
        public int getItemCount() {

            return messageList.size();
        }

        public class MessageViewHolder extends RecyclerView.ViewHolder {

            @BindView(R.id.ivimage)
            SimpleDraweeView ivimage;
            @BindView(R.id.tvtime)
            TextView tvtime;
            @BindView(R.id.tvlastmessage)
            TextView tvlastmessage;
            @BindView(R.id.tvareaname)
            TextView tvareaname;
            @BindView(R.id.llclick)
            ConstraintLayout llclick;
            @BindView(R.id.ivcount_image)
            ImageView ivcount_image;

            public MessageViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
    }

    private void updateMessage(String evaid) {


        String A_sAppname = "";

        if (evacuationResponsible != null)
            if (evacuationResponsible.getUserType().equalsIgnoreCase("N")) {

                showPD(this);

                A_sAppname = IpAddress.VIPadm;

            } else {
                showPD(this);
                A_sAppname = IpAddress.VIPRECEPTION;

            }
        if (A_sAppname != null) {


            SoapUpdateMessageReadStatusRequestEnvelope soapUpdateMessageReadStatusRequestEnvelope = new
                    SoapUpdateMessageReadStatusRequestEnvelope();

            UpdateMessageReadStatusRequestBody updateMessageReadStatusRequestBody = new UpdateMessageReadStatusRequestBody();

            UpdateMessageReadStatus updateMessageReadStatus = new UpdateMessageReadStatus();
            updateMessageReadStatus.setA_sAppName(A_sAppname);
            updateMessageReadStatus.setAuthToken(getAuthTokenPref_Evacuation());
            updateMessageReadStatus.setA_sBarcode(getBarcode());
            updateMessageReadStatus.setA_sClientId(getClientID());
            updateMessageReadStatus.setA_sEvacuationId(evaid);

            updateMessageReadStatusRequestBody.setUpdateMessageReadStatus(updateMessageReadStatus);
            soapUpdateMessageReadStatusRequestEnvelope.setBody(updateMessageReadStatusRequestBody);

            Observable<SoapUpdateMessageReadStatusResponseEnvelope> lClientDetailsObservable =
                    service.updateMessageReadStatus(soapUpdateMessageReadStatusRequestEnvelope);

            lClientDetailsObservable.
                    subscribeOn(Schedulers.io())
                    .map(this::onSoapUpdateMessageReadStatus)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::onUpdate, this::onError);
        }

    }

    private String onSoapUpdateMessageReadStatus(SoapUpdateMessageReadStatusResponseEnvelope
                                                         soapUpdateMessageReadStatusResponseEnvelope) {

        UpdateMessageReadStatusResponseBody updateMessageReadStatusResponseBody =
                soapUpdateMessageReadStatusResponseEnvelope.getBody();

        return updateMessageReadStatusResponseBody.getUpdateMessageReadStatusResponse().getUpdateMessageReadStatusResult();
    }

    private void onUpdate(String status) {

        hidePD();
        hideSoft();
        Intent intent = new Intent(MessageSingleActivity.this,
                EvacuationMessagesActivity.class);

        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        if (message_selected != null) {

            intent.putExtra(IpAddress.MESSAGEID, message_selected.getMessageId());
            intent.putExtra(IpAddress.BARCODE, message_selected.getBarcode());
            intent.putExtra(IpAddress.EVACUATIONID, message_selected.getEvacuationId());
            intent.putExtra(IpAddress.EVACUATIONMESSAGE, message_selected.getAreaName() + "-" +
                    message_selected.getFloor());
            startActivityForResult(intent, Constants.EVACUATIONREFRESH);
        }


    }

}
