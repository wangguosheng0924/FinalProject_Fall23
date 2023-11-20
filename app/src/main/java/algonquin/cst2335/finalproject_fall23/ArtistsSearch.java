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
    ArrayList<SongInfo> artistSongs =null;

    RecyclerView.Adapter myAdapter;

    SongViewModel songModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityArtistsSearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        songModel = new ViewModelProvider(this).get(SongViewModel.class);
        artistSongs = songModel.artistSongs;




        binding.searchButton.setOnClickListener(click ->{
            String userInput =binding.textInput.getText().toString();
            myAdapter.notifyItemChanged(artistSongs.size()-1);



            SongInfo si= new SongInfo(userInput,"duration", "album_name",
                    "album_cover");
            artistSongs.add(si);
            binding.textInput.setText("");
            myAdapter.notifyDataSetChanged();//will redraw
        });

        binding.songList.setAdapter(myAdapter=
                new RecyclerView.Adapter<MyRowHolder>() {
            @NonNull
            @Override
            public MyRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                SongListBinding binding=
                        SongListBinding.inflate(getLayoutInflater());

                return new MyRowHolder(binding.getRoot());
            }

            @Override
            public void onBindViewHolder(@NonNull MyRowHolder holder, int position) {
                String obj1 = artistSongs.get(position).title;


                holder.songName.setText(obj1);
                holder.albumName.setText(artistSongs.get(position).duration);
                holder.year.setText(artistSongs.get(position).album_name);

                /*need to set album and year as well */
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
        TextView albumName;
        TextView year;
        public MyRowHolder(@NonNull View itemView) {
            super(itemView);
            songName=itemView.findViewById(R.id.songName);
            albumName=itemView.findViewById(R.id.albumName);
            year=itemView.findViewById(R.id.year);
        }
    }
}