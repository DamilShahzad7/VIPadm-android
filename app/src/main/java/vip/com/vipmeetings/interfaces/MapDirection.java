package vip.com.vipmeetings.interfaces;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;
import vip.com.vipmeetings.utilities.Constants;

/**
 * Created by Srinath on 16/03/18.
 */

public interface MapDirection {


    @GET(Constants.POSTDIRECTION)
    public Observable<String> piingCustomerDirection(@Query("origin") String origin,
                                                     @Query("destination") String destination,
                                                     @Query("sensor") boolean sensor
            , @Query("key") String key
    );
    //String url = "http://maps.googleapis.com/maps/api/geocode/json?latlng="
    //                    + lati + "," + longi + "&sensor=false" + "& key = " + getString(R.string.api);

    // String url = "https://maps.googleapis.com/maps/api/geocode/json?address="
    //                    + strAddress.trim() + "&key=" + getString(R.string.api);


    @GET(Constants.POSTGEOCODE)
    public Observable<String> piingAddressByLocation(@Query("latlng") String location
            , @Query("key") String key
    );

    @GET(Constants.POSTGEOCODE)
    public Observable<String> piinggetLocationByNameorCityName(@Query("address") String origin
            , @Query("key") String key
    );

}
