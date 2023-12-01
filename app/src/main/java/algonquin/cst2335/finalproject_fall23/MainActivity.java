package algonquin.cst2335.finalproject_fall23;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import algonquin.cst2335.finalproject_fall23.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflate the layout for this view and set it as the content view
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.dictionaryButton.setOnClickListener(click -> {
            startActivity(new Intent(this, DictionaryActivity.class));
        });

        binding.sunriseButton.setOnClickListener(click -> {
            startActivity(new Intent(this, SunriseMain.class));
        });

        binding.recipeButton.setOnClickListener(click -> {
            startActivity(new Intent(this, RecipeMain.class));
        });

        binding.songButton.setOnClickListener(click -> {
            startActivity(new Intent(this, ArtistsSearch.class));
        });

        binding.recipeButton.setOnClickListener(click -> {
            startActivity(new Intent(this, RecipeMain.class));
        });

    }
}