package com.example.jayhind.xmlparsing;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

public class AudioActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener {

    ImageButton btnPlay;
    TextView tvTotalDuration, tvCurrentPosition;
    MediaPlayer mp;
    Context context;
    SeekBar songProgress;
    Handler handler=new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio);
        context=this;
        songProgress=findViewById(R.id.songProgress);
        btnPlay=findViewById(R.id.btnPlay);
        tvTotalDuration=findViewById(R.id.tvTotalDuration);
        tvCurrentPosition=findViewById(R.id.tvCurrentPosition);
        songProgress.setOnSeekBarChangeListener(this);
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mp==null) {
                    mp = MediaPlayer.create(context, R.raw.song);
                    mp.start();
                    btnPlay.setImageResource(android.R.drawable.ic_media_pause);
                    setHandler();
                }else{
                    if(mp.isPlaying()){
                        mp.pause();
                        btnPlay.setImageResource(android.R.drawable.ic_media_play);
                    }else{
                        mp.start();
                        btnPlay.setImageResource(android.R.drawable.ic_media_pause);
                    }
                }
            }
        });

    }
    void setHandler(){
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                long total=mp.getDuration();
                long current=mp.getCurrentPosition();
                tvTotalDuration.setText(millisToTimer(total));
                tvCurrentPosition.setText(millisToTimer(current));
                int progress=(int)((current*100)/total);
                songProgress.setProgress(progress);
                handler.postDelayed(this,1000);
            }
        },1000);
    }

    private String millisToTimer(long total) {

        long hour=total/(60*60*1000);
        long min=(total%(60*60*1000))/(60*1000);
        long sec=((total%(60*60*1000))%(60*1000))/1000;
        return hour+":"+min+":"+sec;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean byUser) {
        if(byUser){
            int total=mp.getDuration();
            int current = (progress*total)/100;
            mp.seekTo(current);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
