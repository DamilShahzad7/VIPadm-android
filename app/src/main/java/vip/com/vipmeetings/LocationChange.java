package vip.com.vipmeetings;

import android.location.Location;

import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * Created by Srinath on 12/03/18.
 */

public class LocationChange extends LocationCallback {


    EventBus eventBus;


    public LocationChange() {
        super();
        eventBus = EventBus.getDefault();
    }

    @Override
    public void onLocationResult(LocationResult locationResult) {
        super.onLocationResult(locationResult);
        List<Location> locationList = locationResult.getLocations();

        boolean loc = true;
        for (Location location : locationList) {

            if (location != null) {

                loc = false;
                eventBus.post(location);
                break;
            }
        }
        if (locationResult.getLastLocation() != null) {

            if (loc)
                eventBus.post(locationResult.getLastLocation());

        }
    }

    @Override
    public void onLocationAvailability(LocationAvailability locationAvailability) {
        super.onLocationAvailability(locationAvailability);
        if (locationAvailability.isLocationAvailable()) {
            // Constants.e("LOCATION", IpAddress.LOCATIONAVAILABLE);
        } else {
            // Constants.e("LOCATION", IpAddress.LOCATIONAVAILABLE_NO);
        }
    }
}
