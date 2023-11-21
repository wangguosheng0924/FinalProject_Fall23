package algonquin.cst2335.finalproject_fall23;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.zip.Inflater;

import algonquin.cst2335.finalproject_fall23.databinding.ActivitySongDetailBinding;

public class SongDetail extends AppCompatActivity {
    SongListDAO sDAO;

    ArrayList<SongList> songCollect = null;

    ActivitySongDetailBinding binding;
    TextView songTitle;

    TextView duration;

    TextView artistName;

    TextView albumName;

    String albumCoverName ;
    Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        songCollect = new ArrayList<>();

        binding = ActivitySongDetailBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());
        songTitle= findViewById(R.id.songTitle);

        artistName= findViewById(R.id.artistName);
        albumName= findViewById(R.id.albumName);
        duration= findViewById(R.id.duration);
        saveButton=findViewById(R.id.saveButton);

        Intent intent = getIntent();
        songTitle.setText(intent.getStringExtra("SONG_TITLE"));
        artistName.setText(intent.getStringExtra("ARTIST_NAME"));
        duration.setText(intent.getStringExtra("DURATION"));
        albumName.setText(intent.getStringExtra("ALBUM_NAME"));

        //load messages from the database:
        PersonalSongListData db = Room.databaseBuilder(getApplicationContext(),
                        PersonalSongListData.class,
                        "file")
                .fallbackToDestructiveMigration()
                .build();

        sDAO = db.cmDAO();//get a DAO object to interact with database


        Executor thread2 = Executors.newSingleThreadExecutor();
        thread2.execute(() -> {
            List<SongList> fromDatabase = sDAO.getAllMessages();//return a List
            songCollect.addAll(fromDatabase);//this adds all messages from the database

        });

        //end of loading from database


        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            String artist = extras.getString("artistName", "Default Artist");
            String song = extras.getString("songTitle", "Default Song");
            albumCoverName = extras.getString("albumCover", "Default Song");


            artistName.setText(artist);
            songTitle.setText(song);

        }

        binding.deleteButton.setOnClickListener(click -> {
            String songTitleToDelete = songTitle.getText().toString();
            AlertDialog.Builder builder =
                    new AlertDialog.Builder(SongDetail.this);

            builder.setNegativeButton("No", (btn, obj) -> { /* if no is clicked */ });
            builder.setMessage("Do you want to delete this song?");
            builder.setTitle("Delete");


            builder.setPositiveButton("yes", (p1, p2) -> {
//                    //add to database on another thread
                Executor thread = Executors.newSingleThreadExecutor();
                /*this runs in another thread*/
                thread.execute(() -> {
                    for (SongList song : songCollect) {
                        if (song.songTitle.equals(songTitleToDelete)) {
                            sDAO.deleteMessage(song);
                            break; // Exit loop after finding and deleting the song
                        }
                    }
                    runOnUiThread(() -> {
                Intent returnIntent = new Intent(SongDetail.this, CollectionList.class);
                startActivity(returnIntent);
                finish(); // Close the current activity

                    });
                });
            });
                builder.create().show(); //this has to be last

            });
//

        binding.saveButton.setOnClickListener(click -> {

            // Assuming you have artistName and songTitle TextViews

            String artistN = artistName.getText().toString();
            String song = songTitle.getText().toString();
            String aN = albumName.getText().toString();
            String du = duration.getText().toString();


            // Create a SongList object
            SongList thisSong = new SongList(artistN, song, albumCoverName, du, aN);

            songCollect.add(thisSong);
            // Database insertion on a background thread
            Executor  thread3 = Executors.newSingleThreadExecutor();
            thread3.execute(() -> {

                thisSong.Id = sDAO.insertMessage(thisSong); // Method to
                // insert SongList into the database

            });

            Toast.makeText(SongDetail.this, "Song saved!",
                    Toast.LENGTH_SHORT).show();
        });

        binding.historyButton.setOnClickListener(click -> {


            Intent intent2 = new Intent(SongDetail.this, CollectionList.class);

            startActivity(intent2);




            });
        };

    }
