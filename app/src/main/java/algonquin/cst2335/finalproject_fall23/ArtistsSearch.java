package algonquin.cst2335.finalproject_fall23;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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
    ArrayList<String> artistSongs = new ArrayList<>();

    private RecyclerView.Adapter myAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding=ActivityArtistsSearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.searchButton.setOnClickListener(click ->{
            artistSongs.add(binding.textInput.getText().toString());
            myAdapter.notifyItemChanged(artistSongs.size()-1);

            binding.textInput.setText("");
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
                holder.songName.setText("");
                holder.albumName.setText("");
                holder.year.setText("");
                String obj = artistSongs.get(position);
                holder.songName.setText(obj);
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