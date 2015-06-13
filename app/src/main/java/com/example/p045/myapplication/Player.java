package com.example.p045.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;


public class Player extends Activity implements View.OnClickListener {
    int position;
    TextView tv1;
    Button play,next,prev;
    SeekBar sb;
    Thread update;
    Uri u;

    static MediaPlayer mp;
    ArrayList<File> mysongs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);



        //getActionBar().setDisplayHomeAsUpEnabled(true);

        play = (Button)findViewById(R.id.playButton);
        next = (Button)findViewById(R.id.nextButton);
        prev = (Button)findViewById(R.id.prevButton);
        tv1 = (TextView)findViewById(R.id.textView);
        sb = (SeekBar)findViewById(R.id.seekbar);
        update = new Thread(){
            @Override
            public  void run(){

                int totalDuration  = mp.getDuration();
                int currentPosition = 0;
                //sb.setMax(totalDuration);
                while(currentPosition < totalDuration)
                {
                    try {
                        sleep(500);
                        currentPosition = mp.getCurrentPosition();
                        totalDuration  = mp.getDuration();
                        sb.setProgress(currentPosition);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }


                //super.run();
            }

        };

        play.setOnClickListener(this);
        next.setOnClickListener(this);
        prev.setOnClickListener(this);

        if(mp!=null)
        {
            mp.stop();
            mp.release();
        }

        Intent i = getIntent();
        Bundle b = i.getExtras();
        mysongs = (ArrayList)b.getParcelableArrayList("songlist");
        position = b.getInt("pos", 0);

        u = Uri.parse(mysongs.get(position).toString());
        mp = MediaPlayer.create(getApplicationContext(), u);

        mp.start();
        tv1.setText(mysongs.get(position).toString());
        sb.setMax(mp.getDuration());
        update.start();

        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mp.seekTo(seekBar.getProgress());

            }
        });



        /*try{
            ViewConfiguration config = ViewConfiguration.get(this);
            Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
            if(menuKeyField != null){
                menuKeyField.setAccessible(true);
                menuKeyField.setBoolean(config, false);
            }

        }catch (Exception e)
        {

        }

        ActionBar ab = getActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.show();*/



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_player, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            Intent sp;
            Class cts = null;
            try {
                cts = Class.forName("com.example.p045.myapplication.Developers");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            sp = new Intent(this,cts);
            startActivity(sp);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();
        switch (id)
        {
            case  R.id.playButton :
                if(mp.isPlaying())
                {
                    play.setText("|>");
                    mp.pause();

                }
                else
                {
                    play.setText("||");
                    mp.start();
                    sb.setMax(mp.getDuration());
                }
                break;
            case R.id.nextButton :
                mp.stop();
                mp.release();
                position = (position+1 )% (mysongs.size());
                u = Uri.parse(mysongs.get(position).toString());
                mp = MediaPlayer.create(getApplicationContext(), u);

                mp.start();
                tv1.setText(mysongs.get(position).toString());
                sb.setMax(mp.getDuration());

                break;
            case R.id.prevButton :
                mp.stop();
                mp.release();
                position = (position-1)<0?mysongs.size()-1 : position-1;
                u = Uri.parse(mysongs.get(position).toString());
                mp = MediaPlayer.create(getApplicationContext(), u);

                mp.start();
                tv1.setText(mysongs.get(position).toString());
                sb.setMax(mp.getDuration());

                break;

        }

    }
}
