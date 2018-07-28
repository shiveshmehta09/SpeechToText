package com.mehta.shivesh.speechtotext.network;

import com.mehta.shivesh.speechtotext.network.model.GetSpeechResponse;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Shivesh
 */

public interface ApiInterface {

    @GET("getData.php")
    Call<GetSpeechResponse> getSpeechFromServer();
}
