package com.app.yombovoice.Activities;

import java.io.File;
import java.io.IOException;

import android.content.Context;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.os.Vibrator;

import com.app.yombovoice.AppLog;
import com.app.yombovoice.R;
import com.app.yombovoice.common.DonutProgress;

public class MainActivity extends AppCompatActivity {
    ImageButton btn_rec;
    private static final String AUDIO_RECORDER_FILE_EXT_3GP = ".3gp";
    private static final String AUDIO_RECORDER_FILE_EXT_MP4 = ".mp4";
    private static final String filename = "vombo";
    private static final String AUDIO_RECORDER_FOLDER = "AudioRecorder";
    private MediaRecorder recorder = null;
    private int currentFormat = 0;
    private int output_formats[] = { MediaRecorder.OutputFormat.MPEG_4, MediaRecorder.OutputFormat.THREE_GPP };
    private String file_exts[] = { AUDIO_RECORDER_FILE_EXT_MP4, AUDIO_RECORDER_FILE_EXT_3GP };
    private DonutProgress donutProgress;
    int currentPos;
    int statue;
    Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable() {

        @Override
        public void run() {
            if( currentPos >= 200 ){
                stopRecording();
                currentPos = 0;
                timerHandler.removeCallbacks(timerRunnable);
            }
            else {
                timerHandler.postDelayed(this, 100);
                currentPos++;
                donutProgress.setProgress(donutProgress.getProgress() + 1);

                donutProgress.setText(String.format("%d",20-currentPos/10));
            }

        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        donutProgress = (DonutProgress) findViewById(R.id.donut_progress);
        btn_rec=(ImageButton)findViewById(R.id.btn_mic);
        donutProgress.setProgress(0);
        donutProgress.setText("");
        donutProgress.setMax(200);
        currentPos = statue = 0;
        ImageButton btn_next = (ImageButton) findViewById(R.id.btn_screen2);
//        btn_next.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(MainActivity.this, OptionActivity.class));
//                finish();
//            }
//        });
        ImageButton btn_setting = (ImageButton) findViewById(R.id.btn_setting);
        btn_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SettingActivity.class));
            }
        });
        btn_rec.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                if(statue == 1) return  false;
                switch(event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        AppLog.logString("Start Recording");
                        startRecording();
                        break;
                    case MotionEvent.ACTION_UP:
                        AppLog.logString("stop Recording");
                        stopRecording();
                        break;
                }
                return false;
            }
        });


    }


    private String getFilename(){
        String filepath = Environment.getExternalStorageDirectory().getPath();
        File file = new File(filepath,AUDIO_RECORDER_FOLDER);

        if (!file.exists()) {
            file.mkdirs();
        }

//        return (file.getAbsolutePath() + "/" + System.currentTimeMillis() + file_exts[currentFormat]);
        return (file.getAbsolutePath() + "/" + filename + file_exts[currentFormat]);
    }
    private void startRecording(){
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(output_formats[currentFormat]);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        recorder.setOutputFile(getFilename());
        recorder.setMaxDuration(20000);//MAX = 20 sec
        recorder.setOnErrorListener(errorListener);
        recorder.setOnInfoListener(infoListener);
        currentPos = 0;
        try {
            recorder.prepare();
            recorder.start();
            btn_rec.setVisibility(View.INVISIBLE);
            Vibrator v = (Vibrator) MainActivity.this.getSystemService(Context.VIBRATOR_SERVICE);
            // Vibrate for 500 milliseconds
            v.vibrate(500);
            timerHandler.postDelayed(timerRunnable, 0);
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private MediaRecorder.OnErrorListener errorListener = new MediaRecorder.OnErrorListener() {
        @Override
        public void onError(MediaRecorder mr, int what, int extra) {
            AppLog.logString("Error: " + what + ", " + extra);
        }
    };

    private MediaRecorder.OnInfoListener infoListener = new MediaRecorder.OnInfoListener() {
        @Override
        public void onInfo(MediaRecorder mr, int what, int extra) {
            AppLog.logString("Warning: " + what + ", " + extra);
        }
    };

    private void stopRecording(){
        if(null != recorder){
            recorder.stop();
            recorder.reset();
            recorder.release();
            recorder = null;
            Vibrator v = (Vibrator) MainActivity.this.getSystemService(Context.VIBRATOR_SERVICE);
            v.vibrate(500);
            timerHandler.removeCallbacks(timerRunnable);
            btn_rec.setVisibility(View.VISIBLE);
            donutProgress.setText("");
//            audioPlayer(AUDIO_RECORDER_FOLDER, "vombo.mp4");
            startActivity(new Intent(MainActivity.this,OptionActivity.class));
            finish();
        }
    }

}