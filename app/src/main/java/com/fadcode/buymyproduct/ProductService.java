package com.fadcode.buymyproduct;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ProductService {

    String BASE_URL = "http://www.vasedhonneurofficiel.com/ws/";

    @GET("productsList.php")
    Call<List<Product>> getProduct();
}
