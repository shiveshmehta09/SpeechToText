package com.mehta.shivesh.speechtotext.ui;

import android.Manifest;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.speech.RecognizerIntent;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.DownloadListener;
import com.mehta.shivesh.speechtotext.R;
import com.mehta.shivesh.speechtotext.adapter.ViewPagerAdapter;
import com.mehta.shivesh.speechtotext.db.entity.GetSpeechEntity;
import com.mehta.shivesh.speechtotext.utils.AppConstants;
import com.mehta.shivesh.speechtotext.utils.AppUtils;
import com.mehta.shivesh.speechtotext.utils.PermissionsUtils;
import com.mehta.shivesh.speechtotext.utils.SharedPreferenceHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

/**
 * Created by Shivesh
 */

public class MainActivity extends AppCompatActivity implements ViewPagerAdapter.OnClickEvent {

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private MainViewModel viewModel;

    private Button mBtnNext;
    private TextView mTxvPercentage;
    private ViewPager viewPager;
    private int totalItemCount = 0, currentPosition = 0;
    private MediaPlayer mediaPlayer;
    private List<GetSpeechEntity> getSpeechResponses;
    private ViewPagerAdapter adapter;
    private String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private final int MY_PERMISSIONS_REQUEST_RW_EXTERNAL = 1;


    private void initViews() {
        setContentView(R.layout.activity_main);
        viewPager = findViewById(R.id.viewpager);
        mBtnNext = findViewById(R.id.btn_next);
        mTxvPercentage = findViewById(R.id.txv_percentage);
        getSpeechResponses = new ArrayList<>();

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();
        initData();

        mBtnNext.setOnClickListener(view -> {
            if (currentPosition < totalItemCount) {
                viewPager.setCurrentItem(currentPosition + 1);
                currentPosition++;
                if (currentPosition == totalItemCount) {
                    mBtnNext.setEnabled(false);
                    mBtnNext.setBackgroundResource(R.color.grey_200);
                }
                mTxvPercentage.setVisibility(View.GONE);

            }
        });

        mediaPlayer.setOnCompletionListener(mediaPlayer -> {
            mediaPlayer.reset();
            adapter.setData(getSpeechResponses);
            viewPager.setAdapter(adapter);
            viewPager.setCurrentItem(currentPosition);
        });
    }


    @Override
    public void onClickAction(int identifier, String url, int playStopIdentifier, String pronountiation) {
        if (identifier == 1) {
            if (AppUtils.isNetworkAvailable()) {
                if (playStopIdentifier == 0) {
                    try {
                        mediaPlayer.setDataSource(url);
                        mediaPlayer.prepare();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    mediaPlayer.start();
                } else {
                    if (mediaPlayer.isPlaying()) {
                        mediaPlayer.stop();

                    }
                    mediaPlayer.reset();
                }
            }
            else {
                Toast.makeText(this, "Network not available", Toast.LENGTH_SHORT).show();

            }

        } else {
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(intent, 10);
            } else {
                Toast.makeText(this, "Your Device Don't Support Speech Input", Toast.LENGTH_SHORT).show();
            }
        }
        SharedPreferenceHelper.setSharedPreferenceString(this, AppConstants.STRING_PRONOUNTIATION, pronountiation);


        if (!AppUtils.isNetworkAvailable()){
            if (identifier == 1) {
                if (AppUtils.isNetworkAvailable()) {
                    if (playStopIdentifier == 0) {
                        try {
                            mediaPlayer.setDataSource(getApplicationContext(), Uri.parse(url));
                            mediaPlayer.prepare();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        mediaPlayer.start();
                    } else {
                        if (mediaPlayer.isPlaying()) {
                            mediaPlayer.stop();

                        }
                        mediaPlayer.reset();
                    }
                }
                else {
                    Toast.makeText(this, "Network not available", Toast.LENGTH_SHORT).show();

                }

            } else {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(intent, 10);
                } else {
                    Toast.makeText(this, "Your Device Don't Support Speech Input", Toast.LENGTH_SHORT).show();
                }
            }
            SharedPreferenceHelper.setSharedPreferenceString(this, AppConstants.STRING_PRONOUNTIATION, pronountiation);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 10:
                if (resultCode == RESULT_OK && data != null) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    mTxvPercentage.setVisibility(View.VISIBLE);
                    String percentage = AppUtils.computeInPercentage(SharedPreferenceHelper.getSharedPreferenceString(this,
                            AppConstants.STRING_PRONOUNTIATION, null), result.get(0));
                    mTxvPercentage.setText(percentage + " " + getString(R.string.voice_matched));
                }
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_RW_EXTERNAL:
                if (PermissionsUtils.hasGranted(grantResults)) {
                    downloadFile(getSpeechResponses);
                } else {
                    Toast.makeText(this, R.string.permission, Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void downloadFile(List<GetSpeechEntity> responseValue) {
        for (int i = 0; i < responseValue.size(); i++) {
            final int position = i;
            AndroidNetworking.download(responseValue.get(i).getAudioUrl(),
                    String.valueOf(Environment.getExternalStorageDirectory()), "MULTIBHASHI_AUDIO_" + i)
                    .setTag("downloadTest")
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .setDownloadProgressListener((bytesDownloaded, totalBytes) -> {
                        // do anything with progress
                    })
                    .startDownload(new DownloadListener() {
                        @Override
                        public void onDownloadComplete() {
                            // do anything after completion
                            //   Toast.makeText(MainActivity.this, "Download complete" + position, Toast.LENGTH_SHORT).show();
                            Log.d("download audio ", "Download complete" + String.valueOf(position));
                        }

                        @Override
                        public void onError(ANError error) {
                            // handle error
                        }
                    });
        }
    }


    private void initData() {
        AndroidInjection.inject(this);
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(MainViewModel.class);
        adapter = new ViewPagerAdapter(this);
        viewPager.setAdapter(adapter);
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        if (AppUtils.isNetworkAvailable()) {
            viewModel.getLiveData().observe(this, ApiResponse -> {
                if (ApiResponse != null) {
                    if (PermissionsUtils.isMarshMellow()) {
                        if (PermissionsUtils.hasSelfPermissions(MainActivity.this, PermissionsUtils.asArray(permissions))) {
                            downloadFile(ApiResponse.getResponse().getLessonData());
                        } else {
                            PermissionsUtils.requestAllPermissions(MainActivity.this, PermissionsUtils.asArray(permissions),
                                    MY_PERMISSIONS_REQUEST_RW_EXTERNAL);
                        }
                    }
                    totalItemCount = ApiResponse.getResponse().getLessonData().size();
                    getSpeechResponses = ApiResponse.getResponse().getLessonData();
                    adapter.setData(getSpeechResponses);
                    adapter.notifyDataSetChanged();

                }
            });
        } else {
            viewModel.getDataFromDb().observe(this, dataFromDb -> {
                if (dataFromDb != null && !dataFromDb.isEmpty()) {
                    adapter.setData(dataFromDb);
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(this, "Some Error occured please try again", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

}