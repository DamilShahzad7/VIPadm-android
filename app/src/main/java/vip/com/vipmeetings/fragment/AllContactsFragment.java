package vip.com.vipmeetings.fragment;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import vip.com.vipmeetings.AddParticipantsActivity;
import vip.com.vipmeetings.R;
import vip.com.vipmeetings.body.GetContactsRequestBody;
import vip.com.vipmeetings.body.GetFormerVisitorsRequestBody;
import vip.com.vipmeetings.envelope.SoapGetContactsRequestEnvelope;
import vip.com.vipmeetings.envelope.SoapGetContactsResponseEnvelope;
import vip.com.vipmeetings.envelope.SoapGetFormerVisitorsRequestEnvelope;
import vip.com.vipmeetings.envelope.SoapGetFormerVisitorsResponseEnvelope;
import vip.com.vipmeetings.evacuate.IpAddress;
import vip.com.vipmeetings.interfaces.ContactsApi;
import vip.com.vipmeetings.models.Contact;
import vip.com.vipmeetings.models.ContactManual;
import vip.com.vipmeetings.models.Contacts;
import vip.com.vipmeetings.models.LetterModel;
import vip.com.vipmeetings.models.Visitor;
import vip.com.vipmeetings.models.Visitors;
import vip.com.vipmeetings.request.GetContacts;
import vip.com.vipmeetings.request.GetFormerVisitors;
import vip.com.vipmeetings.utilities.Constants;

import static vip.com.vipmeetings.utilities.Constants.PHONEURI;
import static vip.com.vipmeetings.utilities.Constants.allContactList;
import static vip.com.vipmeetings.utilities.Constants.allContactList1;
import static vip.com.vipmeetings.utilities.Constants.companyContactList1;
import static vip.com.vipmeetings.utilities.Constants.phoneContactList1;
import static vip.com.vipmeetings.utilities.Constants.visContactList1;

/**
 * Created by Srinath on 13/06/17.
 */

public class AllContactsFragment extends BaseFragment {


    @BindView(R.id.rvcontacts)
    RecyclerView rvcontacts;

    @BindView(R.id.rvletter)
    RecyclerView rvletter;

    LetterAdapter letterAdapter;


    @BindView(R.id.swipe)
    SwipeRefreshLayout swipeRefreshLayout;

    List<Contact> contactsList;
    Adapter adapter;

    Bundle mBundle;

    HashMap<String, String> hashMap;

    AddParticipantsActivity addParticipantsActivity;

