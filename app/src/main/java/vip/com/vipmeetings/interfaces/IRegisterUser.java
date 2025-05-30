package vip.com.vipmeetings.interfaces;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by srinath on 20-06-2016.
 */
public interface IRegisterUser {


    @GET("/WsVIPDoor/VipDoor.asmx/RegisterUser")
    Call<ResponseBody> registerUser(@Query("A_sClientId") String A_sClientId,
                                    @Query("A_sName") String A_sName,
                                    @Query("A_sMobile") String A_sMobile,
                                    @Query("A_sEmail") String A_sEmail,
                                    @Query("A_sAddress") String A_sAddress,
                                    @Query("A_sHouseNo") String A_sHouseNo);

    @GET("/WsVIPDoor/VipDoor.asmx/AuthentcateUser")
    Call<ResponseBody> loginUser(@Query("A_sClientId") String A_sClientId,
                                 @Query("A_sEmail") String A_sEmail);

    @GET("/WsVIPDoor/VipDoor.asmx/SendSMS")
    Call<ResponseBody> otpUser(@Query("A_sMessage") String A_sMessage,
                               @Query("A_sMobile") String A_sMobile);


//    @GET("/WsVIPDoor/VipDoor.asmx/SendSMS")
//    Call<ResponseBody> updateUUID(@Query("A_sUserId") String A_sUserId,
//                                  @Query("A_sUUID") String A_sUUID);


    @GET("/WsVIPDoor/VipDoor.asmx/GetUserStatus")
    Call<ResponseBody> GetUserStatus(@Query("A_sUserId") String A_sUserId,
                                     @Query("A_sDoorOpener") String A_sDoorOpener);

    @GET("/WsVIPDoor/VipDoor.asmx/UpdateUUID")
    Call<ResponseBody> UpdateUUID(@Query("A_sUserId") String A_sUserId,
                                     @Query("A_sUUID") String A_sUUID);



    @GET("/WsVIPDoor/VipDoor.asmx/GetUserUUID")
    Call<ResponseBody> GetUserStatus1(@Query("A_sUserId") String A_sUserId);





}
