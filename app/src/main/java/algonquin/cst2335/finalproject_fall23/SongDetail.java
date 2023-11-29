package algonquin.cst2335.finalproject_fall23;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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


    Button saveButton;

    String imageFilePath;

    protected RequestQueue queue = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySongDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.myToolbar);

        queue = Volley.newRequestQueue(this);

        songCollect = new ArrayList<>();
        //load messages from the database:
        PersonalSongListData db = Room.databaseBuilder(getApplicationContext(),
                        PersonalSongListData.class,
                        "file")
                .fallbackToDestructiveMigration()
                .build();

        sDAO = db.cmDAO();//get a DAO object to interact with database




        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String songTitle = extras.getString("SONG_TITLE", "Default Song");
            String artistName = extras.getString("ARTIST_NAME", "Default Artist");
            String duration = extras.getString("DURATION", "0");
            String albumName = extras.getString("ALBUM_NAME", "");
            String collection = extras.getString("COLLECTION", "");
          String imageURL = extras.getString("IMAGE_URL", "");

            binding.songTitle.setText(songTitle);
            binding.artistName.setText(artistName);
            binding.duration.setText(duration);
            binding.albumName.setText(albumName);
            binding.collection.setText(collection);
            //save image
              imageURL =
                      "https://e-cdns-images.dzcdn.net/images/artist/" + imageFilePath + "/250x250-000000-80-0-0.jpg";


            File coverImage = new File(getFilesDir() + "/" + imageFilePath);
            if (coverImage.exists()) {
                Bitmap bitmap = BitmapFactory.decodeFile(coverImage.getAbsolutePath());
                binding.albumCover.setImageBitmap(bitmap);
            } else {
                ImageRequest imgReq =
                        new ImageRequest(imageURL,
                                bitmap -> {
                                    binding.albumCover.setImageBitmap(bitmap);


                                    FileOutputStream fOut = null;
                                    try {
                                        fOut =
                                                openFileOutput(imageFilePath + ".jpg", Context.MODE_PRIVATE);

                                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
                                        fOut.flush();
                                        fOut.close();
                                    } catch (
                                            IOException e) {
                                        throw new RuntimeException(e);
                                    }
                                }, 1024, 1024,
                                ImageView.ScaleType.FIT_CENTER, null, (error) -> {
                            Log.e("MainActivity", "Image request error: " + error.getMessage());
                        });
                queue.add(imgReq);

                //  image download
            }

        }

        imageFilePath = extras.getString("imageURL", "");
        binding.songTitle.setText(extras.getString("songTitle", "Default Song"));
        binding.artistName.setText(extras.getString("artistName", "Default Artist"));
        int duration = extras.getInt("duration", 0); // Defaulting to 0 if no value is found
        binding.duration.setText(String.valueOf(duration));


        binding.albumName.setText(extras.getString("albumName", ""));
        binding.collection.setText(extras.getString("Collection", ""));


        //save image
        String imageURL = "https://e-cdns-images.dzcdn.net/images/artist/" + imageFilePath + "/250x250-000000-80-0-0.jpg";


        File coverImage = new File(getFilesDir() + "/" + imageFilePath);
        if (coverImage.exists()) {
            Bitmap bitmap = BitmapFactory.decodeFile(coverImage.getAbsolutePath());
            binding.albumCover.setImageBitmap(bitmap);
        } else {
            ImageRequest imgReq =
                    new ImageRequest(imageURL,
                            bitmap -> {
                                binding.albumCover.setImageBitmap(bitmap);


                                FileOutputStream fOut = null;
                                try {
                                    fOut =
                                            openFileOutput(imageFilePath + ".jpg", Context.MODE_PRIVATE);

                                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
                                    fOut.flush();
                                    fOut.close();
                                } catch (
                                        IOException e) {
                                    throw new RuntimeException(e);
                                }
                            }, 1024, 1024,
                            ImageView.ScaleType.FIT_CENTER, null, (error) -> {
                        Log.e("MainActivity", "Image request error: " + error.getMessage());
                    });
            queue.add(imgReq);

            //  image download
        }


        Executor thread2 = Executors.newSingleThreadExecutor();
        thread2.execute(() -> {
            List<SongList> fromDatabase = sDAO.getAllMessages();//return a List
            songCollect.addAll(fromDatabase);//this adds all messages from the database

        });

        //end of loading from database


        binding.saveButton.setOnClickListener(click -> {
            String userInput = binding.collection.getText().toString();

            String artistN = binding.artistName.getText().toString();
            String song = binding.songTitle.getText().toString();
            String aN = binding.albumName.getText().toString();
            int du = Integer.parseInt(binding.duration.getText().toString());


            // Create a SongList object
            SongList thisSong = new SongList(artistN, song,
                    du, aN, userInput,imageFilePath);


            songCollect.add(thisSong);
            // Database insertion on a background thread
            Executor thread3 = Executors.newSingleThreadExecutor();
            thread3.execute(() -> {

                thisSong.Id = sDAO.insertMessage(thisSong); // Method to
                // insert SongList into the database

            });

            Toast.makeText(SongDetail.this, "Song saved!",
                    Toast.LENGTH_SHORT).show();
        });


    }

    ;

    @Override //initialize the toolbar
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.my_menu, menu);
        return true;

    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.item_1:


                String songTitleToDelete = binding.songTitle.getText().toString();
                AlertDialog.Builder builder =
                        new AlertDialog.Builder(SongDetail.this);

                builder.setNegativeButton("No", (btn, obj) -> { /* if no is clicked */ });
                builder.setMessage("Do you want to delete this song?");
                builder.setTitle("Delete?");


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


//
                break;

            case R.id.item_2:


                Intent intent2 = new Intent(SongDetail.this, CollectionList.class);

                startActivity(intent2);


                break;
        }
        return true;
    }
}
