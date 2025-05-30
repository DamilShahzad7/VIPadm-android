package vip.com.vipmeetings.interfaces;


import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import vip.com.vipmeetings.envelope.SoapAddContactLeaveRequestEnvelope;
import vip.com.vipmeetings.envelope.SoapAddContactLeaveResponseEnvelope;
import vip.com.vipmeetings.envelope.SoapAddOrderRequestEnvelope;
import vip.com.vipmeetings.envelope.SoapAddOrderResponseEnvelope;
import vip.com.vipmeetings.envelope.SoapAddParticipantsRequestEnvelope;
import vip.com.vipmeetings.envelope.SoapAddParticipantsResponseEnvelope;
import vip.com.vipmeetings.envelope.SoapAppointmentRequestEnvelope;
import vip.com.vipmeetings.envelope.SoapAppointmentResponseEnvelope;
import vip.com.vipmeetings.envelope.SoapAuthTokenRequestEnvelope;
import vip.com.vipmeetings.envelope.SoapAuthTokenResponseEnvelope;
import vip.com.vipmeetings.envelope.SoapGetEvacuationInformationRequestEnvelope;
import vip.com.vipmeetings.envelope.SoapGetEvacuationInformationResponseEnvelope;
import vip.com.vipmeetings.envelope.SoapGetFormerVisitorsRequestEnvelope;
import vip.com.vipmeetings.envelope.SoapGetFormerVisitorsResponseEnvelope;
import vip.com.vipmeetings.envelope.SoapGetMeetingDetailsVisitorsRequestEnvelope;
import vip.com.vipmeetings.envelope.SoapGetMeetingDetailsVisitorsResponseEnvelope;
import vip.com.vipmeetings.envelope.SoapGetUpdatePINRequestEnvelope;
import vip.com.vipmeetings.envelope.SoapGetUpdatePINResponseEnvelope;
import vip.com.vipmeetings.envelope.SoapRegisterInRequestEnvelope;
import vip.com.vipmeetings.envelope.SoapRegisterInResponseEnvelope;
import vip.com.vipmeetings.envelope.SoapCitiesRequestEnvelope;
import vip.com.vipmeetings.envelope.SoapCitiesResponseEnvelope;
import vip.com.vipmeetings.envelope.SoapClientUrlRequestEnvelope;
import vip.com.vipmeetings.envelope.SoapClientUrlResponseEnvelope;
import vip.com.vipmeetings.envelope.SoapDeleteContactLeaveRequestEnvelope;
import vip.com.vipmeetings.envelope.SoapDeleteContactLeaveResponseEnvelope;
import vip.com.vipmeetings.envelope.SoapDeleteMeetingRequestEnvelope;
import vip.com.vipmeetings.envelope.SoapDeleteMeetingResponseEnvelope;
import vip.com.vipmeetings.envelope.SoapEmployEvacuationResponseEnvelope;
import vip.com.vipmeetings.envelope.SoapEmployRequestEnvelope;
import vip.com.vipmeetings.envelope.SoapEmployResponseEnvelope;
import vip.com.vipmeetings.envelope.SoapEvacuationEmployRequestEnvelope;
import vip.com.vipmeetings.envelope.SoapFindAvailableRoomsRequestEnvelope;
import vip.com.vipmeetings.envelope.SoapFindAvailableRoomsResponseEnvelope;
import vip.com.vipmeetings.envelope.SoapFindRoomRequestEnvelope;
import vip.com.vipmeetings.envelope.SoapFindRoomResponseEnvelope;
import vip.com.vipmeetings.envelope.SoapGetClient;
import vip.com.vipmeetings.envelope.SoapGetClientResponseEnvelope;
import vip.com.vipmeetings.envelope.SoapGetContactLeaveRequestEnvelope;
import vip.com.vipmeetings.envelope.SoapGetContactLeaveResponseEnvelope;
import vip.com.vipmeetings.envelope.SoapGetInHouseContactsCountRequestEnvelope;
import vip.com.vipmeetings.envelope.SoapGetInHouseContactsCountResponseEnvelope;
import vip.com.vipmeetings.envelope.SoapGetProductsRequestEnvelope;
import vip.com.vipmeetings.envelope.SoapGetProductsResponseEnvelope;
import vip.com.vipmeetings.envelope.SoapGetRoomBookingsRequestEnvelope;
import vip.com.vipmeetings.envelope.SoapGetRoomBookingsResponseEnvelope;
import vip.com.vipmeetings.envelope.SoapGetRoomsRequestEnvelope;
import vip.com.vipmeetings.envelope.SoapGetRoomsResponseEnvelope;
import vip.com.vipmeetings.envelope.SoapMeetingsRequestEnvelope;
import vip.com.vipmeetings.envelope.SoapMeetingsResponseEnvelope;
import vip.com.vipmeetings.envelope.SoapRegisterOutRequestEnvelope;
import vip.com.vipmeetings.envelope.SoapRegisterOutResponseEnvelope;
import vip.com.vipmeetings.envelope.SoapSaveEventRequestEnvelope;
import vip.com.vipmeetings.envelope.SoapSaveEventResponseEnvelope;
import vip.com.vipmeetings.envelope.SoapUnRegisterOutRequestEnvelope;
import vip.com.vipmeetings.envelope.SoapUnRegisterOutResponseEnvelope;
import vip.com.vipmeetings.envelope.SoapUpdateContactLeaveRequestEnvelope;
import vip.com.vipmeetings.envelope.SoapUpdateContactLeaveResponseEnvelope;
import vip.com.vipmeetings.envelope.SoapValidateUserRequestEnvelope;
import vip.com.vipmeetings.envelope.SoapValidateUserResponseEnvelope;

