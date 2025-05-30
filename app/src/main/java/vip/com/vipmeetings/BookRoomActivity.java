package vip.com.vipmeetings;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;

import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import vip.com.vipmeetings.body.FindRoomRequestBody;
import vip.com.vipmeetings.body.FindRoomResponseBody;
import vip.com.vipmeetings.envelope.SoapFindRoomRequestEnvelope;
import vip.com.vipmeetings.envelope.SoapFindRoomResponseEnvelope;
import vip.com.vipmeetings.interfaces.MainApi;
import vip.com.vipmeetings.models.RoomAvailability;
import vip.com.vipmeetings.models.RoomProfile;
import vip.com.vipmeetings.request.FindRoomAvailability;
import vip.com.vipmeetings.utilities.Constants;

/**
 * Created by Srinath on 29/05/17.
 */

public class BookRoomActivity extends BaseActivity {


    RoomProfile room;
    DateTimeModel dateTimeModel;


    @BindView(R.id.tvnext)
    TextView tvnext;

    @BindView(R.id.tvdate)
    TextView tvdate;

    @BindView(R.id.tvsubject)
    TextView tvsubject;

    @BindView(R.id.tvtime)
    TextView tvtime;


    @BindView(R.id.tvroomname)
    TextView tvroomname;
    @BindView(R.id.tvseats)
    TextView tvseats;
    @BindView(R.id.tvfloors)
    TextView tvfloors;
    @BindView(R.id.ivroom)
    SimpleDraweeView ivroom;

    @BindView(R.id.ivbooking)
    SimpleDraweeView ivbooking;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookroom);

        ButterKnife.bind(this);
        room = mGson.fromJson(getIntent().getStringExtra(Constants.BOOKROOM),
                RoomProfile.class);

        dateTimeModel = mGson.fromJson(getIntent().getStringExtra(Constants.DATETIMEMODEL), DateTimeModel.class);

        if (room != null && dateTimeModel != null) {


            dateTimeModel.setRoomName(room.getRoomName());
            dateTimeModel.setRoomId(room.getRoomId());
            dateTimeModel.setRoomURL(room.getImage());
            tvsubject.setText(dateTimeModel.getSubject());
            tvdate.setText(dateTimeModel.getDateString());
            tvtime.setText(
                    String.format("%02d", dateTimeModel.getStartHour()) + "." +
                            String.format("%02d", dateTimeModel.getStartMinute()) + " - " +
                            String.format("%02d", dateTimeModel.getEndHour()) + "." +
                            String.format("%02d", dateTimeModel.getEndMinute()));

            tvroomname.setText(room.getRoomName());


        //    tvseats.setText("Seats : " + room.getCapacity());

            String text1 = "<font color=#64829a> Seats : " + "</font> <font color=#000000>" + room.getCapacity() + "</font>";
            tvseats.setText(Html.fromHtml(text1));

//            tvfloors.setText( " Floor : "
//                    + room.getFloorNumber());

            String text2 = "<font color=#64829a> Floor : " + "</font> <font color=#000000>" + room.getFloorNumber() + "</font>";
            tvfloors.setText(Html.fromHtml(text2));


            ivroom.setImageURI(room.getImage());
            ivbooking.setImageURI(room.getImage());

        }

    }

    @OnClick(R.id.ivhome)
    public void onHome(View v) {

        gotoHome();

    }


    private void bookRoom() {

        setRetrofit(getApp().getTcpIp());
        MainApi mainApi = retrofit.create(MainApi.class);

        Constants.e("GSON1", mGson.toJson(dateTimeModel));


        SoapFindRoomRequestEnvelope soapFindRoomRequestEnvelope = new SoapFindRoomRequestEnvelope();

        FindRoomRequestBody findRoomRequestBody = new FindRoomRequestBody();

        FindRoomAvailability findRoomAvailability = new FindRoomAvailability();
        findRoomAvailability.setAuthToken(getAuthTokenPref_Mobile());
        findRoomAvailability.setA_sRoomId(dateTimeModel.getRoomId());
        findRoomAvailability.setA_sRoomName(dateTimeModel.getRoomName());
        findRoomAvailability.setA_sFromDateYMD(dateTimeModel.getFormatDate());
        findRoomAvailability.setA_sToDateYMD(dateTimeModel.getFormatDate());

        findRoomAvailability.setA_sFromTime(dateTimeModel.getStartHour() + "." +
                dateTimeModel.getStartMinute());
        findRoomAvailability.setA_sToTime(dateTimeModel.getEndHour() + "." + dateTimeModel.getEndMinute());

        findRoomAvailability.setA_sUserId(employModel.getUSERID());
        findRoomAvailability.setA_sMeetingId("");
        findRoomAvailability.setA_sRoomSelectionId("");


        findRoomRequestBody.setFindRoomAvailability(findRoomAvailability);
        soapFindRoomRequestEnvelope.setBody(findRoomRequestBody);
        Observable<SoapFindRoomResponseEnvelope> lClientDetailsObservable =
                mainApi.FindRoomAvailability(soapFindRoomRequestEnvelope);

        lClientDetailsObservable.
                subscribeOn(Schedulers.newThread())
                .map(this::onSoapFindRoom)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::findRoom, this::onError);
    }

    private RoomAvailability onSoapFindRoom(SoapFindRoomResponseEnvelope soapFindRoomResponseEnvelope) {



        FindRoomResponseBody findRoomResponseBody = soapFindRoomResponseEnvelope.getBody();

        return findRoomResponseBody.getFindRoomAvailabilityResponse()
                .getFindRoomAvailabilityResult().getRoomAvailability();
    }

    private void findRoom(RoomAvailability status) {


        if (status != null && status.getStatus() != null) {

            if (status.getStatus().get_status().equalsIgnoreCase("Success")) {


                if (status.getRoomSelectionId() != null) {

                    setRoomSelection(status.getRoomSelectionId().get_roomselectionid());
                    Intent i = new Intent(this, ConfirmationActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    i.putExtra(Constants.DATETIMEMODEL, mGson.toJson(dateTimeModel));
                    startActivity(i);
                }


            } else if (status.getStatus().get_status().equalsIgnoreCase("FAILED")) {
                {

                    showToast(status.getError().get_error() + "");
                }
            }

        } else {

            showToast(getString(R.string.someerror));
        }

    }


    @OnClick(R.id.tvnext)
    public void onNext() {

        if (room != null && dateTimeModel != null) {


            bookRoom();

        }
    }


    @OnClick({R.id.tvback, R.id.llback})
    public void onBack(View v) {

        onBackPressed();
    }
}
