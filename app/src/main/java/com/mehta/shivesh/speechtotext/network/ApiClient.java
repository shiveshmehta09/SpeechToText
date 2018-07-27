package com.mehta.shivesh.speechtotext.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mehta.shivesh.speechtotext.utils.AppConstants;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by Shivesh
 */

public class ApiClient {

    private static Retrofit retrofit = null;

    public static Retrofit getClient(OkHttpClient.Builder httpClient) {
        if (retrofit == null) {

            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

            retrofit = new Retrofit.Builder()
                    .baseUrl(AppConstants.BASE_URL)                                  //<-- Base URL is loaded
                    .addConverterFactory(GsonConverterFactory.create(gson)) //<-- Uses GSON convertor to convert the JSON to JAVA objects
                    .client(httpClient.build())                         //<-- Builds the request with OkHttp
                    .build();
        }
        return retrofit;
    }
}
