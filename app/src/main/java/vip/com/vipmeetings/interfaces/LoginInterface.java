package vip.com.vipmeetings.interfaces;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import vip.com.vipmeetings.envelope.SaveResponsiblePhotoResponseEnevelope;
import vip.com.vipmeetings.envelope.SoapAddDeviceTokenRequestEnvelope;
import vip.com.vipmeetings.envelope.SoapAddDeviceTokenResponseEnvelope;
import vip.com.vipmeetings.envelope.SoapClearTimeResponseEnvelope;
import vip.com.vipmeetings.envelope.SoapEndEvacuationRequestEnvelope;
import vip.com.vipmeetings.envelope.SoapEndEvacuationResponseEnvelope;
import vip.com.vipmeetings.envelope.SoapEvacuationResponsibleDetailsRequestEnvelope;
import vip.com.vipmeetings.envelope.SoapEvacuationResponsibleDetailsResponseEnvelope;
import vip.com.vipmeetings.envelope.SoapGetEvacuationResponsiblesRequestEnvelope;
import vip.com.vipmeetings.envelope.SoapGetEvacuationResponsiblesResponseEnvelope;
import vip.com.vipmeetings.envelope.SoapGetEvacuationStatusEnvelope;
import vip.com.vipmeetings.envelope.SoapGetEvacuationStatusResponseEnvelope;
import vip.com.vipmeetings.envelope.SoapGetEvacuationsRequestEnvelope;
import vip.com.vipmeetings.envelope.SoapGetEvacuationsResponseEnvelope;
import vip.com.vipmeetings.envelope.SoapGetMessagesRequestEnvelope;
import vip.com.vipmeetings.envelope.SoapGetMessagesResponseEnvelope;
import vip.com.vipmeetings.envelope.SoapGetPlaceIDRequestEnvelope;
import vip.com.vipmeetings.envelope.SoapGetPlaceIDResponseEnvelope;
import vip.com.vipmeetings.envelope.SoapGetPlacesRequestEnvelope;
import vip.com.vipmeetings.envelope.SoapGetPlacesResponseEnvelope;
import vip.com.vipmeetings.envelope.SoapGetResponsiblePhotoUrlRequest;
import vip.com.vipmeetings.envelope.SoapGetResponsiblePhotoUrlResponse;
import vip.com.vipmeetings.envelope.SoapGetUnreadMessageCountResponseEnvelope;
import vip.com.vipmeetings.envelope.SoapNotifyAboutAreaClearence;
import vip.com.vipmeetings.envelope.SoapNotifyAboutAreaClearenceResponse;
import vip.com.vipmeetings.envelope.SoapNotifyAboutEvacuationEnabledRequest;
import vip.com.vipmeetings.envelope.SoapNotifyAboutEvacuationEnabledResponse;
import vip.com.vipmeetings.envelope.SoapNotifyAboutEvacuationEndedRequest;
import vip.com.vipmeetings.envelope.SoapNotifyAboutEvacuationEndedResponse;
import vip.com.vipmeetings.envelope.SoapNotifyAboutResponsibleAvailability;
import vip.com.vipmeetings.envelope.SoapNotifyAboutResponsibleAvailabilityResponse;
import vip.com.vipmeetings.envelope.SoapRemoveClearTimeRequestEnvelope;
import vip.com.vipmeetings.envelope.SoapRemoveClearTimeResponseEnvelope;
import vip.com.vipmeetings.envelope.SoapRemoveResponsiblePhotoEnvelope;
import vip.com.vipmeetings.envelope.SoapRemoveResponsiblePhotoResponseEnvelope;
import vip.com.vipmeetings.envelope.SoapSaveResponsiblePhotoEnvelope;
import vip.com.vipmeetings.envelope.SoapSendEvaciationPushRequestEnvelope;
import vip.com.vipmeetings.envelope.SoapSendEvacuationPushResponseEnvelope;
import vip.com.vipmeetings.envelope.SoapSendPushRequestEnvelope;
import vip.com.vipmeetings.envelope.SoapSendPushResponseEnvelope;
import vip.com.vipmeetings.envelope.SoapSetClearTimeRequestEnvelope;
import vip.com.vipmeetings.envelope.SoapStartEvacuationRequestEnvelope;
import vip.com.vipmeetings.envelope.SoapStartEvacuationResponseEnvelope;
import vip.com.vipmeetings.envelope.SoapUnreadMessageCountRequestEnvelope;
import vip.com.vipmeetings.envelope.SoapUpdateClearTimeRequestEnvelope;
import vip.com.vipmeetings.envelope.SoapUpdateClearTimeResponseEnvelope;
import vip.com.vipmeetings.envelope.SoapUpdateMessageReadStatusRequestEnvelope;
import vip.com.vipmeetings.envelope.SoapUpdateMessageReadStatusResponseEnvelope;
import vip.com.vipmeetings.envelope.SoapUpdateResponsibleRequestEnvelope;
import vip.com.vipmeetings.envelope.SoapUpdateResponsibleResponseEnvelope;

