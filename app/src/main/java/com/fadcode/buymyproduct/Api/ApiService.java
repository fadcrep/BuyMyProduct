package com.fadcode.buymyproduct.Api;

import com.fadcode.buymyproduct.Model.Comment;
import com.fadcode.buymyproduct.Model.Product;
import com.fadcode.buymyproduct.Model.User;
import com.fadcode.buymyproduct.Views.ProductDetailsActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;


public interface ApiService {

    String BASE_URL = "http://www.vasedhonneurofficiel.com/ws/";

    @GET("productsList.php")
    Call<List<Product>> getProduct();

    //    Ajput maybe
    @FormUrlEncoded
    @POST("commentsList.php")
    Call<List<Comment>> getComments(@Field("productId") String productId);

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

    @FormUrlEncoded
    @POST("addComment.php")
    Call<Comment> createComment(@Field("productId") String productId,
                                               @Field("email") String email,
                                               @Field("content") String content);

    @FormUrlEncoded
    @POST("modifyUserAccount.php")
    Call<User> updateUser(@Field("id") String id,
                          @Field("name") String name,
                          @Field("email") String email,
                          @Field("password") String password
            );

    @POST("modifyUserAccount.php")
    Call<User> updateUserWithBody(@Body() User user);

}
