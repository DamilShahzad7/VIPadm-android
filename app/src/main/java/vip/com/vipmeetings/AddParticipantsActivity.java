package vip.com.vipmeetings;

import static java.security.AccessController.getContext;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.tabs.TabLayout;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import vip.com.vipmeetings.fragment.AllContactsFragment;
import vip.com.vipmeetings.models.ContactManual;
import vip.com.vipmeetings.utilities.Constants;

/**
 * Created by Srinath on 30/05/17.
 */

public class AddParticipantsActivity extends BaseActivity {

    private static final int MY_PERMISSIONS_REQUEST_READ_LOCATION = 101;

    @BindView(R.id.vpcontacts)
    ViewPager vpcontacts;

    @BindView(R.id.tabcontacts)
    TabLayout tabcontacts;

    ContactAdapter contactAdapter;

    public List<ContactManual> contactManualList;

    @BindView(R.id.etsearch)
    EditText etsearch;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addparticipants);
        ButterKnife.bind(this);
        contactManualList = mGson.fromJson(getIntent().getStringExtra(Constants.CONTACTLIST),
                new TypeToken<List<ContactManual>>() {
                }.getType());

        onTokenRefresh();
        etsearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Fragment fragment = getSupportFragmentManager().findFragmentByTag("android:switcher:" + vpcontacts.getId() + ":" + vpcontacts.getCurrentItem());

                    String query = s.toString().trim().toLowerCase();
                    // Retrieve the currently active fragment from the ViewPager
                    if (fragment instanceof AllContactsFragment) {
                        // Filter the data based on the query
                        ((AllContactsFragment) fragment).filterData(query);
                    }
                }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }

    private void onTokenRefresh() {

        if (contactManualList == null) {
            contactManualList = new ArrayList<>();
        }

        if(getApp().isContactRequest()) {
            checkLocationPermission(getApp().isContactRequest());
        }else
        {
            permissionEnable();
        }
    }


    public void addContact(List<ContactManual> contactManualList1) {

        try {

            for (ContactManual c : contactManualList1) {
                if (contactManualList.contains(c)) {

                } else {
                    contactManualList.add(c);
                }
            }

            Constants.e("size", contactManualList.size() + "");

        } catch (Exception e) {

        }

    }

    @OnClick(R.id.ivhome)
    public void onHome(View v) {

        gotoHome();

    }


    @OnClick(R.id.tvsave)
    public void onSave(View v) {

        onSave1();
    }

    @OnClick(R.id.tvback)
    public void onBAck(View v) {

        hideSoft();
        onBackPressed();
    }

    @Override
    public void onBackPressed() {

        onSave1();
    }

    public List<ContactManual> getContactManualList() {
        return contactManualList;
    }

    public void setContactManualList(List<ContactManual> contactManualList) {
        this.contactManualList = contactManualList;
    }

    private void onSave1() {


        Intent intent = getIntent();
        if (contactManualList != null && contactManualList.size() > 0) {
            intent.putExtra(Constants.ADDCONTACT, mGson.toJson(contactManualList));
        } else
            intent.putExtra(Constants.NOCONTACT, true);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void checkLocationPermission(boolean flag) {

        int permissionCheck = ContextCompat.checkSelfPermission(getApplicationContext(),
                android.Manifest.permission.READ_CONTACTS);


        if (flag && permissionCheck == PackageManager.PERMISSION_GRANTED) {

            permissionEnable();

        } else {


            if (flag) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        android.Manifest.permission.READ_CONTACTS)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(AddParticipantsActivity.this);

                    builder.setMessage("Please enable permission to read contacts");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            dialogInterface.dismiss();
                            ActivityCompat.requestPermissions(AddParticipantsActivity.this,
                                    new String[]{android.Manifest.permission.READ_CONTACTS},
                                    MY_PERMISSIONS_REQUEST_READ_LOCATION);

                        }
                    });
                    builder.show();

                } else {

                    // No explanation needed, we can request the permission.

                    ActivityCompat.requestPermissions(this,
                            new String[]{android.Manifest.permission.READ_CONTACTS},
                            MY_PERMISSIONS_REQUEST_READ_LOCATION);

                    // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                }
            }
        }


    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {


                    permissionEnable();

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.

                    getApp().setContactRequest(false);


                }

            }
            break;


            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private void permissionEnable() {

        contactAdapter = new ContactAdapter(getSupportFragmentManager());
        vpcontacts.setAdapter(contactAdapter);
        vpcontacts.setOffscreenPageLimit(3);
        tabcontacts.setupWithViewPager(vpcontacts);

        for (int i = 0; i < tabcontacts.getTabCount(); i++) {
            TabLayout.Tab tab = tabcontacts.getTabAt(i);


            tab.setCustomView(contactAdapter.getTabView(i));



//            LinearLayout layout = ((LinearLayout) ((LinearLayout)
//                    tabcontacts.getChildAt(0)).getChildAt(i));
//            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) layout.getLayoutParams();
//            layoutParams.weight = 1f; // e.g. 0.5f
//            layout.setLayoutParams(layoutParams);
        }
    }

    public void clearContact() {

        try {
            contactManualList.clear();
        } catch (Exception e) {

        }
    }


    public class ContactAdapter extends FragmentPagerAdapter {


        public ContactAdapter(FragmentManager fm) {
            super(fm);
        }


        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }


        @Override
        public Fragment getItem(int position) {


            switch (position) {

                case 0:
                    Bundle bundle = new Bundle();
                    bundle.putString(Constants.CONTACTLIST,
                            mGson.toJson(contactManualList));
                    bundle.putInt(Constants.POS, position);
                    return AllContactsFragment.newInstance(bundle);
                case 1:
                    bundle = new Bundle();
                    bundle.putString(Constants.CONTACTLIST,
                            mGson.toJson(contactManualList));
                    bundle.putInt(Constants.POS, position);
                    return AllContactsFragment.newInstance(bundle);
                case 2:
                    bundle = new Bundle();
                    bundle.putString(Constants.CONTACTLIST,
                            mGson.toJson(contactManualList));
                    bundle.putInt(Constants.POS, position);
                    return AllContactsFragment.newInstance(bundle);
                case 3:
                    bundle = new Bundle();
                    bundle.putString(Constants.CONTACTLIST,
                            mGson.toJson(contactManualList));
                    bundle.putInt(Constants.POS, position);
                    return AllContactsFragment.newInstance(bundle);


            }
            return null;
        }

        @Override
        public CharSequence getPageTitle(int position) {

            switch (position) {


                case 0:
                    return "All";
                case 1:
                    return "Company staff";
                case 2:
                    return "Former visitors";
                case 3:
                    return "My contacts";


            }
            return super.getPageTitle(position);
        }

        @Override
        public int getCount() {
            return 4;
        }

        public View getTabView(int i) {

            View v = LayoutInflater.from(AddParticipantsActivity.this).inflate(R.layout.custom_tab, null);
            TextView tv = (TextView) v.findViewById(R.id.tv);
            if(i==0) {
               // tv.setTextColor(ContextCompat.getColor(AddParticipantsActivity.this,
                 //       R.color.bg));
            }
            tv.setText(getPageTitle(i));
            ViewGroup.LayoutParams layoutParams = new
                    TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            v.setLayoutParams(layoutParams);
            return v;
        }
    }


}
