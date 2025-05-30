package vip.com.vipmeetings.models;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Created by Srinath on 30/05/17.
 */
@Root(name = "City", strict = false)
public class City {

    //
    @Element(name = "UseCatering", required = false)
    private String UseCatering;
    @Element(name = "CountryName", required = false)
    private String CountryName;
    @Element(name = "CityName", required = false)
    private String CityName;
    @Element(name = "SkipRoom", required = false)
    private String SkipRoom = "True";
    @Element(name = "CityCode", required = false)
    private String CityCode;
    @Element(name = "RoomCount", required = false)
    private String RoomCount;
    @Element(name = "CityMapUrl", required = false)
    private String CityMapUrl;
    @Element(required = false)
    boolean isSelect;
    @Element(required = false)
    boolean isLocal;
    @Element(name = "Address", required = false)
    String Address;
    @Element(required = false)
    String identity;
    @Element(name = "latitude", required = false)
    double latitude;
    @Element(name = "longitude", required = false)
    double longitude;
    String placeID;


    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getRoomCount() {
        return RoomCount;
    }

    public void setRoomCount(String roomCount) {
        RoomCount = roomCount;
    }

    public String getCityMapUrl() {
        return CityMapUrl;
    }

    public void setCityMapUrl(String cityMapUrl) {
        CityMapUrl = cityMapUrl;
    }

    @Override
    public String toString() {

        if (CityName != null)
            return CityName;
        else if (Address != null)
            return Address;

        return super.toString();
    }


    @Override
    public int hashCode() {


        return CityName.hashCode();
    }


    @Override
    public boolean equals(Object obj) {


        if (obj instanceof City) {


            if (((City) obj).isLocal()) {

                if (((City) obj).getCityName() != null &&
                        getCityName() != null &&
                        ((City) obj).getCityName().equalsIgnoreCase(getCityName().trim())) {
                    return true;
                } else if (((City) obj).getAddress() != null &&
                        getAddress() != null &&
                        ((City) obj).getAddress().trim().equalsIgnoreCase(getAddress().trim())) {
                    return true;
                }

            } else if (((City) obj).placeID != null && getPlaceID() != null
                    && placeID.trim().length() > 0 && ((City) obj).placeID.trim().length() > 0) {

                if (((City) obj).placeID.equalsIgnoreCase(getPlaceID())) {

                    return true;
                }


            } else if (((City) obj).getCityCode() != null &&
                    ((City) obj).getCityCode().trim().length() > 0 &&
                    getCityCode() != null && getCityCode().trim().length() > 0
                    && getCityCode().trim().equalsIgnoreCase(
                    ((City) obj).getCityCode().trim()))

                return true;
            else if (((City) obj).getCityName() != null &&
                    getCityName() != null &&
                    ((City) obj).getCityName().equalsIgnoreCase(getCityName().trim())) {
                return true;
            } else if (((City) obj).getAddress() != null &&
                    getAddress() != null &&
                    ((City) obj).getAddress().trim().equalsIgnoreCase(getAddress().trim())) {
                return true;
            }
        }

        return false;
    }

    public String getUseCatering() {
        return UseCatering;
    }

    public void setUseCatering(String UseCatering) {
        this.UseCatering = UseCatering;
    }

    public String getCityName() {
        return CityName;
    }

    public void setCityName(String CityName) {
        this.CityName = CityName;
    }

    public String getSkipRoom() {
        return SkipRoom;
    }

    public void setSkipRoom(String SkipRoom) {
        this.SkipRoom = SkipRoom;
    }

    public String getCityCode() {
        return CityCode;
    }

    public void setCityCode(String CityCode) {
        this.CityCode = CityCode;
    }

    public boolean isLocal() {

        return isLocal;
    }


    public void setLocal(boolean b) {

        isLocal = b;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public String getIdentity() {
        return identity;
    }

    public void setPlaceID(String placeID) {
        this.placeID = placeID;
    }

    public String getPlaceID() {
        return placeID;
    }
}
