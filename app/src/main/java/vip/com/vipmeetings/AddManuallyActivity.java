package vip.com.vipmeetings;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.gson.reflect.TypeToken;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import vip.com.vipmeetings.evacuate.IpAddress;
import vip.com.vipmeetings.models.ContactManual;
import vip.com.vipmeetings.utilities.Constants;

/**
 * Created by Srinath on 15/06/17.
 */

public class AddManuallyActivity extends BaseActivity {


    @BindView(R.id.tvsave)
    TextView tvsave;
    @BindView(R.id.etfirstname)
    EditText etfirstname;
    @BindView(R.id.etsurname)
    EditText etsurname;
    @BindView(R.id.ivchecked)
    ImageView ivchecked;

    @BindView(R.id.llcompany)
    LinearLayout llcompany;

    @BindView(R.id.etcompany)
    EditText etcompany;

    @BindView(R.id.tvemailtext)
    TextView tvemailtext;

    @BindView(R.id.etphone)
    EditText etphone;

    @BindView(R.id.etemail)
    EditText etemail;

    @BindView(R.id.etcode)
    EditText etcode;

    ContactManual contactManual;
    private List<ContactManual> contactManualList;

    @BindView(R.id.rlemail)
    RelativeLayout rlEmail;
   @BindView(R.id.rladd)
   RelativeLayout rlAdd;
    @BindView(R.id.rlphone)
    RelativeLayout rlPhone;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addmanual);
        ButterKnife.bind(this);

