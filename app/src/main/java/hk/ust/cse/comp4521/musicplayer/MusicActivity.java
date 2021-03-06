package hk.ust.cse.comp4521.musicplayer;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Observable;
import java.util.Observer;

import hk.ust.cse.comp4521.musicplayer.player.MusicPlayer;
import hk.ust.cse.comp4521.musicplayer.player.PlayerState;
import hk.ust.cse.comp4521.musicplayer.player.Playlist;

public class MusicActivity extends AppCompatActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener, Playlist.OnSongSelectedListener {

    private static final String TAG = "MusicPlayer";
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create a layout for the Activity with four buttons:
        // Rewind, Pause, Play and Forward and set it to the view of this activity
        setContentView(R.layout.activity_music);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Log.i(TAG, "Activity: onCreate()");

        // create a new instance of the music player
        player = MusicPlayer.getMusicPlayer();
        player.setContext(this);

        startSong(songIndex);

        android.app.Fragment firstFragment = new SongPlaying();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.add(R.id.fragment_container, firstFragment, getResources().getString(R.string.NowPlaying));
        ft.commit();
    }

    private	void startSong(int index){
        final String[] songFile	= getResources().getStringArray(R.array.filename);
        final String[] songList = getResources().getStringArray(R.array.Songs);

        player.start(getResources().getIdentifier(songFile[index], "raw", getPackageName()), songList[index]);
    }


    @Override
    protected void onDestroy() {

        Log.i(TAG, "Activity: onDestroy()");

        // reset the music player and release the media player
        player.reset();

        super.onDestroy();
    }

    @Override
    protected void onPause() {

        super.onPause();
        Log.i(TAG, "Activity: onPause()");
    }

    @Override
    protected void onRestart() {

        super.onRestart();
        Log.i(TAG, "Activity: onRestart()");
    }

    @Override
    protected void onResume() {

        super.onResume();
        Log.i(TAG, "Activity: onResume()");
    }

    @Override
    protected void onStart() {

        super.onStart();
        Log.i(TAG, "Activity: onStart()");
    }

    @Override
    protected void onStop() {

        super.onStop();
        Log.i(TAG, "Activity: onStop()");
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_music, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_playlist:
                Intent i = new Intent(getApplicationContext(), Playlist.class);
                startActivityForResult(i, 100);
                return true;
            default:
                break;

        }

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == 100) {
            songIndex = data.getExtras().getInt("songIndex");

            player.reset();

            startSong(songIndex);
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

    @Override
    public void onSongSelected(int id) {

    }
}
