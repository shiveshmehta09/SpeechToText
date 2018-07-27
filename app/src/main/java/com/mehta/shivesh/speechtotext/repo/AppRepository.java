package com.mehta.shivesh.speechtotext.repo;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.mehta.shivesh.speechtotext.db.dao.SpeechDao;
import com.mehta.shivesh.speechtotext.db.entity.GetSpeechEntity;
import com.mehta.shivesh.speechtotext.network.ApiInterface;
import com.mehta.shivesh.speechtotext.network.ApiResponse;
import com.mehta.shivesh.speechtotext.network.model.GetSpeechResponse;
import com.mehta.shivesh.speechtotext.utils.AppExecutor;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AppRepository {

    private final ApiInterface apiInterface;
    private final SpeechDao dao;
    private final AppExecutor executor;

    public AppRepository(ApiInterface apiInterface, SpeechDao dao, AppExecutor executor) {
        this.apiInterface = apiInterface;
        this.dao = dao;
        this.executor = executor;
    }

    public LiveData<ApiResponse<GetSpeechResponse>> getData() {
        final MutableLiveData<ApiResponse<GetSpeechResponse>> liveData = new MutableLiveData<>();
        Call<GetSpeechResponse> call = apiInterface.getSpeechFromServer();
        call.enqueue(new Callback<GetSpeechResponse>() {
            @Override
            public void onResponse(@NonNull Call<GetSpeechResponse> call, @NonNull Response<GetSpeechResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    liveData.setValue(new ApiResponse<>(response.body()));
                    executor.diskIO().execute(() -> {
                        dao.saveDataToDb(response.body().getLessonData());
                    });
                } else {
                    liveData.setValue(null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<GetSpeechResponse> call, @NonNull Throwable t) {
                liveData.setValue(new ApiResponse<>(t));
            }
        });
        return liveData;
    }

    public LiveData<List<GetSpeechEntity>> getDataFromDb(){
        return dao.loadDataFromDb();
    }

}