//        etcode.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS |
//                InputType.TYPE_CLASS_PHONE);

        etphone.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS |
                InputType.TYPE_CLASS_PHONE);

        etcompany.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS |
                InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);

        etfirstname.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS |
                InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        etsurname.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS |
                InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);


        if (isPatient()) {

            llcompany.setVisibility(View.GONE);
            tvemailtext.setVisibility(View.GONE);
        } else {
            llcompany.setVisibility(View.VISIBLE);
            tvemailtext.setVisibility(View.VISIBLE);
        }

        if (getIntent().hasExtra(Constants.EDITCONTACTS)) {
            etcode.setVisibility(View.GONE);
        } else {
            etcode.setVisibility(View.GONE);
        }
        onTokenRefresh();


        etemail.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS | InputType.TYPE_CLASS_TEXT |
                InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);


        etfirstname.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
        if (getIntent().hasExtra(Constants.POS)) {
            if (contactManual.getEmailId()!=null && !contactManual.getEmailId().isEmpty() && !contactManual.getEmailId().equalsIgnoreCase("''") ) {
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) rlEmail.getLayoutParams();
                params.addRule(RelativeLayout.BELOW, R.id.rladd);
                RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams) rlPhone.getLayoutParams();
                params1.addRule(RelativeLayout.BELOW, R.id.rlemail);
            }
        }

    }

    /*void below( View view) {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) llPhoneNumber.getLayoutParams();
        params.addRule(RelativeLayout.BELOW, R.id.below_id);
    }*/
    private void onTokenRefresh() {

        contactManualList = mGson.fromJson(getIntent().getStringExtra(Constants.CONTACTLIST),
                new TypeToken<List<ContactManual>>() {
                }.getType());

        etcompany.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (editable.length() > 0) {
                    ivchecked.setImageResource(R.mipmap.ic_off);
                }
            }
        });

        if (getIntent().hasExtra(Constants.POS)) {

            contactManual = mGson.fromJson(getIntent().getStringExtra(Constants.ADDCONTACT),
                    ContactManual.class);

            if (contactManual != null) {


                if (contactManual.isPrivate()) {

                    etcompany.setText("");
                    ivchecked.setImageResource(R.mipmap.ic_on);
                } else if (contactManual.getCompany() != null
                        && contactManual.getCompany().length() > 0
                        &&
                        !contactManual.getCompany().equalsIgnoreCase("''")) {
                    ivchecked.setImageResource(R.mipmap.ic_off);
                    etcompany.setText(contactManual.getCompany());
                }
                if (contactManual.getEmailId() != null
                        && !contactManual.getEmailId().equalsIgnoreCase("''"))
                    etemail.setText(contactManual.getEmailId());

                if (contactManual.isHasCountryCode()) {


                    if (!contactManual.getMobileNo().contains("+"))
                        contactManual.setMobileNo("+" + contactManual.getMobileNo());
                    else
                        contactManual.setMobileNo(contactManual.getMobileNo());

                }

                IpAddress.e("code1", contactManual.getMobileNo());


                if (contactManual.getMobileNo() != null
                        && !contactManual.getMobileNo().equalsIgnoreCase("''")) {

                    PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
                    try {
                        // phone must begin with '+'
                        Phonenumber.PhoneNumber numberProto = phoneUtil.parse(contactManual.getMobileNo(),
                                "");
                        int countryCode = numberProto.getCountryCode();

                      //  etcode.setText("+" + String.valueOf(countryCode));
                        IpAddress.e("code", countryCode + "" +
                                numberProto.getNationalNumber());
                        etphone.setText(String.valueOf(numberProto.getNationalNumber()));
                       // etcode.setVisibility(View.VISIBLE);
                        contactManual.setHasCountryCode(true);
                    } catch (NumberParseException e) {
                        IpAddress.e("NumberParseException was thrown: ", e.toString());
                        if (getIntent().hasExtra(Constants.EDITCONTACTS)) {
                            etcode.setVisibility(View.GONE);
                        } else {


                          //  etcode.setVisibility(View.VISIBLE);
                          //  etcode.setText("");
                        }


                        etphone.setText(contactManual.getMobileNo());
                        contactManual.setHasCountryCode(false);
                    }

                }

                if (contactManual.getSurName() != null
                        && !contactManual.getSurName().equalsIgnoreCase("''")) {
                    etsurname.setText(contactManual.getSurName());
                }

                if (contactManual.getFirstName() != null
                        && !contactManual.getFirstName().equalsIgnoreCase("''")) {


                    String[] split = contactManual.getFirstName().split("\\s+", 2);


                    if (etsurname.getText().length() == 0) {
                        if (split!=null && split.length == 2) {

                            etfirstname.setText(split[0]);
                            etsurname.setText(split[1]);
                        } else {
                            etfirstname.setText(contactManual.getFirstName());
                        }

                    } else {

                        etfirstname.setText(contactManual.getFirstName());
                    }
                }


            }

        }
        if (contactManual == null) {
          //  etcode.setText("+47");
            contactManual = new ContactManual();
        }
    }

    @OnClick(R.id.rlhide)
    public void hide(View v) {
        hideSoft();
    }

    @OnClick(R.id.ivhome)
    public void onHome(View v) {

        hideSoft();
        gotoHome();

    }


    @OnClick(R.id.rbprivate)
    public void onRbprivate(View v) {


        if (contactManual.isPrivate()) {

            contactManual.setPrivate(false);
            contactManual.setCompany(etcompany.getText().toString().trim());
            ivchecked.setImageResource(R.mipmap.ic_off);

        } else {

            ivchecked.setImageResource(R.mipmap.ic_on);
            contactManual.setPrivate(true);
            etcompany.setText("");
            contactManual.setCompany("");
        }

    }

    @OnClick(R.id.tvback)
    public void onBack() {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {


        hideSoft();
        super.onBackPressed();


    }

    @OnClick(R.id.tvsave)
    public void onSave(View v) {


        //if (etfirstname.getText().toString().trim().length() > 0) {

            // if (etsurname.getText().toString().trim().length() > 0) {


//            if (etcode.getText().toString().trim().length() > 0 ||
//                    getIntent().hasExtra(Constants.EDITCONTACTS)) {


                if (etphone.getText().toString().length() > 0 || etemail.getText().toString().length() > 0) {

                    saveData();

                    //if (etemail.getText().toString().length() > 0) {


//                        if (isValidEmail(etemail.getText().toString())) {
//
//                            saveData();
//                        } else {
//                            showToast("Please enter valid email");
//                        }

//                    } else {
//                        saveData();
//                        // showToast(getString(R.string.enteremail));
//                    }
                } else {
                    showToast(getString(R.string.enterphone));
                }

//            } else {
//                showToast(getString(R.string.entercode));
//
//            }

            // }
//            else {
//
//                showToast(getString(R.string.entersurname));
//            }


//        } else {
//
//            showToast(getString(R.string.entername));
//        }


    }


    //1253
    private void saveData() {


        contactManual.setCountry("");
        contactManual.setCity(getStoreCitySelect().getCityName());
        contactManual.setSendInvitation("YES");
        contactManual.setMeetingId(getMeetingId());
        contactManual.setCompany(etcompany.getText().toString());

/*
        if (etcode.getText().toString().startsWith("+")) {

            String code = etcode.getText().toString();

            //modifted before it was empty 20-mar-2019
            if (code.contains("+")) {

                // code = code.replaceAll("\\+", "00");

                contactManual.setMobileNo(code +
                        etphone.getText().toString().trim());
            } else {
                contactManual.setMobileNo(code +
                        etphone.getText().toString().trim());
            }

        } else {
            contactManual.setMobileNo(etcode.getText().toString().trim() +
                    etphone.getText().toString().trim());
        }*/
        contactManual.setMobileNo(etphone.getText().toString().trim());
        contactManual.setFirstName(etfirstname.getText().toString().trim());
        contactManual.setSurName(etsurname.getText().toString().trim());
        contactManual.setEmailId(etemail.getText().toString().trim());


        if (contactManualList != null && contactManualList.size() > 0) {

            if (contactManualList.contains(contactManual)) {

               if(containsEmail(contactManualList, contactManual.getEmailId()))
               {
                   hideSoft();
                   Toast.makeText(AddManuallyActivity.this, "Contact already exist",
                           Toast.LENGTH_SHORT).show();

               }
               else if(containsMobileNum(contactManualList, contactManual.getMobileNo())){
                   hideSoft();
                   Toast.makeText(AddManuallyActivity.this, "Contact already exist",
                           Toast.LENGTH_SHORT).show();
               }
               else{
                   addContactManual();
               }

               /* for (int i = 0; i < contactManualList.size(); i++) {
                    // ContactManual contact = contactManualList.get(i);
                     if (!contactManualList.get(i).getEmailId().isEmpty() && contactManualList.get(i).getEmailId().equalsIgnoreCase(contactManual.getEmailId())){
                         hideSoft();
                         Toast.makeText(AddManuallyActivity.this, "Contact already exist",
                                 Toast.LENGTH_SHORT).show();
                         return;
                     }
                     else if(!contactManualList.get(i).getMobileNo().isEmpty() && contactManualList.get(i).getMobileNo().equalsIgnoreCase(contactManual.getMobileNo())){
                         hideSoft();
                         Toast.makeText(AddManuallyActivity.this, "Contact already exist",
                                 Toast.LENGTH_SHORT).show();
                         return;
                     }
                     else if(!contactManualList.get(i).getEmailId().isEmpty() && !contactManualList.get(i).getEmailId().equalsIgnoreCase(contactManual.getEmailId()) && !contactManualList.get(i).getMobileNo().isEmpty() && !contactManualList.get(i).getMobileNo().equalsIgnoreCase(contactManual.getMobileNo())){
                         addContactManual();
                     }

                }*/


            } else {
                addContactManual();
            }


        } else {

            addContactManual();
        }
    }
    public static boolean containsEmail(List<ContactManual> c, String email) {
        for(ContactManual o : c) {
            if(o != null && !o.getEmailId().isEmpty() && o.getEmailId().equals(email)) {
                return true;
            }
        }
        return false;
    }
    public static boolean containsMobileNum(List<ContactManual> c, String mobileNum) {
        for(ContactManual o : c) {
            if(o != null && !o.getMobileNo().isEmpty() && o.getMobileNo().equals(mobileNum)) {
                return true;
            }
        }
        return false;
    }
    private void addContactManual() {

        if (getIntent().hasExtra(Constants.POS)) {

            hideSoft();
            Intent i = new Intent();
            i.putExtra(Constants.ADDCONTACT, mGson.toJson(contactManual));
            i.putExtra(Constants.POS, getIntent().getIntExtra(Constants.POS, 0));
            setResult(RESULT_OK, i);
            finish();

        } else {
            hideSoft();
            Intent i = new Intent();
            i.putExtra(Constants.ADDCONTACT, mGson.toJson(contactManual));
            setResult(RESULT_OK, i);
            finish();
        }
    }


}
