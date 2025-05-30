package vip.com.vipmeetings;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import vip.com.vipmeetings.evacuate.IpAddress;
import vip.com.vipmeetings.models.City;
import vip.com.vipmeetings.utilities.Constants;

/**
 * Created by Srinath on 25/06/17.
 */

public class SkipRoomActivity extends BaseLocationActivity implements OnMapReadyCallback {


    @BindView(R.id.tvsave)
    TextView tvsave;

    @BindView(R.id.tvcontact)
    TextView tvcontact;

    @BindView(R.id.tvdate)
    TextView tvdate;

    @BindView(R.id.tvtime)
    TextView tvtime;

    @BindView(R.id.tvroom)
    TextView tvroom;

    @BindView(R.id.tvsms)
    TextView tvsms;

    @BindView(R.id.tvsubject)
    TextView tvsubject;

    @BindView(R.id.ivplaceholder)
    SimpleDraweeView ivplaceholder;

    Intent intent;
    DateTimeModel dateTimeModel;

    SupportMapFragment mapFragment;
    private GoogleMap googleMap;
    private LatLng pos_desti;
    City city;
    Location mLocation_desti;
    Bitmap smallMarker;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skiproom);
        ButterKnife.bind(this);
        int height = IpAddress.MARKER_HEIGHT;
        int width = IpAddress.MARKER_WIDTH;
        BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.mipmap.marker_myloc2);
        Bitmap b = bitmapdraw.getBitmap();
        smallMarker = Bitmap.createScaledBitmap(b, width, height, false);

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_fragment);
        mapFragment.getMapAsync(this);

        intent = getIntent();
        dateTimeModel = mGson.fromJson(intent.getStringExtra(Constants.DATETIMEMODEL),
                DateTimeModel.class);

        if (dateTimeModel != null) {
            tvdate.setText(dateTimeModel.getDateString());
            tvsubject.setText(dateTimeModel.getSubject());
            tvtime.setText(String.format("%02d", dateTimeModel.getStartHour()) + "." +
                    String.format("%02d", dateTimeModel.getStartMinute()) + " - " +
                    String.format("%02d", dateTimeModel.getEndHour()) + "." +
                    String.format("%02d", dateTimeModel.getEndMinute()));


            if (dateTimeModel.getRoomName() != null &&
                    dateTimeModel.getRoomName().length() > 0) {

                ivplaceholder.setVisibility(View.VISIBLE);
                tvroom.setText("Room : " + dateTimeModel.getRoomName());

            } else {

                tvroom.setText("Location : " + getCityName_Address());
                ivplaceholder.setVisibility(View.VISIBLE);
            }

            if (getStoreCitySelect() != null
                    && (getStoreCitySelect().getSkipRoom().equalsIgnoreCase("True")
                    || getStoreCitySelect().getRoomCount().equalsIgnoreCase("0"))) {
                ivplaceholder.setVisibility(View.VISIBLE);
                dateTimeModel.setRoomURL(getStoreCitySelect().getCityMapUrl());
                ivplaceholder.setImageURI(dateTimeModel.getImage1());
            } else if (getStoreCitySelect().getRoomCount().equalsIgnoreCase("0")) {
                ivplaceholder.setVisibility(View.VISIBLE);
                dateTimeModel.setRoomURL(getStoreCitySelect().getCityMapUrl());
                Constants.e("url", dateTimeModel.getRoomURL());
                ivplaceholder.setImageURI(dateTimeModel.getImage1());
            } else {
                ivplaceholder.setVisibility(View.VISIBLE);
                ivplaceholder.setImageURI(dateTimeModel.getImage1());
            }


            if (dateTimeModel.getContactString() != null &&
                    dateTimeModel.getContactString().length() > 0) {
                tvcontact.setText(dateTimeModel.getContactString());
            } else {
                tvsms.setVisibility(View.GONE);
                tvcontact.setText("");
            }
            if (dateTimeModel.isInvite()) {
                ivplaceholder.setImageResource(0);
                ivplaceholder.setVisibility(View.VISIBLE);
                tvroom.setText("");
            }
        }
    }

    boolean flag;

    @Override
    public void getLocation(Location mSource) {

        if (pos_desti == null) {
            pos_desti = new LatLng(mSource.getLatitude(), mSource.getLongitude());
            addMarker(pos_desti);
            if (googleMap != null) {
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pos_desti, IpAddress.ZOOMCAMERA));
            }
        }

    }


    @OnClick(R.id.ivhome)
    public void onHome(View v) {
        Intent i = new Intent(this, HomeActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        finish();

    }

    @Override
    public void onBackPressed() {
        // super.onBackPressed();
    }

    @OnClick(R.id.tvsave)
    public void onSave(View v) {


        Intent i = new Intent(this, HomeActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        finish();

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        this.googleMap = googleMap;
        onMapLoad();

    }

    private void onMapLoad() {

        try {
            city = getStoreCitySelect();
            if (city != null) {
                mLocation_desti = new Location("des");
                if (city.getLongitude() != 0 && city.getLatitude() != 0) {

                    mLocation_desti.setLatitude(city.getLatitude());
                    mLocation_desti.setLongitude(city.getLongitude());
                    pos_desti = new LatLng(mLocation_desti.getLatitude(), mLocation_desti.getLongitude());
                    addMarker(pos_desti);
                    if (googleMap != null) {
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pos_desti, IpAddress.ZOOMCAMERA));
                    }
                } else if (mSharedPreferences.contains(Constants.LAT) && !mSharedPreferences.getString(Constants.LAT, "0").equalsIgnoreCase("0")) {

                    mLocation_desti.setLatitude(Double.parseDouble
                            (mSharedPreferences.getString(Constants.LAT, "0")));
                    mLocation_desti.setLongitude(Double.parseDouble
                            (mSharedPreferences.getString(Constants.LON, "0")));
                    pos_desti = new LatLng(mLocation_desti.getLatitude(), mLocation_desti.getLongitude());
                    addMarker(pos_desti);
                    if (googleMap != null) {
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pos_desti, IpAddress.ZOOMCAMERA));
                    }
                } else if (mSharedPreferences.contains(IpAddress.LOCATIONLAT)) {
                    mLocation_desti.setLatitude(Double.parseDouble
                            (mSharedPreferences.getString(IpAddress.LOCATIONLAT, "0")));
                    mLocation_desti.setLongitude(Double.parseDouble
                            (mSharedPreferences.getString(IpAddress.LOCATIONLON, "0")));
                    pos_desti = new LatLng(mLocation_desti.getLatitude(), mLocation_desti.getLongitude());
                    addMarker(pos_desti);
                    if (googleMap != null) {
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pos_desti, IpAddress.ZOOMCAMERA));
                    }
                }

            } else {

                if (marker_myloc != null) {
                    marker_myloc.remove();
                    marker_myloc = null;
                }
            }
        } catch (Exception e) {

        }
    }

    private void addMarker(LatLng pos_desti) {

        if (googleMap != null && pos_desti != null) {


            if (marker_myloc == null) {

                if (city != null && city.getAddress() != null
                        && !city.getAddress().isEmpty()
                        && city.getAddress().trim().length() > 0) {
                    marker_myloc = googleMap.
                            addMarker(new MarkerOptions()
                                    .position(pos_desti)
                                    .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
                                    .title(city.getAddress())
                            );
                } else

                    marker_myloc = googleMap.
                            addMarker(new MarkerOptions()
                                    .position(pos_desti)
                                    .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
                                    .title(getStoreCitySelect().getCityName() + "")
                            );
            } else {
                marker_myloc.setPosition(pos_desti);
                if (city != null && city.getAddress() != null
                        && !city.getAddress().isEmpty()
                        && city.getAddress().trim().length() > 0) {
                    marker_myloc.setTitle(city.getAddress());
                } else
                    marker_myloc.setTitle(getStoreCitySelect().getCityName() + "");
            }


        }
    }
}
