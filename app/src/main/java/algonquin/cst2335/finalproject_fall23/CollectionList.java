package algonquin.cst2335.finalproject_fall23;

import android.app.AlertDialog;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import algonquin.cst2335.finalproject_fall23.databinding.ActivityCollectionListBinding;
import algonquin.cst2335.finalproject_fall23.databinding.CollectionListBinding;
import algonquin.cst2335.finalproject_fall23.databinding.SongListBinding;
/**
 * The CollectionList activity provides functionality to display a list of songs in a collection
 * @author Lei Luo
  * @version 1.1
 */
public class CollectionList extends AppCompatActivity {
    /** Adapter for the RecyclerView to display songs. */
    RecyclerView.Adapter myAdapter;

    /** Data Access Object for the song list. */
    SongListDAO sDAO;

    /** View Binding for the Activity. */
    ActivityCollectionListBinding binding;

    /** ViewModel for managing song data. */
    SongViewModel songModel;

    /** ArrayList to hold the collection of songs. */
    ArrayList<SongList> songCollect;

    /** FrameLayout used to display song details. */
    FrameLayout frameLayout;

    /** Holds the position of the selected row. */
    int selectedRow;

    /**
     * Called when the activity is starting.
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down,
     *                           this Bundle contains the data it most recently supplied in onSaveInstanceState(Bundle).
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = ActivityCollectionListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.myToolbar);
        songCollect = new ArrayList<>();

        Toast.makeText(this, "Scroll down to see more songs", Toast.LENGTH_LONG).show();


        frameLayout = binding.fragmentLocation;
        frameLayout.setVisibility(View.GONE);


        myAdapter =
                new RecyclerView.Adapter<ViewHolder>() {

                    //        onCreateViewHolder function is responsible for creating a layout
                    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


                        CollectionListBinding binding = CollectionListBinding.inflate(getLayoutInflater(), parent, false);
                        return new ViewHolder(binding.getRoot());


                    }


                    @Override
                    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
                        SongList song = songCollect.get(position);

                        holder.songTitle.setText(song.songTitle);
                        holder.artistName.setText(song.artist);


                    }


                    @Override
                    public int getItemCount() {
                        return songCollect.size();
                    }
                };//populate the list
        initializeDatabaseAndLoadData();
        binding.songRecyclerView.setAdapter(myAdapter);
        //get data from ViewModel
        songModel = new ViewModelProvider(this).get(SongViewModel.class);
        songModel.selectedMessage.observe(this, newSelected -> {
            CollectionDetailsFragment newFragment = new CollectionDetailsFragment(newSelected);
            //to load fragments
            getSupportFragmentManager().beginTransaction().addToBackStack("").add(R.id.fragmentLocation, newFragment).commit();// This line actually loads the fragment into the specified FrameLayout

        });
        binding.songRecyclerView.setLayoutManager(new LinearLayoutManager(this));


    };
    /**
     * Initializes the Room database and loads data.
     */
    private void initializeDatabaseAndLoadData() {
        PersonalSongListData db = Room.databaseBuilder(getApplicationContext(),
                        PersonalSongListData.class,
                        "file")
                .fallbackToDestructiveMigration()
                .build();

        sDAO = db.cmDAO();//get a DAO object to interact with database


        Executor thread = Executors.newSingleThreadExecutor();
        thread.execute(() -> {
            List<SongList> fromDatabase = sDAO.getAllMessages();//return a List

            runOnUiThread(() -> {
                songCollect.addAll(fromDatabase);
                myAdapter.notifyDataSetChanged();
            });


        });

    }
    /**
     * ViewHolder class for RecyclerView items.
     */

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView songTitle, artistName, duration, albumName, collection; // and other
        // TextViews

        public ViewHolder(View view) {
            super(view);

            CollectionListBinding binding = CollectionListBinding.bind(view);
            Switch toggle = binding.switchControl;
            if (toggle != null) {
                toggle.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    if (isChecked) {
                        // Show the FrameLayout
                        frameLayout.setVisibility(View.VISIBLE);

                        int position = getAbsoluteAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            SongList selectedSong = songCollect.get(position);
                            songModel.selectedMessage.setValue(selectedSong); // Update the ViewModel
                        }
                    } else {
                        // Hide the FrameLayout
                        frameLayout.setVisibility(View.GONE);
                    }
                });
            } else {
                Log.e("ArtistsSearch", "Switch not found in the layout");
            }
            songTitle = binding.songTitle;


            artistName = binding.artistName;


            view.setOnClickListener(click -> {
                int position = getAbsoluteAdapterPosition();//which row this is


                SongList selectedSong = songCollect.get(position);

                // Action to show details
                Intent intent = new Intent(CollectionList.this, SongDetail.class);
                intent.putExtra("SONG_TITLE", selectedSong.songTitle);
                intent.putExtra("songTitle", selectedSong.songTitle);
                intent.putExtra("artistName", selectedSong.artist);
                intent.putExtra("duration",
                        String.valueOf(selectedSong.duration));
                intent.putExtra("albumName", selectedSong.albumName);
                intent.putExtra("Collection", selectedSong.Collection);
                intent.putExtra("imageURL", selectedSong.imageURL); // Make sure imageURL is properly set in SongList object
                startActivity(intent);
                startActivity(intent);


            });
        }
    }

    /**
     * Initialize the contents of the Activity's standard options menu.
     * @param menu The options menu in which items are placed.
     * @return true for the menu to be displayed; if false, it will not be shown.
     */
    @Override //initialize the toolbar
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.last_menu, menu);
        return true;

    }

    /**
     * Called whenever an item in your options menu is selected.
     * @param item The menu item that was selected.
     * @return false to allow normal menu processing to proceed; true to consume it here.
     */
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {


            case R.id.item_2:


                Intent intent2 = new Intent(CollectionList.this, ArtistsSearch.class);

                startActivity(intent2);


                break;

            case R.id.item_1:

                AlertDialog.Builder helpDialogBuilder = new AlertDialog.Builder(this);
                helpDialogBuilder.setTitle("How to Use");
                helpDialogBuilder.setMessage("Toggle the switch for more song" +
                        " info. \n\nUse the toolbar button to search for " +
                        "artists. \n\nClick on a song title to see its details.");
                helpDialogBuilder.setPositiveButton("OK", null);
                helpDialogBuilder.show();

                break;

            case R.id.item_3:


                Intent intent = new Intent(CollectionList.this, MainActivity.class);

                startActivity(intent);


                break;
        }
        return true;
    }

}





