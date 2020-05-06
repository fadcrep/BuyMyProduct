package com.fadcode.buymyproduct.Api;

public class ApiUtils {

    private ApiUtils() {}

    public static final String BASE_URL = "http://www.vasedhonneurofficiel.com/ws/";

    public static ApiService getAPIService() {

        return RetrofitClient.getRetrofit(BASE_URL).create(ApiService.class);
    }
}
