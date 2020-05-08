package com.fadcode.buymyproduct.Api;

import com.fadcode.buymyproduct.Model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;


public interface ApiService {
    @FormUrlEncoded
    @POST("authentication.php")
     Call<List<User>>login(@Field("email") String email,
                      @Field("password") String password
    );


    @FormUrlEncoded
    @POST("registration.php")
    Call<User> register(@Field("email") String email,
                              @Field("password") String password

    );


}
