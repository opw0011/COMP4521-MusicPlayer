package hk.ust.cse.comp4521.musicplayer.player;

import android.app.ListActivity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import hk.ust.cse.comp4521.musicplayer.MusicActivity;
import hk.ust.cse.comp4521.musicplayer.R;

public class Playlist extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final String[] songList = getResources().getStringArray(R.array.Songs);

        this.setListAdapter(new ArrayAdapter<String>(this, R.layout.activity_playlist, R.id.songList, songList));

        ListView listView = getListView();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent in = new Intent(getApplicationContext(), MusicActivity.class);
                in.putExtra("songIndex", i);

                setResult(100, in);
                // exit from this activity
                finish();
            }
        });

//        setContentView(R.layout.activity_playlist);
    }




}
