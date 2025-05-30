package vip.com.vipmeetings;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import vip.com.vipmeetings.body.FindAvailableRoomsRequestBody;
import vip.com.vipmeetings.body.FindAvailableRoomsResponseBody;
import vip.com.vipmeetings.envelope.SoapFindAvailableRoomsRequestEnvelope;
import vip.com.vipmeetings.envelope.SoapFindAvailableRoomsResponseEnvelope;
import vip.com.vipmeetings.interfaces.MainApi;
import vip.com.vipmeetings.models.ArrayOfRoomProfile;
import vip.com.vipmeetings.models.City;
import vip.com.vipmeetings.models.RoomProfile;
import vip.com.vipmeetings.request.FindAvailableRooms;
import vip.com.vipmeetings.utilities.Constants;

/**
 * Created by Srinath on 29/05/17.
 */

public class RoomListActivity extends BaseActivity {


    @BindView(R.id.rvroom)
    RecyclerView rvroom;


    @BindView(R.id.tvdate)
    TextView tvdate;

    @BindView(R.id.tvsubject)
    TextView tvsubject;

    @BindView(R.id.tvtime)
    TextView tvtime;

    @BindView(R.id.tvskip)
    TextView tvskip;

    @BindView(R.id.tvlocation)
    TextView tvlocation;

    @BindView(R.id.tvedit)
    TextView tvedit;


    @BindView(R.id.ivhome)
    ImageView ivhome;

    RoomAdapter roomAdapter;
    List<RoomProfile> roomList;
    DateTimeModel dateTimeModel;

    StringBuilder sb;
    private static int Location = 11;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roomlist);
        ButterKnife.bind(this);

        dateTimeModel = getDateTimeModel(getIntent());

        if (dateTimeModel != null) {


            tvlocation.setText(getCityName_Address());
            tvsubject.setText(dateTimeModel.getSubject());
            tvdate.setText(
                    dateTimeModel.getDateString());
            tvtime.setText(
                    String.format("%02d", dateTimeModel.getStartHour()) + "." +
                            String.format("%02d", dateTimeModel.getStartMinute()) + " - " +
                            String.format("%02d", dateTimeModel.getEndHour()) + "." +
                            String.format("%02d", dateTimeModel.getEndMinute()));
        }
        roomList = new ArrayList<>();
        rvroom.setLayoutManager(new LinearLayoutManager(this));
        roomAdapter = new RoomAdapter();
        rvroom.setAdapter(roomAdapter);


        getRooms();
    }

    @OnClick(R.id.tvskip)
    public void onSkip() {
        dateTimeModel.setInvite(true);
        Intent i = new Intent(this, ConfirmationActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.putExtra(Constants.DATETIMEMODEL, mGson.toJson(dateTimeModel));
        startActivity(i);

    }


    @OnClick(R.id.tvedit)
    public void onEdit() {
        Intent i = new Intent(this, LocationActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.putExtra(Constants.SKIPROOMS, true);
        startActivityForResult(i, Location);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == Location) {


            City city = getStoreCitySelect();

            if (city != null && city.getRoomCount() != null &&
                    !city.getRoomCount().equalsIgnoreCase("0")
                    && city.getRoomCount().trim().length() > 0) {


                getRooms();


            } else {

                roomList.clear();
                roomAdapter.notifyDataSetChanged();


            }
            tvlocation.setText(getCityName_Address());

        }
    }

    @OnClick(R.id.ivhome)
    public void onHome(View v) {

        gotoHome();

    }

    @OnClick(R.id.tvback)
    public void onBack(View v) {

        onBackPressed();
    }

    //1253
    private DateTimeModel getDateTimeModel(Intent intent) {


        dateTimeModel = mGson.fromJson(intent.getStringExtra(Constants.DATETIMEMODEL), DateTimeModel.class);
        dateTimeModel.setCityCode(getStoreCitySelect().getCityCode());
        dateTimeModel.setCountryCode(getStoreCitySelect().getCityCode());

        return dateTimeModel;
    }

    private void getRooms() {

        try {
            showPD();
            setRetrofit(getApp().getTcpIp());
            mainApi = retrofit.create(MainApi.class);

            SoapFindAvailableRoomsRequestEnvelope
                    soapFindAvailableRoomsRequestEnvelope = new SoapFindAvailableRoomsRequestEnvelope();

            FindAvailableRoomsRequestBody findAvailableRoomsRequestBody = new FindAvailableRoomsRequestBody();


            FindAvailableRooms findAvailableRooms = new FindAvailableRooms();

            findAvailableRooms.setAuthToken(getAuthTokenPref_Mobile());
            findAvailableRooms.setA_sCityCode(getStoreCitySelect().getCityCode());
            findAvailableRooms.setA_sUserEmail(getEmail());
            findAvailableRooms.setA_sFromDateYMD(dateTimeModel.getFormatDate());
            findAvailableRooms.setA_sToDateYMD(dateTimeModel.getFormatDate());
            findAvailableRooms.setA_sFromTimeHM(dateTimeModel.getStartHour() + "." + dateTimeModel.getStartMinute());
            findAvailableRooms.setA_sToTimeHM(dateTimeModel.getEndHour() + "." + dateTimeModel.getEndMinute());

            findAvailableRoomsRequestBody.setFindAvailableRooms(findAvailableRooms);

            soapFindAvailableRoomsRequestEnvelope.setBody(findAvailableRoomsRequestBody);

            Observable<SoapFindAvailableRoomsResponseEnvelope> lClientDetailsObservable =
                    mainApi.findAvailableRooms2(soapFindAvailableRoomsRequestEnvelope);

            lClientDetailsObservable.
                    subscribeOn(Schedulers.newThread())
                    .map(this::onSoapRoom)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::onRooms, this::onError);
        } catch (Exception e) {
            hidePD();
            Constants.e("rooms", e.toString());
        }

    }

    private List<RoomProfile> onSoapRoom(SoapFindAvailableRoomsResponseEnvelope soapFindAvailableRoomsResponseEnvelope) {


        FindAvailableRoomsResponseBody findAvailableRoomsResponseBody =
                soapFindAvailableRoomsResponseEnvelope.getBody();

        return findAvailableRoomsResponseBody.getAddContactLeaveResponse().getFindAvailableRoomsResult().getRoomProfile();
    }

    private void onRooms(List<RoomProfile> rooms) {


        hidePD();
        roomList.clear();

        if (rooms != null && rooms.size() > 0
                && rooms.get(0).getStatus() != null
                && rooms.get(0).getStatus().equalsIgnoreCase("Failed")) {


            showMessage(rooms.get(0).getStatus());
            roomAdapter.notifyDataSetChanged();

        } else if (rooms != null && rooms.size() > 0 && rooms.get(0) != null) {

            roomList.addAll(rooms);

        }
        roomAdapter.notifyDataSetChanged();

    }

