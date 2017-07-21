package com.levadbe.berlin.app.client;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface FixerAPIInterface {

    @GET("/latest")
    Call<FixerResponse> getCurrency(@Query("symbols") String symbols);//EUR,ILS

}