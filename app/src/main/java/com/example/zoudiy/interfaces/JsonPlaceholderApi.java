package com.example.zoudiy.interfaces;

import com.example.zoudiy.models.OtpResponse;
import com.example.zoudiy.models.UpdateProfileResponse;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface JsonPlaceholderApi {

    @FormUrlEncoded
    @POST("auth/send-otp")

    Call<ResponseBody> Sendotp
            (
                    @Field("phone") String mobileno
            );
    @FormUrlEncoded
    @POST("auth/verify-otp")
    Call<OtpResponse> Verifyotp
            (
                    @Field("phone") String mobileno,
                    @Field("otp") String Otp
            );

    @FormUrlEncoded
    @POST("profile/update-profile")
    Call<UpdateProfileResponse> Updateprofile
            (
                    @Field("name") String fullname,
                    @Field("email") String emailid,
                    @Field("token") String token

            );
}