import static vip.com.vipmeetings.utilities.Constants.POSTCLIENT;
import static vip.com.vipmeetings.utilities.Constants.POSTEVACUATION;
import static vip.com.vipmeetings.utilities.Constants.POSTMOBILE;
import static vip.com.vipmeetings.utilities.Constants.POSTRECEPTION;

/**
 * Created by Srinath on 29/05/17.
 */

public interface MainApi {


    //1253 evacuation
    @Headers({
            "Content-Type: text/xml; charset=utf-8"
    })
    @POST(POSTCLIENT)
    Observable<SoapGetClientResponseEnvelope> getClient(@Body SoapGetClient
                                                                                 soapAuthTokenEnvelope);


    //1253 evacuation
    @Headers({
            "Content-Type: text/xml; charset=utf-8"
    })
    @POST(POSTCLIENT)
    Observable<SoapAuthTokenResponseEnvelope> getAuthTokenClientSettings(@Body SoapAuthTokenRequestEnvelope
                                                                                 soapAuthTokenEnvelope);


    //1253 token afterlogin
    @Headers({
            "Content-Type: text/xml; charset=utf-8"
    })
    @POST(POSTCLIENT)
    Observable<SoapAuthTokenResponseEnvelope> getAuthTokenMobileMeetings(@Body SoapAuthTokenRequestEnvelope
                                                                                 soapAuthTokenEnvelope);


    @Headers({
            "Content-Type: text/xml; charset=utf-8"
    })
    @POST(POSTMOBILE)
    Observable<SoapMeetingsResponseEnvelope> getMeetings(@Body
                                                                 SoapMeetingsRequestEnvelope soapMeetingsRequestEnvelope);

    @Headers({
            "Content-Type: text/xml; charset=utf-8"
    })
    @POST(POSTMOBILE)
    Observable<SoapAppointmentResponseEnvelope> getAppointments(@Body
                                                                SoapAppointmentRequestEnvelope soapAppointmentRequestEnvelope);



