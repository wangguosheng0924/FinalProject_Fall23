package algonquin.cst2335.finalproject_fall23;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import algonquin.cst2335.finalproject_fall23.databinding.ActivityArtistsSearchBinding;

public class MainActivity extends AppCompatActivity {







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button songBtton = findViewById(R.id.songBtton);

        songBtton.setOnClickListener(view -> {
            // Create an Intent to start the SongSearch Activity
            Intent intent = new Intent(MainActivity.this, ArtistsSearch.class);
            startActivity(intent);
        });

    }
}