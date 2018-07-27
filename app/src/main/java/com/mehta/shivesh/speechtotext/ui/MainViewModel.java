package com.mehta.shivesh.speechtotext.ui;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.mehta.shivesh.speechtotext.db.entity.GetSpeechEntity;
import com.mehta.shivesh.speechtotext.network.ApiResponse;
import com.mehta.shivesh.speechtotext.network.model.GetSpeechResponse;
import com.mehta.shivesh.speechtotext.repo.AppRepository;

import java.util.List;

import javax.inject.Inject;

public class MainViewModel extends ViewModel {

    private AppRepository repository;
    private MutableLiveData<ApiResponse<GetSpeechResponse>> LiveData = new MutableLiveData<>();
    private MutableLiveData<List<GetSpeechEntity>> dataFromDb = new MutableLiveData<>();

    @Inject
    MainViewModel(AppRepository repository){
        this.repository = repository;

        repository.getData().observeForever(apiResponse -> LiveData.setValue(apiResponse));
        repository.getDataFromDb().observeForever(getSpeechEntities -> getDataFromDb().setValue(getSpeechEntities));
    }

    public MutableLiveData<ApiResponse<GetSpeechResponse>> getLiveData() {
        return LiveData;
    }

    public MutableLiveData<List<GetSpeechEntity>> getDataFromDb() {
        return dataFromDb;
    }
}
