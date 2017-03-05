package hk.ust.cse.comp4521.musicplayer;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.Observable;
import java.util.Observer;

import hk.ust.cse.comp4521.musicplayer.player.MusicPlayer;
import hk.ust.cse.comp4521.musicplayer.player.PlayerState;
import hk.ust.cse.comp4521.musicplayer.player.Playlist;


public class SongPlaying extends android.app.Fragment implements View.OnClickListener, SeekBar.OnSeekBarChangeListener, Observer {
    private static final String TAG = "SongPlaying";
    private static ImageButton playerButton, rewindButton, forwardButton;
    public static Handler handler;
    private TextView songTitleText, curTime, remTime;
    private static int songIndex = 0;
    private SeekBar songProgressBar;


    /*
     * Class Name: MusicPlayer
     *
     *    This class implements support for playing a music file using the MediaPlayer class in Android.
     *    It supports the following methods:
     *
     *    play_pause(): toggles the player between playing and paused states
     *    resume(): resume playing the current song
     *    pause(): pause the currently playing song
     *    rewind(): rewind the currently playing song by one step
     *    forward(): forward the currently playing song by one step
     *    stop(): stop the currently playing song
     *    reset(): reset the music player and release the MediaPlayer associated with it
     *    reposition(value): repositions the playing position of the song to value% and resumes playing
     *
     *    progress(): returns the percentage of the playback completed. useful to update the progress bar
     *    completedTime(): Amount of the song time completed playing
     *    remainingTime(): Remaining time of the song being played
     *
     *    You should use these methods to manage the playing of the song.
     *
     */
    private MusicPlayer player;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create a layout for the Activity with four buttons:
        // Rewind, Pause, Play and Forward and set it to the view of this activity
//        setContentView(R.layout.activity_music);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        Log.i(TAG, "onCreate");

        // create a new instance of the music player
        player = MusicPlayer.getMusicPlayer();
//        player.setContext(this);
        player.addObserver(this);

//        startSong(songIndex);

        handler = new Handler();

//        setProgress(player.progress());
//        curTime.setText(player.completedTime());
//        remTime.setText("-" + player.remainingTime());


    }



    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.play:
                player.play_pause();
                break;

            case R.id.forward:
                player.forward();
                break;

            case R.id.rewind:
                player.rewind();
                break;

            default:
                break;
        }
    }



    @Override
    public void update(Observable observable, Object o) {
        switch ((PlayerState) o) {
            case Ready:
                Log.i(TAG, "Activity: Player State Changed to Ready");
//                songTitleText.setText(player.getSongTitle());
//                playerButton.setImageResource(R.drawable.img_btn_play);
//                songProgressBar.setProgress(player.progress());
//                curTime.setText(player.completedTime());
//                remTime.setText("-" + player.remainingTime());
                break;
            case Paused:
                Log.i(TAG, "Activity: Player State Changed to Paused");
                playerButton.setImageResource(R.drawable.img_btn_play);
                cancelUpdateSongProgress();
                songProgressBar.setProgress(player.progress());
                curTime.setText(player.completedTime());
                remTime.setText("-" + player.remainingTime());
                break;
            case Stopped:
                Log.i(TAG, "Activity: Player State Changed to Stopped");
                playerButton.setImageResource(R.drawable.img_btn_play);
                cancelUpdateSongProgress();
                break;
            case Playing:
                Log.i(TAG, "Activity: Player State Changed to Playing");
                playerButton.setImageResource(R.drawable.img_btn_pause);
                updateSongProgress();
                break;
            case Reset:
                Log.i(TAG, "Activity: Player State Changed to Reset");
                playerButton.setImageResource(R.drawable.img_btn_play);
                cancelUpdateSongProgress();
                break;
            default:
                break;
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        cancelUpdateSongProgress();
        if(fromUser && player.isPlaying()) {
            player.reposition(progress);
        }
        updateSongProgress();
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    public void updateSongProgress() {
        handler.postDelayed(songProgressUpdate, 500);
        playerButton.setImageResource(R.drawable.img_btn_pause);
    }

    public void cancelUpdateSongProgress() {
        handler.removeCallbacks(songProgressUpdate);
        playerButton.setImageResource(R.drawable.img_btn_play);
    }

    private Runnable songProgressUpdate = new Runnable() {
        @Override
        public void run() {
            songProgressBar.setProgress(player.progress());
            curTime.setText(player.completedTime());
            remTime.setText("-" + player.remainingTime());

            // schedule another update for every 500 msec later
            handler.postDelayed(songProgressUpdate, 500);
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_song_playing, container, false);

        Log.i(TAG, "onCreateView");

        // Get the references to the buttons from the layout of the activity
        playerButton = (ImageButton) view.findViewById(R.id.play);
        playerButton.setOnClickListener(this);

        rewindButton = (ImageButton) view.findViewById(R.id.rewind);
        rewindButton.setOnClickListener(this);

        forwardButton = (ImageButton) view.findViewById(R.id.forward);
        forwardButton.setOnClickListener(this);

        //	get	a	reference	to	the	song	title	TextView	in	the	UI
        songTitleText = (TextView) view.findViewById(R.id.songTitle);

        // get reference to the SeekBar, completion time and remaining time.
        songProgressBar = (SeekBar) view.findViewById(R.id.songProgressBar);
        //set max to 100, means that complete song has been played
        songProgressBar.setMax(100);
        //initializing SeekBarChangeListener
        songProgressBar.setOnSeekBarChangeListener(this);

        curTime= (TextView) view.findViewById(R.id.tvSongCurrentDuration);
        remTime = (TextView) view.findViewById(R.id.tvSongRemainingDuration);

        return view;
    }
}
