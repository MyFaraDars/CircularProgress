package com.ahj.mr.circularprogress;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SeekBar;

public class MainActivity extends AppCompatActivity {

    private CircularProgress circularProgress;
    private SeekBar seekBar;
    private CheckBox cbAutoColored, cbShowPercent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        cbAutoColored.setChecked(circularProgress.isAutoColored());
        cbShowPercent.setChecked(circularProgress.isShowPercent());

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                circularProgress.setProgressWithAnimation(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        cbAutoColored.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                circularProgress.setAutoColored(isChecked);
            }
        });

        cbShowPercent.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                circularProgress.setShowPercent(isChecked);
            }
        });
    }

    public void init(){
        circularProgress = (CircularProgress) findViewById(R.id.circle_progress);
        seekBar = (SeekBar) findViewById(R.id.seekbar_progress);
        cbAutoColored = (CheckBox) findViewById(R.id.cb_auto_colored);
        cbShowPercent = (CheckBox) findViewById(R.id.cb_show_percent);
    }
}
