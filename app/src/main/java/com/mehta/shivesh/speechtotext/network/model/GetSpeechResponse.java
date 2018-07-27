package com.mehta.shivesh.speechtotext.network.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mehta.shivesh.speechtotext.db.entity.GetSpeechEntity;

import java.util.List;

public class GetSpeechResponse {


    @SerializedName("lesson_data")
    @Expose
    private List<GetSpeechEntity> lessonData = null;

    public List<GetSpeechEntity> getLessonData() {
        return lessonData;
    }

    public void setLessonData(List<GetSpeechEntity> lessonData) {
        this.lessonData = lessonData;
    }
}
