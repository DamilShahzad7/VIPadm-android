package vip.com.vipmeetings.interfaces;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import vip.com.vipmeetings.envelope.SoapGetContactsRequestEnvelope;
import vip.com.vipmeetings.envelope.SoapGetContactsResponseEnvelope;
import vip.com.vipmeetings.envelope.SoapGetExcelDataRequestEnvelope;
import vip.com.vipmeetings.envelope.SoapGetExcelDataResponseEnvelope;
import vip.com.vipmeetings.envelope.SoapGetFormerVisitorsRequestEnvelope;
import vip.com.vipmeetings.envelope.SoapGetFormerVisitorsResponseEnvelope;

import static vip.com.vipmeetings.utilities.Constants.POSTMOBILE;

/**
 * Created by Srinath on 30/05/17.
 */

public interface ContactsApi  {


    @Headers({
            "Content-Type: text/xml; charset=utf-8"
    })
    @POST(POSTMOBILE)
    Observable<SoapGetFormerVisitorsResponseEnvelope> getFormerVisitors(
            @Body SoapGetFormerVisitorsRequestEnvelope soapGetFormerVisitorsRequestEnvelope
    );

    @Headers({
            "Content-Type: text/xml; charset=utf-8"
    })
    @POST(POSTMOBILE)
    Observable<SoapGetContactsResponseEnvelope> getContacts2(@Body SoapGetContactsRequestEnvelope soapGetContactsRequestEnvelope);


    @Headers({
            "Content-Type: text/xml; charset=utf-8"
    })
    @POST(POSTMOBILE)
    Observable<SoapGetExcelDataResponseEnvelope> getExcelData(@Body SoapGetExcelDataRequestEnvelope soapGetExcelDataRequestEnvelope);


}