//    private void showMessage(String status) {
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setMessage(status);
//        builder.setCancelable(false);
//        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//
//
//                finish();
//            }
//        });
//        builder.show();
//    }


    private void onRooms(ArrayOfRoomProfile rooms) {


        hidePD();
        roomList.clear();
        if (rooms != null && rooms.getRoomProfile() != null) {

            roomList.addAll(rooms.getRoomProfile());

        }
        roomAdapter.notifyDataSetChanged();

    }


    public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.VH> {


        @Override
        public VH onCreateViewHolder(ViewGroup parent, int viewType) {
            return new VH(getLayoutInflater().inflate(R.layout.inflate_roominflate, parent, false));
        }

        @Override
        public void onBindViewHolder(VH holder, int position) {


            RoomProfile room = roomList.get(position);

            holder.rlbook.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    RoomProfile room = roomList
                            .get(holder.getAdapterPosition());
                    if (!room.getStatus().equalsIgnoreCase("Failed")) {
                        dateTimeModel.setRoomName(room.getRoomName());
                        dateTimeModel.setRoomId(room.getRoomId());
                        dateTimeModel.setRoomURL(room.getImage());
                        dateTimeModel.setImage1(room.getImage());
                        dateTimeModel.setInvite(false);
                        Intent i = new Intent(RoomListActivity.this, ConfirmationActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        i.putExtra(Constants.DATETIMEMODEL, mGson.toJson(dateTimeModel));
                        startActivity(i);
                    }

                }
            });

            if (room.getRoomName() != null)
                holder.tvroomname.setText(room.getRoomName());
            else
                holder.tvroomname.setText("");
            sb = new StringBuilder();
            if (room.getCapacity() != null && room.getFloorNumber() != null) {
                sb.append("Seats : " + room.getCapacity() + "   Floor : " + room.getFloorNumber());
            } else if (room.getCapacity() != null) {
                sb.append("Seats : " + room.getCapacity());

            } else if (room.getFloorNumber() != null) {
                sb.append("   Floor : " + room.getFloorNumber());
            }
            holder.tvseats.setText(sb.toString());


            if(room.getImage()!=null)
            holder.ivroom.setImageURI(room.getImage());

        }

        @Override
        public int getItemCount() {
            return roomList.size();
        }

        public class VH extends RecyclerView.ViewHolder {


            @BindView(R.id.tvroomname)
            TextView tvroomname;
            @BindView(R.id.tvseats)
            TextView tvseats;
            @BindView(R.id.ivroom)
            SimpleDraweeView ivroom;

            @BindView(R.id.rlbook)
            RelativeLayout rlbook;


            public VH(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }


    }
}
