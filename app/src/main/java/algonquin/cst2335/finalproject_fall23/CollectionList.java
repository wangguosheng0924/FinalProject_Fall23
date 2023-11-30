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

public class CollectionList extends AppCompatActivity {
    RecyclerView.Adapter myAdapter;
    SongListDAO sDAO;
    ActivityCollectionListBinding binding;

    SongViewModel songModel;

    ArrayList<SongList> songCollect;
    FrameLayout frameLayout;
    int selectedRow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = ActivityCollectionListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        songCollect = new ArrayList<>();

        Toast.makeText(this, "Scroll down to see more songs", Toast.LENGTH_LONG).show();



        frameLayout =binding.fragmentLocation;
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
        songModel.selectedMessage.observe(this, newSelected -> {CollectionDetailsFragment newFragment = new CollectionDetailsFragment(newSelected);
            //to load fragments
            getSupportFragmentManager().beginTransaction().addToBackStack("").add(R.id.fragmentLocation, newFragment).commit();// This line actually loads the fragment into the specified FrameLayout

        });
        binding.songRecyclerView.setLayoutManager(new LinearLayoutManager(this));





    };

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


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView songTitle, artistName, duration, albumName, collection; // and other
        // TextViews

        public ViewHolder(View view) {
            super(view);

            CollectionListBinding binding = CollectionListBinding.bind(view);
Switch toggle=binding.switchControl;
            if (toggle != null) {
                toggle.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    if (isChecked) {
                        // Show the FrameLayout
                        frameLayout.setVisibility(View.VISIBLE);
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


}