    @Headers({
            "Content-type:text/xml; charset=utf-8"
    })
    @POST(POSTCLIENT)
    Observable<SoapClientUrlResponseEnvelope> getClientURL(@Body SoapClientUrlRequestEnvelope ClientId);



    @Headers({
            "Content-type:text/xml; charset=utf-8"
    })
    @POST(POSTMOBILE)
    Observable<SoapEmployResponseEnvelope> authenticateClient(@Body SoapEmployRequestEnvelope soapEmployRequestEnvelope);



    @Headers({
            "Content-type:text/xml; charset=utf-8"
    })
    @POST(POSTEVACUATION)
    Observable<SoapEmployEvacuationResponseEnvelope> authenticateClient2(@Body SoapEvacuationEmployRequestEnvelope
                                                                                 soapEvacuationEmployRequestEnvelope);


    //internalurl toekn
    @Headers({
            "Content-type:text/xml; charset=utf-8"
    })
    @POST(POSTMOBILE)
    Observable<SoapCitiesResponseEnvelope> getLocations(@Body SoapCitiesRequestEnvelope soapCitiesRequestEnvelope);

    @Headers({
            "Content-type:text/xml; charset=utf-8"
    })
    @POST(POSTMOBILE)
    Observable<SoapGetInHouseContactsCountResponseEnvelope> GetInHouseContactsCount(@Body SoapGetInHouseContactsCountRequestEnvelope
                                                                                            SoapGetInHouseContactsCountRequestEnvelope);

    @Headers({
            "Content-type:text/xml; charset=utf-8"
    })
    @POST(POSTRECEPTION)
    Observable<SoapGetEvacuationInformationResponseEnvelope> GetEvacuationInformation(@Body SoapGetEvacuationInformationRequestEnvelope
                                                                                            SoapGetEvacuationInformationRequestEnvelope);

    @Headers({
            "Content-type:text/xml; charset=utf-8"
    })
    @POST(POSTRECEPTION)
    Observable<SoapGetUpdatePINResponseEnvelope> GetUpdatePIN(@Body SoapGetUpdatePINRequestEnvelope
                                                                                  SoapGetUpdatePINRequestEnvelope);
    @Headers({
            "Content-type:text/xml; charset=utf-8"
    })
    @POST(POSTRECEPTION)
    Observable<SoapRegisterInResponseEnvelope> RegisterIn(@Body SoapRegisterInRequestEnvelope
                                                                      soapRegisterInRequestEnvelope);
    @Headers({
            "Content-type:text/xml; charset=utf-8"
    })
    @POST(POSTRECEPTION)
    Observable<SoapRegisterOutResponseEnvelope> RegisterOut(@Body SoapRegisterOutRequestEnvelope
                                                                    soapRegisterOutRequestEnvelope);

    @Headers({
            "Content-type:text/xml; charset=utf-8"
    })
    @POST(POSTRECEPTION)
    Observable<SoapUnRegisterOutResponseEnvelope> UnRegisterOut(@Body SoapUnRegisterOutRequestEnvelope
                                                                    soapUnRegisterOutRequestEnvelope);

    @Headers({
            "Content-type:text/xml; charset=utf-8"
    })
    @POST(POSTMOBILE)
    Observable<SoapValidateUserResponseEnvelope> validateUser(@Body SoapValidateUserRequestEnvelope
                                                                      soapValidateUserRequestEnvelope);

    @Headers({
            "Content-type:text/xml; charset=utf-8"
    })
    @POST(POSTMOBILE)
    Observable<SoapUpdateContactLeaveResponseEnvelope> updateContactLeave(@Body SoapUpdateContactLeaveRequestEnvelope
                                                                                  soapUpdateContactLeaveRequestEnvelope);

    @Headers({
            "Content-type:text/xml; charset=utf-8"
    })
    @POST(POSTMOBILE)
    Observable<SoapAddContactLeaveResponseEnvelope> addContactLeave(@Body


                                                                            SoapAddContactLeaveRequestEnvelope
                                                                            soapAddContactLeaveRequestEnvelope);