import static vip.com.vipmeetings.utilities.Constants.POSTEVACUATION;
import static vip.com.vipmeetings.utilities.Constants.POSTMOBILE;

/**
 * Created by Srinath on 29/03/17.
 */

public interface LoginInterface {


    @Headers({
            "Content-Type: text/xml; charset=utf-8"
    })
    @POST(POSTMOBILE)
    Observable<SoapAddDeviceTokenResponseEnvelope> addDeviceToken(@Body SoapAddDeviceTokenRequestEnvelope soapAddDeviceTokenRequestEnvelope);


    @Headers({
            "Content-Type: text/xml; charset=utf-8"
    })
    @POST(POSTEVACUATION)
    Observable<SoapEvacuationResponsibleDetailsResponseEnvelope> getEvacuationResponsibleDetails(@Body SoapEvacuationResponsibleDetailsRequestEnvelope
                                                                                                         soapEvacuationResponsibleDetailsRequestEnvelope);

    //1253
    @Headers({
            "Content-Type: text/xml; charset=utf-8"
    })
    @POST(POSTEVACUATION)
    Observable<SoapGetEvacuationResponsiblesResponseEnvelope> getEvacuationResponsibles(@Body SoapGetEvacuationResponsiblesRequestEnvelope soapGetEvacuationResponsiblesRequestEnvelope);


    @Headers({
            "Content-Type: text/xml; charset=utf-8"
    })
    @POST(POSTEVACUATION)
    Observable<SoapGetEvacuationsResponseEnvelope> getEvacuations(@Body SoapGetEvacuationsRequestEnvelope soapGetEvacuationsRequestEnvelope
    );

    @Headers({
            "Content-Type: text/xml; charset=utf-8"
    })
    @POST(POSTEVACUATION)
    Observable<SoapNotifyAboutEvacuationEnabledResponse> notifyAboutEvacuationEnabled(@Body
                                                                                              SoapNotifyAboutEvacuationEnabledRequest
                                                                                              soapNotifyAboutEvacuationEnabledRequest);

    @Headers({
            "Content-Type: text/xml; charset=utf-8"
    })
    @POST(POSTEVACUATION)
    Observable<SoapNotifyAboutEvacuationEndedResponse> notifyAboutEvacuationEnded(@Body
                                                                                          SoapNotifyAboutEvacuationEndedRequest
                                                                                          soapNotifyAboutEvacuationEnabledRequest);


    @Headers({
            "Content-Type: text/xml; charset=utf-8"
    })
    @POST(POSTEVACUATION)
    Observable<SoapStartEvacuationResponseEnvelope> startEvacuation(@Body SoapStartEvacuationRequestEnvelope soapStartEvacuationRequestEnvelope);


    @Headers({
            "Content-Type: text/xml; charset=utf-8"
    })
    @POST(POSTEVACUATION)
    Observable<SoapEndEvacuationResponseEnvelope> endEvacuation(@Body SoapEndEvacuationRequestEnvelope soapEndEvacuationRequestEnvelope);


    @Headers({
            "Content-Type: text/xml; charset=utf-8"
    })
    @POST(POSTEVACUATION)
    Observable<SoapGetMessagesResponseEnvelope> getResponsibleEvacuationMessages(@Body SoapGetMessagesRequestEnvelope soapGetMessagesRequestEnvelope);


    @Headers({
            "Content-Type: text/xml; charset=utf-8"
    })
    @POST(POSTEVACUATION)
    Observable<SoapUpdateMessageReadStatusResponseEnvelope> updateMessageReadStatus(@Body
                                                                                            SoapUpdateMessageReadStatusRequestEnvelope
                                                                                            soapUpdateMessageReadStatusRequestEnvelope);


    @Headers({
            "Content-Type: text/xml; charset=utf-8"
    })
    @POST(POSTEVACUATION)
    Observable<SoapClearTimeResponseEnvelope> setClearTimeToEvacuations(@Body SoapSetClearTimeRequestEnvelope soapSetClearTimeRequestEnvelope
    );


