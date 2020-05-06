package com.fadcode.buymyproduct.Api;

import com.fadcode.buymyproduct.Model.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;


public interface ApiService {

    @POST("authentication.php")
     Call<User> login(@Body User user);


}