    List<LetterModel> stringList;
    RecyclerView.SmoothScroller smoothScroller;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            addParticipantsActivity = (AddParticipantsActivity) context;
        } catch (Exception e) {

        }
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        try {
            if (isVisibleToUser && isVisible()) {

                setHashmap();
                adapter.notifyDataSetChanged();
            }
        } catch (Exception e) {

        }
    }

    private void setHashmap() {


        hashMap.clear();
        for (ContactManual c : addParticipantsActivity.getContactManualList()) {

            if (c.getSurName() != null)
                hashMap.put(c.getMobileNo(), c.getFirstName() + " " + c.getSurName());
            else {
                hashMap.put(c.getMobileNo(), c.getFirstName());
            }
        }
        Constants.e("hash", "map" + hashMap.toString());


    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_allcontacts, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        hashMap = new HashMap<>();
        swipeRefreshLayout.setRefreshing(false);
        swipeRefreshLayout.setEnabled(false);
        swipeRefreshLayout.setOnRefreshListener(null);
        mBundle = getArguments();
        setHashmap();
        contactsList = new ArrayList<>();
        stringList = new ArrayList<>();
        contactsList.clear();
        adapter = new Adapter();
        rvcontacts.setAdapter(adapter);
        letterAdapter = new LetterAdapter();
        rvletter.setAdapter(letterAdapter);

        smoothScroller = new
                LinearSmoothScroller(getActivity()) {
                    @Override
                    protected int getVerticalSnapPreference() {

                        return LinearSmoothScroller.SNAP_TO_START;
                    }
                };


        if (mBundle.getInt(Constants.POS) == 0) {

            if (allContactList != null && allContactList.size() > 0) {

                setContactList(allContactList);
            } else if (allContactList1 != null && allContactList1.size() > 0) {


                setContactList(allContactList1);

                //contactsList = allContactList1;

            }
            adapter.notifyDataSetChanged();

        } else if (mBundle.getInt(Constants.POS) == 1) {

            if (companyContactList1 != null && companyContactList1.size() > 0) {
                // contactsList = companyContactList1;

                setContactList(companyContactList1);
                adapter.notifyDataSetChanged();
            }


        } else if (mBundle.getInt(Constants.POS) == 2) {


            if (visContactList1 != null && visContactList1.size() > 0) {

//                Collections.sort(visContactList1);
//                // contactsList = visContactList1;
//                for (Contact contact : visContactList1) {
//
//                    if (Character.isLetter(contact.getName().trim().charAt(0))) {
//                        Contact contact1 = new Contact();
//                        contact1.setName(contact.getName().trim().substring(0, 1));
//                        if (contactsList.contains(contact1)) {
//
//                        } else
//                            contactsList.add(contact1);
//                    }
//                    contactsList.add(contact);
//                }

                setContactList(visContactList1);
                adapter.notifyDataSetChanged();
            }


        } else if (mBundle.getInt(Constants.POS) == 3) {

            //  contactsList = phoneContactList1;

            setContactList(phoneContactList1);
//            if (phoneContactList1 != null && phoneContactList1.size() > 0) {
//
//                Collections.sort(phoneContactList1);
//
//                for (Contact contact : phoneContactList1) {
//
//                    if (Character.isLetter(contact.getName().trim().charAt(0))) {
//                        Contact contact1 = new Contact();
//                        contact1.setName(contact.getName().trim().substring(0, 1));
//                        if (contactsList.contains(contact1)) {
//
//                        } else
//                            contactsList.add(contact1);
//                    }
//                    contactsList.add(contact);
//                }
//
//            }
            adapter.notifyDataSetChanged();

        }

        try {
            if (contactsList == null || contactsList.size() == 0) {
                setRetrofit(getApp().getTcpIp());
                getContacts();
            }
        } catch (Exception e) {
            Constants.e("err", e.toString());
        }
    }

    private void setContactList(List<Contact> allContactList1) {


//        Collections.sort(companyContactList1);
//        for (Contact contact : companyContactList1) {
//
//            if (Character.isLetter(contact.getName().trim().charAt(0))) {
//                Contact contact1 = new Contact();
//                contact1.setName(contact.getName().trim().substring(0, 1));
//                if (contactsList.contains(contact1)) {
//
//                } else
//                    contactsList.add(contact1);
//            }
//            contactsList.add(contact);
//        }
        stringList.clear();
        if (allContactList1 != null && allContactList1.size() > 0) {

            Collections.sort(allContactList1);

            for (Contact contact : allContactList1) {

                Contact contact1 = new Contact();
                if (Character.isLetter(contact.getName().trim().charAt(0))) {

                    LetterModel letterModel = new LetterModel();
                    letterModel.setLetter(contact.getName().trim().substring(0, 1));
                    letterModel.setPos(contactsList.size());
                    if (stringList.contains(letterModel)) {

                    } else
                        stringList.add(letterModel);
                    contact1.setName(contact.getName().trim().substring(0, 1));
                    if (contactsList.contains(contact1)) {

                    } else
                        contactsList.add(contact1);
                }
                contactsList.add(contact);
            }
        }

        letterAdapter.notifyDataSetChanged();
    }


    private void getContacts() {
        contactsApi = retrofit.create(ContactsApi.class);
        if (mBundle.getInt(Constants.POS) == 0) {
            swipeRefreshLayout.setEnabled(true);
            swipeRefreshLayout.setRefreshing(true);
            getAllContacts();
        } else if (mBundle.getInt(Constants.POS) == 1) {

            swipeRefreshLayout.setEnabled(true);
            swipeRefreshLayout.setRefreshing(true);
            getCompanyContacts();

        } else if (mBundle.getInt(Constants.POS) == 2) {

            swipeRefreshLayout.setEnabled(true);
            swipeRefreshLayout.setRefreshing(true);
            getFormerContacts();
        } else if (mBundle.getInt(Constants.POS) == 3) {
            swipeRefreshLayout.setEnabled(true);
            swipeRefreshLayout.setRefreshing(true);

            int permissionCheck = ContextCompat.checkSelfPermission(getContext(),
                    Manifest.permission.READ_CONTACTS);

            if (permissionCheck == PackageManager.PERMISSION_GRANTED) {

                getPhoneContacts4();
            }
        }
    }


    public List<Contact> getPhoneContacts4() {

        try {
            contactsList.clear();
            Cursor phones = getActivity().getContentResolver().query(PHONEURI,
                    null, null, null, null);

            if (phones != null) {

                phoneContactList1 = new ArrayList<>();
                while (phones.moveToNext()) {

                    Contact contact = new Contact();
                    contact.setName(phones.getString(phones.getColumnIndex
                            (ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)));
                    contact.setMobile(phones.getString(phones.getColumnIndex
                            (ContactsContract.CommonDataKinds.Phone.NUMBER)));

                    if (contact.getMobile() != null
                            && contact.getMobile().trim().length() > 0) {

                        if (hashMap.containsKey(contact.getMobile())) {
                            contact.setSelect(true);
                        } else {
                            contact.setSelect(false);
                        }

                        if (phoneContactList1.contains(contact)) {

                        } else {
                            phoneContactList1.add(contact);
                        }

                        if (contactsList.contains(contact)) {

                        } else

                            contactsList.add(contact);
                    }

                }
                phones.close();
            }
        } catch (Exception e) {

            Constants.e("err", e.toString());
        }

        refresh4();
        return contactsList;
    }

    private void refresh4() {

        if (contactsList != null && contactsList.size() > 0) {

            if (mBundle.getInt(Constants.POS) == 3)
                setContactList(contactsList);
            adapter.notifyDataSetChanged();
        }
        swipeRefreshLayout.setEnabled(false);
        swipeRefreshLayout.setRefreshing(false);
    }

    public String getAuthTokenPref_Mobile() {

        return mSharedPreferences.getString(IpAddress.AUTHTOKEN_MOBILE, "CC484588-C0B4-4777-B6C6-3F149728FC49");
    }


    private void getAllContacts() {


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


        Observable<SoapGetContactsResponseEnvelope> lClientDetailsObservable =
                contactsApi.getContacts2(soapGetContactsRequestEnvelope);

        Observable<SoapGetFormerVisitorsResponseEnvelope> contactsObservable =
                contactsApi.getFormerVisitors(soapGetFormerVisitorsRequestEnvelope);

        int permissionCheck = ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.READ_CONTACTS);
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {

            lClientDetailsObservable.
                    subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .map(this::soapContacts)
                    .map(this::addContacts)
                    .map(this::getPhoneContacts)
                    .subscribeOn(Schedulers.newThread())
                    .flatMap(contacts -> contactsObservable.
                            subscribeOn(Schedulers.newThread()).
                            observeOn(AndroidSchedulers.mainThread()))
                    .map(this::soapVisitors)
                    .subscribe(this::addVisitors, this::onError);
        } else {
            lClientDetailsObservable.
                    subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .map(this::soapContacts)
                    .map(this::addContacts)
                    .subscribeOn(Schedulers.newThread())
                    .flatMap(contacts -> contactsObservable.
                            subscribeOn(Schedulers.newThread()).
                            observeOn(AndroidSchedulers.mainThread()))
                    .map(this::soapVisitors)
                    .subscribe(this::addVisitors, this::onError);

        }


    }

    private Contacts soapContacts(SoapGetContactsResponseEnvelope soapGetContactsResponseEnvelope) {


        Contacts contacts = soapGetContactsResponseEnvelope.getBody().getGetContactsResponse()
                .getGetContactsResult().getContacts();
        if (contacts == null) {
            contacts = new Contacts();
        }
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

    private void addVisitors(Visitors visitors) {

        Constants.e("size", "3");
        if (visitors != null && visitors.getVisitor() != null &&
                visitors.getVisitor().size() > 0) {
            visContactList1 = new ArrayList<>();
            for (Visitor visitor : visitors.getVisitor()) {
                Contact contacts = new Contact();
                contacts.setMobile(visitor.getMobile());
                contacts.setCompany(visitor.getCompanyName());
                contacts.setName(visitor.getName());
                contacts.setEmail(visitor.getEMail());
                contacts.setType(visitor.getType());

                if (contactsList.contains(contacts)) {

                } else
                    contactsList.add(contacts);
                if (visContactList1 != null) {
                    if (visContactList1.contains(contacts)) {

                    } else
                        visContactList1.add(contacts);
                }
                if (allContactList1 != null) {

                    if (allContactList1.contains(contacts)) {

                    } else
                        allContactList1.add(contacts);

                    if (allContactList1.size() > 0 && mBundle.getInt(Constants.POS) == 0)
                        setContactList(allContactList1);
                }

            }

        }

        if (contactsList != null) {

            if (visContactList1 != null && visContactList1.size() > 0
                    && mBundle.getInt(Constants.POS) == 2) {

                setContactList(visContactList1);
            }

            adapter.notifyDataSetChanged();
        }

        swipeRefreshLayout.setEnabled(false);
        swipeRefreshLayout.setRefreshing(false);

    }

    public void onError(Throwable throwable) {


        swipeRefreshLayout.setEnabled(false);
        swipeRefreshLayout.setRefreshing(false);
        Constants.e("error", throwable.toString());

    }


    private Contacts addContacts(Contacts contacts) {

        if (allContactList1 == null) {
            allContactList1 = new ArrayList<>();
        }
        Constants.e("size", "1");
        refresh0(contacts);
        return contacts;
    }

    private void refresh0(Contacts contacts) {

        swipeRefreshLayout.setEnabled(false);
        swipeRefreshLayout.setRefreshing(false);
        if (contacts != null && contacts.getContact() != null) {
            for (Contact c : contacts.getContact()) {
                if (hashMap.containsKey(c.getMobile())) {
                    c.setSelect(true);
                } else {
                    c.setSelect(false);
                }
                c.setType("All");

                if (allContactList1.contains(c)) {

                } else

                    allContactList1.add(c);

                if (contactsList.contains(c)) {

                } else
                    contactsList.add(c);
            }
            adapter.notifyDataSetChanged();
        } else {

        }

    }

    public Contacts getPhoneContacts(Contacts contacts) {
        try {
            Constants.e("size", "2");
            Cursor phones = getActivity().getContentResolver().query(PHONEURI,
                    null, null, null, null);

            if (phones != null) {
                while (phones.moveToNext()) {

                    Contact contact = new Contact();
                    contact.setName(phones.getString
                            (phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)));
                    contact.setMobile(phones.getString
                            (phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));

                    contact.setType("My Contact");

                    if (hashMap.containsKey(contact.getMobile()))
                        contact.setSelect(true);

                    if (contactsList.contains(contact)) {

                    } else
                        contactsList.add(contact);

                    if (allContactList1.contains(contact)) {

                    } else
                        allContactList1.add(contact);

                }
                phones.close();
            }
        } catch (Exception e) {

            Constants.e("err", e.toString());
        }

        adapter.notifyDataSetChanged();

        return contacts;
    }

    private void getCompanyContacts() {

        SoapGetContactsRequestEnvelope soapGetContactsRequestEnvelope = new SoapGetContactsRequestEnvelope();

        GetContactsRequestBody getContactsRequestBody = new GetContactsRequestBody();

        GetContacts getContacts = new GetContacts();
        getContacts.setAuthToken(getAuthTokenPref_Mobile());
        getContacts.setA_sUserEmail(getEmail());
        getContactsRequestBody.setGetContacts(getContacts);
        soapGetContactsRequestEnvelope.setBody(getContactsRequestBody);


        Observable<SoapGetContactsResponseEnvelope> lClientDetailsObservable =
                contactsApi.getContacts2(soapGetContactsRequestEnvelope);


        lClientDetailsObservable.
                subscribeOn(Schedulers.newThread())
                .map(this::soapContacts)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onContacts, this::onError);
    }

    public void getFormerContacts() {

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
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::addVisitors, this::onError);

    }

    private void onContacts(Contacts contacts) {


        refresh2(contacts);
    }

    private void refresh2(Contacts contacts) {

        swipeRefreshLayout.setEnabled(false);
        swipeRefreshLayout.setRefreshing(false);
        if (contacts != null && contacts.getStatus().get_status()
                .equalsIgnoreCase(Constants.AVAILABLE)) {

            contactsList = contacts.getContact();
            if (contactsList != null && contactsList.size() > 0) {

                companyContactList1 = new ArrayList<>();
                for (Contact c : contactsList) {
                    if (hashMap.containsKey(c.getMobile())) {
                        c.setSelect(true);
                    } else {
                        c.setSelect(false);
                    }
                    c.setType("Company Staff");
                    if (companyContactList1.contains(c)) {

                    } else
                        companyContactList1.add(c);
                }

                if (mBundle.getInt(Constants.POS) == 1)
                    setContactList(companyContactList1);
            }
            adapter.notifyDataSetChanged();

        } else {


        }
    }

    public static Fragment newInstance(Bundle bundle) {

        AllContactsFragment allContactsFragment = new AllContactsFragment();
        allContactsFragment.setArguments(bundle);
        return allContactsFragment;
    }
    public void filterData(String query) {
        List<Contact> filteredList = new ArrayList<>();
        if (query.length() > 0) {
            for (Contact contact : contactsList) {
                if (contact.getName() != null && contact.getMobile() != null) {
                    if (contact.getName().toLowerCase().contains(query.toLowerCase()) ||
                            contact.getMobile().contains(query)) {
                        filteredList.add(contact);
                    }
                }
            }
        } else {
            // If query is empty or has only one character, reset the filter
            resetFilter();
            return;
        }
        // Update RecyclerView with filtered data
        adapter.setData(filteredList);
    }


    public void resetFilter() {
        if (mBundle.getInt(Constants.POS) == 0) {
            adapter.setData(allContactList);
        }
        else if (mBundle.getInt(Constants.POS) == 1) {
            adapter.setData(companyContactList1);
        }
        else if (mBundle.getInt(Constants.POS) == 2) {
            adapter.setData(visContactList1);
        }
        else if (mBundle.getInt(Constants.POS) == 3) {
            adapter.setData(phoneContactList1);
        }
        else{
            adapter.setData(allContactList);
        }
        adapter.notifyDataSetChanged();
        letterAdapter.notifyDataSetChanged();
    }


    public class Adapter extends RecyclerView.Adapter<Adapter.VH> {


        @Override
        public VH onCreateViewHolder(ViewGroup parent, int viewType) {

            if (viewType == 0)

                return new VH(LayoutInflater.from(getContext()).inflate(R.layout.inflate_contact1, parent, false));

            else
                return new VH(LayoutInflater.from(getContext()).inflate(R.layout.inflate_contact2, parent, false));

        }

        @Override
        public int getItemViewType(int position) {

            if (contactsList.get(position).getName() != null
                    && contactsList.get(position).getName().trim().length() == 1
                    && contactsList.get(position).getMobile() == null)

                return 1;

            return 0;
        }
        public void setData(List<Contact> newData) {
            contactsList = newData;
            notifyDataSetChanged();
        }
        @Override
        public void onBindViewHolder(VH holder, int position) {


            if (getItemViewType(position) == 0) {
                Contact contact = contactsList.get(position);

                holder.ivtruefalse.setVisibility(View.VISIBLE);
                holder.tvcontactname.setText(contact.getName());

                holder.rledit.setBackgroundResource(R.drawable.underline_grey);


                if (hashMap.containsKey(contact.getMobile())) {

                    String name = hashMap.get(contact.getMobile());
                    if (name != null && name.contains(contact.getName())) {
                        holder.ivtruefalse.setImageResource(R.mipmap.ic_true);
                    } else {
                        holder.ivtruefalse.setImageResource(R.mipmap.ic_false);

                    }
                } else {
                    holder.ivtruefalse.setImageResource(R.mipmap.ic_false);

                }


                holder.rledit.setOnClickListener(view -> {

                    contactsList.get(holder.getAdapterPosition()).setSelect(
                            !contactsList.get(holder.getAdapterPosition()).isSelect());
                    addContact(contactsList.get(holder.getAdapterPosition()));
                });
            } else {
                holder.tvcontactname.setText(contactsList.get(position).getName());
                holder.ivtruefalse.setVisibility(View.GONE);
                holder.rledit.setBackgroundResource(R.drawable.underline_grey1);
                holder.rledit.setOnClickListener(null);
            }


        }

        @Override
        public int getItemCount() {
            return contactsList.size();
        }

        public class VH extends RecyclerView.ViewHolder {

            @BindView(R.id.tvcontactname)
            TextView tvcontactname;

            @BindView(R.id.rledit)
            ViewGroup rledit;

            @BindView(R.id.ivtruefalse)
            ImageView ivtruefalse;

            public VH(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
    }

    private void addContact(Contact contact) {
        ContactManual contactManual = new ContactManual();
        contactManual.setCompany("''");
        contactManual.setCity("''");
        contactManual.setCountry("''");
        contactManual.setMeetingId(getMeetingId());
        contactManual.setSendInvitation("YES");
        contactManual.setSurName("''");
        contactManual.setFirstName(contact.getName());
        contactManual.setEmailId(contact.getEmail());
        contactManual.setMobileNo(contact.getMobile());
        contactManual.setType("existing");
        List<ContactManual> contactManualList = addParticipantsActivity.getContactManualList();

        if (contact.isSelect()) {
            if (!contactManualList.contains(contactManual)) {
                contactManualList.add(contactManual);
            }
        } else {
            contactManualList.remove(contactManual);
        }

        Constants.e("CONTACT", mGson.toJson(contactManualList));
        addParticipantsActivity.addContact(contactManualList);
        setHashmap();
        adapter.notifyDataSetChanged();
    }



    public class LetterAdapter extends RecyclerView.Adapter<LetterAdapter.VH> {

        public void setData(List<LetterModel> newData) {
            stringList = newData;
            notifyDataSetChanged();
        }
        @NonNull
        @Override
        public VH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            return new VH(getLayoutInflater().inflate(R.layout.inflate_letter, viewGroup, false));
        }

        @Override
        public void onBindViewHolder(@NonNull VH vh, int i) {


            vh.tvletter.setText(stringList.get(i).getLetter());

            vh.clletter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    if (adapter != null &&
                            adapter.getItemCount() > 0 &&
                            rvcontacts != null) {

                        smoothScroller.setTargetPosition
                                (stringList.get(vh.getAdapterPosition()).getPos());
                        rvcontacts.getLayoutManager().startSmoothScroll(smoothScroller);


                        //  rvcontacts.scrollToPosition(stringList.get(vh.getAdapterPosition()).getPos());
                    }

                }
            });
        }

        @Override
        public int getItemCount() {
            return stringList.size();
        }

        public class VH extends RecyclerView.ViewHolder {


            @BindView(R.id.clletter)
            ConstraintLayout clletter;

            @BindView(R.id.tvletter)
            AppCompatTextView tvletter;

            public VH(@NonNull View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
    }
}
