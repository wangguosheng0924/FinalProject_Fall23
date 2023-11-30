package algonquin.cst2335.finalproject_fall23;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.ColumnInfo;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import algonquin.cst2335.finalproject_fall23.databinding.ActivityArtistsSearchBinding;
import algonquin.cst2335.finalproject_fall23.databinding.ActivitySongDetailBinding;
import algonquin.cst2335.finalproject_fall23.databinding.CollectionListBinding;
import algonquin.cst2335.finalproject_fall23.databinding.SongListBinding;

/**
 * The ArtistsSearch activity allows users to search for artists and view their top songs.
 * @author Lei Luo
 * @version 1.1
 */
public class ArtistsSearch extends AppCompatActivity {

    /** Binding instance for ActivityArtistsSearch. */
    ActivityArtistsSearchBinding binding;

    /** List to hold songs of a searched artist. */
    ArrayList<SongList> artistSongs;

    /** Name of the artist being searched. */
    String artist;

    /** Adapter for the RecyclerView to display songs. */
    RecyclerView.Adapter myAdapter;

    /** ViewModel for managing song data. */
    SongViewModel songModel;

    /** Title of the current song. */
    String songTitle;

    /** ID of the artist. */
    int ArtistID;

    /** ID used for the image associated with the song. */
    String imageId;

    /** Cover image of the album. */
    String albumCover;

    /** Preview URL of the song. */
    String preview;

    /** Duration of the song in seconds. */
    int duration;

    /** Name of the album. */
    String albumName;

    /** RequestQueue for network requests. */
    protected RequestQueue queue;

    /**
     * Called when the activity is starting.
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down,
     *                           this Bundle contains the data it most recently supplied in onSaveInstanceState(Bundle).
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityArtistsSearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.firstToolbar);


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

            binding.artistName.setText("Songs of: " + userInput);
            binding.artistName.setVisibility(View.VISIBLE);

            Toast.makeText(ArtistsSearch.this, "Click song to check " +
                            "the details",
                    Toast.LENGTH_SHORT).show();

            // First API Request

            try {
                String stringURL = "https://api.deezer.com/search/artist/?q=" + URLEncoder.encode(userInput, "UTF-8");

                JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                        stringURL, null,
                        response -> {
                            try {
                                JSONObject start = response.getJSONArray(
                                        "data").getJSONObject(0);
                                ArtistID = start.getInt("id");

                                // Second API Request
                                String artistStringURL = "https://api.deezer.com/artist/" + ArtistID +
                                        "/top?limit=50";

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
                                                            albumName = albumObject.getString("title");
                                                            imageId = songObject.getString("md5_image");
                                                            preview = songObject.getString("preview");
                                                            ;


                                                            runOnUiThread(() -> {
                                                                // Create SongList object and add to the list
                                                                SongList song =
                                                                        new SongList(userInput, songTitle, duration, albumName, imageId, preview);

                                                                Log.d(
                                                                        "imageIdChange", "imageIdatconstructor: " + song.imageURL);

                                                                artistSongs.add(song);

                                                                myAdapter.notifyDataSetChanged();
                                                            });

                                                        }// for loop
                                                    } catch (
                                                            JSONException e) {
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
                            Log.e("ArtistSearch", "Volley error: " + error.getMessage());
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
                        holder.duration.setText(String.valueOf(song.duration));


                        View.OnClickListener clickListener = view -> {

                            // Now start the SongDetail activity
                            Intent intent = new Intent(ArtistsSearch.this, SongDetail.class);
                            intent.putExtra("artistName", song.artist);
                            intent.putExtra("songTitle", song.songTitle);
                            intent.putExtra("imageURL", song.imageURL);
                            intent.putExtra("duration", song.duration);
                            intent.putExtra("albumName", song.albumName);
                            intent.putExtra("Collection", song.Collection);
                            intent.putExtra("preview", song.preview);

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

    /**
     * Represents a ViewHolder for a song item in the RecyclerView.
     */
    class MyRowHolder extends RecyclerView.ViewHolder {


        TextView songName, duration;


        public MyRowHolder(@NonNull View itemView) {
            super(itemView);
            songName = itemView.findViewById(R.id.songName);
            duration = itemView.findViewById(R.id.duration);

        }
    }


    /**
     * Initialize the contents of the Activity's standard options menu.
     * @param menu The options menu in which items are placed.
     * @return You must return true for the menu to be displayed; if you return false, it will not be shown.
     */

    @Override //initialize the toolbar
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.first_menu, menu);
        return true;

    }


    /**
     * Called whenever an item in your options menu is selected.
     * @param item The menu item that was selected.
     * @return Return false to allow normal menu processing to proceed; return true to consume it here.
     */
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {


            case R.id.item_2:


                Intent intent2 = new Intent(ArtistsSearch.this, CollectionList.class);

                startActivity(intent2);


                break;

            case R.id.item_1:

                AlertDialog.Builder helpDialogBuilder = new AlertDialog.Builder(this);
                helpDialogBuilder.setTitle("How to Use");
                helpDialogBuilder.setMessage("Enter the full name of an " +
                        "artist and press 'Search' to view their top 50 songs" +
                        ".\n\n Click on a song title for detailed information " +
                        "about the song.");
                helpDialogBuilder.setPositiveButton("OK", null);
                helpDialogBuilder.show();

                break;
        }
        return true;
    }
}