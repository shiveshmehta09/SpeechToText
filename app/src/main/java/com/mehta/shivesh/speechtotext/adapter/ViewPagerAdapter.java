package com.mehta.shivesh.speechtotext.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mehta.shivesh.speechtotext.R;
import com.mehta.shivesh.speechtotext.db.entity.GetSpeechEntity;
import com.mehta.shivesh.speechtotext.utils.AppUtils;

import java.util.List;

public class ViewPagerAdapter extends PagerAdapter {

    private List<GetSpeechEntity> responseValues;
    private LayoutInflater mLayoutInflater;
    private OnClickEvent onClickEvent;
    private Context context;
    private int playStopIdentifier = 0;

    public ViewPagerAdapter(Context context) {
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
        try {
            this.onClickEvent = ((OnClickEvent) context);
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement OnBuilderSelectedListener.");
        }
    }

    public void setData(List<GetSpeechEntity> responseValues) {
        this.responseValues = responseValues;
        playStopIdentifier = 0;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = mLayoutInflater.inflate(R.layout.item, container, false);
        TextView mTxvEnglish = view.findViewById(R.id.txv_eng);
        TextView mTxvTips = view.findViewById(R.id.txv_tips);
        TextView mTxvOtherLang = view.findViewById(R.id.txv_other_language);
        TextView mTxvPronountiation = view.findViewById(R.id.txv_prono);
        FloatingActionButton mFabPlay = view.findViewById(R.id.fab_play);
        ImageView learnImgView = view.findViewById(R.id.imageView3);
        FloatingActionButton fabSpeak = view.findViewById(R.id.fab_speak);

        mTxvEnglish.setText(responseValues.get(position).getConceptName());
        mTxvOtherLang.setText(responseValues.get(position).getTargetScript());

        if (responseValues.get(position).getType().equals("learn")) {
            learnImgView.setVisibility(View.VISIBLE);
            fabSpeak.setVisibility(View.GONE);
            mTxvTips.setVisibility(View.VISIBLE);
            mTxvTips.setOnClickListener(v -> {
                mTxvPronountiation.setVisibility(View.VISIBLE);
                learnImgView.setVisibility(View.GONE);
                mTxvTips.setVisibility(View.GONE);
                mTxvPronountiation.setText("Pronountiation:" + " " + responseValues.get(position).getPronunciation());
            });
            mTxvPronountiation.setOnClickListener(v -> {
                mTxvPronountiation.setVisibility(View.GONE);
                learnImgView.setVisibility(View.VISIBLE);
                mTxvTips.setVisibility(View.VISIBLE);
            });


        } else if (responseValues.get(position).getType().equals("question")) {
            fabSpeak.setVisibility(View.VISIBLE);
            learnImgView.setVisibility(View.GONE);
            mTxvTips.setVisibility(View.GONE);
            mTxvPronountiation.setVisibility(View.GONE);

        }

       /* if (!AppUtils.isNetworkAvailable()){
            if(AppUtils.listFiles(context) != null){

            //fetching file from data storage----

                for (int i = 0; i< AppUtils.listFiles(context).size(); i++){

                    String dataFromFile = AppUtils.listFiles(context).get(i).toString();
                    mFabPlay.setOnClickListener(view1 -> {
                        onClickEvent.onClickAction(1, dataFromFile, playStopIdentifier, responseValues.get(position).getPronunciation());
                        if (playStopIdentifier == 0) {
                            mFabPlay.setImageResource(R.drawable.ic_stop);
                            playStopIdentifier++;
                        } else {
                            mFabPlay.setImageResource(R.drawable.ic_play_circle_outline_black_24dp);
                            playStopIdentifier = 0;
                        }
                    });


                    fabSpeak.setOnClickListener(view1 ->
                            onClickEvent.onClickAction(2, "", playStopIdentifier, responseValues.get(position).getPronunciation()));
                }
            }
        }*/


        mFabPlay.setOnClickListener(view1 -> {
            onClickEvent.onClickAction(1, responseValues.get(position).getAudioUrl(), playStopIdentifier, responseValues.get(position).getPronunciation());
            if (playStopIdentifier == 0) {
                mFabPlay.setImageResource(R.drawable.ic_stop);
                playStopIdentifier++;
            } else {
                mFabPlay.setImageResource(R.drawable.ic_play_circle_outline_black_24dp);
                playStopIdentifier = 0;
            }
        });


        fabSpeak.setOnClickListener(view1 ->
                onClickEvent.onClickAction(2, "", playStopIdentifier, responseValues.get(position).getPronunciation()));

        container.addView(view, 0);
        return view;
    }

    @Override
    public int getCount() {
        return responseValues != null ? responseValues.size() : 0;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object obj) {
        return view == obj;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "";
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        View view = (View) object;
        container.removeView(view);
    }

    public interface OnClickEvent {
        void onClickAction(int identifier, String url, int playStopIdentifier, String pronountiation);
    }
}