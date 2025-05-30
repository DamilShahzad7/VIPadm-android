package vip.com.vipmeetings;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import vip.com.vipmeetings.models.ContactManual;
import vip.com.vipmeetings.utilities.Constants;

/**
 * Created by Srinath on 15/06/17.
 */

public class EditContactActivity extends BaseActivity {

    @BindView(R.id.rvcontacts)
    RecyclerView rvcontacts;


    List<ContactManual> contactManualList;
    ContactAdapter contactAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editcontact);
        ButterKnife.bind(this);

        contactManualList = mGson.fromJson(getIntent().getStringExtra(
                Constants.CONTACTLIST), new TypeToken<List<ContactManual>>() {
        }.getType());

        if (contactManualList != null && contactManualList.size() > 0) {

            contactAdapter = new ContactAdapter();
            rvcontacts.setLayoutManager(new LinearLayoutManager(this));
            rvcontacts.setAdapter(contactAdapter);
        }
    }

    @OnClick(R.id.ivhome)
    public void onHome(View v) {

        gotoHome();

    }

    @OnClick(R.id.tvback)
    public void onBack(View v) {

        Intent i = new Intent();
        i.putExtra(Constants.ADDCONTACT, mGson.toJson(contactManualList));
        setResult(RESULT_OK, i);
        finish();

    }


    @Override
    public void onBackPressed() {
        // super.onBackPressed();

        Intent i = new Intent();
        i.putExtra(Constants.ADDCONTACT, mGson.toJson(contactManualList));
        setResult(RESULT_OK, i);
        finish();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {


            switch (requestCode) {

                case Constants.EDITCONTACT:
                    Intent i = new Intent();


                    if (data.hasExtra(Constants.POS)) {
                        contactManualList.set(data.getIntExtra(Constants.POS, 0),
                                mGson.fromJson(data.getStringExtra(Constants.ADDCONTACT), ContactManual.class));
                        i.putExtra(Constants.ADDCONTACT, mGson.toJson(contactManualList));
                        setResult(RESULT_OK, i);
                        finish();
                    }
                    break;
            }

        }
    }

    public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.VH> {


        @Override
        public VH onCreateViewHolder(ViewGroup parent, int viewType) {

            return new VH(getLayoutInflater().inflate(R.layout.inflate_contact, parent,false));
        }

        @Override
        public void onBindViewHolder(VH holder, int position) {


            ContactManual leave = contactManualList.get(position);

            StringBuilder name = new StringBuilder();

            if (leave.getFirstName() != null
                    && leave.getFirstName().trim().length() > 0
                    && !leave.getFirstName().equalsIgnoreCase("''")) {
                name.append(leave.getFirstName()+", ");
            }


            if (leave.getSurName() != null
                    && leave.getSurName().trim().length() > 0
                    && !leave.getSurName().equalsIgnoreCase("''")) {



                name.append(leave.getSurName()+", ");
            }

            if (leave.getMobileNo() != null
                    && leave.getMobileNo().trim().length() > 0
                    && !leave.getMobileNo().equalsIgnoreCase("''")) {
                name.append(leave.getMobileNo()+", ");
            }


            if (leave.getEmailId() != null
                    && leave.getEmailId().trim().length() > 0
                    && !leave.getEmailId().equalsIgnoreCase("''")) {
                name.append(leave.getEmailId()+", ");
            }


            if (leave.getCompany() != null
                    && leave.getCompany().trim().length() > 0
                    && !leave.getCompany().equalsIgnoreCase("''")) {
                name.append(leave.getCompany());
            }

            holder.tvcontactname.setText(name);


            holder.btdelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    contactManualList.remove(holder.getAdapterPosition());
                    notifyDataSetChanged();

                    if (contactManualList.size() == 0) {
                        Intent i = new Intent();
                        i.putExtra(Constants.ADDCONTACT, mGson.toJson(contactManualList));
                        setResult(RESULT_OK, i);
                        finish();
                    }

                }
            });

//            holder.rledit.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//
//
//                    Intent i = new Intent(EditContactActivity.this, AddManuallyActivity.class);
//                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    i.putExtra(Constants.POS, holder.getAdapterPosition());
//                    i.putExtra(Constants.ADDCONTACT
//                            , mGson.toJson(contactManualList.get(holder.getAdapterPosition())));
//                    startActivityForResult(i, Constants.EDITCONTACT);
//
//                }
//            });


        }


        @Override
        public int getItemCount() {
            return contactManualList.size();
        }

        public class VH extends RecyclerView.ViewHolder {


            @BindView(R.id.btdelete)
            Button btdelete;

            @BindView(R.id.rledit)
            RelativeLayout rledit;

            @BindView(R.id.tvcontactname)
            TextView tvcontactname;


            public VH(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }


    }


}
