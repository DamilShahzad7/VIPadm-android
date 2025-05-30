package vip.com.vipmeetings;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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
import vip.com.vipmeetings.body.GetRoomsRequestBody;
import vip.com.vipmeetings.body.GetRoomsResponseBody;
import vip.com.vipmeetings.envelope.SoapGetRoomsRequestEnvelope;
import vip.com.vipmeetings.envelope.SoapGetRoomsResponseEnvelope;
import vip.com.vipmeetings.interfaces.MainApi;
import vip.com.vipmeetings.models.Room;
import vip.com.vipmeetings.models.Rooms;
import vip.com.vipmeetings.request.GetRooms;
import vip.com.vipmeetings.utilities.Constants;

/**
 * Created by Srinath on 25/06/17.
 */

public class BookSpecificRoomActivity extends BaseActivity {


    List<Room> roomList;
    @BindView(R.id.tvroomname)
    TextView tvroomname;

    @BindView(R.id.tvfloors)
    TextView tvfloors;

    @BindView(R.id.tvseats)
    TextView tvseats;

    @BindView(R.id.rvroom)
    RecyclerView rvroom;
    @BindView(R.id.ivroom)
    SimpleDraweeView ivroom;
    RoomAdapter roomAdapter;

    Room room;

    DateTimeModel bookSpecificModel;
    StringBuilder sb;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookspecificroom);
        ButterKnife.bind(this);
        roomList = new ArrayList<>();
        rvroom.setLayoutManager(new LinearLayoutManager(this));
        roomAdapter = new RoomAdapter();
        rvroom.setAdapter(roomAdapter);
        bookSpecificModel = new DateTimeModel();
        bookSpecificModel.setSpecific(true);
        getRooms();
    }


    @OnClick(R.id.tvnext)
    public void onNext(View v) {

        if (bookSpecificModel != null && bookSpecificModel.getRoomId() != null)
            gotoBookDatetime();
    }

    private void gotoBookDatetime() {

        Intent intent = new Intent(BookSpecificRoomActivity.this,
                BookSpecificRoomDateTimeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(Constants.BOOKROOMSPECIFIC, mGson.toJson(bookSpecificModel));
        startActivity(intent);
    }

    private void getRooms() {

        showPD();
        setRetrofit(getApp().getTcpIp());
        mainApi = retrofit.create(MainApi.class);


        SoapGetRoomsRequestEnvelope soapGetRoomsRequestEnvelope = new SoapGetRoomsRequestEnvelope();

        GetRoomsRequestBody getRoomsRequestBody = new GetRoomsRequestBody();

        GetRooms getRooms = new GetRooms();
        getRooms.setAuthToken(getAuthTokenPref_Mobile());
        if(getStoreCitySelect()!=null && getStoreCitySelect().getCityCode()!=null)
            getRooms.setA_iCityCode(getStoreCitySelect().getCityCode());
        else
            getRooms.setA_iCityCode("");
        getRooms.setA_iCapacity("0");
        getRooms.setA_iUserId(employModel.getUSERID());
        getRoomsRequestBody.setGetRooms(getRooms);
        soapGetRoomsRequestEnvelope.setBody(getRoomsRequestBody);

        Observable<SoapGetRoomsResponseEnvelope> lClientDetailsObservable =
                mainApi.getRooms(soapGetRoomsRequestEnvelope);
        lClientDetailsObservable.
                subscribeOn(Schedulers.newThread())
                .map(this::onSoapRooms)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onRooms, this::onError);

    }

    private Rooms onSoapRooms(SoapGetRoomsResponseEnvelope soapGetRoomsResponseEnvelope) {


        GetRoomsResponseBody getRoomsResponseBody = soapGetRoomsResponseEnvelope.getBody();

        return getRoomsResponseBody.getGetRoomsResponse().getGetRoomsResult().getRooms();
    }

    @OnClick(R.id.ivhome)
    public void onHome(View v) {
        onBackPressed();

    }

    @OnClick(R.id.tvback)
    public void onBack(View v) {
        onBackPressed();
    }

    private void onRooms(Rooms rooms) {


        hidePD();
        roomList.clear();
        if (rooms != null && rooms.getRoom() != null) {

            roomList.addAll(rooms.getRoom());

        }
        roomAdapter.notifyDataSetChanged();

    }


    public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.VH> {


        @Override
        public VH onCreateViewHolder(ViewGroup parent, int viewType) {
            return new VH(getLayoutInflater().inflate(R.layout.inflate_roominflate, parent,false));
        }

        @Override
        public void onBindViewHolder(final VH holder, int position) {


            Room room1 = roomList.get(position);

            if (bookSpecificModel != null && bookSpecificModel.getRoomId() != null) {


                if (bookSpecificModel.getRoomId().equalsIgnoreCase(room1.getRoomId())) {
                    holder.llbg.setBackgroundResource(R.drawable.rect_room1);
                } else {
                    holder.llbg.setBackgroundResource(R.drawable.rect_room);
                }

            } else {
                holder.llbg.setBackgroundResource(R.drawable.rect_room);
            }

            holder.rlbook.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    room = roomList.get(holder.getAdapterPosition());


//                    if (room.getFloorNumber() != null) {
//                        tvfloors.setText("Floor : " + room.getFloorNumber());
//                    }

                    if (room.getFloorNumber() != null) {
                        String text = "<font color=#64829a> Floor : " + "</font> <font color=#000000>" + room.getFloorNumber() + "</font>";
                        tvfloors.setText(Html.fromHtml(text));
                    }
//                    if (room.getCapacity() != null)
//                        tvseats.setText("Seats : " + room.getCapacity());

                    if (room.getCapacity() != null) {
                        String text1 = "<font color=#64829a> Seats : " + "</font> <font color=#000000>" + room.getCapacity() + "</font>";
                        tvseats.setText(Html.fromHtml(text1));
                    }

                    if (room.getRoomName() != null)
                        tvroomname.setText(room.getRoomName());
                    ivroom.setImageURI(room.getRoomImage());
                    bookSpecificModel.setCapacity(room.getCapacity());
                    bookSpecificModel.setRoomId(room.getRoomId());
                    bookSpecificModel.setFloorNumber(room.getFloorNumber());

                    bookSpecificModel.setRoomName(room.getRoomName());
                    bookSpecificModel.setImage1(room.getRoomImage());
                    bookSpecificModel.setRoomURL(room.getRoomImage());
                    gotoBookDatetime();

                }
            });

            if (room1.getRoomName() != null)
                holder.tvroomname.setText(room1.getRoomName());

            sb = new StringBuilder();

            if (room1.getCapacity() != null && room1.getFloorNumber() != null) {
                sb.append("Seats : " + room1.getCapacity() + "      " +
                        "Floor : " + room1.getFloorNumber());

            } else if (room1.getCapacity() != null) {
                sb.append("Seats : " + room1.getCapacity());
            } else if (room1.getFloorNumber() != null) {
                sb.append("      " +
                        "Floor : " + room1.getFloorNumber());
            }
            holder.tvseats.setText(sb.toString());

            holder.ivroom.setImageURI(room1.getRoomImage());

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

            @BindView(R.id.llbg)
            LinearLayout llbg;


            public VH(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }


    }
}