    @Headers({
            "Content-type:text/xml; charset=utf-8"
    })
    @POST(POSTMOBILE)
    Observable<SoapGetContactLeaveResponseEnvelope> getContactLeaves(@Body
                                                                             SoapGetContactLeaveRequestEnvelope soapGetContactLeaveRequestEnvelope);

    @Headers({
            "Content-type:text/xml; charset=utf-8"
    })
    @POST(POSTMOBILE)
    Observable<SoapDeleteContactLeaveResponseEnvelope> deleteContactLeave(
            @Body SoapDeleteContactLeaveRequestEnvelope soapDeleteContactLeaveRequestEnvelope
    );


    @Headers({
            "Content-type:text/xml; charset=utf-8"
    })
    @POST(POSTMOBILE)
    Observable<SoapDeleteMeetingResponseEnvelope> deleteMeeting(@Body SoapDeleteMeetingRequestEnvelope soapDeleteMeetingRequestEnvelope);


    @Headers({
            "Content-type:text/xml; charset=utf-8"
    })
    @POST(POSTMOBILE)
    Observable<SoapGetRoomsResponseEnvelope> getRooms(@Body SoapGetRoomsRequestEnvelope soapGetRoomsRequestEnvelope);


    @Headers({
            "Content-type:text/xml; charset=utf-8"
    })
    @POST(POSTMOBILE)
    Observable<SoapFindAvailableRoomsResponseEnvelope> findAvailableRooms2(
            @Body SoapFindAvailableRoomsRequestEnvelope soapFindAvailableRoomsRequestEnvelope);


    @Headers({
            "Content-type:text/xml; charset=utf-8"
    })
    @POST(POSTMOBILE)
    Observable<SoapGetRoomBookingsResponseEnvelope> getRoomBookings(@Body SoapGetRoomBookingsRequestEnvelope
                                                                            soapGetRoomBookingsRequestEnvelope);

    @Headers({
            "Content-type:text/xml; charset=utf-8"
    })
    @POST(POSTMOBILE)
    Observable<SoapFindRoomResponseEnvelope> FindRoomAvailability(@Body SoapFindRoomRequestEnvelope soapFindRoomRequestEnvelope);


    @Headers({
            "Content-type:text/xml; charset=utf-8"
    })
    @POST(POSTMOBILE)
    Observable<SoapSaveEventResponseEnvelope> saveEvent(@Body
                                                                SoapSaveEventRequestEnvelope
                                                                saveEventRequestEnvelope);


    @Headers({
            "Content-type:text/xml; charset=utf-8"
    })
    @POST(POSTMOBILE)
    Observable<SoapAddParticipantsResponseEnvelope> addParticipants(@Body
                                                                            SoapAddParticipantsRequestEnvelope
                                                                            soapAddParticipantsRequestEnvelope);


    @Headers({
            "Content-type:text/xml; charset=utf-8"
    })
    @POST(POSTMOBILE)
    Observable<SoapAddOrderResponseEnvelope> addOrder(@Body
                                                              SoapAddOrderRequestEnvelope soapAddOrderRequestEnvelope);


    @Headers({
            "Content-type:text/xml; charset=utf-8"
    })
    @POST(POSTMOBILE)
    Observable<SoapGetProductsResponseEnvelope> getProducts(@Body
                                                                    SoapGetProductsRequestEnvelope soapGetProductsRequestEnvelope);
    @Headers({
            "Content-Type: text/xml; charset=utf-8"
    })
    @POST(POSTMOBILE)
    Observable<SoapGetMeetingDetailsVisitorsResponseEnvelope> getMeetingDetailsVisitors(
            @Body SoapGetMeetingDetailsVisitorsRequestEnvelope soapGetMeetingDetailsVisitorsRequestEnvelope
    );

}
