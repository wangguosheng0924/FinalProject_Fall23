package algonquin.cst2335.finalproject_fall23;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class SongDetail extends AppCompatActivity {
    TextView songTitle;
    ImageView albumCover;

    TextView artistName;

    TextView albumName;

    Button saveButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_detail);

          songTitle= findViewById(R.id.songTitle);
          albumCover=findViewById(R.id.albumCover);
          artistName= findViewById(R.id.artistName);
          albumName= findViewById(R.id.albumName);
          saveButton=findViewById(R.id.saveButton);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String artist = extras.getString("artistName", "Default Artist");
            String song = extras.getString("songTitle", "Default Song");
            artistName.setText(artist);
            songTitle.setText(song);
        }

    }
}