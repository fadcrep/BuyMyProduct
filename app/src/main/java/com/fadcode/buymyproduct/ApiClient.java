package com.fadcode.buymyproduct;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private static Retrofit getRetrofit(){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ProductService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit;
    }

    public static ProductService getProductService(){
        ProductService productService = getRetrofit().create(ProductService.class);
        return productService;
    }

}
