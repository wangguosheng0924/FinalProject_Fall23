package algonquin.cst2335.finalproject_fall23;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import algonquin.cst2335.finalproject_fall23.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }
    public void onDictionaryButtonClick(View view){
        Intent intent = new Intent(this, DictionaryActivity.class);
        startActivity(intent);

        binding =ActivityMainBinding.inflate((getLayoutInflater()));
        setContentView(binding.getRoot());
    }


     binding.dictionaryButton.setOnClickListener( click -> {
        startActivity(new Intent(this,DictionaryActivity.class));
    });

     binding.sunriseButton.setOnClickListener( click -> {
        startActivity(new Intent(this,SunriseMain.class));
    });

        binding.recipeButton.setOnClickListener( click -> {
            startActivity(new Intent(this,RecipeMain.class));
        });

        binding.songBtton.setOnClickListener( click -> {
            startActivity(new Intent(this,ArtistsSearch.class));
        });

        binding.recipeButton.setOnClickListener( click -> {
            startActivity(new Intent(this,RecipeMain.class));
        });

    }
}