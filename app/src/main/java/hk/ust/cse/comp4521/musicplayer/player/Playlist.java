package hk.ust.cse.comp4521.musicplayer.player;

import android.app.ListActivity;
import android.app.ListFragment;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import hk.ust.cse.comp4521.musicplayer.MusicActivity;
import hk.ust.cse.comp4521.musicplayer.R;

public class Playlist extends ListFragment {

    private static final String TAG = "Playlist";
    OnSongSelectedListener mListener = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onCreate");
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Give some text to display if there is no data. In a real
        // application this would come from a resource.
        setEmptyText("No songs yet");

        // We have a menu item to show in action bar.
        setHasOptionsMenu(true);

        // create a string array and initialize it with string array resources from strings.xml
        final String[] songList = getResources().getStringArray(R.array.Songs);

        // create a list adapter and supply it to the listview so that the list of songs can
        // be displayed in the listview
        this.setListAdapter((ListAdapter) new ArrayAdapter<String>(getActivity(), R.layout.activity_playlist, R.id.songList, songList));

        ListView lv = getListView();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                mListener.onSongSelected(position);
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof OnSongSelectedListener) {
            mListener = (OnSongSelectedListener) context;
        }
        else {
            throw new RuntimeException(context.toString() + " must implement OnSongSelectedListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    //    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        final String[] songList = getResources().getStringArray(R.array.Songs);
//
//        this.setListAdapter(new ArrayAdapter<String>(this, R.layout.activity_playlist, R.id.songList, songList));
//
//        ListView listView = getListView();
//
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Intent in = new Intent(getApplicationContext(), MusicActivity.class);
//                in.putExtra("songIndex", i);
//
//                setResult(100, in);
//                // exit from this activity
//                finish();
//            }
//        });
//
////        setContentView(R.layout.activity_playlist);
//    }


    // the interface that must be implemented by the host activity for communicating from the
    // fragment to the activity
    public interface OnSongSelectedListener {
        public void onSongSelected(int id);
    }

}
