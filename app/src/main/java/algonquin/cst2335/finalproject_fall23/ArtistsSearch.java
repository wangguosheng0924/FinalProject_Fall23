package algonquin.cst2335.finalproject_fall23;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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


            SongList si = new SongList(userInput, new ArrayList<>());
            artistSongs.add(si);
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
                        String obj1 = artistSongs.get(position).artist;
                        holder.artistName.setText(obj1);
                        holder.songName.setText("Song 1");
                        holder.songName2.setText("Song 2");
                        holder.songName3.setText("Song 3");
                        holder.songName4.setText("Song 4");
                        holder.songName5.setText("Song 5");
                        holder.songName6.setText("Song 6");
                        holder.songName7.setText("Song 7");
                        holder.songName8.setText("Song 8");



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