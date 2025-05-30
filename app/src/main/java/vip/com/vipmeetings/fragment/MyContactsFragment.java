package vip.com.vipmeetings.fragment;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import vip.com.vipmeetings.R;
import vip.com.vipmeetings.models.Contact;
import vip.com.vipmeetings.utilities.Constants;

/**
 * Created by Srinath on 14/06/17.
 */

public class MyContactsFragment extends BaseFragment {


    @BindView(R.id.rvcontacts)
    RecyclerView rvcontacts;
    List<Contact> contactsList;

    Adapter adapter;

    public static final Uri PHONEURI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_allcontacts, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        contactsList = new ArrayList<>();
        adapter = new Adapter();
        rvcontacts.setLayoutManager(new LinearLayoutManager(getContext()));
        rvcontacts.setAdapter(adapter);
        getContacts();
    }

    private void getContacts() {

        getPhoneContacts();

        if (contactsList != null && contactsList.size() > 0) {

            adapter.notifyDataSetChanged();
        }
    }

    public List<Contact> getPhoneContacts() {
        contactsList.clear();
        try {
            Cursor phones = getActivity().getContentResolver().query(PHONEURI,
                    null, null, null, null);

            if(phones!=null) {
                while (phones.moveToNext()) {

                    Contact contact = new Contact();
                    contact.setName(phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)));
                    contact.setMobile(phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
                    contactsList.add(contact);

                }
                phones.close();
            }
        } catch (Exception e) {

            Constants.e("err", e.toString());
        }


        return contactsList;
    }



        public static Fragment newInstance(Bundle bundle) {

            MyContactsFragment allContactsFragment = new MyContactsFragment();
            allContactsFragment.setArguments(bundle);
            return allContactsFragment;
        }


    public class Adapter extends RecyclerView.Adapter<Adapter.VH> {


        @Override
        public VH onCreateViewHolder(ViewGroup parent, int viewType) {

            return new VH(LayoutInflater.from(getContext()).inflate(R.layout.inflate_contact1, null));
        }

        @Override
        public void onBindViewHolder(VH holder, int position) {


            Contact contact = contactsList.get(position);

            holder.tvcontactname.setText(contact.getName());


            if(contact.isSelect())
            {
                holder.ivtruefalse.setImageResource(R.mipmap.ic_true);

            }else
            {
                holder.ivtruefalse.setImageResource(R.mipmap.ic_false);

            }


            holder.rledit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    contactsList.get(holder.getAdapterPosition()).setSelect(
                            !contactsList.get(holder.getAdapterPosition()).isSelect());

                    notifyDataSetChanged();
                }
            });

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

}
