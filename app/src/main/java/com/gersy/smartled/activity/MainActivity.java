package com.gersy.smartled.activity;

import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatSeekBar;

import com.gersy.smartled.R;
import com.gersy.smartled.base.BaseActivity;
import com.gersy.smartled.okhttp.HttpCallback;
import com.gersy.smartled.okhttp.OkhttpRequest;
import com.orhanobut.logger.Logger;
import com.skydoves.colorpickerview.ColorEnvelope;
import com.skydoves.colorpickerview.ColorPickerView;
import com.skydoves.colorpickerview.flag.BubbleFlag;
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener;
import com.skydoves.colorpickerview.sliders.BrightnessSlideBar;


public class MainActivity extends BaseActivity {

    private ColorPickerView colorPickerView;
    private int currentColor;

    @Override
    protected int setLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        colorPickerView = findViewById(R.id.colorPickerView);
        BubbleFlag bubbleFlag = new BubbleFlag(this);
//        bubbleFlag.setFlagMode(FlagMode.FADE);
        colorPickerView.setFlagView(bubbleFlag);
        colorPickerView.setColorListener(
                (ColorEnvelopeListener)
                        (envelope, fromUser) -> {
//                            setLayoutColor(envelope);
//                            Logger.e(envelope.toString());
                            setColor(envelope);
                        });

        // attach alphaSlideBar
//        final AlphaSlideBar alphaSlideBar = findViewById(R.id.alphaSlideBar);
//        colorPickerView.attachAlphaSlider(alphaSlideBar);

        // attach brightnessSlideBar
//        final BrightnessSlideBar brightnessSlideBar = findViewById(R.id.brightnessSlide);
//        colorPickerView.attachBrightnessSlider(brightnessSlideBar);
//        colorPickerView.setLifecycleOwner(this);

        AppCompatSeekBar brightnessSeekBar = findViewById(R.id.sb_brightness);
        AppCompatSeekBar speedSeekBar = findViewById(R.id.sb_speed);
        TextView tvFlow = findViewById(R.id.tv_flow);

        brightnessSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                setBrightness(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        speedSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                setSpeed(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        tvFlow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startFlow();
            }
        });
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {

    }

    private void startFlow(){
        OkhttpRequest.get("http://192.168.4.1/flow").async(new HttpCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Logger.e(result);
            }

            @Override
            public void onFailure(Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void setBrightness(int brightness){
        OkhttpRequest.get("http://192.168.4.1/setBrightness?brightness="+brightness).async(new HttpCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Logger.e(result);
            }

            @Override
            public void onFailure(Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void setSpeed(int speed){
        OkhttpRequest.get("http://192.168.4.1/setSpeed?speed="+speed).async(new HttpCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Logger.e(result);
            }

            @Override
            public void onFailure(Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void setColor(ColorEnvelope colorEnvelope){
        if (currentColor==colorEnvelope.getColor()){
            return;
        }
        int[] argb = colorEnvelope.getArgb();
        String color="["+argb[1]+","+argb[2]+","+argb[3]+"]";
        OkhttpRequest.get("http://192.168.4.1/setColor?color="+color).async(new HttpCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Logger.e(result);
            }

            @Override
            public void onFailure(Exception e) {
                e.printStackTrace();
            }
        });
    }
}
