package algonquin.cst2335.finalproject_fall23;

import android.app.AlertDialog;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    ArrayList<SongList> songCollect;

    int selectedRow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        songCollect = new ArrayList<>();
        binding = ActivityCollectionListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Toast.makeText(this, "Scroll down to see more songs", Toast.LENGTH_LONG).show();

        myAdapter =
                new RecyclerView.Adapter<ViewHolder>() {

                    //        onCreateViewHolder function is responsible for creating a layout
                    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

//                        View view = LayoutInflater.from(parent.getContext())
//                                .inflate(R.layout.collection_list, parent, false); // Inflate the item layout
//                        return new ViewHolder(view);

                        CollectionListBinding binding =
                                CollectionListBinding.inflate(getLayoutInflater(), parent, false);

                        return new ViewHolder(binding.getRoot());


                    }


                    @Override
                    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
                        SongList song = songCollect.get(position);

                        holder.songTitle.setText(song.songTitle);
                        holder.artistName.setText(song.artist);
                        holder.duration.setText(song.duration);
                        holder.albumName.setText(song.albumName);


                    }


                    @Override
                    public int getItemCount() {
                        return songCollect.size();
                    }
                };//populate the list
        initializeDatabaseAndLoadData();
        binding.songRecyclerView.setAdapter(myAdapter);

        binding.songRecyclerView.setLayoutManager(new LinearLayoutManager(this));

//        binding.deleteButton.setOnClickListener(click -> {
//
//            SongList selected = songCollect.get( selectedRow);
//            AlertDialog.Builder builder =
//                    new AlertDialog.Builder(CollectionList.this);
//
//            builder.setNegativeButton("No", (btn, obj) -> { /* if no is clicked */ });
//            builder.setMessage("Do you want to delete this song?");
//            builder.setTitle("Delete");
//
//
//            builder.setPositiveButton("yes", (p1, p2) -> {
////                    //add to database on another thread
//                Executor thread = Executors.newSingleThreadExecutor();
//                /*this runs in another thread*/
//                thread.execute(() -> {
//                    sDAO.deleteMessage(selected);//get the id from
////                    });
//                    songCollect.remove(selectedRow);//remove from the array list
//                    myAdapter.notifyDataSetChanged();//redraw the list
////
//
//                    Snackbar.make(binding.deleteButton, "You deleted the row", Snackbar.LENGTH_LONG)
//                            .setAction("Undo", (btn) -> {
//                                Executor thread2 = Executors.newSingleThreadExecutor();
//                                thread2.execute(() -> {
//                                    sDAO.insertMessage(selected);
//                                });
////
////
//                                songCollect.add(selectedRow, selected);
//                                myAdapter.notifyDataSetChanged();//redraw the list
//                            })
//                            .show();
//
//                });
//                builder.create().show(); //this has to be last
//
//            });
//        });
    }

    ;

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
        TextView songTitle, artistName, duration, albumName; // and other
        // TextViews

        public ViewHolder(View view) {
            super(view);
            songTitle = view.findViewById(R.id.songTitle);
            artistName = view.findViewById(R.id.artistName);
            duration = view.findViewById(R.id.duration);
            albumName = view.findViewById(R.id.albumName);

            view.setOnClickListener(click -> {
                int position = getAbsoluteAdapterPosition();//which row this is

                if (position != RecyclerView.NO_POSITION) {
                    selectedRow = position;
                    SongList selectedSong = songCollect.get(position);

                                // Action to show details
                                Intent intent = new Intent(CollectionList.this, SongDetail.class);
                                intent.putExtra("SONG_TITLE", selectedSong.songTitle);
                                intent.putExtra("ARTIST_NAME", selectedSong.artist);
                                intent.putExtra("DURATION", selectedSong.duration);
                                intent.putExtra("ALBUM_NAME", selectedSong.albumName);
                                startActivity(intent);
                            }



            });
        }
    }


}





