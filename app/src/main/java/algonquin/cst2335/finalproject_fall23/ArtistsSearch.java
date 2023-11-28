package algonquin.cst2335.finalproject_fall23;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.ColumnInfo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import algonquin.cst2335.finalproject_fall23.databinding.ActivityArtistsSearchBinding;
import algonquin.cst2335.finalproject_fall23.databinding.ActivitySongDetailBinding;
import algonquin.cst2335.finalproject_fall23.databinding.CollectionListBinding;
import algonquin.cst2335.finalproject_fall23.databinding.SongListBinding;

public class ArtistsSearch extends AppCompatActivity {

    ActivityArtistsSearchBinding binding;
    ArrayList<SongList> artistSongs = null;
    String artist;
    RecyclerView.Adapter myAdapter;

    SongViewModel songModel;

    String songTitle;

    int ArtistID;

    String albumCover;

    int duration;
    String albumName;

    protected RequestQueue queue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityArtistsSearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        SharedPreferences prefs = getSharedPreferences("ArtistSearchPrefs", MODE_PRIVATE);
        String lastSearch = prefs.getString("lastSearch", "");

        binding.textInput.setText(lastSearch);

        songModel = new ViewModelProvider(this).get(SongViewModel.class);

        artistSongs = songModel.artistSongs;

        queue = Volley.newRequestQueue(this);

        //Listener
        binding.searchButton.setOnClickListener(click -> {
            String userInput = binding.textInput.getText().toString();

            SharedPreferences pref = getSharedPreferences("ArtistSearchPrefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            editor.putString("lastSearch", userInput);
            editor.apply();

            binding.artistName.setText("Here are the songs of: " + userInput);
            binding.artistName.setVisibility(View.VISIBLE);

            Toast.makeText(ArtistsSearch.this, "Click song tile to check " +
                            "the details",
                    Toast.LENGTH_SHORT).show();

            // First API Request

            try {
                artist = URLEncoder.encode(userInput, "UTF-8");
                String stringURL = "https://api.deezer.com/search/artist/?q=" + artist;
                JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                        stringURL, null,
                        response -> {
                            try {

                                JSONObject start = response.getJSONArray(
                                        "data").getJSONObject(0);
                                ArtistID = start.getInt("id");

                                String artistStringURL = "https://api.deezer.com/artist/" + ArtistID +
                                        "/top?limit=50";

//nested request
                                JsonObjectRequest request2 =
                                        new JsonObjectRequest(Request.Method.GET, artistStringURL, null,
                                                response2 -> {
                                                    try {

                                                        JSONArray dataArray =
                                                                response2.getJSONArray(
                                                                        "data");



                                                        for (int i = 0; i < 50; i++) {
                                                            JSONObject songObject = dataArray.getJSONObject(i);
                                                            songTitle = songObject.getString("title");
                                                            duration = songObject.getInt("duration");
                                                            JSONObject albumObject = songObject.getJSONObject("album");
                                                            albumName =
                                                                    albumObject.getString("title");
                                                            String md5_image = albumObject.getString("md5_image");






                                                            // Create SongList object and add to the list
                                                            SongList song =
                                                                    new SongList(userInput, songTitle, duration, albumName,md5_image );

                                                            artistSongs.add(song);

                                                        myAdapter.notifyDataSetChanged();
                                                    }
                                                    } catch (JSONException e) {
                                                        Log.e("ArtistSearch",
                                                                "JSON " +
                                                                        "parsing error: " + e.getMessage());
                                                    }
                                                },
                                                error -> Log.e("ArtistSearch", "Volley error: " + error.getMessage())
                                        );
                                queue.add(request2);

                            } catch (JSONException e) {
                                e.printStackTrace(); // Handle JSON parsing error
                            }
                        },
                        error -> {
                            // Handle error
                        });

                queue.add(request);

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace(); // Handle UnsupportedEncodingException
            }
        });


        binding.songList.setAdapter(myAdapter =
                new RecyclerView.Adapter<MyRowHolder>() {
                    @NonNull
                    @Override
                    public MyRowHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                          int viewType) {

                        SongListBinding binding =
                                SongListBinding.inflate(getLayoutInflater(), parent, false);


                        return new MyRowHolder(binding.getRoot());
                    }

                    @Override
                    public void onBindViewHolder(@NonNull MyRowHolder holder, int position) {
                        SongList song = artistSongs.get(position);

                        holder.songName.setText(song.songTitle);


                        View.OnClickListener clickListener = view -> {
                            Intent intent = new Intent(ArtistsSearch.this, SongDetail.class);
                            intent.putExtra("artistName", song.artist);
                            intent.putExtra("songTitle",  song.songTitle);
                            intent.putExtra("imageURL", song.imageURL);
                            intent.putExtra("duration", song.duration);
                            intent.putExtra("albumName", song.albumName);
                            intent.putExtra("Collection", song.Collection);

                            startActivity(intent);
                        };


                        holder.songName.setOnClickListener(clickListener);


                    }


                    @Override
                    public int getItemCount() {
                        return artistSongs.size();
                    }
                });
        binding.songList.setAdapter(myAdapter);
        binding.songList.setLayoutManager(new

                LinearLayoutManager(this));


    }

    class MyRowHolder extends RecyclerView.ViewHolder {


        TextView songName;


        public MyRowHolder(@NonNull View itemView) {
            super(itemView);
            songName = itemView.findViewById(R.id.songName);


        }
    }
}