package algonquin.cst2335.finalproject_fall23;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import algonquin.cst2335.finalproject_fall23.databinding.ActivityArtistsSearchBinding;
import algonquin.cst2335.finalproject_fall23.databinding.SongListBinding;

public class ArtistsSearch extends AppCompatActivity {

    ActivityArtistsSearchBinding binding;
    ArrayList<SongList> artistSongs = null;

    RecyclerView.Adapter myAdapter;

    SongViewModel songModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityArtistsSearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        songModel = new ViewModelProvider(this).get(SongViewModel.class);
        artistSongs = songModel.artistSongs;


        binding.searchButton.setOnClickListener(click -> {
            String userInput = binding.textInput.getText().toString();
            myAdapter.notifyItemChanged(artistSongs.size() - 1);


            SongList sl = new SongList(userInput, "String b","String c",
                    "String d","String e");
            artistSongs.add(sl);
            binding.textInput.setText("");
            myAdapter.notifyDataSetChanged();//will redraw
        });

        binding.songList.setAdapter(myAdapter =
                new RecyclerView.Adapter<MyRowHolder>() {
                    @NonNull
                    @Override
                    public MyRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        SongListBinding binding =
                                SongListBinding.inflate(getLayoutInflater());

                        return new MyRowHolder(binding.getRoot());
                    }

                    @Override
                    public void onBindViewHolder(@NonNull MyRowHolder holder, int position) {
                        SongList song = artistSongs.get(position);
                        holder.artistName.setTag("Here is the song list for "+song.artist);
                        holder.songName.setTag("Song 1");
                        holder.songName2.setTag("Song 2");
                        holder.songName3.setTag("Song 3");
                        holder.songName4.setTag("Song 4");
                        holder.songName5.setTag("Song 5");
                        holder.songName6.setTag("Song 6");
                        holder.songName7.setTag("Song 7");
                        holder.songName8.setTag("Song 8");

                        View.OnClickListener clickListener = view -> {
                            String clickedText = view.getTag().toString();

                            Intent intent = new Intent(ArtistsSearch.this, SongDetail.class);
                            intent.putExtra("artistName", song.artist);
                            intent.putExtra("songTitle", clickedText);

                            intent.putExtra("albumCover", "app_icon");
                            startActivity(intent);
                        };

                        holder.artistName.setOnClickListener(clickListener);
                        holder.songName.setOnClickListener(clickListener);
                        holder.songName2.setOnClickListener(clickListener);
                        holder.songName3.setOnClickListener(clickListener);
                        holder.songName4.setOnClickListener(clickListener);
                        holder.songName5.setOnClickListener(clickListener);
                        holder.songName6.setOnClickListener(clickListener);
                        holder.songName7.setOnClickListener(clickListener);
                        holder.songName8.setOnClickListener(clickListener);


                    }


                    @Override
                    public int getItemCount() {
                        return artistSongs.size();
                    }
                });
        binding.songList.setLayoutManager(new LinearLayoutManager(this));

    }

    class MyRowHolder extends RecyclerView.ViewHolder {

        TextView songName;

        TextView songName2;
        TextView songName3;
        TextView songName4;
        TextView songName5;
        TextView songName6;
        TextView songName7;
        TextView songName8;


        TextView artistName;


        public MyRowHolder(@NonNull View itemView) {
            super(itemView);
            songName = itemView.findViewById(R.id.songName);
            songName2 = itemView.findViewById(R.id.songName2);
            songName3 = itemView.findViewById(R.id.songName3);
            songName4 = itemView.findViewById(R.id.songName4);
            songName5 = itemView.findViewById(R.id.songName5);
            songName6 = itemView.findViewById(R.id.songName6);
            songName7 = itemView.findViewById(R.id.songName7);
            songName8 = itemView.findViewById(R.id.songName8);
            artistName = itemView.findViewById(R.id.artistName);


        }
    }
}