package com.mehta.shivesh.speechtotext.db.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mehta.shivesh.speechtotext.utils.AppConstants;

@Entity(tableName = AppConstants.TABLE_DATA)
public class GetSpeechEntity {

    @PrimaryKey(autoGenerate = true)
    private Integer id;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("conceptName")
    @Expose
    private String conceptName;
    @SerializedName("pronunciation")
    @Expose
    private String pronunciation;
    @SerializedName("targetScript")
    @Expose
    private String targetScript;
    @SerializedName("audio_url")
    @Expose
    private String audioUrl;

    public GetSpeechEntity(Integer id, String type, String conceptName, String pronunciation, String targetScript, String audioUrl) {
        this.id = id;
        this.type = type;
        this.conceptName = conceptName;
        this.pronunciation = pronunciation;
        this.targetScript = targetScript;
        this.audioUrl = audioUrl;
    }

    public Integer getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getConceptName() {
        return conceptName;
    }

    public String getPronunciation() {
        return pronunciation;
    }

    public String getTargetScript() {
        return targetScript;
    }

    public String getAudioUrl() {
        return audioUrl;
    }
}