    @Headers({
            "Content-Type: text/xml; charset=utf-8"
    })
    @POST(POSTEVACUATION)
    Observable<SoapGetUnreadMessageCountResponseEnvelope> getResposibleUnReadEvacuationMessagesCount(@Body
                                                                                                             SoapUnreadMessageCountRequestEnvelope soapUnreadMessageCountRequestEnvelope);


    @Headers({
            "Content-Type: text/xml; charset=utf-8"
    })
    @POST(POSTEVACUATION)
    Observable<SoapUpdateResponsibleResponseEnvelope> updateResponsibleAvailability(@Body SoapUpdateResponsibleRequestEnvelope soapUpdateResponsibleRequestEnvelope);


    @Headers({
            "Content-Type: text/xml; charset=utf-8"
    })
    @POST(POSTEVACUATION)
    Observable<SoapUpdateClearTimeResponseEnvelope> updateClearTimetoEvacuation(@Body SoapUpdateClearTimeRequestEnvelope
                                                                                        soapUpdateClearTimeRequestEnvelope);


    @Headers({
            "Content-Type: text/xml; charset=utf-8"
    })
    @POST(POSTEVACUATION)
    Observable<SoapRemoveClearTimeResponseEnvelope> removeClearTimetoEvacuation(@Body SoapRemoveClearTimeRequestEnvelope
                                                                                        soapRemoveClearTimeRequestEnvelope);


    @Headers({
            "Content-Type: text/xml; charset=utf-8"
    })
    @POST(POSTEVACUATION)
    Observable<SoapSendPushResponseEnvelope> saveReceptionEvacuationMessage(@Body SoapSendPushRequestEnvelope soapSendPushRequestEnvelope);


    @Headers({
            "Content-Type: text/xml; charset=utf-8"
    })
    @POST(POSTEVACUATION)
    Observable<SoapNotifyAboutAreaClearenceResponse> notifyAboutAreaClearence(@Body SoapNotifyAboutAreaClearence
                                                                                      soapSendEvaciationPushRequestEnvelope);


    @Headers({
            "Content-Type: text/xml; charset=utf-8"
    })
    @POST(POSTEVACUATION)
    Observable<SoapNotifyAboutResponsibleAvailabilityResponse> notifyAboutResponsibleAvailability(@Body
                                                                                                          SoapNotifyAboutResponsibleAvailability soapSendEvaciationPushRequestEnvelope);


    @Headers({
            "Content-Type: text/xml; charset=utf-8"
    })
    @POST(POSTEVACUATION)
    Observable<SoapSendEvacuationPushResponseEnvelope> sendEvacuationPush(@Body SoapSendEvaciationPushRequestEnvelope soapSendEvaciationPushRequestEnvelope);


    @Headers({
            "Content-type:text/xml; charset=utf-8"
    })
    @POST(POSTEVACUATION)
    Observable<SoapGetPlaceIDResponseEnvelope> getPlaceID(@Body SoapGetPlaceIDRequestEnvelope
                                                                  soapGetProductsRequestEnvelope);

    @Headers({
            "Content-type:text/xml; charset=utf-8"
    })
    @POST(POSTEVACUATION)
    Observable<SoapGetEvacuationStatusResponseEnvelope> getEvacuationStatus(@Body SoapGetEvacuationStatusEnvelope
                                                                                    soapGetProductsRequestEnvelope);


    @Headers({
            "Content-type:text/xml; charset=utf-8"
    })
    @POST(POSTEVACUATION)
    Observable<SoapGetPlacesResponseEnvelope> getPlaces(@Body SoapGetPlacesRequestEnvelope
                                                                soapGetPlacesRequestEnvelope);


    @Headers({
            "Content-type:text/xml; charset=utf-8"
    })
    @POST(POSTEVACUATION)
    Observable<SoapRemoveResponsiblePhotoResponseEnvelope> removeResponsiblePhoto(@Body
                                                                                          SoapRemoveResponsiblePhotoEnvelope
                                                                                          soapRemoveResponsiblePhotoEnvelope);


    @Headers({
            "Content-type:text/xml; charset=utf-8"
    })
    @POST(POSTEVACUATION)
    Observable<SaveResponsiblePhotoResponseEnevelope> saveResponsiblePhoto(@Body SoapSaveResponsiblePhotoEnvelope
                                                                                   soapSaveResponsiblePhotoEnvelope);



    @Headers({
            "Content-type:text/xml; charset=utf-8"
    })
    @POST(POSTEVACUATION)
    Observable<SoapGetResponsiblePhotoUrlResponse> getResponsiblePhotoUrl(@Body SoapGetResponsiblePhotoUrlRequest
                                                                       soapGetResponsiblePhotoUrlRequest);




}
