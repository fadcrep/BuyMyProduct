package com.fadcode.buymyproduct;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ProductService {

    String BASE_URL = "http://www.vasedhonneurofficiel.com/ws/";

    @GET("productsList.php")
    Call<List<Product>> getProduct();

//    Ajput maybe
    @FormUrlEncoded
    @POST("commentsList.php")
    Call<List<Comment>> getComments(@Field("productId") String productId);

}
