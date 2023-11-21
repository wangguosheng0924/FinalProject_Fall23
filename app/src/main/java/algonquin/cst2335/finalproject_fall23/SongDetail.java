package algonquin.cst2335.finalproject_fall23;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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
        });

        binding.historyButton.setOnClickListener(click -> {

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
        });

    }
}